import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame_Prof extends JFrame {
	private GamePanel gamePanel = new GamePanel("@", "M");
	
	public GameFrame_Prof() {
		super("���ͷκ��� �޾Ƴ���");
		setSize(300, 300);
		setContentPane(gamePanel);
		setVisible(true);
		
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
	}
	
	class MonsterThread extends Thread {
		private JLabel avataLabel, monsterLabel;
		long delay;
		
		public MonsterThread(JLabel avataLabel, JLabel monsterLabel, long delay) {
			this.avataLabel = avataLabel;
			this.monsterLabel = monsterLabel;
			this.delay = delay;
		}
		
		@Override
		public void run() {
			while(true) {
				// x ��ġ ����
				if(monsterLabel.getX() < avataLabel.getX())
					monsterLabel.setLocation(monsterLabel.getX() + 5, monsterLabel.getY());
				else 
					monsterLabel.setLocation(monsterLabel.getX() - 5, monsterLabel.getY());
				
				// y ��ġ ����
				if(monsterLabel.getY() < avataLabel.getY())
					monsterLabel.setLocation(monsterLabel.getX(), monsterLabel.getY()+5);
				else 
					monsterLabel.setLocation(monsterLabel.getX(), monsterLabel.getY()-5);
				
				monsterLabel.getParent().repaint();
				
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// ���� �߰��� �κ�
					// �����尡 ����� �� monsterLabel�� ������
					Container c = monsterLabel.getParent(); // Container Ŭ���� import �� ��
					c.remove(monsterLabel); // �θ� �����̳ʿ��� �ڽ� monsterLabel�� ������� ��
					c.repaint(); // �θ� �����̳ʿ��� ���� �гο� �����ִ� �̹����� ����� �ٽ� �׸����� ��.
					             // �̶� �����̳��� ��� �ڽ��� ���� �׸�
					// �������
					return; // ������ ���� ��ų ����
				}
				
			}
		}
	}
	
	class GamePanel extends JPanel {
		private String avataChar, monsterChar;
		private JLabel avataLabel = null;
		private JLabel monsterLabel [] = new JLabel [10];
		
		// ���� �߰� ����. 10���� ������ ���۷��� �迭
		MonsterThread th [] = new MonsterThread [10];
		
		public GamePanel(String avataChar, String monsterChar) {
			this.avataChar = avataChar;
			this.monsterChar = monsterChar;
			
			avataLabel = new JLabel(avataChar);
		
			setLayout(null);
			avataLabel.setSize(30,30);
			avataLabel.setLocation(10,  10);
			add(avataLabel);

			for(int i=0; i<10; i++) {
				monsterLabel[i] = new JLabel(monsterChar);
				monsterLabel[i].setSize(30,30);
				monsterLabel[i].setLocation((int)(Math.random()*200),  
						(int)(Math.random()*200));
				add(monsterLabel[i]);
				
				// �� ���� ����
				th[i] = new MonsterThread(avataLabel, monsterLabel[i], 300);
				th[i].start();
			}
			
			
			this.addKeyListener(new MyKey());
		}
		
		
		class MyKey extends KeyAdapter {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'q')
					System.exit(-1);
				
				if(e.getKeyChar() == '\n') {
					for(int i=0; i<10; i++) {
						monsterLabel[i].setLocation((int)(Math.random()*200),  
								(int)(Math.random()*200));
					}
					return;
				}
				
				// ���� �߰��� �κ�
				if(e.getKeyChar() == 'k') {
					for(int i=0; i<10; i++) 
						th[i].interrupt(); // k Ű�� �Է��ϸ� 10���� ������ �����Ŵ
					return;
				} // �������. �������� ��� ���� �Ŀ��� �����¿� Ű�� �� �۵���!
				
				int x = avataLabel.getX();
				int y = avataLabel.getY();
				
				int keyCode = e.getKeyCode();
				switch(keyCode) {
					case KeyEvent.VK_UP : avataLabel.setLocation(x, y-10); break; 
					case KeyEvent.VK_DOWN : avataLabel.setLocation(x, y+10); break;
					case KeyEvent.VK_LEFT : avataLabel.setLocation(x-10, y); break;
					case KeyEvent.VK_RIGHT : avataLabel.setLocation(x+10, y); break;
				}
				
				avataLabel.getParent().repaint(); // 12�� 
			}
		}
	}
	
	public static void main(String[] args) {
		new GameFrame_Prof();
	}

}
