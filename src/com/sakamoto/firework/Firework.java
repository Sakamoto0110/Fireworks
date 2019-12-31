package com.sakamoto.firework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import com.sakamoto.main.Main;
import com.sakamoto.main.Sketch;
import com.sakamoto.main.Vector2D;

public class Firework {
	
	
	// PHYSICS
	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D position;
	
	// FLAGS
	public boolean blowTime = false;
	public boolean atDeadZone = false;
	
	// LAYERS
	public int layer;
	private int nChild;
	
	
	// VISUAL
	private int radius;
	public float hue;
	
	
	public Firework(float x, float y, float dx, float dy, int r, int nLayer, int nChild) {
		this.acceleration = new Vector2D(0,0);
		this.velocity = new Vector2D(dx,dy);
		this.position = new Vector2D(x,y);
		this.layer = nLayer;
		this.nChild = nChild;
		this.radius = r;
		this.hue = new Random().nextFloat();
	}
	
	public Tail getTail() {
		return new Tail(position.x+radius/2,position.y+radius/2, velocity.x,velocity.y, Tail.LINE, 20);
	}
	
	public Firework[] release(int releaseForce) {
		Firework[] child = new Firework[nChild];
		for(int c = 0; c < nChild; c++) {
			Random rand = new Random();
			float dx = rand.nextFloat()*(rand.nextBoolean()?1:-1);
			float dy = -rand.nextFloat()*2;
			Vector2D dir = new Vector2D(dx,dy);
			dir.mult(releaseForce);
			
			child[c] = new Firework(position.x,position.y,dir.x,dir.y,radius,layer-1,nChild);
			//child[c].hue = this.hue;
		}
		
		return child;
	}
	
	public Particle[] blow1(float blowForce, int nParticles) {
		Particle[] particles = new Particle[nParticles];
		for(int c = 0; c < nParticles; c++) {
			Random rand = new Random();
			float dx = rand.nextFloat()*(rand.nextBoolean()?1:-1);
			float dy = rand.nextFloat()*(rand.nextBoolean()?1:-1);
			Vector2D dir = new Vector2D(dx,dy);
			dir.mult(blowForce);
			particles[c] = new Particle(position.x,position.y,dir.x,dir.y,40,Sketch.normal_gravity,10 );
			particles[c].hue = hue;
		}
		return particles;
	}
	
	public Particle[] blow2(float blowForce, int nParticles) {
		Particle[] particles = new Particle[nParticles];
		for(int c = 0; c < nParticles; c++) {
			Random rand = new Random();
			int angle = rand.nextInt(360);
			float xx = (float) (position.x + rand.nextFloat() * Sketch.sinV[angle][0]); 
			float yy = (float) (position.y + rand.nextFloat() * Sketch.cosV[angle][0]);
			Vector2D v = new Vector2D(xx,yy);
			v.sub(position);
			v.mult(blowForce);
			particles[c] = new Particle(xx,yy,v.x,v.y,40,Sketch.low_gravity,5);
			particles[c].hue = hue;
		}
		return particles;
	}
	
	
	public Particle[] blowCircle(float blowForce, int nParticles) { 
		Particle[] particles = new Particle[nParticles];
		for(int c = 0; c < nParticles; c++) {
			Random rand = new Random();
			int angle = rand.nextInt(360);
			float xx = (float) (position.x + 1 * Sketch.sinV[angle][0]); 
			float yy = (float) (position.y + 1 * Sketch.cosV[angle][0]);
			Vector2D v = new Vector2D(xx,yy);
			v.sub(position);
			v.mult(blowForce);
			particles[c] = new Particle(xx,yy,v.x,v.y,40,Sketch.low_gravity,5);
			particles[c].hue = hue;
		}
		return particles;
	} 
	public Particle[] blowHeart(float blowForce, int nParticles) {
		Particle[] particles = new Particle[nParticles];
		for(int c = 0; c < nParticles; c++) {
			Random rand = new Random();
			double xx = -.3f * (16*Sketch.sinV[c][0]*Sketch.sinV[c][0]*Sketch.sinV[c][0]);
			double yy = -.3f * (13*Sketch.cosV[c][0] - 5*Sketch.cosV[c][1] - 2*Sketch.cosV[c][2]-Sketch.cosV[c][3]);
			xx += position.x;
			yy += position.y;
			
			Vector2D v = new Vector2D((float)xx,(float)yy);
			v.sub(position);
			v.mult(blowForce);
			particles[c] = new Particle((float)xx,(float)yy,v.x,v.y,40,Sketch.no_gravity,5);
			particles[c].hue = hue;
		}
		return particles;
	}
	
	
	 
	
	public void applyForce(Vector2D f) { 
		acceleration.add(f); 
	}
	 
	 

	public void tick() {
		velocity.add(acceleration);  
		position.add(velocity); 
		if(velocity.y >= 0) { 
			blowTime = true; 
		} 
		if(position.y >= Main.HEIGHT) {
			atDeadZone = true;
		}
		acceleration.mult(0);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.getHSBColor(hue, 1,1));
		//g2.fillOval((int)position.x,(int)position.y, 5, 5);
	}

}
