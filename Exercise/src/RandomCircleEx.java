import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RandomCircleEx extends JFrame{
	
	public RandomCircleEx() {
		super("Make Drawing to Start");
		setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyPanel myPanel = new MyPanel();
		this.setContentPane(myPanel);
		
		setVisible(true);
		
		System.out.println(myPanel.toString());
	}
	
	public static void main(String [] args) {
		new RandomCircleEx();
	}
	
	class MyPanel extends JPanel{
		public MyPanel () {
			
			CircleThread th = new CircleThread();
			th.start();
			
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					th.startDrawing();
				}
				@Override
				public void mouseExited (MouseEvent e) {
					System.out.println("drawing Change to false");
					th.set(false);
				}
			});
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.MAGENTA);
			int x = (int)(Math.random()*this.getParent().getWidth());
			int y = (int)(Math.random()*this.getParent().getHeight());
			int w = (int)(Math.random()*this.getParent().getWidth() + 1);
			int h = (int)(Math.random()*this.getParent().getHeight() + 1);
			g.fillOval(x, y, w, h);
		}
		
		public String toString() {
			return "myPanel ¿‘¥œ¥Ÿ.";
		}
	}
	
	class CircleThread extends Thread{
		private MyPanel myPanel = null;
		private boolean drawing = false;
		
		public CircleThread() {	}
		
		synchronized public void waitForDrawing() {
			if(!drawing) {
				try {
					System.out.println("Thread wait()");
					Thread.currentThread().wait();
					//this.wait();
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		
		synchronized public void startDrawing() {
			drawing = true;
			System.out.println("Thread notify()");
			this.notify();
		}
		
		public void set(boolean drawing) {
			this.drawing = drawing;
		}
		
		
		@Override
		public void run() {
			while(true) {
				waitForDrawing();
				repaint();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		
	}
	
}