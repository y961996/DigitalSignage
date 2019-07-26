package server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DigitalServerWindow extends JFrame implements WindowListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JLabel serverMessageLbl;
	
	private DigitalServer server;
	private JTextField txtPort;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DigitalServerWindow frame = new DigitalServerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public DigitalServerWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DigitalServerWindow.class.getResource("/server/icon.gif")));
		setTitle("Digital Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.setToolTipText("Starts the server on specified port.");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(server != null && server.isRunning()) {
					JOptionPane.showMessageDialog(contentPane, "Server is already running...", "SERVER ALREADY RUNNING", JOptionPane.PLAIN_MESSAGE);
					System.out.println("Server is already running...");
					return;
				}
				int port = Integer.parseInt(txtPort.getText());
				server = new DigitalServer(port);
				serverMessageLbl.setBounds(115, 201, 200, 23);
				serverMessageLbl.setForeground(Color.BLUE);
				serverMessageLbl.setText("Server is running on port " + port);
			}
		});
		btnStart.setBounds(97, 157, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setToolTipText("Shutsdown the server.");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(server == null || !server.isRunning()) {
					JOptionPane.showMessageDialog(contentPane, "Server is already stopped!", "SERVER IS STOPPED ALREADY!", JOptionPane.PLAIN_MESSAGE);
					System.out.println("Server is not running.");
				}else {
					server.quit();
					serverMessageLbl.setForeground(Color.RED);
					serverMessageLbl.setBounds(156, 201, 145, 23);
					serverMessageLbl.setText("Server stopped");
					System.out.println("Server stopped.");
				}
			}
		});
		btnStop.setBounds(227, 157, 89, 23);
		contentPane.add(btnStop);
		
		JLabel lblDgtalServer = new JLabel("DIGITAL SERVER");
		lblDgtalServer.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblDgtalServer.setBounds(117, 25, 239, 35);
		contentPane.add(lblDgtalServer);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblPort.setBounds(97, 87, 74, 23);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtPort.setText("8192");
		txtPort.setBounds(177, 90, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		serverMessageLbl = new JLabel("Server is not running.");
		serverMessageLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
		serverMessageLbl.setForeground(Color.RED);
		serverMessageLbl.setBounds(141, 201, 145, 23);
		contentPane.add(serverMessageLbl);
		
		addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(server == null || !server.isRunning()) {
			System.out.println("Server is not running.");
		}else {
			server.quit();
			System.out.println("Server stopped.");
		}
		System.out.println("Exiting system...");
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
