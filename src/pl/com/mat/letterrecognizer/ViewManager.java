package pl.com.mat.letterrecognizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class ViewManager extends JFrame {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 450;
	
	private DrawPanel panelRys, panelWys;
	private JPanel rightPanel;
	private JButton recogn, clear, clearMem, switchModel;
	private JTextField lettField;
	private JTextArea ta, tb;
	
	private JMenuBar menu;
	private JMenu m1;
	private JMenuItem p1, p2;
	
	private JFileChooser fc;
	
	public ViewManager() {
		super("Writing Recognition");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		getContentPane().setLayout(new GridLayout(1, 3, 3, 3));
		getContentPane().setBackground(Color.GRAY);
		
		menu = new JMenuBar();                        // menu górne
		m1 = new JMenu("File");
		p1 = new JMenuItem("Load Letter Base", KeyEvent.VK_L);
		p2 = new JMenuItem("Save Letter Base", KeyEvent.VK_S);
		m1.add(p1);
		m1.add(p2);
		menu.add(m1);
		setJMenuBar(menu);
		
		fc = new JFileChooser();
		FileFilter fil = new FileFilter() {
			
			public String getDescription() {
				return "Letter's Model Files .rlt";
			}
			
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String path = file.getAbsolutePath().toLowerCase();
					if (path.endsWith(".rlt")) {
							return true;
					}
				}
				return false;
			}
		};
		fc.setFileFilter(fil);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		recogn = new JButton("Recognize");
		clear = new JButton("Clear");
		clearMem = new JButton("Clear All Memory");
		switchModel = new JButton("Switch Recognize Model");
		lettField = new JTextField(2);
		
		ta = new JTextArea();
		ta.setEditable(false);
		
		JScrollPane sc = new JScrollPane(ta);
		sc.setPreferredSize(new Dimension(230, 120));
		sc.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		tb = new JTextArea();
		tb.setEditable(false);
		
		JScrollPane sc2 = new JScrollPane(tb);
		sc2.setPreferredSize(new Dimension(230, 120));
		sc2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		rightPanel.add(Box.createRigidArea(new Dimension(180,5)));
		rightPanel.add(recogn);
		rightPanel.add(Box.createRigidArea(new Dimension(15,10)));
		rightPanel.add(clear);
		rightPanel.add(Box.createRigidArea(new Dimension(30,20)));
		rightPanel.add(new JLabel("Learn"));
		rightPanel.add(Box.createRigidArea(new Dimension(8,0)));
		rightPanel.add(lettField);
		rightPanel.add(sc);
		rightPanel.add(clearMem);
		rightPanel.add(sc2);
		rightPanel.add(switchModel);
		
		
		panelRys = new DrawPanel();
		panelWys = new DrawPanel();
		
		getContentPane().add(panelRys);
		getContentPane().add(panelWys);
		getContentPane().add(rightPanel);
		setFocusable(true);
		setVisible(true);
	}
	
	public void addListeners(ActionListener al, MouseListener ml, MouseMotionListener mot, KeyListener kl) {
		panelRys.addMouseListener(ml);
		panelRys.addMouseMotionListener(mot);
		
		lettField.addKeyListener(kl);
		
		p1.setActionCommand("Load");
		p1.addActionListener(al);
		p2.setActionCommand("Save");
		p2.addActionListener(al);
		
		recogn.setActionCommand("Rec");
		recogn.addActionListener(al);
		clear.setActionCommand("clr");
		clear.addActionListener(al);
		clearMem.setActionCommand("clrMem");
		clearMem.addActionListener(al);
		switchModel.setActionCommand("SwitchModel");
		switchModel.addActionListener(al);
	}
	
	public JFileChooser getFChooser() {
		return fc;
	}
	
	public DrawPanel getDrawPanel() {
		return panelRys;
	}
	
	public DrawPanel getShowPanel() {
		return panelWys;
	}
	
	public void clearLetterField() {
		lettField.setText("");
	}
	
	public String getLetterField() throws DataFormatException, NamingException {
		if(lettField.getText().equals(""))
			throw new NamingException();
		else if(lettField.getText().length()>1) {
			lettField.setText("");
			throw new DataFormatException();
		}
		else 
			return lettField.getText().toUpperCase();
	}
	
	public void refreshLetters(ArrayList<Letter> l) {
		ta.setText("");
		for(Letter let: l) {
			ta.append(let.getName() + ": " + let.getTests().size() + "\n");
		}
	}
	
	public void clearLetters() {
		ta.setText("");
	}
	
	public void refreshProbability(ArrayList<Letter> l) {
		tb.setText("");
		for(Letter let: l) {
			tb.append(String.format("%s: %.2f\n", let.getName(), let.getPj()));
		}
	}
	
	public void clearProbability() {
		tb.setText("");
	}
}
