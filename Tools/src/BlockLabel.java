import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;

import javax.swing.JLabel;

public class BlockLabel extends JLabel{	
	private BlockPanel blockPanel = null;
	
	private int labelX, labelY, pointerX, pointerY, diffX, diffY;
	private int direction = 0; // 기본 값 = 0
	private int speed = 50; // 기본 값 = 50
	private int distance = 100; // 기본 값 = 100
	private int attack = 0; // 기본 값 = 0
	private int attackSpeed = 50; // 기본 값 = 50
	private int life = 1; // 기본 값 = 1
	private int score = 10; // 기본 값 = 10;
	private String imgPath = "";
	
//	private boolean isClicked = false;
	
	private BlockLabel label = this;
	
	public BlockLabel(BlockPanel blockPanel) {
		this.blockPanel = blockPanel;
		
		blockPanel.setBlockLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
		blockPanel.setXText(label.getX());
		blockPanel.setYText(label.getY());
		blockPanel.setWText(label.getWidth());
		blockPanel.setHText(label.getHeight());
		blockPanel.setDirectionRadio(direction);
		blockPanel.setAttackRadio(attack);
		
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseMotionListener());
	}
	
	class MouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			blockPanel.setBlockLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
			
			labelX = label.getX();
			labelY = label.getY();
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			diffX = pointerX - labelX;
			diffY = pointerY - labelY;
			
			blockPanel.setXText(label.getX());
			blockPanel.setYText(label.getY());
			blockPanel.setWText(label.getWidth());
			blockPanel.setHText(label.getHeight());
			blockPanel.setDirectionRadio(direction);
			blockPanel.setSpeed(speed);
			blockPanel.setDistance(distance);
			blockPanel.setAttackRadio(attack);
			blockPanel.setAttackSpeed(attackSpeed);
			blockPanel.setLife(life);
			blockPanel.setScore(score);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			blockPanel.setXText(label.getX());
			blockPanel.setYText(label.getY());
			blockPanel.setWText(label.getWidth());
			blockPanel.setHText(label.getHeight());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) { // 더블 클릭 => 제거
				blockPanel.getSelectedBlockLabelV().remove(label);
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
			
			blockPanel.setXText(label.getX());
			blockPanel.setYText(label.getY());
			blockPanel.setWText(label.getWidth());
			blockPanel.setHText(label.getHeight());
		}
	}
	
	public void setDirection(int dir) {this.direction = dir;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setDistance(int distance) {this.distance = distance;}
	public void setAttack(int attack) {this.attack = attack;}
	public void setAttackSpeed(int attackSpeed) {this.attackSpeed = attackSpeed;}
	public void setLife(int life) {this.life = life;}
	public void setScore(int score) {this.score = score;}
	public void setBlockImage(String imgPath) {this.imgPath = imgPath;}

	public int getLabelX() {return labelX;}
	public int getLabelY() {return labelY;}
	public int getDirection() {return direction;}
	public int getSpeed() {return speed;}
	public int getDistance() {return distance;}
	public int getAttack() {return attack;}
	public int getAttackSpeed() {return attackSpeed;}
	public int getLife() {return life;}
	public int getScore() {return score;}
	public String getBlockImg() {return this.imgPath;}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
//		if(isClicked)
//			g2.setColor(Color.RED);
//		else 
			g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
	    g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}
}
