package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DigitalClient {

	private String name;
	private String address;
	private int port;
	private int ID = -1;
	
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread send;
	
	public DigitalClient(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		}catch(UnknownHostException | SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String message = new String(packet.getData());
		return message;
	}
	
	public void send(final byte[] data) {
		send = new Thread("Send_Thread") {
			@Override
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public void close() {
		new Thread() {
			@Override
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		};
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public InetAddress getIp() {
		return ip;
	}
	
	
	
}
