package pl.com.mat.letterrecognizer;

public class Model1 extends AbstractModel {
	
	public Model1() {
		super();
	}
	
	public double[] getFeatures() {
		double[] ft = new double[2];
		
		ft[0] = (double)(getMax(makeSubSet(0, 0.3, 1), 2)-getMin(makeSubSet(0, 0.3, 1), 2))/(getMax(getLetter(), 2)-getMin(getLetter(), 2));
		ft[1] = (double)(getMax(makeSubSet(0.4, 0.6, 2), 1)-getMin(makeSubSet(0.4, 0.6, 2), 1))/(getMax(getLetter(), 1)-getMin(getLetter(), 1));
		
		//System.out.println(ft[0] + " " +  ft[1]);
		
		return ft;
	}
}
