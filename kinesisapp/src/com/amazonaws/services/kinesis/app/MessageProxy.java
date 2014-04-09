package com.amazonaws.services.kinesis.app;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class MessageProxy extends WebSocketAdapter {
	
	static {
		self = new MessageProxy();
	}
	
	private static MessageProxy self;
	
	public MessageProxy() {
		self = this;
	}
	
	public static MessageProxy getInstance() {
		return self;
	}
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		System.out.println("onWebSocketConnect : " + sess);
		drawTargetArea();
	}
	
	private void drawTargetArea() {
		
	}
	
	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println(message);
		try {
			this.getRemote().sendString(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		System.out.println("onWebSocketClose : " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		System.out.println("onWebSocketError : " + cause.getMessage());
	}

	public void sendMesg(String message) {
		if (getRemote() != null) {
			try {
				this.getRemote().sendString(message);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
	}
	
}
