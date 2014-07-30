package com.amazonaws.services.kinesis.app;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

public class KinesisServer {

	public static void main(String[] args) throws Exception {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");
        
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(80);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase(".");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder holderEvents = new ServletHolder("ws-events", MessageProxyServlet.class);
        context.addServlet(holderEvents, "/kinesisapp/*");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, context, new DefaultHandler() });
        server.setHandler(handlers);        
        server.start();
        
        AWSCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider();
        KinesisClientLibConfiguration kinesisConfig = new KinesisClientLibConfiguration(
        		System.getProperty("kinesisapp.name"), 
        		System.getProperty("kinesisapp.stream"), 
        		credentialsProvider, Long.toString(System.currentTimeMillis()))
        		.withKinesisEndpoint(System.getProperty("kinesisapp.endpoint"));
        
        IRecordProcessorFactory factory = new Factory();
        Worker worker = new Worker(factory, kinesisConfig);
        worker.run();
        
        /*
		Processor p = new Processor();
		p.test();
		*/
	}
}
