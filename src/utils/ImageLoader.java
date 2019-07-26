package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {

	private BufferedImage image;
	
	public BufferedImage loadImage(String path) throws IOException{
		URL url = new File(path).toURI().toURL();
		image = ImageIO.read(url);
		return image;
	}
}
