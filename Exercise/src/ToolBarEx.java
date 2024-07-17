import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.management.remote.SubjectDelegationPermission;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class ToolBarEx extends JFrame {
	private Container contentPane;
	
	public ToolBarEx() {
		super("툴바 만들기 예제");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = getContentPane();
		createToolBar();
		setSize(400, 200);
		setVisible(true);
	}
	
	private void createToolBar() {
		JToolBar toolBar = new JToolBar("Kitae Menu");
		toolBar.setBackground(Color.LIGHT_GRAY);
		
		toolBar.add(new JButton("New"));
		toolBar.add(new JButton(new ImageIcon("open.JPG")));
		toolBar.addSeparator();
		toolBar.add(new JButton(new ImageIcon("save.JPG")));
		toolBar.add(new JLabel("search"));
		toolBar.add(new JTextField("text find"));
		
		JComboBox <String> combo = new JComboBox <String> ();
		combo.addItem("Java");
		combo.addItem("C++");
		combo.addItem("C");
		combo.addItem("C#");
		
		toolBar.add(combo);
		
		contentPane.add(toolBar, BorderLayout.NORTH);
	}
	
	
	public static void main (String [] args) {
		new ToolBarEx();
	}
}