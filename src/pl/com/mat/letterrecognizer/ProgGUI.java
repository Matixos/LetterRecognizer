package pl.com.mat.letterrecognizer;

public class ProgGUI {

	public static void main(String args[]) {
		ViewManager win=new ViewManager();
		new Controller(win);
	}
}
