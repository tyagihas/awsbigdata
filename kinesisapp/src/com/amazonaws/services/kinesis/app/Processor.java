package com.amazonaws.services.kinesis.app;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Processor 
 * 
 * Message format : <user id>,<latitude>,<longitude>
 * eg. 
 * 10290,35.40.100,139.45.205
 * 20135,35.38.200,139.35.208
 * ...
 * 
**/
public class Processor implements IRecordProcessor {

	private long counter;
	private int target;
	
	private HashSet<String> users;
	private CoordinateListener coordsListener;
	private LogAnalyzer logAnalyzer;
	
	private byte[] bytearray;
	
	public Processor() {
		counter = 0;
		target = 50;
		bytearray = new byte[128];
	}
	
	@Override
	public void initialize(String arg0) {
		coordsListener = new CoordinateListener();
		try {
			logAnalyzer = new LogAnalyzer();
			users = logAnalyzer.getUsers();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void processRecords(List<Record> arg0, IRecordProcessorCheckpointer arg1) {
		counter += arg0.size();
		if (counter > target) {
			System.out.println("Received : " + counter + " records");
			target += target;
		}
		Record rec;
		for(int i = 0; i < arg0.size(); i++) {
			rec = arg0.get(i);
			try {
				verifyRecord(rec.getData());
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean verifyRecord(ByteBuffer buffer) throws JSONException {
		buffer.get(bytearray, 0, buffer.remaining());
		JSONObject json = new JSONObject(new String(bytearray));
		String user = json.getString("user");
		if (users.contains(user)) {
			MessageProxy proxy = MessageProxy.getInstance();
			String color = "#0" + user;
			double x = json.getDouble("latitude");
			double y = json.getDouble("longitude");
			proxy.sendMesg(color + "," + Double.toString(700-(x-35.52)*3684) + "," + Double.toString((y-139.51)*1769));
			System.out.println(x + "," + y);
			if (coordsListener.verifyCoordinates(x, y)) {
				System.out.println("Matched! '" + user + "' is at (" + x + ", " + y + ")");
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void shutdown(IRecordProcessorCheckpointer arg0, ShutdownReason arg1) {
	}
	
	public void test() throws JSONException {
		String str = "00001,35.65,139.65";
		initialize("test");
		ByteBuffer buffer = ByteBuffer.allocateDirect(128);
		buffer.put(str.getBytes());
		buffer.flip();
		verifyRecord(buffer);
	}

}
 