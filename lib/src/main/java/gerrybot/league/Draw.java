package gerrybot.league;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Draw {	
	protected BufferedImage drawRunes(ArrayList<BufferedImage> runes) {
		int loadedPages = runes.size() / 11;
		
		BufferedImage finalImage = new BufferedImage(1,1,2);
		
		for(int i = 0; i < loadedPages; i++) {
			finalImage = concatImageVertically(finalImage, drawRunePage(runes, i*11));
		}
		
		return finalImage;
	}
	
	protected BufferedImage drawBuilds(ArrayList<BufferedImage> runes) {
		return null;
	}
	
	protected BufferedImage drawAll(ArrayList<BufferedImage> runes) {
		return null;
	}
	
	private BufferedImage drawRunePage(ArrayList<BufferedImage> runes, int firstElement) {
		int width = 300;
		int height = 75;
		
		BufferedImage runePage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = runePage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		
		//Symbol rune 01
		g2.drawImage(resize(runes.get(firstElement), 25), null, 0, 2);
		
		//pRune
		g2.drawImage(runes.get(firstElement + 1), null, 24, 13);
		

		//sRune 01
		g2.drawImage(resize(runes.get(firstElement + 2), 30), null, 74, 25);
		
		//sRune 02
		g2.drawImage(resize(runes.get(firstElement + 3), 30), null, 106, 25);
		
		//sRune 03
		g2.drawImage(resize(runes.get(firstElement + 4), 30), null, 138, 25);
		
		
		//Symbol rune 02
		g2.drawImage(resize(runes.get(firstElement + 5), 25),null, 178, 2);
		
		//sRune 04
		g2.drawImage(resize(runes.get(firstElement + 6), 28), null, 207, 24);
		
		//sRune 05
		g2.drawImage(resize(runes.get(firstElement + 7), 28), null, 237, 24);
		
		
		//mRune 01
		g2.drawImage(resize(runes.get(firstElement + 8), 20), null, 275, 3);
		
		//mRune 02
		g2.drawImage(resize(runes.get(firstElement + 9), 20), null, 275, 25);
		
		//mRune 03
		g2.drawImage(resize(runes.get(firstElement + 10), 20), null, 275, 47);
		
		g2.dispose();
		
		return runePage;
	}
	
	private BufferedImage concatImagesHorizontally(BufferedImage image1, BufferedImage image2) {
		int hGap = 2;
		int width = image1.getWidth() + image2.getWidth() + hGap;
		int height = Math.max(image1.getHeight(), image2.getHeight()) + hGap;
		BufferedImage newImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(image1, null, 0, 0);
		g2.drawImage(image2, null, image1.getWidth() + hGap, 0);
		g2.dispose();
		return newImage;
	}
	
	private BufferedImage concatImageVertically(BufferedImage image1, BufferedImage image2) {
		int vGap = 3;
		int width = Math.max(image1.getWidth(), image2.getWidth());
		int height = image1.getHeight() + image2.getHeight();
		BufferedImage newImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(image1, null, 0, 0);
		g2.drawImage(image2, null, 0, image1.getHeight() + vGap);
		g2.dispose();
		return newImage;
	}

	private BufferedImage resize(BufferedImage img, int newSize) { 
	    Image tmp = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
}
