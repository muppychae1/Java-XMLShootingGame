import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.w3c.dom.Node;

public class GameFrame extends JFrame {
	private XMLReader xml = new XMLReader("sample2.xml");
	private GameInfoPanel gameInfoPanel = new GameInfoPanel(xml.getGameInfoPanelElement(), xml.getGamePanelElement());
//	private MainPanel gamePanel = new MainPanel(xml.getGamePanelElement(), gameInfoPanel);
	private MainPanel gamePanel = new MainPanel(xml, gameInfoPanel);
//	private GameInfoPanel gameInfoPanel = null;
//	private MainPanel gamePanel = null;
	private Node blockGameNode = null;
	
	private JMenuItem replayItem = new JMenuItem("다시시작");
	private JMenuItem exitItem = new JMenuItem("게임종료");
	private JMenuItem pauseItem = new JMenuItem("일시정지");
	private JMenuItem resumeItem = new JMenuItem("이어서");
	private JMenuItem soundOnItem = new JMenuItem("배경음 ON");
	private JMenuItem soundOffItem = new JMenuItem("배경음 OFF");

	
	public GameFrame() {
//		XMLReader xml = new XMLReader("sample2.xml");
//		GameInfoPanel gameInfoPanel = new GameInfoPanel(xml.getGameInfoPanelElement());
//		MainPanel gamePanel = new MainPanel(xml.getGamePanelElement(), gameInfoPanel);
		
		setTitle("Game");
		
		blockGameNode = xml.getBlockGameElement();
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h));
		
		//setContentPane(new GamePanel(xml.getGamePanelElement()));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 컴퓨터 화면 중앙에 뜨게 하기
		setResizable(false);
		
		splitPane();
		makeMenu();
		
		setVisible(true);
		
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
	}
	
	public void splitPane() {
		JSplitPane hPane = new JSplitPane();
		
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		hPane.setDividerLocation(70);
		hPane.setDividerSize(0);
		hPane.setEnabled(false);
		
		hPane.setBottomComponent(gamePanel);
		hPane.setTopComponent(gameInfoPanel);
		
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// 게임 메뉴에 메뉴 아이템 넣기
		JMenu gameMenu = new JMenu("게임");
		gameMenu.add(replayItem);
		gameMenu.add(exitItem);
		gameMenu.add(pauseItem);
		gameMenu.add(resumeItem);
		resumeItem.setEnabled(false);
		
		// 소리 메뉴에 메뉴 아이템 넣기
		JMenu soundMenu = new JMenu("소리");
		soundMenu.add(soundOnItem);
		soundMenu.add(soundOffItem);
		soundOnItem.setEnabled(false);
		
		
		// 메뉴바에 메뉴들 붙이기
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		
		// 메뉴 아이템에 이벤트 리스너
		replayItem.addActionListener(new replayItemListener());
		exitItem.addActionListener(new exitItemListener());
		pauseItem.addActionListener(new pauseItemListener());
		resumeItem.addActionListener(new resumeItemListener());
		soundOnItem.addActionListener(new soundOnItemListener());
		soundOffItem.addActionListener(new soundOffItemListener());

	}
	
	private class replayItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, "게임을 다시 시작하시겠습니까?", "Retry", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				Node playerNode = XMLReader.getNode(blockGameNode, XMLReader.E_PLAYER);
				int life = Integer.parseInt(XMLReader.getAttr(playerNode, "life"));
				gamePanel.setRound(1);
				gameInfoPanel.resetScore(0);
				gameInfoPanel.setLife(life);
				gamePanel.getPlayerBlock().setLife(life);
				
				gamePanel.resetGame();
				gamePanel.startGame();
			}
		}
	}
	
	private class exitItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, "정말 게임을 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}
	
	
	private class pauseItemListener implements ActionListener{
		private BlockThread [] blockThreadList = null;
		private WallThread [] wallThreadList = null;
		@Override
		public void actionPerformed(ActionEvent e) {
			blockThreadList = gamePanel.getBlockThreadList();
			for(int i=0; i<blockThreadList.length; i++) {
				if(blockThreadList[i] == null) continue;
				blockThreadList[i].setBlockRunning(false);
			}
			
			wallThreadList = gamePanel.getWallThreadList();
			for(int i=0; i<wallThreadList.length; i++) {
				if(wallThreadList[i] == null) continue;
				wallThreadList[i].setWallRunning(false);
			}
			
			gamePanel.getMainClip().stop();
			pauseItem.setEnabled(false);
			resumeItem.setEnabled(true);
		}
	}
	
	private class resumeItemListener implements ActionListener{
		private BlockThread [] blockThreadList = null;
		private WallThread [] wallThreadList = null;
		@Override
		public void actionPerformed(ActionEvent e) {
			blockThreadList = gamePanel.getBlockThreadList();
			for(int i=0; i<blockThreadList.length; i++) {
				if(blockThreadList[i] == null) continue;
				blockThreadList[i].startBlockRunning();
			}
			
			wallThreadList = gamePanel.getWallThreadList();
			for(int i=0; i<wallThreadList.length; i++) {
				if(wallThreadList[i] == null) continue;
				wallThreadList[i].startWallRunning();
			}
			
			gamePanel.getMainClip().loop(Clip.LOOP_CONTINUOUSLY);
			pauseItem.setEnabled(true);
			resumeItem.setEnabled(false);
		}
	}
	
	private class soundOnItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel.getMainClip().start();
			soundOffItem.setEnabled(true);
			soundOnItem.setEnabled(false);
		}
	}
	private class soundOffItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel.getMainClip().stop();
			soundOffItem.setEnabled(false);
			soundOnItem.setEnabled(true);
		}
	}

	
	public static void main(String [] args) {
		new GameFrame();
	}
}

