package com.sakamoto.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import com.sakamoto.firework.Firework;
import com.sakamoto.firework.Particle;
import com.sakamoto.firework.Tail;


public class Sketch {
	// MAGIC GRAVITY
	public static final Vector2D low_gravity = new Vector2D(0,0.03f);
	public static final Vector2D normal_gravity = new Vector2D(0,0.12f);
	public static final Vector2D strong_gravity = new Vector2D(0,0.2f);
	public static final Vector2D no_gravity = new Vector2D(0,0);
	
	// MAGIC SPAWN CAP
	public final int minX = 300;
	public final int maxX = Main.WIDTH-600;
	
	// MAGIC NUMBERS
	public final int nParticles = 64;
	public final int blowForce = 4;
	public final int clusterSize = 3;
	public final int nCluster = 1;
	
	public final int fireworkLayers = 1; // the numbers of layers of each firework CAUTON -> can cascade and have huge performace issues
	public final int internalFireworks = 3;
	
	// LISTS
	public ArrayList<Firework[]> firework_clusters;
	public ArrayList<Particle[]> particle_clusters;
	public ArrayList<Tail> tails;
	
	
	// TEXT
	public float textHue;
	public float xoff = 0;
	
	// NUMBERS TO AVOID MATH
	public static double[][] sinV = new double[360][4];
	public static double[][] cosV = new double[360][4];
	
	public Sketch() {
		firework_clusters = new ArrayList<Firework[]>();
		particle_clusters = new ArrayList<Particle[]>();
		tails = new ArrayList<Tail>();
		for(int c = 0; c < 360; c++) {
			sinV[c][0] = Math.sin(c);
			cosV[c][0] = Math.cos(c);
			sinV[c][1] = Math.sin(c*2);
			cosV[c][1] = Math.cos(c*2);
			sinV[c][2] = Math.sin(c*3);
			cosV[c][2] = Math.cos(c*3);	
			sinV[c][3] = Math.sin(c*4);
			cosV[c][3] = Math.cos(c*4);
		}
		generateClusters();
	}
	
	public void generateClusters() {
		Firework[] cluster = new Firework[clusterSize];
		for(int c = 0; c < clusterSize; c++) {
			Random rand = new Random();
			int x = rand.nextInt(maxX)+minX;
			int y = Main.HEIGHT;
			float dx = 1 * (rand.nextBoolean()?1:-1);
			float dy = (rand.nextFloat()*-rand.nextInt(5))-8;
			int fireworkLayers_ = this.fireworkLayers;
			int innerFireworks = this.internalFireworks;
			int radius = 2;
			cluster[c] = new Firework(x,y,dx,dy,radius,fireworkLayers_,innerFireworks);
		}
		firework_clusters.add(cluster);
	}
	
	public void tick() {
		// GLOBAL
		
		if(firework_clusters.size() < nCluster) {
			generateClusters();
		}
		
		// FIREWORKS
		for(int c = firework_clusters.size()-1; c>= 0; c--) {
			int nullP = 0;
			Firework[] fw_cluster = firework_clusters.get(c);
			for(int c1 = 0; c1 < fw_cluster.length; c1++) {
				Firework firework = fw_cluster[c1];
				if(firework != null) {
					firework.applyForce(normal_gravity);
					firework.tick();
					Tail fw_tail = firework.getTail();
					fw_tail.hue = firework.hue;
					tails.add(fw_tail);
					if(firework.blowTime) {
						if(firework.layer > 0) {
							// RELEASE OTHERS FIREWORKS
							// get new fireworks[] and add to list
							Firework[] new_fw_cluster = firework.release(5);
							firework_clusters.add(new_fw_cluster);	
						} 
						if(firework.layer <= 0) {
							// BOOOOOOOM
							// get particles[] and add to list
							Particle[] new_particle_cluster = null;
							int explosionType = new Random().nextInt(4)+1;
							switch(explosionType) {
							case 1:
								// explosion 1
								new_particle_cluster = firework.blow1(blowForce*0.7f, nParticles);
								break;
							case 2:
								// explosion 2			
								new_particle_cluster = firework.blow2(blowForce*1f, nParticles);
								
								break;
							case 3:
								// circle
								new_particle_cluster = firework.blowCircle(blowForce*0.6f, nParticles);
								break;
							case 4:
								// heart
								new_particle_cluster = firework.blowHeart(blowForce*0.1f, nParticles);
								break;
							default:
								new_particle_cluster = null;
								break;
							}
							if(new_particle_cluster != null) {
								particle_clusters.add(new_particle_cluster);
							}
						}
						fw_cluster[c1] = null;
						
					
						
					}
					if(firework.atDeadZone) {
						fw_cluster[c1] = null;
					}
				}else {
					nullP++;
				}
			}
			if(nullP == fw_cluster.length) {
				firework_clusters.remove(c);
			}
		}
		// PARTICLES
		
		for(int c = particle_clusters.size()-1; c >= 0; c--) {
			Particle[] p_cluster = particle_clusters.get(c);
			int nullP = 0;
			for(int c1 = 0; c1 < p_cluster.length; c1++) {
				Particle particle = p_cluster[c1];
				if(particle != null) {
					if(particle.life > 0) {
						particle.tick();
						int tailChance = new Random().nextInt(100);
						if(tailChance > 75) {
							Tail tail = particle.getTail();
							tail.hue = particle.hue;
							tails.add(tail);
						}
						
					}else {
						p_cluster[c1] = null;
					}
				}else {
					nullP++;
				}
			}
			if(nullP == p_cluster.length) {
				particle_clusters.remove(c);
			}
		}
		
		
		// TAILS
		for(int c = tails.size()-1; c >= 0; c--) {
			Tail tail = tails.get(c);
			if(tail.life <= 0) {
				tails.remove(c);
			}
		}
		
		// TEXT
		xoff+=0.0005f;
		textHue = (float) Math.sin(xoff);
	}
	
	public void render(Graphics g) {
		
		
		// FIREWORK
		for(int c = firework_clusters.size()-1; c>= 0; c--) {
			Firework[] fw_cluster = firework_clusters.get(c);
			for(int c1 = 0; c1 < fw_cluster.length; c1++) {
				Firework firework = fw_cluster[c1];
				if(firework != null) {
				
					firework.render(g);
				}
			}
		}
		
		// PARTICLES
		
		for(int c = particle_clusters.size()-1; c >= 0; c--) {
			Particle[] p_cluster = particle_clusters.get(c);
			for(int c1 = 0; c1 < p_cluster.length; c1++) {
				Particle particle = p_cluster[c1];
				if(particle != null) {
				
					particle.render(g);
						
					
				}
			}
		}
		
		// TAIL
		for(int c = 0; c < tails.size(); c++) {
			Tail tail = tails.get(c);
			tail.render(g);
		}
		
		
		// TEXT
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		String fontName = "Arial";
		int fontSize = 72;
		String text = "Happy new year!!";
		int x = Main.WIDTH/2;
		int y = 100;
		int width = 0;
		int height = 0;
		g2.setColor(Color.getHSBColor(textHue, 1, 1));
		Font font = new Font(fontName, Font.BOLD, fontSize);
		FontMetrics metrics = g.getFontMetrics(font);
		int xx = x + (width-metrics.stringWidth(text))/2;
		int yy = y + ((height - metrics.getHeight()) /2 + metrics.getAscent());
		g2.setFont(font);
		g2.drawString(text, xx, yy);
				
	}
	
}
