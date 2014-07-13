package pl.com.mat.letterrecognizer;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

public interface IModel {
	public double[] getFeatures();
	public void addLetterTest(String s, double[] val);
	public String bayes(double[] features) throws DataFormatException;
	
	public ArrayList<Letter> getLetterList();
}
