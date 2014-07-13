package pl.com.mat.letterrecognizer;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ModelChain {
	
	private ArrayList<Point> letter;
	
	private int count = 2;                // ktora lista aktualna
	private String recentPath = "";       // do save-owania
	
	private Model1 mod1;
	private Model2 mod2;
	
	public ModelChain() {
		letter = new ArrayList<Point>();
		
		mod1 = new Model1();
		mod2 = new Model2();
	}

	public IModel getModel() {
		if(count == 2) { count=1; return mod1; }
		else { count=2; return mod2; }
	}
	
	public String getCounter() {
		if(count == 1)
			return "Standard Model";
		else
			return "My Model";
	}
	
	public void addLetterTests(String s, BufferedImage val) throws Exception {
		refreshLetterSet(val);
		
		mod1.addLetterTest(s, mod1.getFeatures());
		mod2.addLetterTest(s, mod2.getFeatures());
	}
	
	public void clearMemory() {
		mod1.getLetterList().clear();
		mod2.getLetterList().clear();
	}
	
	public void clearLetters() {
		mod1.clrLetter();
		mod2.clrLetter();
	}
	
	public void refreshLetterSet(BufferedImage img) throws Exception {
		letter.clear();
		
		for(int i=0; i < img.getWidth(); i++)
			for(int j=0; j<img.getHeight(); j++)
				if(img.getRGB(i, j) == Color.BLACK.getRGB())    	  // jeœli pixel jest czarny
					letter.add(new Point(i, img.getHeight()-j));      // uk³ad wspó³rzednych normalny
	
		if(letter.size()==0)
			throw new Exception();
		
		mod1.refreshLetterSet(letter);
		mod2.refreshLetterSet(letter);
	}
	
	public String getRecentPath() {
		return recentPath;
	}
	
	public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
		
		filename = filename.split("\\.")[0];
		recentPath = filename;
		
		clearMemory();
		
		Letter let = (Letter)in.readObject();
		while (!let.getName().equals("End1")) {
			mod1.getLetterList().add(let);
			let = (Letter)in.readObject();
		}
		let = (Letter)in.readObject();
		while (!let.getName().equals("End2")) {
			mod2.getLetterList().add(let);
			let = (Letter)in.readObject();
		}
			
		in.close();
	}	
	
	public void saveToFile(String filename) throws IOException {
		ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(
			filename + ".rlt"));
		
		for(Letter l: mod1.getLetterList())
			file.writeObject(l);
		
		file.writeObject(new Letter("End1", null));
		
		for(Letter l: mod2.getLetterList())
			file.writeObject(l);
		
		file.writeObject(new Letter("End2", null));
		
		file.flush();
		file.close();
	}
}
