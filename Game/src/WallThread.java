
public class WallThread extends Thread{
	private Block [] wallBlockList = null;
	private WallThread [] wallThreadList = null;
	private Block  wallBlock = null;
	private int i;
	private MainPanel gamePanel = null;
	
	private boolean running = true;
	
	public WallThread(Block [] wallBlockList, WallThread [] wallThreadList, Block wallBlock, int i, MainPanel gamePanel) {
		this.wallBlockList = wallBlockList;
		this.wallThreadList = wallThreadList;
		this.wallBlock = wallBlock;
		this.i = i;
		this.gamePanel = gamePanel;
	}
	
	synchronized public void waitForWallRunning() {
		if(!running) {
			try {
				System.out.println("WallThread wait()");
				this.wait();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	synchronized public void startWallRunning() {
		running = true;
		System.out.println("WallThread notify()");
		this.notify();
	}
	
	public void setWallRunning (boolean running) {
		this.running = running;
	}
	
	
	@Override
	public void run() {
		if(wallBlock.getDir() == 0) { // 움직이지 않는 wall
			while(true) {
				waitForWallRunning();
				try {
					Thread.sleep(wallBlock.getSpeed());
				} catch (InterruptedException e) {
					// 게임을 리셋해야되서 그냥 스레드가 종료되어야 하는 경우,
					if(gamePanel.getWallFlag()) {
						return;
					}
					
					// 총알에 명중당했을 때
					else {
						wallBlock.setHit(wallBlock.getHit() - 1); // block의 목숨 차감
						if(wallBlock.getHit() == 0) { // block의 목숨이 다 차감되었을 때
							wallBlockList[i] = null;
							wallBlock.getParent().remove(wallBlock);
							wallThreadList[i] = null;
							return;
						}
					}
				}
			} // end of while()
		}
		
		// 좌우 움직이는 블럭
		else if(wallBlock.getDir() == 1) {
			int startX = wallBlock.getX();
			int finishX = startX + wallBlock.getDistance();
			
			while(true) {
				waitForWallRunning();
				if(wallBlock.getX() == startX) {
					while(true) {
						int x = wallBlock.getX();
						int y = wallBlock.getY();
						
						if( x == finishX) break;
						
						wallBlock.setLocation(x + 5, y);
						wallBlock.getParent().repaint();
						try {
							Thread.sleep(wallBlock.getSpeed());
						} catch (InterruptedException e) {
							// 게임을 리셋해야되서 그냥 스레드가 종료되어야 하는 경우,
							if(gamePanel.getWallFlag()) {
								return;
							}
							else {
								// 총알에 명중당했을 때
								wallBlock.setHit(wallBlock.getHit() - 1); // block의 목숨 차감
								if(wallBlock.getHit() == 0) { // block의 목숨이 다 차감되었을 때
									wallBlockList[i] = null;
									wallBlock.getParent().remove(wallBlock);
									wallThreadList[i] = null;
									return;
								}
							}
						}
					}
				}
				
				if(wallBlock.getX() == finishX) {
					while(true) {
						int x = wallBlock.getX();
						int y = wallBlock.getY();
						
						if( x == startX) break;
						
						wallBlock.setLocation(x - 5, y);
						wallBlock.getParent().repaint();
						try {
							Thread.sleep(wallBlock.getSpeed());
						} catch (InterruptedException e) {
							// 게임을 리셋해야되서 그냥 스레드가 종료되어야 하는 경우,
							if(gamePanel.getWallFlag()) {
								return;
							}
							else {
								// 총알에 명중당했을 때
								wallBlock.setHit(wallBlock.getHit() - 1); // block의 목숨 차감
								if(wallBlock.getHit() == 0) { // block의 목숨이 다 차감되었을 때
									wallBlockList[i] = null;
									wallBlock.getParent().remove(wallBlock);
									wallThreadList[i] = null;
									return;
								}
							}
						}
					}
				}
				
			}//end of while()		
		} // end of else if()
		
		// 상하 움직이는 블럭
		else if(wallBlock.getDir() == -1) {
			int startY = wallBlock.getY();
			int finishY = startY + wallBlock.getDistance();
			
			while(true) {
				waitForWallRunning();
				if(wallBlock.getY() == startY) {
					while(true) {
						int x = wallBlock.getX();
						int y = wallBlock.getY();
						
						if( y == finishY) break;
						
						wallBlock.setLocation(x, y + 5);
						wallBlock.getParent().repaint();
						try {
							Thread.sleep(wallBlock.getSpeed());
						} catch (InterruptedException e) {
							// 게임을 리셋해야되서 그냥 스레드가 종료되어야 하는 경우,
							if(gamePanel.getWallFlag()) {
								return;
							}
							else {
								// 총알에 명중당했을 때
								wallBlock.setHit(wallBlock.getHit() - 1); // block의 목숨 차감
								if(wallBlock.getHit() == 0) { // block의 목숨이 다 차감되었을 때
									wallBlockList[i] = null;
									wallBlock.getParent().remove(wallBlock);
									wallThreadList[i] = null;
									return;
								}
							}
						}
					}
				}
				
				if(wallBlock.getY() == finishY) {
					while(true) {
						int x = wallBlock.getX();
						int y = wallBlock.getY();
						
						if( y == startY) break;
						
						wallBlock.setLocation(x, y - 5);
						wallBlock.getParent().repaint();
						try {
							Thread.sleep(wallBlock.getSpeed());
						} catch (InterruptedException e) {
							// 게임을 리셋해야되서 그냥 스레드가 종료되어야 하는 경우,
							if(gamePanel.getWallFlag()) {
								return;
							}
							else {
								// 총알에 명중당했을 때
								wallBlock.setHit(wallBlock.getHit() - 1); // block의 목숨 차감
								if(wallBlock.getHit() == 0) { // block의 목숨이 다 차감되었을 때
									wallBlockList[i] = null;
									wallBlock.getParent().remove(wallBlock);
									wallThreadList[i] = null;
									return;
								}
							}
						}
					}
				}
				
			}//end of while()		
		} // end of else if()
		
	}// end of run()

}
