package com.amazonaws.services.kinesis.app;

import java.util.Arrays;
import java.util.List;

public class CoordinateListener {

	private class Coordinate {
		public double x;
		public double y;
	}
	
	public static Coordinate coordsw;
	public static Coordinate coordne;
	
	public CoordinateListener() {
		coordsw = new Coordinate();
		coordne = new Coordinate();		
		List<String>list = Arrays.asList(System.getProperty("kinesisapp.coordsw").split(","));
		coordsw.x = new Double(list.get(0)).doubleValue();
		coordsw.y = new Double(list.get(1)).doubleValue();
		list = Arrays.asList(System.getProperty("kinesisapp.coordne").split(","));
		coordne.x = new Double(list.get(0)).doubleValue();
		coordne.y = new Double(list.get(1)).doubleValue();
	}
	
	public boolean verifyCoordinates(double x, double y) {
		if ((x < coordne.x) && (coordsw.x < x) && (y < coordne.y) && (coordsw.y < y)) return true;
		
		return false;
	}
}
