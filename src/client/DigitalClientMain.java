package client;

import java.util.Scanner;

public class DigitalClientMain implements Runnable{

	public static final String NAME = "Yunus";
	public static final String ADDRESS = "localhost";
	public static final int PORT = 8192;

	private static final Scanner scanner = new Scanner(System.in);
	
	private DigitalClient client;
	private Thread run;
	private Thread listen;
	private boolean running = false;
	
	public DigitalClientMain(String name, String address, int port) {
		client = new DigitalClient(name, address, port);
		boolean connected = client.openConnection(address);
		
		if(!connected) {
			System.err.println("Connection failed!");
			console("Connection failed!");
			return;
		}
		
		console("Attempting a connection to " + address + ":" + port + ". User: " + name);
		
		String connectionPacket = "/c/" + name + "/e/";
		client.send(connectionPacket.getBytes());
		
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	@Override
	public void run() {
		listen();
		while(running) {
			String message = scanner.nextLine();
			send(message, true);
		}
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			@Override
			public void run() {
				while(running) {
					String message = client.receive();
					
					if(message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						console("Successfully connected to server! ID: " + client.getID());
					}else if(message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
					}else if(message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					}
				}
			}
		};
		listen.start();
	}
	
	private void send(String message, boolean text) {
		if(message.equals("")) {
			return;
		}
		if(text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message + "/e/";
		}
		client.send(message.getBytes());
	}
	
	public void console(String message) {
		System.out.println(message);
	}
}
