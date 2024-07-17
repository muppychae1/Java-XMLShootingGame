import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.management.remote.SubjectDelegationPermission;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabbedPaneEx extends JFrame {

	public TabbedPaneEx() {
		super("ÅÇÆÒ");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		JTabbedPane pane = createTabbedPane();
		
		c.add(pane, BorderLayout.CENTER);
		setSize(250, 250);
		setVisible(true);
	}
	
	public JTabbedPane createTabbedPane() {
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("tab1", new JLabel(new ImageIcon("original.jpg")));
		pane.addTab("tab2", new JLabel( new ImageIcon("open.JPG")));
		pane.addTab("tab3", new MyPanel());
		return pane;
	}
	
	class MyPanel extends JPanel{
		public MyPanel() {
			this.setBackground(Color.YELLOW);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.red);
			g.fillRect(10, 10, 50, 50);
			g.setColor(Color.blue);
			g.fillOval(10, 70, 50, 50);
			g.setColor(Color.black);
			g.drawString("tab3¿¡ µé¾î°¡´Â JPanelÀÔ´Ï´Ù.", 30, 50);
		}
	}
	
	public static void main(String [] args) {
		new TabbedPaneEx();
	}
}
