package com.sakamoto.firework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import com.sakamoto.main.Utils;
import com.sakamoto.main.Vector2D;

public class Particle {

	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D position;
	public Vector2D gravity;
	
	public int maxLife;
	public int life;
	
	public int tailSize;
	public float hue;
	
	public Particle(float x, float y, float dx, float dy, int life, Vector2D Gravity, int tailS) {
		
		
		Random rand = new Random(); 
		float ex = 0.1f * rand.nextFloat() * (rand.nextBoolean()?1:-1);
		float ey = 0.1f * rand.nextFloat() * (rand.nextBoolean()?1:-1);
		this.acceleration = new Vector2D(0,0);
		this.velocity = new Vector2D(dx+ex,dy+ey);
		this.position = new Vector2D(x,y);
		this.gravity = Gravity;
		this.tailSize = tailS;
		this.maxLife = life;
		this.life = maxLife;
	}
	
	public Tail getTail() {
		return new Tail(position.x,position.y,velocity.x,velocity.y, Tail.LINE, tailSize);
	}
	
	
	public void applyForce(Vector2D f) {
		acceleration.add(f);
	}
	
	public void tick() {
		applyForce(gravity);
		velocity.add(acceleration);
		position.add(velocity);
		
		life-=new Random().nextInt(2);
		acceleration.mult(0);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int alpha = (int)Utils.map(life, maxLife, 0,255,0);
		int r = (int)Utils.map(alpha,255,0,6,0);
		g2.setColor(Color.getHSBColor(hue, 1, 1));
		g2.fillOval((int)(position.x+velocity.x-r/2), (int)(position.y+velocity.y-r/2), r,r);
		
	}
}
