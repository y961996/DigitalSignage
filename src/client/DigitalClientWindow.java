package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class DigitalClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	
	private DigitalClient client;
	private Thread run;
	private Thread listen;
	private boolean running = false;
	private JMenuBar menuBar;
	private JMenu mnFle;
	private JMenuItem mnýtmExit;
	private JMenuItem mnýtmManageDigitalScreens;
	
	public DigitalClientWindow(String name, String address, int port) {
		setTitle("Digital Client");
		client = new DigitalClient(name, address, port);
		
		boolean connected = client.openConnection(address);
		
		if(!connected) {
			System.err.println("Connection failed!");
			console("Connection failed!");
		}
		
		createWindow();
		console("Attempting a connection to " + address + ":" + port + ". User: " + name);
		
		String connection = "/c/" + name + "/e/";
		client.send(connection.getBytes());
		
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880, 550);
		setMinimumSize(new Dimension(500, 330));
		setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFle = new JMenu("File");
		menuBar.add(mnFle);
		
		mnýtmManageDigitalScreens = new JMenuItem("Manage Digital Screens");
		mnýtmManageDigitalScreens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DigitalClientManagementWindow(DigitalClientWindow.this);
			}
		});
		mnFle.add(mnýtmManageDigitalScreens);
		
		mnýtmExit = new JMenuItem("Exit");
		mnýtmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(1);
			}
		});
		mnFle.add(mnýtmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28, 815, 30, 7};
		gbl_contentPane.rowHeights = new int[]{25, 485, 40};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		history.setFont(new Font("Monospaced", Font.PLAIN, 14));
		history.setEditable(false);
		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(history);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		scrollConstraints.weightx = 1;
		scrollConstraints.weighty = 1;
		scrollConstraints.insets = new Insets(0, 5, 0, 0);
		contentPane.add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(), true);
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		gbc_txtMessage.weightx = 1;
		gbc_txtMessage.weighty = 0;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText(), true);
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.weightx = 0;
		gbc_btnSend.weighty = 0;
		contentPane.add(btnSend, gbc_btnSend);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.close();
			}
		});
		
		setVisible(true);
		
		txtMessage.requestFocusInWindow();
	}
	
	public void run() {
		listen();
	}
	
	private void send(String message, boolean text) {
		if(message.equals("")) {
			return;
		}
		if(message.startsWith("/s")) {
			message = "/s/ " + message.substring(3) + "/e/";
			txtMessage.setText("");
		}else if(message.startsWith("/p")) {
			message = "/p/" + message.substring(3) + "/e/";
			txtMessage.setText("");
		}else if(text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message + "/e/";
			txtMessage.setText("");
		}
		
		client.send(message.getBytes());
	}
	
	public void send(String message) {
		send(message, true);
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
						console("-----------------------------------------------------------------------");
						console("If you know the commands to manage screens use this command line window \r\n"
								+ "otherwise click menu then choose manage digital screens.");
						//console("To see the commands type !help");
					}else if(message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
					}else if(message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					}else if(message.startsWith("/u/")) {
						//String[] u = message.split("/u/|/n/|/e/");  // For further improvement purposes
					}
				}
			}
		};
		listen.start();
	}
	
	public void console(String message) {
		history.append(message + "\n\r");
	}
}
