package client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import utils.ImageLoader;
import utils.Window;

public class DigitalScreenWindow extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Screen-";
	public static final String ADDRESS = "localhost";
	public static final int PORT = 8192;

	private Window window;
	public static int screen_count;
	private Color color = Color.CYAN;
	
	private DigitalClient client;
	private Thread run;
	private Thread listen;
	private boolean running = false;
	private boolean renderPicture = false;
	private BufferedImage picture = null;
	private ImageLoader loader;
	private double fontSize = 36;
	
	// WebStuff
	private WebView webView;
	private JFXPanel jfxWebPanel;
	
	// Video Stuff
	private JFXPanel jfxVideoPanel;
	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	
	{
		++screen_count;
	}
	
	public DigitalScreenWindow(String name, String address, int port) {
		client = new DigitalClient(name, address, port);
		boolean connected = client.openConnection(address);
		
		if(!connected) {
			System.err.println("Connection failed!");
			console("Connection failed!");
			return;
		}
				
		console("Attempting a connection to " + address + ":" + port + ". ScreenName: " + name);

		String connectionPacket = "/c/" + name + "/e/";
		client.send(connectionPacket.getBytes());
		
		loader = new ImageLoader();
		window = new Window(1080, 640, "Digital Screen", this);
		
		start();
	}
	
	private void start() {
		running = true;
		run = new Thread(this);
		run.start();
	}
	
	private void stop() {
		running = false;
		try {
			run.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.requestFocus();
		
		listen();
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				delta--;
				frames++;
			}
			render();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS:" + frames);
				frames = 0;
			}
		}
		
		stop();
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
					}else if(message.startsWith("/s/")) {
						String action = message.split("/s/|/e/")[1].split(" ")[1];
						if(action.equals("color")) {
							if(jfxVideoPanel != null) {
								removeVideo();
							}
							String[] colors = message.split("/s/|/e/")[1].split(" ");
							int r = Integer.parseInt(colors[2]);
							int g = Integer.parseInt(colors[3]);
							int b = Integer.parseInt(colors[4]);
							color = new Color(r, g, b);
							renderPicture = false;
							if(jfxWebPanel != null) {
								removeWebpage();
							}
						}else if(action.equals("website")) {
							if(jfxVideoPanel != null) {
								removeVideo();
							}
							String url = message.split("/s/|/e/")[1].split(" ")[2];
							System.out.println("URL: " + url);
							showWebpage(url);
							renderPicture = false;
						}else if(action.equals("picture")) {
							if(jfxVideoPanel != null) {
								removeVideo();
							}
							String path = message.split("/s/|/e/")[1].replaceFirst("\\s+","").substring(8);
							new Thread() {
								@Override
								public void run() {
									try {
										picture = loader.loadImage(path);
										System.out.println("Image loaded: " + path);
										renderPicture = true;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}.start();
							if(jfxWebPanel != null) {
								removeWebpage();
							}
						}else if(action.equals("video")) {
							String path =  message.split("/s/|/e/")[1].replaceFirst("\\s+","").substring(6);
							System.out.println(path);
							showVideo(path);
						}
					}
				}
			}
		};
		listen.start();
	}
	
	private void renderPicture(Graphics g) {
		g.drawImage(picture, 0, 0, getWidth(), getHeight(), null);
	}
	
	private void showWebpage(String website) {
		if(jfxWebPanel == null) {
			jfxWebPanel = new JFXPanel();
			
			Platform.runLater(() -> {
				webView = new WebView();
				webView.setMaxSize(1080, 640);
				webView.setMinSize(1080, 640);
				webView.setPrefSize(1080, 640);
				jfxWebPanel.setSize(1080, 640);
				jfxWebPanel.setScene(new Scene(webView));
				webView.getEngine().load("https://" + website);
			});

			window.getWebPanel().add(jfxWebPanel);
			window.getWebPanel().setVisible(true);
			window.getFrame().setComponentZOrder(window.getWebPanel(), 0);
		}else {
			Platform.runLater(() -> {
				webView.getEngine().load("https://" + website);
				jfxWebPanel.setVisible(true);
				window.getWebPanel().setVisible(true);
				window.getFrame().setComponentZOrder(window.getWebPanel(), 0);
			});
		}
	}
	
	private void removeWebpage() {
		window.getWebPanel().setVisible(false);
		window.getFrame().setComponentZOrder(window.getScreen(), 0);
		jfxWebPanel.setVisible(false);
	}
	
	private void showVideo(String path) {
		Group root = new Group();
		jfxVideoPanel = new JFXPanel();
		
		media = new Media("file:///" + path.replace('\\', '/'));
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		
		root.getChildren().add(mediaView);
		Scene scene = new Scene(root, 1080, 640);
		
		jfxVideoPanel.setScene(scene);
		jfxVideoPanel.setVisible(true);
		
		window.getVideoPanel().add(jfxVideoPanel);
		window.getFrame().setComponentZOrder(window.getVideoPanel(), 0);
		window.getVideoPanel().setVisible(true);
		
		mediaPlayer.play();
		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				mediaView.setFitWidth(scene.getWidth());
				mediaView.setFitHeight(scene.getHeight());
			}
		});
	}
	
	private void removeVideo() {
		jfxVideoPanel.setVisible(false);
		window.getVideoPanel().setVisible(false);
		mediaPlayer.stop();
	}
	
	private void update() {
		if(!renderPicture) {
			fontSize = (fontSize + 0.1) % 72;
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		if(!renderPicture) {
			g.setColor(color);
			g.fillRect(0, 0, 1080, 640);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("ComicSans", Font.PLAIN, (int)fontSize));
			g.drawString("Digital Screen Prototype", 160, 250);
		}else if(renderPicture) {
			renderPicture(g);
		}		
		g.dispose();
		bs.show();
	}
	
	public void console(String message) {
		System.out.println(message);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		--screen_count;
	}
	
	public static int getInstanceCount() {
		return screen_count;
	}
}
