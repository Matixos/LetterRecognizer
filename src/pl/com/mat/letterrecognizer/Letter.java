package pl.com.mat.letterrecognizer;

import java.io.Serializable;
import java.util.ArrayList;
import Jama.Matrix;

@SuppressWarnings("serial")
public class Letter implements Serializable {

	private String name;
	private ArrayList<double[]> tests;
	private double theta;
	
	private Matrix mi;
	private Matrix sigma;
	
	private double pj;
	
	public Letter(String name, double[] val) {
		this.name = name;
		tests= new ArrayList<double[]>();
		tests.add(val);
	}
	
	public void addTest(double[] vals) {
		tests.add(vals);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<double[]> getTests() {
		return tests;
	}
	
	public void setTheta(double t) {
		theta = t;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public void setPj(double p) {
		pj=p;
	}
	
	public double getPj() {
		return pj;
	}
	
	public void refreshParams() {
		mi = new Matrix(1,tests.get(0).length,0);
		for(double[] doub: tests)
			mi.plusEquals(new Matrix(doub, 1));
		
		mi = mi.times((double)1/tests.size());
		
		sigma = new Matrix(tests.get(0).length,tests.get(0).length,0);
		Matrix t1;
		for(double[] doub: tests) {
			t1 = (new Matrix(doub, 1)).minus(mi);
			sigma.plusEquals((t1.transpose()).times(t1));   // w Jamie domyslne mac - wiersz
		}
		
		sigma = sigma.times((double)1/tests.size());
	}
	
	public double normalDistribution(double[] features) {
		Matrix x = new Matrix(features, 1);
		x.minusEquals(mi);
		Matrix temp = (x.times((sigma.inverse()).times(x.transpose()))).times((double)-1/2);
		
		double pt = (double) 1./(Math.pow(2*Math.PI, features.length/2)*Math.sqrt(sigma.det()))*Math.exp(temp.get(0, 0));
		
		return pt;
	}
	
}
