import java.util.Vector;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

public class BlockThread extends Thread{
	private Block block = null;
	private Block playerBlock = null;
	private int i;
	private int round;
	
	private Block [] blockList = null;
	private BlockThread [] blockThreadList = null;
	
	private MainPanel gamePanel = null;
	
	private Node bulletNode = null;
	
	private boolean wallFlag = false;  // ���� ���������� �ٲ� wall interrupt �� �ϱ� ���ؼ�(������ ���� ����)
	private boolean running = true;
	
	// ���ݷ��� ���� BlockThread
	public BlockThread(Block [] blockList, BlockThread [] blockThreadList, Block block, int i, MainPanel gamePanel) {
		this.blockList = blockList;
		this.blockThreadList = blockThreadList;
		this.block = block;
		this.i = i;
		this.gamePanel = gamePanel;
		this.round = gamePanel.getRound();
	}
	
	// ���ݷ��� �ִ� BlockThread
	public BlockThread(Block [] blockList, BlockThread [] blockThreadList, Block block, int i, Node bulletNode, Block playerBlock, MainPanel gamePanel) {
		this.blockList = blockList;
		this.blockThreadList = blockThreadList;
		this.block = block;
		this.i = i;
		this.gamePanel = gamePanel;
		this.bulletNode = bulletNode;
		this.playerBlock = playerBlock;
		this.round = gamePanel.getRound();
	}
	
