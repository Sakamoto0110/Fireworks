package com.sakamoto.main;

public class Utils {

	public static double map(double value, double oldMin, double oldMax, double newMin, double newMax){
		return newMin + ((value - oldMin)*(newMax - newMin))/(oldMax - oldMin);
	}
	
}
