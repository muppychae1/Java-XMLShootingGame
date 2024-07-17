import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private ImageIcon bgImg = new ImageIcon("");
	
	private int playerLabelCnt = 0; // playerLabel의 개수
	private int bulletLabelCnt = 0; // bulletLabel의 개수
	private int infoLabelCnt = 0;

	
	public GamePanel() {	
		this.setLayout(null);
	}
	
	public void setBgPath(String path) {
		bgImg = new ImageIcon(path);
		repaint();
	}
	
	public void setPlayerLabelCnt(int playerLabelCnt) {this.playerLabelCnt = playerLabelCnt;}
	public void setBulletLabelCnt(int bulletLabelCnt) {this.bulletLabelCnt = bulletLabelCnt;}
	public void setInfoLabelCnt(int infoLabelCnt) {this.infoLabelCnt = infoLabelCnt;}
	
	public int getPlayerLabelCnt() {return playerLabelCnt;}
	public int getBulletLabelCnt() {return bulletLabelCnt;}
	public int getInfoLabelCnt() {return infoLabelCnt;}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
