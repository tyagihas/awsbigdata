package com.amazonaws.services.kinesis.client;

import java.nio.ByteBuffer;
import java.util.Random;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;

/**
 * Client 
 * 
 * Message format : <user id>,<latitude>,<longitude>
 * eg. 
 * 		
 * 20135,35.38.200,139.35.208
 * ...
 * 
**/
public class Client {

	public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");
        
		AmazonKinesisClient client = new AmazonKinesisClient();
		client.setEndpoint("https://kinesis.us-east-1.amazonaws.com");

		String stream = "test";
		int iteration = 100;
		int threashold = 1000;
		String data = new String("{\"user\":\"10125\",\"line\":\"aaa\",\"station\":\"bbb\",\"latitude\":35.");
		Random rand = new Random();
		try {				
			long start = System.currentTimeMillis();
			String myKey = Long.toString(Thread.currentThread().getId());
			for (int i = 0; i < iteration; i++) {
				try {
				  PutRecordRequest putRecordRequest = new PutRecordRequest();
				  putRecordRequest.setStreamName(stream);
				  putRecordRequest.setData(ByteBuffer.wrap((data+Integer.toString(rand.nextInt(19)+52)+",\"longitude\":139."+Integer.toString(rand.nextInt(39)+51)+"}").getBytes()));
				  putRecordRequest.setPartitionKey(myKey);
				  PutRecordResult putRecordResult = client.putRecord(putRecordRequest);
				}
				catch(Exception iex) {
				}
			}
			System.out.println("Elapsed time(ms) for task " + Thread.currentThread().getId() + " : " + (System.currentTimeMillis() - start));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        
	}

}
