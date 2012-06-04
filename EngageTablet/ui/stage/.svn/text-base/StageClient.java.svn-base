package ui.stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import main.Logger;

public class StageClient {

	Socket client;
	InetSocketAddress host;
	BufferedReader serverInput;
	PrintStream clientOutput;
	
	private static final String DEFAULT_ADDRESS = "128.237.225.116";
	
	public StageClient() throws UnknownHostException {
		this(DEFAULT_ADDRESS);
	}
	
	public StageClient(String address) throws UnknownHostException {
		host = new InetSocketAddress(InetAddress.getByName(address), 8080);
		this.connect();
	}
	
	public boolean connect() {
		try { 
			client = new Socket(); 
			Logger.log(Logger.IMPORTANT,"Connecting..");
			client.connect(host,5000);
		}
		catch (IOException e) { 
			Logger.log(Logger.IMPORTANT,"Could not connect to host: "+e.toString()); 
			return false; 
		}
		
		try { serverInput = new BufferedReader(new InputStreamReader(client.getInputStream())); }
		catch (IOException e) { 
			Logger.log(Logger.IMPORTANT,"Could not connect to server input: "+e.toString()); 
			return false; 
		}
		
		try { clientOutput = new PrintStream(client.getOutputStream()); }
		catch (IOException e) { 
			Logger.log(Logger.IMPORTANT,"Could not connect to client output: "+e.toString()); 
			return false; 
		}
		
		Logger.log(Logger.IMPORTANT, "Successfully connected to host.");
		return true;
	}
	
	public void displayImage(String path) {
		if(client != null && client.isConnected()) clientOutput.print(path);
		else if(connect()) clientOutput.print(path);
	}
	
	public void closeConnection() {
		try {
			clientOutput.close();
			serverInput.close();
			client.close();
		}
		catch(IOException e) {
			Logger.log(Logger.IMPORTANT, "Colud not close connection: "+e.toString());
		}
	}
	
	/*
	public static void main(String args[]) throws UnknownHostException {
		
		StageClient theClient = new StageClient(InetAddress.getByName(args[0]));
		
		theClient.printToServer("Hello World!");
		
	}
	*/
}

