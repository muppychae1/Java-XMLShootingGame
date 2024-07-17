import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;

public class ListEx extends JFrame{
	private ImageIcon [] images = {
		new ImageIcon("open.JPG"),
		new ImageIcon("original.jpg"),
		new ImageIcon("save.JPG")
	};
	
	public ListEx() {
		super("");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JList<ImageIcon> imageList = new JList<ImageIcon>();
		imageList.setListData(images);
		c.add(imageList);
		
		setSize(300, 300);
		setVisible(true);
	}

	public static void main(String [] args) {
		new ListEx();
	}
}
