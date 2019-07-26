package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import utils.ImageLoader;

public class DigitalClientManagementWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtWebsite;
	private JLabel lblVideourl;
	
	private Color color = Color.BLACK;
	private BufferedImage imagePreview = null;
	private ImageLoader loader;
	private String picture_Path;
	private String picture_Message;
	private String video_Path;
	private String video_Message;
	
	private int imagePreviewX = 60;
	private int imagePreviewY = 325;
	private int imagePreiviewWidth = 125;
	private int imagePreviewHeight = 125;
	
	@SuppressWarnings("unused")
	private DigitalClientWindow digitalClientWindow;

	public DigitalClientManagementWindow(DigitalClientWindow digitalClientWindow) {
		setResizable(false);
		this.digitalClientWindow = digitalClientWindow;
		loader = new ImageLoader();
		setTitle("Digital Screen Manager");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 470);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChangeScreenBackground = new JLabel("Change Screen Background Color");
		lblChangeScreenBackground.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblChangeScreenBackground.setBounds(32, 34, 268, 21);
		contentPane.add(lblChangeScreenBackground);
		
		JButton btnSelectColor = new JButton("Select Color");
		btnSelectColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Select A Color", Color.BLACK);
				color = c;
				repaint();
			}
		});
		btnSelectColor.setBounds(32, 73, 89, 23);
		contentPane.add(btnSelectColor);
		
		JButton btnChangeColor = new JButton("Change Color");
		btnChangeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "/s" + " color " + color.getRed() + " " + color.getGreen() + " " + color.getBlue();
				digitalClientWindow.send(message);
			}
		});
		btnChangeColor.setBounds(164, 73, 97, 23);
		contentPane.add(btnChangeColor);
		
		JLabel lblRenderAWebsite = new JLabel("Render A Website");
		lblRenderAWebsite.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblRenderAWebsite.setBounds(414, 34, 268, 21);
		contentPane.add(lblRenderAWebsite);
		
		JLabel lblRenderAPicture = new JLabel("Render A Picture");
		lblRenderAPicture.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblRenderAPicture.setBounds(32, 225, 159, 21);
		contentPane.add(lblRenderAPicture);
		
		JLabel lblPlayAVideo = new JLabel("Play A Video");
		lblPlayAVideo.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblPlayAVideo.setBounds(414, 225, 130, 21);
		contentPane.add(lblPlayAVideo);
		
		txtWebsite = new JTextField();
		txtWebsite.setBounds(414, 110, 330, 30);
		contentPane.add(txtWebsite);
		txtWebsite.setColumns(10);
		
		JLabel lblEnterWebsiteUrl = new JLabel("Enter website url (Example: www.google.com)");
		lblEnterWebsiteUrl.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblEnterWebsiteUrl.setBounds(414, 77, 330, 22);
		contentPane.add(lblEnterWebsiteUrl);
		
		JButton btnRenderWebsite = new JButton("Render Website");
		btnRenderWebsite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = txtWebsite.getText();
				if(url == null || url.equals("")) {
					return;
				}
				String message = "/s" + " website " + url;
				digitalClientWindow.send(message);
			}
		});
		btnRenderWebsite.setBounds(414, 163, 130, 23);
		contentPane.add(btnRenderWebsite);
		
		JButton btnChooseAPicture = new JButton("Choose A Picture");
		btnChooseAPicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = fc.showOpenDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					picture_Path = selectedFile.getAbsolutePath();
					try {
						if(ImageIO.read(new File(picture_Path)) != null) {
							picture_Message = "/s" + " picture " + picture_Path;
							imagePreview = loader.loadImage(selectedFile.getAbsolutePath());
							repaint();
						}else {
							JOptionPane.showMessageDialog(contentPane, "This is not a picture please choose valid file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
							System.err.println("This is not image file.");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnChooseAPicture.setBounds(201, 227, 115, 23);
		contentPane.add(btnChooseAPicture);
		
		JButton btnRenderPicture = new JButton("Render Picture");
		btnRenderPicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(picture_Message != null && !picture_Message.equals("")) {
					digitalClientWindow.send(picture_Message);
				}else {
					JOptionPane.showMessageDialog(contentPane, "Please choose a picture to render.", "No file detected.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnRenderPicture.setBounds(201, 257, 115, 23);
		contentPane.add(btnRenderPicture);
		
		JLabel lblPicturepreview = new JLabel("PicturePreview");
		lblPicturepreview.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPicturepreview.setBounds(32, 257, 104, 21);
		contentPane.add(lblPicturepreview);
		
		JButton btnChooseAVideo = new JButton("Choose A Video");
		btnChooseAVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = fc.showOpenDialog(null);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					video_Path = selectedFile.getAbsolutePath();
					video_Message = "/s" + " video " + video_Path;
					JOptionPane.showMessageDialog(contentPane, "You chose video: " + video_Path, "Video Chosen", JOptionPane.INFORMATION_MESSAGE);
					lblVideourl.setText("VideoURL: " + video_Path);
				}
			}
		});
		btnChooseAVideo.setBounds(571, 227, 111, 23);
		contentPane.add(btnChooseAVideo);
		
		JButton btnPlayVideo = new JButton("Play Video");
		btnPlayVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(video_Message != null && !video_Message.equals("")) {
					digitalClientWindow.send(video_Message);
				}else {
					JOptionPane.showMessageDialog(contentPane, "Please choose a video to play.", "No file detected.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnPlayVideo.setBounds(571, 257, 111, 23);
		contentPane.add(btnPlayVideo);
		
		lblVideourl = new JLabel("VideoUrl:");
		lblVideourl.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblVideourl.setBounds(414, 330, 330, 21);
		contentPane.add(lblVideourl);
		
		setVisible(true);
	}
	
	int red = 100;
	int green = 100;
	int blue = 50;
	Color c = new Color(red, green, blue);
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		Line2D vertical = new Line2D.Float(getWidth() / 2, 0, getWidth() / 2, getHeight());
		g2.draw(vertical);
		Line2D horizontal = new Line2D.Float(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g2.draw(horizontal);
		
		g2.setStroke(new BasicStroke(1));
		g2.setColor(color);
		g2.fillRoundRect(60, 140, 50, 50, 5, 5);
		
		if(imagePreview != null) {
			g.drawImage(imagePreview, imagePreviewX, imagePreviewY, imagePreiviewWidth, imagePreviewHeight, null);
		}
	}
}
