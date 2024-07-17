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
	
	private boolean wallFlag = false;  // 게임 스테이지가 바뀔때 wall interrupt 를 하기 위해서(명중할 경우와 구분)
	private boolean running = true;
	
	// 공격력이 없는 BlockThread
	public BlockThread(Block [] blockList, BlockThread [] blockThreadList, Block block, int i, MainPanel gamePanel) {
		this.blockList = blockList;
		this.blockThreadList = blockThreadList;
		this.block = block;
		this.i = i;
		this.gamePanel = gamePanel;
		this.round = gamePanel.getRound();
	}
	
	// 공격력이 있는 BlockThread
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
		
		// 움직이지 않는 Block
		if(block.getDir() == 0) { 
			while(true) {
				waitForBlockRunning();
				// 공격 능력 = 1
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
					// 총알에 명중당했을 때
					block.setHit(block.getHit() - 1); // block의 목숨 차감
					if(block.getHit() <= 0) { // block의 목숨이 다 차감되었을 때
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
		
		
		// 좌우로 움직이는 Block
		else if(block.getDir() == 1) { 
			int startX = block.getX();
			int finishX = block.getX() + block.getDistance();
			
			while(true) {
				waitForBlockRunning();
				if(block.getX() == startX) {
					while(true) {
						// 공격 능력 = 1
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
							// 총알에 명중당했을 때
							block.setHit(block.getHit() - 1); // block의 목숨 차감
							if(block.getHit() <= 0) { // block의 목숨이 다 차감되었을 때
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
						// 공격 능력 = 1
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
							// 총알에 명중당했을 때
							block.setHit(block.getHit() - 1); // block의 목숨 차감
							if(block.getHit() <= 0) { // block의 목숨이 다 차감되었을 때
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
		
		
		// 상하로 움직이는 Block
		else if(block.getDir() == -1) { 
			int startY = block.getY();
			int finishY = block.getY() + block.getDistance();
			
			while(true) {
				waitForBlockRunning();
				if(block.getY() == startY) {
					while(true) {
						// 공격 능력 = 1
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
							// 총알에 명중당했을 때
							block.setHit(block.getHit() - 1); // block의 목숨 차감
							if(block.getHit() <= 0) { // block의 목숨이 다 차감되었을 때
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
						// 공격 능력 = 1
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
							// 총알에 명중당했을 때
							block.setHit(block.getHit() - 1); // block의 목숨 차감
							if(block.getHit() <= 0) { // block의 목숨이 다 차감되었을 때
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
		gamePanel.setWallFlag(true);// resetGame 에서 true로 바꿔줘야함
		return true;
	}


}
