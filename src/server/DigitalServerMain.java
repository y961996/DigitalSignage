package server;

public class DigitalServerMain {

	@SuppressWarnings("unused")
	private DigitalServer server;
	
	public DigitalServerMain(int port) {
		server = new DigitalServer(port);
	}
	
	public static void main(String[] args) {
		int port;
		
		if(args.length > 1) {
			System.out.println("Usage: java -jar \"Jar File Name\".jar [port]");
			return;
		}
		
		port = Integer.parseInt(args[0]);
		new DigitalServerMain(port);
	}
}
