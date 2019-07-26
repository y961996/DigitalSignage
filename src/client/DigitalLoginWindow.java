package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class DigitalLoginWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JLabel lblIpAddress;
	private JLabel lblPort;
	private JLabel lblIPDesc;
	private JLabel lblPortDesc;

	public DigitalLoginWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(67, 50, 165, 28);
		txtName.setText("Yunus");
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(124, 33, 45, 14);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(67, 152, 165, 28);
		txtAddress.setText("localhost");
		contentPane.add(txtAddress);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(114, 135, 65, 14);
		contentPane.add(lblIpAddress);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(130, 207, 34, 14);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(67, 224, 165, 28);
		txtPort.setText("8192");
		contentPane.add(txtPort);
		
		lblIPDesc = new JLabel("(eg: 192.168.0.2)");
		lblIPDesc.setBounds(99, 182, 96, 14);
		contentPane.add(lblIPDesc);
		
		lblPortDesc = new JLabel("(eg: 8192)");
		lblPortDesc.setBounds(114, 254, 65, 14);
		contentPane.add(lblPortDesc);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}
		});
		btnLogin.setBounds(102, 303, 89, 23);
		contentPane.add(btnLogin);
	}

	private void login(String name, String address, int port) {
		dispose();
		new DigitalClientWindow(name, address, port);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DigitalLoginWindow frame = new DigitalLoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
