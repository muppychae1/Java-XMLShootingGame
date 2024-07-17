import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.print.attribute.Size2DSyntax;
import javax.swing.JLabel;

public class InfoLabel extends JLabel {
	private BgPanel bgPanel = null;
	
	private int labelX, labelY, pointerX, pointerY, diffX, diffY;
	private int type = 0;
	private Color labelColor = this.getForeground();
	
	private InfoLabel label = this;
	
	public InfoLabel(BgPanel bgPanel) {
		this.bgPanel = bgPanel;
		
		bgPanel.setInfoLabel(label); // blockPanel에 선택된 label 이 뭔지 알려주기
		bgPanel.setXText(label.getX());
		bgPanel.setYText(label.getY());
		bgPanel.setWText(label.getWidth());
		bgPanel.setHText(label.getHeight());
		bgPanel.setTypeCombo(type);		
		
		this.setFont(new Font("나눔스퀘어", Font.BOLD, 20));
		
		this.addMouseListener(new MouseListener());
		this.addMouseMotionListener(new MouseMotionListener());
	}
	
	class MouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			bgPanel.setInfoLabel(label);
			
			labelX = label.getX();
			labelY = label.getY();
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			diffX = pointerX - labelX;
			diffY = pointerY - labelY;
			
			bgPanel.setTypeCombo(type);
//			bgPanel.setText(label.getText());
			bgPanel.setXText(label.getX());
			bgPanel.setYText(label.getY());
			bgPanel.setWText(label.getWidth());
			bgPanel.setHText(label.getHeight());
			bgPanel.setColor(label.getForeground());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			pointerX =label.getParent().getMousePosition().x;
			pointerY = label.getParent().getMousePosition().y;
			labelX = pointerX - diffX;
			labelY = pointerY - diffY;
			label.setBounds(labelX, labelY, label.getWidth(), label.getHeight());
			repaint();
			
			bgPanel.setXText(label.getX());
			bgPanel.setYText(label.getY());
			bgPanel.setWText(label.getWidth());
			bgPanel.setHText(label.getHeight());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) { // 더블 클릭 => 제거
				bgPanel.getSelectedInfoLabelV().remove(label);
				GamePanel gamePanel = (GamePanel)label.getParent();
				gamePanel.setInfoLabelCnt(gamePanel.getInfoLabelCnt() - 1);
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
			
			bgPanel.setXText(label.getX());
			bgPanel.setYText(label.getY());
			bgPanel.setWText(label.getWidth());
			bgPanel.setHText(label.getHeight());
		}
	}
	public void setType(int type) {this.type = type;}
	
	public int getLabelX() {return labelX;}
	public int getLabelY() {return labelY;}
	public int getType() {return type;}
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
	    g2.drawRect(0, 0, this.getWidth(), this.getHeight());
	}

}
