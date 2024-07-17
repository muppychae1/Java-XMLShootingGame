import java.util.Vector;

import javax.swing.JOptionPane;

public class BulletThread extends Thread {
	private Block bulletBlock = null;
	private Block playerBlock = null;
	private MainPanel gamePanel = null;
	
	private BlockThread hitBlockThread = null;
	private int i;
	
	private Block [] blockList = null;
	private BlockThread [] blockThreadList = null;
	private Block [] wallBlockList = null;
	private WallThread [] wallThreadList = null;
	
	// Player �� BulletThread
	public BulletThread(Block bulletBlock, Block [] blockList, BlockThread [] blockThreadList,
			Block [] wallBlockList, WallThread [] wallThreadList) {
		this.bulletBlock = bulletBlock;
		this.blockList = blockList;
		this.blockThreadList = blockThreadList;
		this.wallBlockList = wallBlockList;
		this.wallThreadList = wallThreadList;
	}
	
	// Block �� BulletThread
	public BulletThread(Block bulletBlock, Block playerBlock, MainPanel gamePanel) {
		this.bulletBlock = bulletBlock;
		this.playerBlock = playerBlock;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		if(playerBlock == null) {
			while(true) {
				// Block ���� Ȯ��
				if(hitBlock()) {
					 // ���� Obj ���ֱ�
					blockThreadList[i].interrupt();
					bulletBlock.getParent().remove(bulletBlock);
					return;
				}
				
				// Wall Block ���� Ȯ��
				else if(hitWall()) {
					wallThreadList[i].interrupt();
					bulletBlock.getParent().remove(bulletBlock);
					return;
				}
				
				else {
					int x, y;
					x = bulletBlock.getX();
					y = bulletBlock.getY() - 5;
					if(y < 0) {
						bulletBlock.getParent().remove(bulletBlock);
						return; // ������ ������
					}
					bulletBlock.setLocation(x, y);
					bulletBlock.getParent().repaint();
				}
				try {
					sleep(bulletBlock.getSpeed());
				}catch(InterruptedException e) {}
			} 
		} // end of if(playerBlock == null)
		
		// Player ���� Ȯ��
		else {
			while(true) {
				if(hitPlayer()) {
					 // playerBlock ��� ����
					playerBlock.setLife(playerBlock.getLife() - 1);
					gamePanel.setLife(playerBlock.getLife());
//					gameInfoPanel.setLife(playerBlock.getLife());
					bulletBlock.getParent().remove(bulletBlock);
					if(playerBlock.getLife() == 0) {
						gamePanel.resetGame();
						JOptionPane.showMessageDialog(null, "GAME OVER\n����� ������ " + gamePanel.getScore(),"Message", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}
					return;
				}
				else {
					int x, y;
					x = bulletBlock.getX();
					y = bulletBlock.getY() + 5;
					if(y > bulletBlock.getParent().getHeight()) {
						bulletBlock.getParent().remove(bulletBlock);
						return; // ������ ������
					}
					
					bulletBlock.setLocation(x, y);
					bulletBlock.getParent().repaint();
				}
				try {
					sleep(bulletBlock.getSpeed());
				}catch(InterruptedException e) {}
			}
		}
	}
	
	//Block �� �¾Ҵ��� Ȯ��
	private boolean hitBlock() {
		if(blockContains(bulletBlock.getX(), bulletBlock.getY()) || 
				blockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()) ||
				blockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()+bulletBlock.getHeight() - 1) ||
				blockContains(bulletBlock.getX(), bulletBlock.getY()+bulletBlock.getHeight() - 1))
			return true;
		else
			return false;
	}
	
	private boolean blockContains(int x, int y) {
		for(int i=0; i<blockList.length; i++) {
			Block b = blockList[i];
			if(b == null) continue;
			if(((b.getX() <= x) && (b.getX() + b.getWidth() - 1 >= x)) &&
					((b.getY() <= y)&& (b.getY() + b.getHeight() - 1 >= y))) {
				this.i = i;
				return true;
			}
		}
		return false;
		
	}
	
	// Wall Block �� �¾Ҵ��� Ȯ��
	private boolean hitWall() {
		if(wallBlockContains(bulletBlock.getX(), bulletBlock.getY()) || 
				wallBlockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()) ||
				wallBlockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()+bulletBlock.getHeight() - 1) ||
				wallBlockContains(bulletBlock.getX(), bulletBlock.getY()+bulletBlock.getHeight() - 1))
			return true;
		else
			return false;
	}
	
	private boolean wallBlockContains(int x, int y) {
		for(int i=0; i<wallBlockList.length; i++) {
			Block b = wallBlockList[i];
			if(b == null) continue;
			if(((b.getX() <= x) && (b.getX() + b.getWidth() - 1 >= x)) &&
					((b.getY() <= y)&& (b.getY() + b.getHeight() - 1 >= y))) {
				this.i = i;
				return true;
			}
		}
		return false;
		
	}
	
	
	// Player Block �� �¾Ҵ��� Ȯ��	
	private boolean hitPlayer() {
		if(playerBlockContains(bulletBlock.getX(), bulletBlock.getY()) || 
				playerBlockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()) ||
				playerBlockContains(bulletBlock.getX() + bulletBlock.getWidth() - 1, bulletBlock.getY()+bulletBlock.getHeight() - 1) ||
				playerBlockContains(bulletBlock.getX(), bulletBlock.getY()+bulletBlock.getHeight() - 1))
			return true;
		else
			return false;
	}
	
	private boolean playerBlockContains(int x, int y) {
		if(((playerBlock.getX() <= x) && (playerBlock.getX() + playerBlock.getWidth() - 1 >= x)) &&
				((playerBlock.getY() <= y)&& (playerBlock.getY() + playerBlock.getHeight() - 1 >= y))) {
//			this.i = i;
			return true;
		}
		return false;
		
	}
}