	synchronized public void waitForBlockRunning() {
		if(!running) {
			try {
				System.out.println("Thread wait()");
				this.wait();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	synchronized public void startBlockRunning() {
		running = true;
		System.out.println("Thread notify()");
		this.notify();
	}
	
	public void setBlockRunning (boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		int cnt = 0;
		
		// �������� �ʴ� Block
		if(block.getDir() == 0) { 
			while(true) {
				waitForBlockRunning();
				// ���� �ɷ� = 1
				if(block.getAttack() == 1 && cnt % block.getAttackSpeed() == 0) { 
					int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
					int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
					int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
					int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
					
					ImageIcon bulletIcon = new ImageIcon("imgBullet/fireball.png");
					
					Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
					bulletBlock.setLocation(block.getX() + block.getWidth()/2, block.getY() + bulletBlock.getHeight());
					block.getParent().add(bulletBlock);
					
					BulletThread bulletThread = new BulletThread(bulletBlock, playerBlock, gamePanel);
					bulletThread.start();
					
				}
				try {
					Thread.sleep(block.getSpeed());
					cnt++;
					cnt %= block.getAttackSpeed();
				} catch (InterruptedException e) {
					// �Ѿ˿� ���ߴ����� ��
					block.setHit(block.getHit() - 1); // block�� ��� ����
					if(block.getHit() <= 0) { // block�� ����� �� �����Ǿ��� ��
						gamePanel.setScore(block.getScore());
						blockList[i] = null;
						block.getParent().remove(block);
						blockThreadList[i] = null;
						if(isFinish()) {
							round = round + 1;
							gamePanel.setRound(round);
							gamePanel.resetGame();
							gamePanel.startGame();
						}
						return;
					}
				}
				
			}
		} // end of if()
		
		
		// �¿�� �����̴� Block
		else if(block.getDir() == 1) { 
			int startX = block.getX();
			int finishX = block.getX() + block.getDistance();
			
			while(true) {
				waitForBlockRunning();
				if(block.getX() == startX) {
					while(true) {
						// ���� �ɷ� = 1
						if(block.getAttack() == 1 && cnt % block.getAttackSpeed() == 0) { 
							int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
							int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
							int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
							int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
							
							ImageIcon bulletIcon = new ImageIcon("imgBullet/fireball.png");
							
							Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
							bulletBlock.setLocation(block.getX() + block.getWidth()/2, block.getY() + bulletBlock.getHeight());
							block.getParent().add(bulletBlock);
							
							BulletThread bulletThread = new BulletThread(bulletBlock, playerBlock, gamePanel);
							bulletThread.start();
						}
						
						int x = block.getX();
						int y = block.getY();
						
						if(x == finishX) break;
						
						block.setLocation(x + 5, y);
						block.getParent().repaint();
						try {
							Thread.sleep(block.getSpeed());
							cnt++;
							cnt %= block.getAttackSpeed();
						} catch (InterruptedException e) {
							// �Ѿ˿� ���ߴ����� ��
							block.setHit(block.getHit() - 1); // block�� ��� ����
							if(block.getHit() <= 0) { // block�� ����� �� �����Ǿ��� ��
								gamePanel.setScore(block.getScore());
								blockList[i] = null;
								block.getParent().remove(block);
								blockThreadList[i] = null;
								if(isFinish()) {
									round = round + 1;
									gamePanel.setRound(round);
									gamePanel.resetGame();
									gamePanel.startGame();
								}
								return;
							}
						}
					}
				} // end of if
				
				else if(block.getX() == finishX) {
					while(true) {
						// ���� �ɷ� = 1
						if(block.getAttack() == 1 && cnt % block.getAttackSpeed() == 0) { 
							int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
							int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
							int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
							int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
							
							ImageIcon bulletIcon = new ImageIcon("imgBullet/fireball.png");
							
							Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
							bulletBlock.setLocation(block.getX() + block.getWidth()/2, block.getY() + bulletBlock.getHeight());
							block.getParent().add(bulletBlock);
							
							BulletThread bulletThread = new BulletThread(bulletBlock, playerBlock, gamePanel);
							bulletThread.start();
						}
						
						int x = block.getX();
						int y = block.getY();
						
						if(x == startX) break;
						
						block.setLocation(x - 5, y);
						block.getParent().repaint();
						try {
							Thread.sleep(block.getSpeed());
							cnt++;
							cnt %= block.getAttackSpeed();
						} catch (InterruptedException e) {
							// �Ѿ˿� ���ߴ����� ��
							block.setHit(block.getHit() - 1); // block�� ��� ����
							if(block.getHit() <= 0) { // block�� ����� �� �����Ǿ��� ��
								gamePanel.setScore(block.getScore());
								blockList[i] = null;
								block.getParent().remove(block);
								blockThreadList[i] = null;
								if(isFinish()) {
									round = round + 1;
									gamePanel.setRound(round);
									gamePanel.resetGame();
									gamePanel.startGame();
								}
								return;
							}
						}
					}
				} // end of else-if
			}
			
		} // end of else-if
		
		
		// ���Ϸ� �����̴� Block
		else if(block.getDir() == -1) { 
			int startY = block.getY();
			int finishY = block.getY() + block.getDistance();
			
			while(true) {
				waitForBlockRunning();
				if(block.getY() == startY) {
					while(true) {
						// ���� �ɷ� = 1
						if(block.getAttack() == 1 && cnt % block.getAttackSpeed() == 0) { 
							int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
							int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
							int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
							int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
							
							ImageIcon bulletIcon = new ImageIcon("imgBullet/fireball.png");
							
							Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
							bulletBlock.setLocation(block.getX() + block.getWidth()/2, block.getY() + bulletBlock.getHeight());
							block.getParent().add(bulletBlock);
							
							BulletThread bulletThread = new BulletThread(bulletBlock, playerBlock, gamePanel);
							bulletThread.start();
						}
						
						int x = block.getX();
						int y = block.getY();
						
						if(y == finishY) break;
						
						block.setLocation(x, y + 5);
						block.getParent().repaint();
						try {
							Thread.sleep(block.getSpeed());
							cnt++;
							cnt %= block.getAttackSpeed();
						} catch (InterruptedException e) {
							// �Ѿ˿� ���ߴ����� ��
							block.setHit(block.getHit() - 1); // block�� ��� ����
							if(block.getHit() <= 0) { // block�� ����� �� �����Ǿ��� ��
								gamePanel.setScore(block.getScore());
								blockList[i] = null;
								block.getParent().remove(block);
								blockThreadList[i] = null;
								if(isFinish()) {
									round = round + 1;
									gamePanel.setRound(round);
									gamePanel.resetGame();
									gamePanel.startGame();
								}
								return;
							}
						}
					}
				} // end of if
				
				else if(block.getY() == finishY) {
					while(true) {
						// ���� �ɷ� = 1
						if(block.getAttack() == 1 && cnt % block.getAttackSpeed() == 0) { 
							int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
							int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
							int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
							int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
							
							ImageIcon bulletIcon = new ImageIcon("imgBullet/fireball.png");
							
							Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
							bulletBlock.setLocation(block.getX() + block.getWidth()/2, block.getY() + bulletBlock.getHeight());
							block.getParent().add(bulletBlock);
							
							BulletThread bulletThread = new BulletThread(bulletBlock, playerBlock, gamePanel);
							bulletThread.start();
						}
						
						int x = block.getX();
						int y = block.getY();
						
						if(y == startY) break;
						
						block.setLocation(x, y - 5);
						block.getParent().repaint();
						try {
							Thread.sleep(block.getSpeed());
							cnt++;
							cnt %= block.getAttackSpeed();
						} catch (InterruptedException e) {
							// �Ѿ˿� ���ߴ����� ��
							block.setHit(block.getHit() - 1); // block�� ��� ����
							if(block.getHit() <= 0) { // block�� ����� �� �����Ǿ��� ��
								gamePanel.setScore(block.getScore());
								blockList[i] = null;
								block.getParent().remove(block);
								blockThreadList[i] = null;
								if(isFinish()) {
									round = round + 1;
									gamePanel.setRound(round);
									gamePanel.resetGame();
									gamePanel.startGame();
								}
								return;
							}
						}
					}
				} // end of else-if
			}
			
		} // end of else-if
		
	}
	
	private boolean isFinish() {
		for(int i=0; i<blockList.length; i++) {
			if(blockList[i] != null)
				return false;
		}
		gamePanel.setWallFlag(true);// resetGame ���� true�� �ٲ������
		return true;
	}


}
