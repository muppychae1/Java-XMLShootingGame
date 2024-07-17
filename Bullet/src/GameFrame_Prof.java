import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame_Prof extends JFrame {
	private GamePanel gamePanel = new GamePanel("@", "M");
	
	public GameFrame_Prof() {
		super("몬스터로부터 달아나기");
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
				// x 위치 조정
				if(monsterLabel.getX() < avataLabel.getX())
					monsterLabel.setLocation(monsterLabel.getX() + 5, monsterLabel.getY());
				else 
					monsterLabel.setLocation(monsterLabel.getX() - 5, monsterLabel.getY());
				
				// y 위치 조정
				if(monsterLabel.getY() < avataLabel.getY())
					monsterLabel.setLocation(monsterLabel.getX(), monsterLabel.getY()+5);
				else 
					monsterLabel.setLocation(monsterLabel.getX(), monsterLabel.getY()-5);
				
				monsterLabel.getParent().repaint();
				
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// 새로 추가된 부분
					// 스레드가 종료될 때 monsterLabel로 제거함
					Container c = monsterLabel.getParent(); // Container 클래스 import 할 것
					c.remove(monsterLabel); // 부모 컨테이너에게 자식 monsterLabel을 떼어내도록 함
					c.repaint(); // 부모 컨테이너에게 현재 패널에 남아있는 이미지를 지우고 다시 그리도록 함.
					             // 이때 컨테이너의 모두 자식을 새로 그림
					// 여기까지
					return; // 스레드 종료 시킬 것임
				}
				
			}
		}
	}
	
	class GamePanel extends JPanel {
		private String avataChar, monsterChar;
		private JLabel avataLabel = null;
		private JLabel monsterLabel [] = new JLabel [10];
		
		// 새로 추가 수정. 10개의 스레드 레퍼런스 배열
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
				
				// 두 라인 수정
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
				
				// 새로 추가된 부분
				if(e.getKeyChar() == 'k') {
					for(int i=0; i<10; i++) 
						th[i].interrupt(); // k 키를 입력하면 10개의 스레드 종료시킴
					return;
				} // 여기까지. 스레들을 모두 없앤 후에도 상하좌우 키를 잘 작동함!
				
				int x = avataLabel.getX();
				int y = avataLabel.getY();
				
				int keyCode = e.getKeyCode();
				switch(keyCode) {
					case KeyEvent.VK_UP : avataLabel.setLocation(x, y-10); break; 
					case KeyEvent.VK_DOWN : avataLabel.setLocation(x, y+10); break;
					case KeyEvent.VK_LEFT : avataLabel.setLocation(x-10, y); break;
					case KeyEvent.VK_RIGHT : avataLabel.setLocation(x+10, y); break;
				}
				
				avataLabel.getParent().repaint(); // 12장 
			}
		}
	}
	
	public static void main(String[] args) {
		new GameFrame_Prof();
	}

}
