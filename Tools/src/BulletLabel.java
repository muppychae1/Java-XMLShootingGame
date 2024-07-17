import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;

public class BulletLabel extends JLabel{
	
	private BulletPanel bulletPanel = null;

	private int labelX, labelY, pointerX, pointerY, diffX, diffY;
	private int power = 1; // 기본 값 = 1
	private int speed = 20;
	private String imgPath = "";
	private String musicPath ="";
	
	private BulletLabel label = this;
	
	public BulletLabel(BulletPanel bulletPanel) {
		this.bulletPanel = bulletPanel;
		
		bulletPanel.setBulletLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
		bulletPanel.setWText(label.getWidth());
		bulletPanel.setHText(label.getHeight());
		
		musicPath = bulletPanel.getBulletMusic();
		
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseMotionListener());
	}

	class MouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			bulletPanel.setBulletLabel(label); // wallPanel에 선택된 label 이 뭔지 알려주기
			
			labelX = label.getX();
			labelY = label.getY();
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			diffX = pointerX - labelX;
			diffY = pointerY - labelY;
			
			bulletPanel.setWText(label.getWidth());
			bulletPanel.setHText(label.getHeight());
			bulletPanel.setPower(power);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			bulletPanel.setWText(label.getWidth());
			bulletPanel.setHText(label.getHeight());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) { // 더블 클릭 => 제거
				GamePanel gamePanel = (GamePanel)label.getParent();
				gamePanel.setBulletLabelCnt(gamePanel.getBulletLabelCnt() - 1);
				gamePanel.remove(label);
				gamePanel.repaint();
			}
		}
	}
	
	class MouseMotionListener extends MouseMotionAdapter{
		@Override
		public void mouseDragged(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			bulletPanel.setWText(label.getWidth());
			bulletPanel.setHText(label.getHeight());
		}
	}
	
	public void setPower(int power) {this.power = power;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setBulletImg(String imgPath) {this.imgPath= imgPath;}
	public void setBulletMusic(String musicPath) {this.musicPath= musicPath;}
		
	public int getPower() {return this.power;}
	public int getSpeed() {return this.speed;}
	public String getBulletImg() {return this.imgPath;}
	public String getBulletMusic() {return this.musicPath;}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
	    g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}
}
