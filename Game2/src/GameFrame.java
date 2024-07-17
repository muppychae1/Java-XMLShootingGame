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
	private XMLReader xml = new XMLReader("sample6.xml");
	private MainPanel gamePanel = new MainPanel(xml);
	private Node blockGameNode = null;
	
	private JMenuItem replayItem = new JMenuItem("�ٽý���");
	private JMenuItem exitItem = new JMenuItem("��������");
	private JMenuItem pauseItem = new JMenuItem("�Ͻ�����");
	private JMenuItem resumeItem = new JMenuItem("�̾");
	private JMenuItem soundOnItem = new JMenuItem("����� ON");
	private JMenuItem soundOffItem = new JMenuItem("����� OFF");

	
	public GameFrame() {		
		setTitle("Game");
		
		blockGameNode = xml.getBlockGameElement();
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // ��ǻ�� ȭ�� �߾ӿ� �߰� �ϱ�
		setResizable(false);
		this.setContentPane(gamePanel);
		makeMenu();
		
		setVisible(true);
		
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// ���� �޴��� �޴� ������ �ֱ�
		JMenu gameMenu = new JMenu("����");
		gameMenu.add(replayItem);
		gameMenu.add(exitItem);
		gameMenu.add(pauseItem);
		gameMenu.add(resumeItem);
		resumeItem.setEnabled(false);
		
		// �Ҹ� �޴��� �޴� ������ �ֱ�
		JMenu soundMenu = new JMenu("�Ҹ�");
		soundMenu.add(soundOnItem);
		soundMenu.add(soundOffItem);
		soundOnItem.setEnabled(false);
		
		
		// �޴��ٿ� �޴��� ���̱�
		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		
		// �޴� �����ۿ� �̺�Ʈ ������
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
			int result = JOptionPane.showConfirmDialog(null, "������ �ٽ� �����Ͻðڽ��ϱ�?", "Retry", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				Node playerNode = XMLReader.getNode(blockGameNode, XMLReader.E_PLAYER);
				int life = Integer.parseInt(XMLReader.getAttr(playerNode, "life"));
				gamePanel.setRound(1);
				gamePanel.resetScore();
				gamePanel.setLife(life);
				gamePanel.getPlayerBlock().setLife(life);
				
				gamePanel.resetGame();
				gamePanel.startGame();
			}
		}
	}
	
	private class exitItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, "���� ������ �����Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION);
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