class GameInfoPanel extends JPanel{
	private JLabel scoreLabel = null;
	private JLabel lifeLabel = null;
	private int score = 0;
	private int life;
	
	private Node gameInfoPanelNode = null;
	private Node gamePanelNode = null;
	
	public GameInfoPanel(Node gameInfoPanelNode, Node gamePanelNode) {
		this.gameInfoPanelNode = gameInfoPanelNode;
		this.gamePanelNode = gamePanelNode;
		this.setLayout(new GridLayout(1,1));
		
		Node bgNode = XMLReader.getNode(gameInfoPanelNode, XMLReader.E_BG);
		int r = Integer.parseInt(XMLReader.getAttr(bgNode, "r"));
		int g = Integer.parseInt(XMLReader.getAttr(bgNode, "g"));
		int b = Integer.parseInt(XMLReader.getAttr(bgNode, "b"));
		Color bgColor = new Color(r, g, b);
		setBackground(bgColor);
		
		scoreLabel = new JLabel ("Score : " + Integer.toString(score));
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("나눔스퀘어", Font.BOLD, 20));
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		add(scoreLabel);
		
		Node playerNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PLAYER);
		life = Integer.parseInt(XMLReader.getAttr(playerNode, "life"));
		lifeLabel = new JLabel("Life : " + Integer.toString(life));
		lifeLabel.setForeground(Color.white);
		lifeLabel.setFont(new Font("나눔스퀘어", Font.BOLD, 20));
		lifeLabel.setHorizontalAlignment(JLabel.CENTER);
		add(lifeLabel);
	}
	
	public void resetScore(int resetNum) {
		this.score = resetNum;
		scoreLabel.setText("Score : " + Integer.toString(this.score));
		scoreLabel.getParent().repaint();
	}

	public void setScore(int score) {
		this.score += score;
		scoreLabel.setText("Score : " + Integer.toString(this.score));
		scoreLabel.getParent().repaint();
	}
	
	public void setLife(int life) {
		this.life = life;
		lifeLabel.setText("Life : " + Integer.toString(this.life));
		lifeLabel.getParent().repaint();
	}
	
	public int getScore() {return score;}
	public int getLife() {return life;}
}

