package com.sakamoto.firework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.sakamoto.main.Utils;
import com.sakamoto.main.Vector2D;

public class Tail {
	public static int POINT = 1;
	public static int LINE = 2;
	
	public Vector2D position; 
	public Vector2D velocity;
	
	public int life;
	public int maxLife;
	private int TYPE = POINT;
	
	public float hue;
	
	public Tail(float x, float y, float dx, float dy, int type, int s) {
		this.velocity = new Vector2D(dx,dy);
		this.position = new Vector2D(x,y);
		this.maxLife = s;
		this.life = maxLife;
		this.TYPE = type;
		
	}
	

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		life--;
		int alpha = (int) Utils.map(life, maxLife, 0, 255, 0);
		int r = (int)Utils.map(alpha, 255, 0, 3, 0);
		
		BasicStroke stroke = new BasicStroke(r, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		
		g2.setColor(Color.getHSBColor(hue, 1, 1));
		if(TYPE == POINT) {
			g2.fillOval((int)position.x, (int)position.y, 5, 5);
		}else if(TYPE == LINE) {
			g2.drawLine((int)position.x, (int)position.y, (int)(position.x+velocity.x), (int)(position.y+velocity.y));
		}
		
	}
}
