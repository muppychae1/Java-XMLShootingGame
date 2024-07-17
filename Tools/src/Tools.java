import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tools extends JFrame{
	public Tools () {
		super("저작도구");
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setLocationRelativeTo(null);
		setResizable(false);
		
		setContentPane(new MyPanel());
		
		setSize(400, 200);
		setVisible(true);
	}
	
	class MyPanel extends JPanel{
		private JLabel editLabel = new JLabel ("[ EDIT ]");
		private JLabel label = new JLabel ("Put Game Frame 'WIDTH' X 'HEIGHT'");
		private JLabel widthLabel = new JLabel ("Width  ");
		private JTextField widthText = new JTextField("0",25);
		private JLabel heightLabel = new JLabel("Height  ");
		private JTextField heightText = new JTextField("0",25);
		
		public MyPanel() {
			this.setLayout(new FlowLayout());			
			this.setBackground(Color.BLACK);
			
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("Gothic", Font.BOLD, 20));
			label.setForeground(Color.white);
			//label.setBounds(15, 70, 350, 50);
			this.add(label);
			
			widthLabel.setFont(new Font("Gothic", Font.PLAIN, 20));
			widthLabel.setForeground(Color.white);
			this.add(widthLabel);
			this.add(widthText);
			
			heightLabel.setFont(new Font("Gothic", Font.PLAIN, 20));
			heightLabel.setForeground(Color.white);
			this.add(heightLabel);
			this.add(heightText);
			
			
			editLabel.setHorizontalAlignment(JLabel.CENTER);
			editLabel.setFont(new Font("Gothic", Font.BOLD, 25));
			editLabel.setForeground(Color.white);
			editLabel.setBounds(90, 200, 100, 50);
			this.add(editLabel);
			
			editLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					editLabel.setForeground(Color.MAGENTA);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					editLabel.setForeground(Color.WHITE);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					int width = Integer.parseInt(widthText.getText());
					int height = Integer.parseInt(heightText.getText());
					if(width <= 0 || height <= 0) {
						JOptionPane.showMessageDialog(null, "다시 입력하세요","warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
					new MainToolFrame(width, height);
				}
			});
		}
	}
	
	public static void main(String [] args) {
		new Tools();
	}
}
