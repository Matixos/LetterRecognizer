package pl.com.mat.letterrecognizer;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.naming.NamingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Controller implements ActionListener {
	
	private ViewManager win;
	private IModel mod;
	private ModelChain mchain;
	
	private int returnVal;
	
	public Controller(ViewManager win) {
		this.win = win;
		mchain = new ModelChain();
		mod = mchain.getModel();
		
		win.addListeners(this, ma, mma, ka);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Rec")){
			try {
				mchain.refreshLetterSet(win.getDrawPanel().getImage());
				win.getShowPanel().drawLetter(mod.bayes(mod.getFeatures()));
				win.refreshProbability(mod.getLetterList());
			} catch (DataFormatException e1) {
				JOptionPane.showMessageDialog(win,
						"There aren't any Letters in database!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			} catch(RuntimeException exc) {
				JOptionPane.showMessageDialog(win,
						"Too few letters to obtain good result! \n There should be about 5 of each at least.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(win,
						"You didn't draw letter!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getActionCommand().equalsIgnoreCase("clr")) {
			win.getDrawPanel().clearImage();
			win.getShowPanel().clearImage();
			win.clearProbability();
			mchain.clearLetters();
		}
		else if(e.getActionCommand().equalsIgnoreCase("clrMem")) {
			mchain.clearMemory();
			win.clearLetters();
			actionPerformed(new ActionEvent(this,1,"clr"));
		}
		else if(e.getActionCommand().equalsIgnoreCase("Load")) {
			returnVal = win.getFChooser().showOpenDialog(win);
			
			switch (returnVal) {
			case JFileChooser.APPROVE_OPTION :
		        try {
					mchain.loadFromFile(win.getFChooser().getSelectedFile().getPath());
					win.refreshLetters(mod.getLetterList());
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		        break;
			case JFileChooser.ERROR_OPTION: System.out.println("ERR");
			}
			win.getFChooser().setSelectedFile(new File(""));
		}
		else if(e.getActionCommand().equalsIgnoreCase("Save")) {
			int response; 
			if(!mchain.getRecentPath().equals(""))
				response = JOptionPane.showConfirmDialog(null, "Do you want to rewrite recent file?", "Confirm",
			        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			else
				response = JOptionPane.NO_OPTION;
	
			if(response==JOptionPane.YES_OPTION)
				try {
					mchain.saveToFile(mchain.getRecentPath());
				} catch (IOException e2) {}
			else {
				returnVal = win.getFChooser().showSaveDialog(win);
			
				switch (returnVal) {
				case JFileChooser.APPROVE_OPTION :
					try {
						mchain.saveToFile(win.getFChooser().getSelectedFile().getPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
						break;
				case JFileChooser.ERROR_OPTION: System.out.println("ERR");
				}
				win.getFChooser().setSelectedFile(new File(""));
			}
		}
		else if(e.getActionCommand().equalsIgnoreCase("SwitchModel")) {
			actionPerformed(new ActionEvent(this,1,"clr"));
			mod = mchain.getModel();
			JOptionPane.showMessageDialog(win,
					"Switch to " + mchain.getCounter() + " successfully!",
					"Info",
					JOptionPane.INFORMATION_MESSAGE);
			win.refreshLetters(mod.getLetterList());
		}
	}
	
	KeyAdapter ka = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					mchain.addLetterTests(win.getLetterField(), win.getDrawPanel().getImage());
					win.refreshLetters(mod.getLetterList());
					win.clearLetterField();
					actionPerformed(new ActionEvent(this,1,"clr"));
				} catch(DataFormatException dexc) {
					JOptionPane.showMessageDialog(win,
							"You can draw ony one letter!",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				} catch(NamingException exc) {
					JOptionPane.showMessageDialog(win,
							"Field can't be empty! Draw Something.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(win,
							"You didn't draw letter!",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	};
	
	MouseAdapter ma = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			win.getDrawPanel().drawPoint(e.getX(), e.getY());
		}
	};

	
	MouseMotionAdapter mma = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
			if(e.getSource() == win.getDrawPanel())
				win.getDrawPanel().draw(e.getX(), e.getY());
		}
	};
}
