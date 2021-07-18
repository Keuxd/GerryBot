package gerrybot.league;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Draw {
	//Symbol == 25 / 23
 	//pRunes == 50
	//sRunes == 30 / 28
	//mRunes == 10(i guess)
	
	
	protected BufferedImage drawRunes(ArrayList<BufferedImage> runes) {
		int loadedRunePages = runes.size() / 11;
		BufferedImage finalImage = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < loadedRunePages; i++) {
			BufferedImage runePage = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
			
			for(int j = 0 + (11*i); j < 11*(i+1); j++) {
				if(j % 11 == 0 || j == 5 || j == 16 || j == 27 || j == 38) {
					runes.set(j, resize(runes.get(j), 25,25));
				}
				else if(j == 1 || j == 12 || j == 23 || j == 34) {
					runes.set(j, resize(runes.get(j), 50,50));
				}
				else {
					runes.set(j, resize(runes.get(j),30,30));
				}
				
				runePage = concatImagesHorizontally(runePage, runes.get(j));
			}

			finalImage = concatImageVertically(finalImage, runePage);
		}
		
		return concatImagesHorizontally(finalImage, new BufferedImage(5,1,2));
	}
	
	protected BufferedImage drawBuilds(ArrayList<BufferedImage> runes) {
		return null;
	}
	
	protected BufferedImage drawAll(ArrayList<BufferedImage> runes) {
		return null;
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
		int vGap = 10;
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
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
}
