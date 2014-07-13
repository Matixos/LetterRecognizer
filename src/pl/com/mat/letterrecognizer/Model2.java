package pl.com.mat.letterrecognizer;

import java.awt.Point;
import java.util.ArrayList;

public class Model2 extends AbstractModel {
	
	public Model2() {
		super();
	}
	
	public double[] getFeatures() {
		double[] ft = new double[4];
		
		ft[0] = (double)getCountPixel(0, 0.5, 1)/getCountPixel(0.5, 1, 1);
		ft[1] = (double)getCountPixel(0, 0.5, 2)/getCountPixel(0.5, 1, 2);
		ft[2] = (double)getCountPixel(0.35, 0.65, 1)/getCountPixel(0.7, 1, 1);
		ft[3] = getSpecialArc();
		
		//System.out.println(ft[0] + " " + ft[1] + " " + ft[2] + " "+ ft[3]);
		
		return ft;
	}
	
	private Point getMiddle(ArrayList<Point> let) throws Exception {
		int y = (int)(((getMax(let,2)-getMin(let,2))/2)+getMin(let,2));
		
		for(Point p: let)
			if(p.getY() == y)
				return p;
		
		throw new Exception();
	}
	protected Point getMax(ArrayList<Point> set) {
		Point temp = new Point(0,0);
		
		for(Point i: set)
			if(i.getY()>temp.getY()) 
				temp = i;
			
		return temp;
	}
	
	protected Point getMin(ArrayList<Point> set) {
		Point temp = new Point(500,500);
		
		for(Point i: set)
			if(i.getY()<temp.getY()) 
				temp = i;
		
		return temp;
	}
	
	private double getSpecialArc() {
		ArrayList<Point> temp = makeSubSet(0, 0.25, 1);
		
		try {
			Double a = arcBetwLines(0, 1, getMiddle(temp).getX()-getMin(temp).getX(), getMiddle(temp).getY()-getMin(temp).getY());
		
			if(a.isNaN())
				return 1.57;
			else
				return a;
		} catch(Exception ex) {
			return getSpecialRelation();
		}		
	}
	
	private double arcBetwLines(double u1, double u2, double w1, double w2) {   // vector way
		return Math.acos((u1*w1+u2*w2)/(Math.sqrt(u1*u1+u2*u2)*Math.sqrt(w1*w1+w2*w2)));
	}
	
	public int getCountPixel(double alfa, double beta, int which) {
		ArrayList<Point> temp = makeSubSet(alfa, beta, which);
		
		return temp.size();
	}
	
	public double getSpecialRelation() {
		System.out.println("specRel");
		ArrayList<Point> temp1 = makeSubSet(0, 0.5, 1);
		temp1 = makeSubSet(temp1, 0.3, 0.7, 2);
		
		return (double)(getLetter().size())/(temp1.size()+1);
	}

}
