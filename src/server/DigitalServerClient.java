package server;

import java.net.InetAddress;

public class DigitalServerClient {

	private final int ID;

	public String name;
	public InetAddress address;
	public int port;
	public int attempt = 0;
	
	public DigitalServerClient(String name, InetAddress address, int port, final int ID) {
		this.name = name;
		this.address = address;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
}
