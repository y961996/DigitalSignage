package utils;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.DigitalScreenWindow;

public class Window{
	
	private JFrame frame;
	private JPanel webPanel;
	private JPanel videoPanel;
	private DigitalScreenWindow screen;
	
	public Window(int width, int height, String title, DigitalScreenWindow screen){
		this.screen = screen;
		frame = new JFrame(title);
		webPanel = new JPanel();
		videoPanel = new JPanel();
		
		webPanel.setVisible(false);
		videoPanel.setVisible(false);
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.add(webPanel, 0);
		frame.add(videoPanel, 1);
		frame.add(this.screen, 2);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JPanel getWebPanel() {
		return webPanel;
	}
	
	public JPanel getVideoPanel() {
		return videoPanel;
	}
	
	public DigitalScreenWindow getScreen() {
		return screen;
	}
}