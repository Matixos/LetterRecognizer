package pl.com.mat.letterrecognizer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.DataFormatException;

public abstract class AbstractModel implements IModel {
	
	private ArrayList<Point> letter;       // zbior czarnych punktow z narysowanej litery
	
	private ArrayList<Letter> letters;         // litery jakie mamy zakodowane - ich modele do matchingu
	private Comparator<Letter> c;

	@SuppressWarnings("serial")
	public AbstractModel() {
		letter = new ArrayList<Point>();
		
		letters = new ArrayList<Letter>() {             // przeciazenie arraylisty
			
			@Override
			public boolean contains(Object o) {
				for(Letter i: this)
					if(i.getName().equals(o))
						return true;
				
				return false;
			}
			
			@Override
			public int indexOf(Object o) {
				for(int i=0; i<this.size(); i++)
					if(this.get(i).getName().equals(o))
						return i;
				
				return -1;
			}
		};
		
		c = new Comparator<Letter>() {
			public int compare(Letter lt1, Letter lt2) { 
				return lt1.getName().compareTo(lt2.getName());
			}
		};
	}
	
	public void clrLetter() {
		letter.clear();
	}
	
	protected void refreshLetterSet(ArrayList<Point> let) {
		letter = let;
	}
	
	protected ArrayList<Point> getLetter() {
		return letter;
	}
	
	public ArrayList<Letter> getLetterList() {
		return letters;
	}
	
	private int getValPoint(Point p, int which) {
		if(which == 1)
			return (int) p.getX();
		else
			return (int) p.getY();
	}
	
	protected int getMax(ArrayList<Point> set, int which) {
		Point temp = new Point(0,0);
		if(which == 1) {
			for(Point i: set)
				if(i.getX()>temp.getX()) 
					temp = i;
			return (int)temp.getX();
		}
		else {
			for(Point i: set)
				if(i.getY()>temp.getY()) 
					temp = i;
			return (int)temp.getY();
		}
	}
	
	protected int getMin(ArrayList<Point> set, int which) {
		Point temp = new Point(500,500);
		if(which == 1) {
			for(Point i: set)
				if(i.getX()<temp.getX()) 
					temp = i;
			return (int)temp.getX();
		}
		else {
			for(Point i: set)
				if(i.getY()<temp.getY()) 
					temp = i;
			return (int)temp.getY();
		}
	}
	
	protected ArrayList<Point> makeSubSet(double alfa, double beta, int which) {
		ArrayList<Point> temp = new ArrayList<Point>();
		
		for(Point i: letter)
			if((1-alfa)*getMin(letter, which)+alfa*getMax(letter, which)<= getValPoint(i, which) && 
					(1-beta)*getMin(letter, which)+beta*getMax(letter, which)>= getValPoint(i, which))
				temp.add(i);
		
		return temp;
	}
	
	protected ArrayList<Point> makeSubSet(ArrayList<Point> set, double alfa, double beta, int which) {
		ArrayList<Point> temp = new ArrayList<Point>();
		
		for(Point i: set)
			if((1-alfa)*getMin(set, which)+alfa*getMax(set, which)<= getValPoint(i, which) && 
					(1-beta)*getMin(set, which)+beta*getMax(set, which)>= getValPoint(i, which))
				temp.add(i);
		
		return temp;
	}
	
	private void refreshThethas() {
		double N=0;
		for(Letter l: letters)
			N += l.getTests().size();
		
		for(Letter l: letters)
			l.setTheta(l.getTests().size()/N);
	}
	
	public void addLetterTest(String s, double[] val) {
		int index;
		if(letters.contains(s)) {
			index = letters.indexOf(s);
			letters.get(index).addTest(val);
		}
		else {
			letters.add(new Letter(s, val));
			index = letters.size()-1;
			Collections.sort(letters, c);
		}
		
		refreshThethas();
		letters.get(index).refreshParams();
	}
	
	public String bayes(double[] features) throws DataFormatException {
		if(letters.size()==0)
			throw new DataFormatException();
		
		double px = 0;
		
		for(Letter l: letters) {
			px += (l.getTheta()*l.normalDistribution(features));
			//System.out.println(l.getName() + " " + l.normalDistribution(features));
		
			if(new Double(l.normalDistribution(features)).isNaN())
				throw new RuntimeException();
		}
		
		double max=0; String maxL="";
		for(Letter l: letters) {
			l.setPj((double)l.normalDistribution(features)*l.getTheta()/px);
			
			if(new Double(l.getPj()).isNaN())
				throw new  RuntimeException();
			
			if(l.getPj()>max) {
				max = l.getPj();
				maxL = l.getName();
			}
		}
		
		return maxL;
	}
	
	public abstract double[] getFeatures();
}
