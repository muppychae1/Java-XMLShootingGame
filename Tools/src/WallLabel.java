import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;

public class WallLabel extends JLabel{
	private WallPanel wallPanel = null;
	
	private int labelX, labelY, pointerX, pointerY, diffX, diffY;
	private int direction = 0; // 기본 값 = 0
	private int speed = 50; // 기본 값 = 50
	private int distance = 100; // 기본 값 = 100
	private int life = 0; // 기본 값 = 0
	private String imgPath = "";
	
	private WallLabel label = this;
	
	public WallLabel(WallPanel wallPanel) {
		this.wallPanel = wallPanel;
		
		wallPanel.setWallLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
		wallPanel.setXText(label.getX());
		wallPanel.setYText(label.getY());
		wallPanel.setWText(label.getWidth());
		wallPanel.setHText(label.getHeight());
		wallPanel.setDirectionRadio(direction);
		
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseMotionListener());
	}
	
	class MouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			wallPanel.setWallLabel(label); // wallPanel에 선택된 label 이 뭔지 알려주기
			
			labelX = label.getX();
			labelY = label.getY();
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			diffX = pointerX - labelX;
			diffY = pointerY - labelY;
			
			wallPanel.setXText(label.getX());
			wallPanel.setYText(label.getY());
			wallPanel.setWText(label.getWidth());
			wallPanel.setHText(label.getHeight());
			wallPanel.setDirectionRadio(direction);
			wallPanel.setSpeed(speed);
			wallPanel.setDistance(distance);
			wallPanel.setLife(life);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			wallPanel.setXText(label.getX());
			wallPanel.setYText(label.getY());
			wallPanel.setWText(label.getWidth());
			wallPanel.setHText(label.getHeight());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) { // 더블 클릭 => 제거
				wallPanel.getSelectedWallLabelV().remove(label);
				GamePanel gamePanel = (GamePanel)label.getParent();
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
			
			wallPanel.setXText(label.getX());
			wallPanel.setYText(label.getY());
			wallPanel.setWText(label.getWidth());
			wallPanel.setHText(label.getHeight());
		}
	}
	
	public void setDirection(int dir) {this.direction = dir;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setDistance(int distance) {this.distance = distance;}
	public void setLife(int life) {this.life = life;}
	public void setWallImage(String imgPath) {this.imgPath = imgPath;}
	
	public int getLabelX() {return labelX;}
	public int getLabelY() {return labelY;}
	public int getDirection() {return direction;}
	public int getSpeed() {return speed;}
	public int getDistance() {return distance;}
	public int getLife() {return life;}
	public String getWallImg() {return this.imgPath;}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(new Color(98,70,55));
		g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
	    g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}
	
}
