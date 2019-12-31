package com.sakamoto.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;



public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;

	// ** //
	public JFrame frame;
	public static int WIDTH = 1600;
	public static int HEIGHT = 900;

	private boolean isRunning;
	private Thread thread;
	// ** //
	private BufferedImage background;
	//
	public static Sketch sketch;

	public Main() {

		setPreferredSize(new Dimension((int) (WIDTH), (int) (HEIGHT)));
		isRunning = true;
		initFrame();

		background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//
		sketch = new Sketch();
		//
		

	}

	public void tick() {
		sketch.tick();
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = background.getGraphics();
		g.setColor(new Color(11,11,11));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// **
		sketch.render(g);
		
		// **
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(background, 0, 0, (int) (WIDTH), (int) (HEIGHT), null);
		bs.show();
	}


	// AVOID EDITING THIS SECTION!
	public static void main(String[] args) {
		Main game = new Main();
		game.start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amoutOfTicks = 60.0;
		double ns = 1000000000 / amoutOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				 System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {
		frame = new JFrame("Fireworks");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}




}
