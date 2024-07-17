import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;

public class PlayerLabel extends JLabel {
	private PlayerPanel playerPanel = null;
	
	private int labelX, labelY, pointerX, pointerY, diffX, diffY;
	private int direction = 0; // 기본 값 = 0
	private int life = 1; // 기본 값 = 1
	private String imgPath = "";
	
	private PlayerLabel label = this;
	
	public PlayerLabel(PlayerPanel playerPanel) {
		this.playerPanel = playerPanel;
		
		playerPanel.setPlayerLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
		playerPanel.setXText(label.getX());
		playerPanel.setYText(label.getY());
		playerPanel.setWText(label.getWidth());
		playerPanel.setHText(label.getHeight());
		playerPanel.setDirectionRadio(direction);
		
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseMotionListener());
	}
	
	class MouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			playerPanel.setPlayerLabel(label); // wallPanel에 선택된 label 이 뭔지 알려주기
			
			labelX = label.getX();
			labelY = label.getY();
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			diffX = pointerX - labelX;
			diffY = pointerY - labelY;
			
			playerPanel.setXText(label.getX());
			playerPanel.setYText(label.getY());
			playerPanel.setWText(label.getWidth());
			playerPanel.setHText(label.getHeight());
			playerPanel.setDirectionRadio(direction);
			playerPanel.setLife(life);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			playerPanel.setXText(label.getX());
			playerPanel.setYText(label.getY());
			playerPanel.setWText(label.getWidth());
			playerPanel.setHText(label.getHeight());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) { // 더블 클릭 => 제거
				GamePanel gamePanel = (GamePanel)label.getParent();
				gamePanel.setPlayerLabelCnt(gamePanel.getPlayerLabelCnt() - 1);
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
			
			playerPanel.setXText(label.getX());
			playerPanel.setYText(label.getY());
			playerPanel.setWText(label.getWidth());
			playerPanel.setHText(label.getHeight());
		}
	}
	
	public void setDirection(int dir) {this.direction = dir;}
	public void setLife(int life) {this.life = life;}
	public void setPlayerImg(String imgPath) {this.imgPath = imgPath;}
	
	public int getLabelX() {return labelX;}
	public int getLabelY() {return labelY;}
	public int getDirection() {return direction;}
	public int getLife() {return life;}
	public String getPlayerImg() {return playerPanel.getPlayerImg();}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.YELLOW);
		g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
	    g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}
}
