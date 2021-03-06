package gerrybot.league;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Draw {	
	protected BufferedImage drawRunes(int[][] runes, HashMap<Integer, BufferedImage> hm) {
		int loadedPages = runes.length;
		BufferedImage finalImage = new BufferedImage(1,1,2);
		
		for(int i = 0; i < loadedPages; i++) {
			finalImage = concatImageVertically(finalImage, drawRunePage(runes[i], hm));
		}
		
		return finalImage;
	}
	
	protected BufferedImage drawBuilds(int[][] builds, HashMap<Integer, BufferedImage> hm) {
		int rows = builds.length;
		BufferedImage buildPage = new BufferedImage(1,1,2);
		
		for(int i = 0; i < rows; i++) {
			BufferedImage uniqueBuild = new BufferedImage(1,1,2);
			for(int j = 0; j < builds[i].length; j++) {
				uniqueBuild = concatImagesHorizontally(uniqueBuild, hm.get(builds[i][j]));
			}
			buildPage = concatImageVertically(buildPage, uniqueBuild);
		}

		return buildPage;
	}
	
	protected BufferedImage drawAll(ArrayList<BufferedImage> runes, ArrayList<BufferedImage> builds) {
		return null;
	}
	
	private BufferedImage drawRunePage(int[] runes, HashMap<Integer, BufferedImage> hm) {
		int width = 300;
		int height = 75;
		
		BufferedImage runePage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = runePage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		
		//Symbol rune 01
		g2.drawImage(hm.get(runes[0]), null, 0, 2);
		
		//pRune
		g2.drawImage(hm.get(runes[1]), null, 24, 13);
		

		//sRune 01
		g2.drawImage(hm.get(runes[2]), null, 74, 25);
		
		//sRune 02
		g2.drawImage(hm.get(runes[3]), null, 106, 25);
		
		//sRune 03
		g2.drawImage(hm.get(runes[4]), null, 138, 25);
		
		
		//Symbol rune 02
		g2.drawImage(hm.get(runes[5]), null, 178, 2);
		
		//sRune 04
		g2.drawImage(resize(hm.get(runes[6]), 28), null, 207, 24);
		
		//sRune 05
		g2.drawImage(resize(hm.get(runes[7]), 28), null, 237, 24);
		
		
		//mRune 01
		g2.drawImage(hm.get(runes[8]), null, 275, 3);
		
		//mRune 02
		g2.drawImage(hm.get(runes[9]), null, 275, 25);
		
		//mRune 03
		g2.drawImage(hm.get(runes[10]), null, 275, 47);
		
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