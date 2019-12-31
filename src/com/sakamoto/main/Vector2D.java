package com.sakamoto.main;

public class Vector2D {
	
	public float x,y;
	
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
	}

	public static Vector2D add(Vector2D a, Vector2D b) {
		return new Vector2D(a.x + b.x, a.y + b.y);
	}

	public void sub(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public static Vector2D sub(Vector2D a, Vector2D b) {
		return new Vector2D(a.x - b.x, a.y - b.y);
	}

	public void mult(double n) {
		this.x *= n;
		this.y *= n;
	}

	public static Vector2D mult(Vector2D a, float n) {
		return new Vector2D(a.x * n, a.y * n);
	}

	public void div(double n) {
		this.x /= n;
		this.y /= n;
	}

	public static Vector2D div(Vector2D a, float n) {
		return new Vector2D(a.x / n, a.y / n);
	}

	public float dot(Vector2D a) {
		return (this.x * a.x) + (this.y * a.y);
	}

	public static float dot(Vector2D a, Vector2D b) {
		return (a.x * b.x) + (a.y * b.y);
	}

	public static float angleBetween(Vector2D a, Vector2D b) {
		return (float) (Math.acos(dot(a, b) / (a.mag() * b.mag())));
	}

	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}

	public double mag() {
		return Math.sqrt((double) ((this.x * this.x) + (this.y * this.y)));
	}

	public void normalize() {
		this.div((float) this.mag());
	}

	public void limit(float n) {
		double mag = this.mag();
		if (mag > n && mag != 0) {
			double r = n / mag;
			this.x *= r;
			this.y *= r;
		}

	}

}
