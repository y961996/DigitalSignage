package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class DigitalServer implements Runnable{

	private List<DigitalServerClient> clients = new ArrayList<DigitalServerClient>();
	
	//private final int MAX_ATTEMPTS = 5;
	
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private boolean detail = false;

	private Thread run;
	private Thread manage;
	private Thread send;
	private Thread receive;
	
	public DigitalServer(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		}catch(SocketException e) {
			e.printStackTrace();
			return;
		}
		
		run = new Thread(this, "Server");
		run.start();
	}
	
	// Run (Server) Thread's run method.
	@Override
	public void run() {
		running = true;
		System.out.println("Server started on port: " + port);
		
		manageClients();
		receive();
		
		while(running) {
			// do stuff here
		}
		
		System.out.println("Server closing...");
		quit();
		System.out.println("Server closed.");
	}
	
	// Manage Thread
	private void manageClients() {
		manage = new Thread("Manage") {
			@Override
			public void run() {
				
			}
		};
		manage.start();
	}
	
	// Receive Thread
	private void receive() {
		receive = new Thread("Receive") {
			@Override
			public void run() {
				while(running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					
					try {
						socket.receive(packet);
					}catch(SocketException e) {
						// Do Nothing
					}catch(IOException e) {
						e.printStackTrace();
					}
					
					processPacket(packet);
				}
			}
		};
		receive.start();
	}
	
	// Send Thread
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			@Override
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}
	
	private void sendToAll(String message) {
		if(message.startsWith("/m/")) {
			String text = message.substring(3);
			text = text.split("/e/")[0].trim();
		}
		for(int i = 0; i < clients.size(); i++) {
			DigitalServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	// Handle received packets
	private void processPacket(DatagramPacket packet) {
		String string = new String(packet.getData());
		
		if(detail) {
			System.out.println("DEBUG: " + string);
		}
		
		if(string.startsWith("/c/")) {
			int id = UniqueIdentifier.getIdentifier();
			String name = string.split("/c/|/e/")[1];
			System.out.println(name + " (ID: " + id +  ") connected.");
			clients.add(new DigitalServerClient(name, packet.getAddress(), packet.getPort(), id));
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		}else if(string.startsWith("/m/")){
			sendToAll(string);
		}else if(string.startsWith("/s/")) {
			sendToAll(string);
		}else if(string.startsWith("/p/")) {
			sendToAll(string);
		}
	}
	
	// Shutsdown the server
	public void quit() {
		running = false;
		socket.close();
	}
	
	public boolean isRunning() {
		return running;
	}
}
