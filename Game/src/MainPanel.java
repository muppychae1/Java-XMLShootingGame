import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainPanel extends JPanel{
	private Clip mainClip;
	private String musicPath;
	private Clip attackClip;
	private String attackPath;
	
	private GameInfoPanel gameInfoPanel = null;
	
	private Node gamePanelNode = null;
	private Node blockNode = null;
	private NodeList blockNodeList = null;
	private Node wallNode = null;
	private NodeList wallNodeList = null;
	private Node playerNode = null;
	private Block playerBlock = null;
	private Node bulletNode = null;
	private Block bulletBlock = null;
	
	private ImageIcon bgImg = null;
	
	private Block [] blockList = null; // Block을 담을 배열
	private Block [] wallBlockList = null; // Wall Block을 담을 배열
	private BlockThread [] blockThreadList = null; // BlockThread를 담을 배열
	private WallThread [] wallThreadList = null; // WallThread를 담을 배열
	
	private int bulletW;
	private int bulletH;
	private int bulletPower;
	private int bulletSpeed;
	
	private ImageIcon bulletIcon = null;
	
	private XMLReader xml = null;
	
	private int round = 1;
	private boolean wallFlag = false; // 게임을 reset 해야하는지에 대한 판단 wallFlag = true 면, reset을 해야하는 상황
	
	public MainPanel(Node gamePanelNode, GameInfoPanel gameInfoPanel) {
		setLayout(null);
		this.gamePanelNode = gamePanelNode;
		this.gameInfoPanel = gameInfoPanel;
		
		Node stageNode = XMLReader.getNode(gamePanelNode, XMLReader.E_STAGE);
		musicPath = XMLReader.getAttr(stageNode, "music");
		System.out.println("musicPath : " + musicPath);
		
		bulletNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BULLET);
		attackPath = XMLReader.getAttr(bulletNode, "music");
		
		loadMainAudio();
		startGame();
		
	}
	
	public MainPanel(XMLReader xml, GameInfoPanel gameInfoPanel) {
		setLayout(null);
		//this.gamePanelNode = gamePanelNode;
		this.xml = xml;
		this.gameInfoPanel = gameInfoPanel;
		
//		Node stageNode = XMLReader.getNode(gamePanelNode, XMLReader.E_STAGE);
//		musicPath = XMLReader.getAttr(stageNode, "music");
//		System.out.println("musicPath : " + musicPath);
//		
//		bulletNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BULLET);
//		attackPath = XMLReader.getAttr(bulletNode, "music");
//		
//		loadMainAudio();
		startGame();
		
	}
	
	public void startGame() {
		if(round == 1) {
			xml = new XMLReader("sample1.xml");
			JOptionPane.showMessageDialog(null, "Game START","Message", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(round == 2) {
			xml = new XMLReader("sample2.xml");
			JOptionPane.showMessageDialog(null, "NEXT Stage","Message", JOptionPane.INFORMATION_MESSAGE);
		}
		else { // 게임 종료
			JOptionPane.showMessageDialog(null, "당신의 점수는 " + gameInfoPanel.getScore(),"Message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		gamePanelNode = xml.getGamePanelElement();
		Node stageNode = XMLReader.getNode(gamePanelNode, XMLReader.E_STAGE);
		musicPath = XMLReader.getAttr(stageNode, "music");
		System.out.println("musicPath : " + musicPath);
		
		bulletNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BULLET);
		attackPath = XMLReader.getAttr(bulletNode, "music");
		
		loadMainAudio();
		
//		Node stageNode = XMLReader.getNode(gamePanelNode, XMLReader.E_STAGE);
//		int round = Integer.parseInt(XMLReader.getAttr(stageNode, "round"));
		
		// 배경 설정
		Node bgNode = xml.getNode(gamePanelNode, xml.E_BG);
		String bgPath = xml.getAttr(bgNode, "img");
		bgImg = new ImageIcon(bgPath);
		
		// Block 내의 Obj
		blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		blockNodeList = blockNode.getChildNodes(); // block 밑의 Obj 노드들을 저장
		System.out.println("blockNodeList.getLength() = " + blockNodeList.getLength());
		blockList = new Block[blockNodeList.getLength() / 2]; // block 배열의 길이 지정
		int cnt = 0;
		for(int i=0; i<blockNodeList.getLength(); i++) {
			System.out.println(i + "번째");
			Node node = blockNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int attack = Integer.parseInt(XMLReader.getAttr(node, "attack"));
				int attackSpeed = Integer.parseInt(XMLReader.getAttr(node, "attackSpeed"));
				int dir = Integer.parseInt(XMLReader.getAttr(node, "dir"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				int hit = Integer.parseInt(XMLReader.getAttr(node, "hit"));
				int score = Integer.parseInt(XMLReader.getAttr(node, "score"));
				int distance = Integer.parseInt(XMLReader.getAttr(node, "distance"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				Block block = new Block(x,y,w,h,attack,attackSpeed,dir,speed,hit,score,distance,icon);
				add(block);
				
				//blockV.add(block);
				
				blockList[cnt] = block;
				cnt++;
				System.out.println("blockList 저장 성공");
			}
		}
		
		// Wall 내의 Obj
		wallNode = XMLReader.getNode(gamePanelNode, XMLReader.E_WALL);
		wallNodeList = wallNode.getChildNodes(); // wall 밑의 Obj 노드들을 저장
		
		wallBlockList = new Block [wallNodeList.getLength() / 2];
		cnt = 0;
		for(int i=0; i<wallNodeList.getLength(); i++) {
			Node node = wallNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int dir = Integer.parseInt(XMLReader.getAttr(node, "dir"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				int hit = Integer.parseInt(XMLReader.getAttr(node, "hit"));
				int distance = Integer.parseInt(XMLReader.getAttr(node, "distance"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				Block block = new Block(x,y,w,h,dir,speed,hit,distance,icon);
				add(block);
				
				//wallBlockV.add(block);
				
				wallBlockList[cnt] = block;
				cnt++;
				System.out.println("wallBlcokList 저장 성공");
			}
		}
		
		// Player
		playerNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PLAYER);
		int x = Integer.parseInt(XMLReader.getAttr(playerNode, "x"));
		int y = Integer.parseInt(XMLReader.getAttr(playerNode, "y"));
		int w = Integer.parseInt(XMLReader.getAttr(playerNode, "w"));
		int h = Integer.parseInt(XMLReader.getAttr(playerNode, "h"));
		int dir = Integer.parseInt(XMLReader.getAttr(playerNode, "dir"));
		int life = Integer.parseInt(XMLReader.getAttr(playerNode, "life"));
//		int bulletPower = Integer.parseInt(XMLReader.getAttr(playerNode, "bulletPower"));
		
		ImageIcon playerIcon = new ImageIcon(XMLReader.getAttr(playerNode, "img"));
//		ImageIcon bulletIcon = new ImageIcon(XMLReader.getAttr(playerNode, "bullet"));
		playerBlock = new Block(x,y,w,h,dir,life,playerIcon);
		add(playerBlock);
		
		this.addKeyListener(new PlayerKey());
		
		
		// 스레드를 시작하는 함수
		threadStart();
	}
	
	class PlayerKey extends KeyAdapter{
		@Override
		synchronized public void keyPressed(KeyEvent e) {
			int x = playerBlock.getX();
			int y = playerBlock.getY();
			
			int keyCode = e.getKeyCode();
			int dir = Integer.parseInt(XMLReader.getAttr(playerNode, "dir"));
			
			//if(dir == 1) { // 좌우로만 움직임 가능
				switch(keyCode) {
				case KeyEvent.VK_LEFT :
					if(x - 10 < 0) playerBlock.setLocation(0, y);
					else playerBlock.setLocation(x - 10, y);
					break;
				case KeyEvent.VK_RIGHT : 
					if(x + playerBlock.getWidth() + 10 > playerBlock.getParent().getWidth())
						playerBlock.setLocation(playerBlock.getParent().getWidth() - playerBlock.getWidth(), y);
					else playerBlock.setLocation(x + 10, y);
					break;
				case KeyEvent.VK_SPACE :
					loadAttackAudio();
					bulletNode = XMLReader.getNode(playerNode, XMLReader.E_BULLET);
					int bw = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
					int bh = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
					int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
					int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
					
					ImageIcon bulletIcon = new ImageIcon(XMLReader.getAttr(bulletNode, "img"));
					
					Block bulletBlock = new Block(bw,bh,power,speed,bulletIcon);
					bulletBlock.setLocation(playerBlock.getX() + playerBlock.getWidth()/2, playerBlock.getY() + bulletBlock.getHeight());
					add(bulletBlock);
					
//					BulletThread bulletThread = new BulletThread(bulletBlock, blockV, blockThreadV, wallBlockV, wallThreadV);
					BulletThread bulletThread = new BulletThread(bulletBlock, blockList, blockThreadList, wallBlockList, wallThreadList);
					bulletThread.start();
					break;
				}
				
			//}
			if(dir == -1) { // 상하좌우로만 움직임 가능
				switch(keyCode) {
				case KeyEvent.VK_UP : 
					if(y-5 < 0) playerBlock.setLocation(x, 0);
					else playerBlock.setLocation(x, y - 5);
					break;
				case KeyEvent.VK_DOWN :
					if(y + playerBlock.getHeight() + 5 > playerBlock.getParent().getHeight())
						playerBlock.setLocation(x, playerBlock.getParent().getHeight() - playerBlock.getHeight());
					else playerBlock.setLocation(x, y + 5);
					break;

				}
			}
			
			playerBlock.getParent().repaint();
		}
	}
	
	public void threadStart() {
		System.out.println("threadStart(), blockList.length" + blockList.length);
		//ObjThread 만들기
		blockThreadList = new BlockThread [blockList.length];
		BlockThread blockThread;
		for(int i=0; i<blockList.length; i++) {
			blockThread = null;
			if(blockList[i].getAttack() == 0) // block의 공격력이 없으면,
				blockThread = new BlockThread(blockList, blockThreadList, blockList[i], i, gameInfoPanel, this);
			else if(blockList[i].getAttack() == 1) // block의 Attack = 1 면,
				blockThread = new BlockThread(blockList, blockThreadList, blockList[i], i, gameInfoPanel, bulletNode, playerBlock, this);
			
			blockThreadList[i] = blockThread; // 지울떄는 null
			blockThread.start();
		}
		
		//WallThread 만들기
		wallThreadList = new WallThread [wallBlockList.length];
		for(int i=0; i<wallBlockList.length; i++) {
			WallThread wallThread = new WallThread(wallBlockList, wallThreadList, wallBlockList[i], i, this);

			wallThreadList[i] = wallThread;
			wallThread.start();
		}
	}
	
	
	public void resetGame() {
		// 재생중이던 노래 멈추게 하기
		mainClip.stop();
		
		// wallThread 다 멈추게 하기
		wallFlag = true;
		for(int i=0; i<wallThreadList.length; i++) {
			if(wallThreadList[i] == null) continue;
			
			wallThreadList[i].interrupt();
			wallThreadList[i] = null;
		}
		wallFlag = false;
		
		//bulletThread 다 멈추기
		this.removeAll();
		this.repaint();
		
	}
	
	public Block getPlayerBlock() {return playerBlock;}
	public int getRound() {return round;}
	public void setRound(int round) {this.round = round;}
	public boolean getWallFlag() {return wallFlag;}
	public void setWallFlag(boolean wallFlag) {this.wallFlag = wallFlag;}
	public Clip getMainClip() {return mainClip;}
	
	public BlockThread [] getBlockThreadList() {return blockThreadList;}
	public WallThread [] getWallThreadList() {return wallThreadList;}
	
	// 메인 오디오 load
	public void loadMainAudio() {
	try {
		mainClip = AudioSystem.getClip();
		File mainAudioFile = new File (musicPath);
		AudioInputStream mainAudioStream = AudioSystem.getAudioInputStream(mainAudioFile);
		mainClip.open(mainAudioStream);
		FloatControl gainControl = 
			    (FloatControl) mainClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-20.0f);
		mainClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException e) { e.printStackTrace(); }
		catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	// 공격 효과음 load
	public void loadAttackAudio() {
		try {
			attackClip = AudioSystem.getClip();
			File attackAudioFile = new File (attackPath);
			AudioInputStream attackAudioStream = AudioSystem.getAudioInputStream(attackAudioFile);
			attackClip.open(attackAudioStream);
//			FloatControl gainControl = 
//				    (FloatControl) mainClip.getControl(FloatControl.Type.MASTER_GAIN);
//				gainControl.setValue(-20.0f);
			attackClip.start();
			} catch (LineUnavailableException e) { e.printStackTrace(); }
			catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
			catch (IOException e) { e.printStackTrace(); }
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}


class Block extends JLabel{
	private Image img;
	private int attack, attackSpeed, dir, hit;
	private int speed, life;
	private int power, score, distance;
	
	//Bullet Block
	public Block(int w, int h, int power, int speed, ImageIcon icon) {
		setSize(w, h);
		this.power = power;
		this.speed = speed;
		img = icon.getImage();
	}
	
	//Wall Block
	public Block(int x, int y, int w, int h, int dir, int speed, int hit, int distance, ImageIcon icon) {
		this.setBounds(x,y,w,h);
		img = icon.getImage();
		this.dir = dir;
		this.speed = speed;
		this.hit = hit;
		this.distance = distance;
	}
	
	// Block
	public Block(int x, int y, int w, int h, int attack, int attackSpeed, int dir, int speed, int hit, int score, int distance, ImageIcon icon) {
		this.setBounds(x,y,w,h);
		img = icon.getImage();
		this.attack = attack;
		this.attackSpeed = attackSpeed;
		this.dir = dir;
		this.speed = speed;
		this.hit = hit;
		this.score = score;
		this.distance = distance;
	}
	
	//PlayerBlock
	public Block(int x,int y,int w,int h,int dir,int life,ImageIcon icon) {
		this.setBounds(x,y,w,h);
		img = icon.getImage();
		this.dir = dir;
		this.life = life;
	}
	
	public int getAttack() {return attack;}
	public int getAttackSpeed() {return attackSpeed;}
	public int getDir() {return dir;}
	public int getDistance() {return distance;}
	public int getHit() {return hit;}
	public int getPower() {return power;}
	public int getSpeed() {return speed;}
	public int getScore() {return score;}
	public int getLife() {return life;}

	public void setHit(int hit) {this.hit = hit;}
	public void setLife(int life) {this.life = life;}
	
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
