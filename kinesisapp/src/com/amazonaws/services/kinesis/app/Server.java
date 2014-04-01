package com.amazonaws.services.kinesis.app;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

public class Server {

	public static void main(String[] args) {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");
        
        AWSCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider();
        KinesisClientLibConfiguration kinesisConfig = new KinesisClientLibConfiguration(
        		System.getProperty("kinesisapp.name"), 
        		System.getProperty("kinesisapp.stream"), 
        		credentialsProvider, Long.toString(System.currentTimeMillis()));
        
        IRecordProcessorFactory factory = new Factory();
        Worker worker = new Worker(factory, kinesisConfig);
        
        int exitCode = 0;
        try {
            worker.run();
        } catch (Throwable t) {
            exitCode = 1;
        }
        System.exit(exitCode);
        
        /*
		Processor p = new Processor();
		p.test();
		*/
	}
}
