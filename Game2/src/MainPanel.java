import java.awt.Color;
import java.awt.Font;
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
	
	private Node gamePanelNode = null;
	private NodeList infoNodeList = null;
	private Node blockNode = null;
	private NodeList blockNodeList = null;
	private Node wallNode = null;
	private NodeList wallNodeList = null;
	private Node playerNode = null;
	private Block playerBlock = null;
	private Node bulletNode = null;
	private Block bulletBlock = null;
	
	private ImageIcon bgImg = null;
	
	private JLabel [] infoList = null;
	private Block [] blockList = null; // Block�� ���� �迭
	private Block [] wallBlockList = null; // Wall Block�� ���� �迭
	private BlockThread [] blockThreadList = null; // BlockThread�� ���� �迭
	private WallThread [] wallThreadList = null; // WallThread�� ���� �迭
	
	private int bulletW;
	private int bulletH;
	private int bulletPower;
	private int bulletSpeed;
	private int score = 0;
	private int life = 0;
	
	private ImageIcon bulletIcon = null;
	
	private XMLReader xml = null;
	
	private int round = 1;
	private boolean wallFlag = false; // ������ reset �ؾ��ϴ����� ���� �Ǵ� wallFlag = true ��, reset�� �ؾ��ϴ� ��Ȳ
	
	private JLabel scoreLabel;
	private String scoreText = "";
	private JLabel lifeLabel;
	private String lifeText = "";
	

	
	public MainPanel(XMLReader xml) {
		setLayout(null);
		this.xml = xml;
		
		startGame();
		
	}
	
	public void startGame() {
		if(round == 1) {
			xml = new XMLReader("sample6.xml");
			JOptionPane.showMessageDialog(null, "Game START","Message", JOptionPane.INFORMATION_MESSAGE);
		}
//		else if(round == 2) {
//			xml = new XMLReader("sample2.xml");
//			JOptionPane.showMessageDialog(null, "NEXT Stage","Message", JOptionPane.INFORMATION_MESSAGE);
//		}
		else { // ���� ����
			JOptionPane.showMessageDialog(null, "����� ������ " + score,"Message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		
		
		gamePanelNode = xml.getGamePanelElement();
		
		Node bgNode = xml.getNode(gamePanelNode, xml.E_BG);
		musicPath = XMLReader.getAttr(bgNode, "music");
		
		infoNodeList = bgNode.getChildNodes();
		infoList = new JLabel[infoNodeList.getLength() / 2]; // block �迭�� ���� ����
		playerNode = xml.getNode(gamePanelNode, xml.E_PLAYER);
		life = Integer.parseInt(xml.getAttr(playerNode, "life"));
		
		for(int i=0; i<infoNodeList.getLength(); i++) {
			Node node = infoNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(xml.E_INFO)) {
				int x = Integer.parseInt(xml.getAttr(node, "x"));
				int y = Integer.parseInt(xml.getAttr(node, "y"));
				int w = Integer.parseInt(xml.getAttr(node, "w"));
				int h = Integer.parseInt(xml.getAttr(node, "h"));
				int r = Integer.parseInt(xml.getAttr(node, "r"));
				int g = Integer.parseInt(xml.getAttr(node, "g"));
				int b = Integer.parseInt(xml.getAttr(node, "b"));
				int type = Integer.parseInt(xml.getAttr(node, "type"));
				
				if(type == 0) {
					scoreText = xml.getAttr(node, "text");
					scoreLabel = new JLabel(scoreText + score);
					Color color  = new Color(r,g,b);
					scoreLabel.setForeground(color);
					scoreLabel.setFont(new Font("����������", Font.BOLD, 20));
					scoreLabel.setBounds(x, y, w, h);
					add(scoreLabel);
				}
				
				else if(type == 1) {
					lifeText = xml.getAttr(node, "text");
					lifeLabel = new JLabel(lifeText + life);
					Color color  = new Color(r,g,b);
					lifeLabel.setForeground(color);
					lifeLabel.setFont(new Font("����������", Font.BOLD, 20));
					lifeLabel.setBounds(x, y, w, h);
					add(lifeLabel);
				}
			}
			
		}//end of for()
		
		
		
		
		bulletNode = xml.getNode(gamePanelNode, XMLReader.E_BULLET);
		attackPath = xml.getAttr(bulletNode, "music");
		
		loadMainAudio();
		
		
		// ��� ����
		String bgPath = xml.getAttr(bgNode, "img");
		bgImg = new ImageIcon(bgPath);
		
		// Block ���� Obj
		blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		blockNodeList = blockNode.getChildNodes(); // block ���� Obj ������ ����
		System.out.println("blockNodeList.getLength() = " + blockNodeList.getLength());
		blockList = new Block[blockNodeList.getLength() / 2]; // block �迭�� ���� ����
		int cnt = 0;
		for(int i=0; i<blockNodeList.getLength(); i++) {
			System.out.println(i + "��°");
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
				
				blockList[cnt] = block;
				cnt++;
				System.out.println("blockList ���� ����");
			}
		}
		
		// Wall ���� Obj
		wallNode = XMLReader.getNode(gamePanelNode, XMLReader.E_WALL);
		wallNodeList = wallNode.getChildNodes(); // wall ���� Obj ������ ����
		
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
				System.out.println("wallBlcokList ���� ����");
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
		
		ImageIcon playerIcon = new ImageIcon(XMLReader.getAttr(playerNode, "img"));
		playerBlock = new Block(x,y,w,h,dir,life,playerIcon);
		add(playerBlock);
		
		this.addKeyListener(new PlayerKey());
		
		
		// �����带 �����ϴ� �Լ�
		threadStart();
	}
	
	class PlayerKey extends KeyAdapter{
		@Override
		synchronized public void keyPressed(KeyEvent e) {
			int x = playerBlock.getX();
			int y = playerBlock.getY();
			
			int keyCode = e.getKeyCode();
			int dir = Integer.parseInt(XMLReader.getAttr(playerNode, "dir"));
			
			//if(dir == 1) { // �¿�θ� ������ ����
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
					
					BulletThread bulletThread = new BulletThread(bulletBlock, blockList, blockThreadList, wallBlockList, wallThreadList);
					bulletThread.start();
					break;
				}
				
			//}
			if(dir == -1) { // �����¿�θ� ������ ����
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
		//ObjThread �����
		blockThreadList = new BlockThread [blockList.length];
		BlockThread blockThread;
		for(int i=0; i<blockList.length; i++) {
			blockThread = null;
			if(blockList[i].getAttack() == 0) // block�� ���ݷ��� ������,
				blockThread = new BlockThread(blockList, blockThreadList, blockList[i], i, this);
			else if(blockList[i].getAttack() == 1) // block�� Attack = 1 ��,
				blockThread = new BlockThread(blockList, blockThreadList, blockList[i], i, bulletNode, playerBlock, this);
			
			blockThreadList[i] = blockThread; // ����� null
			blockThread.start();
		}
		
		//WallThread �����
		wallThreadList = new WallThread [wallBlockList.length];
		for(int i=0; i<wallBlockList.length; i++) {
			WallThread wallThread = new WallThread(wallBlockList, wallThreadList, wallBlockList[i], i, this);

			wallThreadList[i] = wallThread;
			wallThread.start();
		}
	}
	
	
	public void resetGame() {
		// ������̴� �뷡 ���߰� �ϱ�
		mainClip.stop();
		
		// wallThread �� ���߰� �ϱ�
		wallFlag = true;
		for(int i=0; i<wallThreadList.length; i++) {
			if(wallThreadList[i] == null) continue;
			
			wallThreadList[i].interrupt();
			wallThreadList[i] = null;
		}
		wallFlag = false;
		
		//bulletThread �� ���߱�
		this.removeAll();
		this.repaint();
		
	}
	
	public Block getPlayerBlock() {return playerBlock;}
	public int getRound() {return round;}
	public void setRound(int round) {this.round = round;}
	public boolean getWallFlag() {return wallFlag;}
	public void setWallFlag(boolean wallFlag) {this.wallFlag = wallFlag;}
	public Clip getMainClip() {return mainClip;}
	
	public void setScore(int point) {
		this.score += point;
		this.scoreLabel.setText(scoreText + Integer.toString(score));
	}
	public void setLife(int life) {
		this.life = life;
		this.lifeLabel.setText(lifeText + Integer.toString(life));
	}
	public int getScore() {return this.score;}
	public int resetScore() {return this.score = 0;}
	
	public BlockThread [] getBlockThreadList() {return blockThreadList;}
	public WallThread [] getWallThreadList() {return wallThreadList;}
	
	// ���� ����� load
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
	
	// ���� ȿ���� load
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
