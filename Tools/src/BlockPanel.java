import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BlockPanel extends JPanel{
	private GamePanel gamePanel = null;
	
	private String filePath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\imgBlock";
	private File [] fileList = null;
	private Vector <JLabel> blockImageLabelV = new Vector<>();
	private Vector <ImageIcon> blockImageIconV = new Vector<>();
	
	private JList<JLabel> blockImageLabelList = new JList<>(blockImageLabelV);
	
	private TextField xText = new TextField("0", 20);
	private TextField yText = new TextField("0", 20);
	private TextField wText = new TextField("0", 20);
	private TextField hText = new TextField("0", 20);
	private JRadioButton[] directionRadio = new JRadioButton[3];
	private JSpinner speedSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 100, 1));
	private JSpinner distanceSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 200, 10));
	private JRadioButton[] attackRadio = new JRadioButton[2];
	private JSpinner attackSpeedSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 100, 1));
	private JSpinner lifeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
	private TextField scoreText = new TextField("10", 20);
	
	private BlockLabel selectedBlockLabel = null;
	private Vector <BlockLabel> selectedBlockLabelV = new Vector<>();
	private String selectedBlockImage = "";
	
	public BlockPanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		File path = new File(filePath);
		fileList = path.listFiles();
		System.out.println("fileList: " + fileList);
		System.out.println("filePath: " + filePath);
		
		for(int i=0; i<fileList.length; i++) {
			blockImageLabelV.add(changeJLabel(fileList[i].getPath()));
		}
		
		ScrollPanel sPanel = new ScrollPanel();
		JScrollPane scrollPane = new JScrollPane(sPanel);
		//scrollPane.setPreferredSize(new Dimension(250, 150));
		scrollPane.setBounds(15, 10, 200, 150);
		this.add(scrollPane);
		

		
		JLabel xLabel = new JLabel ("X");
		xLabel.setBounds(20, 180, 20, 20);
		add(xLabel);
		xText.setBounds(50, 180, 80, 20 );
		add(xText);
		xText.addActionListener(new MyActionListener());
				
		JLabel yLabel = new JLabel ("Y");
		yLabel.setBounds(150, 180, 20, 20);
		add(yLabel);
		yText.setBounds(180, 180, 80, 20);
		add(yText);
		yText.addActionListener(new MyActionListener());
		
		JLabel wLabel = new JLabel ("W");
		wLabel.setBounds(20, 210, 20, 20);
		add(wLabel);
		wText.setBounds(50, 210, 80, 20 );
		add(wText);
		wText.addActionListener(new MyActionListener());
				
		JLabel hLabel = new JLabel ("H");
		hLabel.setBounds(150, 210, 20, 20);
		add(hLabel);
		hText.setBounds(180, 210, 80, 20);
		add(hText);
		hText.addActionListener(new MyActionListener());
		
		JLabel directionLabel = new JLabel("움직이는 방향");
		directionLabel.setBounds(20, 240, 150, 20);
		add(directionLabel);
		
		ButtonGroup directionGroup = new ButtonGroup();
		directionRadio[0] = new JRadioButton("상하↕");
		directionRadio[1] = new JRadioButton("고정");
		directionRadio[2] = new JRadioButton("좌우↔");
		for(int i=0; i<directionRadio.length; i++) {
			directionGroup.add(directionRadio[i]);
			directionRadio[i].setBounds(20 + 80 * i, 260, 80, 20);
			directionRadio[i].setBackground(Color.WHITE);
			add(directionRadio[i]);
			directionRadio[i].addItemListener(new directionItemListener());
		}
		
		JLabel speedLabel = new JLabel("움직이는 속도");
		speedLabel.setBounds(20, 290, 150, 20);
		add(speedLabel);
		
		speedSpinner.setBounds(150, 290, 50, 20);
		add(speedSpinner);
		speedSpinner.addChangeListener(new MyChangeListener());
		
		JLabel distanceLabel = new JLabel("움직이는 거리");
		distanceLabel.setBounds(20, 320, 150, 20);
		add(distanceLabel);
		
		distanceSpinner.setBounds(150, 320, 50, 20);
		add(distanceSpinner);
		distanceSpinner.addChangeListener(new MyChangeListener());
		
		
		JLabel attackLabel = new JLabel("공격");
		attackLabel.setBounds(20, 350, 50, 20);
		add(attackLabel);
		
		ButtonGroup attackGroup = new ButtonGroup();
		attackRadio[0] = new JRadioButton("X");
		attackRadio[1] = new JRadioButton("O");
		for(int i=0; i<attackRadio.length; i++) {
			attackGroup.add(attackRadio[i]);
			attackRadio[i].setBounds(150 + 50 * i, 350, 50, 20);
			attackRadio[i].setBackground(Color.WHITE);
			add(attackRadio[i]);
			attackRadio[i].addItemListener(new attackItemListener());
		}
		
		JLabel attackSpeedLabel = new JLabel("공격 속도");
		attackSpeedLabel.setBounds(20, 380, 100, 20);
		add(attackSpeedLabel);
		
		attackSpeedSpinner.setBounds(150, 380, 50, 20);
		add(attackSpeedSpinner);
		attackSpeedSpinner.addChangeListener(new MyChangeListener());
		
		JLabel lifeLabel = new JLabel("목숨 개수");
		lifeLabel.setBounds(20, 410, 100, 20);
		add(lifeLabel);
		
		lifeSpinner.setBounds(150, 410, 50, 20);
		add(lifeSpinner);
		lifeSpinner.addChangeListener(new MyChangeListener());
		
		JLabel scoreLabel = new JLabel("점수");
		scoreLabel.setBounds(20, 440, 30, 20);
		add(scoreLabel);
		
		scoreText.setBounds(150, 440, 80, 20);
		add(scoreText);
		scoreText.addActionListener(new MyActionListener());
		
		
	}
	
	private JLabel changeJLabel(String filePath) {
		ImageIcon icon = new ImageIcon (filePath);
		Image img = icon.getImage();
		
		Image changeImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		blockImageIconV.add(changeIcon);
		JLabel imgLabel = new JLabel(changeIcon);
		
		return imgLabel;
	}
	

	class ScrollPanel extends JPanel{
		private int i;
		public ScrollPanel() {
			if(fileList.length % 3 != 0)
				this.setLayout(new GridLayout(fileList.length/3 + 1, 3, 10, 10));
			else
				this.setLayout(new GridLayout(fileList.length/3, 3, 10, 10));
			
			for(i=0; i<blockImageLabelV.size(); i++) {
				blockImageLabelV.get(i).addMouseListener(new MouseAdapter() {
					private int index = i;
					@Override
					public void mousePressed(MouseEvent e) {
						selectedBlockLabel.setBlockImage(fileList[index].getPath());
						//selectedBlockImage = fileList[index].getPath();
						ImageIcon icon = blockImageIconV.get(index);
						Image img = icon.getImage();
						
						Image changeImg = img.getScaledInstance(selectedBlockLabel.getWidth(), selectedBlockLabel.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon changeIcon = new ImageIcon(changeImg);
						selectedBlockLabel.setIcon(changeIcon);
					}
				});
				this.add(blockImageLabelV.get(i));
			}
		}
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == xText || e.getSource() == yText) {
				int x = Integer.parseInt(xText.getText());
				int y = Integer.parseInt(yText.getText());
				if(x < 0 || y < 0 
						|| x + selectedBlockLabel.getWidth() > gamePanel.getWidth()
						|| y + selectedBlockLabel.getHeight() > gamePanel.getHeight()) {
					JOptionPane.showMessageDialog(null, "게임 영역 내로 정하세요 \n" + gamePanel.getWidth() + " X " + gamePanel.getHeight(),
							"Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				selectedBlockLabel.setLocation(x, y);
			}
			
			else if(e.getSource() == wText || e.getSource() == hText) {
				int w = Integer.parseInt(wText.getText());
				int h = Integer.parseInt(hText.getText());
				if(w <= 0 || h <= 0) {
					JOptionPane.showMessageDialog(null, "양수로 입력하세요", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				selectedBlockLabel.setSize(w, h);
				
				ImageIcon icon = (ImageIcon) selectedBlockLabel.getIcon();
				if(icon != null) {
					Image img = icon.getImage();
					
					Image changeImg = img.getScaledInstance(selectedBlockLabel.getWidth(), selectedBlockLabel.getHeight(), Image.SCALE_SMOOTH);
					ImageIcon changeIcon = new ImageIcon(changeImg);
					
					selectedBlockLabel.setIcon(changeIcon);
				}
			} // end of else if(wText||hText)
			
			else if(e.getSource() == scoreText) {
				int score = Integer.parseInt(scoreText.getText());
				selectedBlockLabel.setScore(score);
			}
		}
	}
	
	class directionItemListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return;
			if(directionRadio[0].isSelected()) {
				selectedBlockLabel.setDirection(-1);
				speedSpinner.setEnabled(true);
				distanceSpinner.setEnabled(true);
			}
			else if(directionRadio[1].isSelected()) { // 방향 = 고정일 경우,
				selectedBlockLabel.setDirection(0);
				speedSpinner.setEnabled(false);
				distanceSpinner.setEnabled(false);
			}
			else {
				selectedBlockLabel.setDirection(1);
				speedSpinner.setEnabled(true);
				distanceSpinner.setEnabled(true);
			}
		}
	}
	
	class attackItemListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return;
			if(attackRadio[0].isSelected()) { // 공격 X
				selectedBlockLabel.setAttack(0);
				attackSpeedSpinner.setEnabled(false);
			}
			else {
				selectedBlockLabel.setAttack(1);
				attackSpeedSpinner.setEnabled(true);
			}
		}
	}
	
	class MyChangeListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == (JSpinner)speedSpinner) {
				int speed = (int)speedSpinner.getValue();
				selectedBlockLabel.setSpeed(speed);
			}
			
			else if(e.getSource() == distanceSpinner) {
				int distance = (int)distanceSpinner.getValue();
				selectedBlockLabel.setDistance(distance);
			}
			
			else if(e.getSource() == attackSpeedSpinner) {
				int attackSpeed = (int)attackSpeedSpinner.getValue();
				selectedBlockLabel.setAttackSpeed(attackSpeed);
			}
			
			else if(e.getSource() == lifeSpinner) {
				int life = (int)lifeSpinner.getValue();
				selectedBlockLabel.setLife(life);
			}
		}
	}
	
	public void setXText(int x) {xText.setText(Integer.toString(x));}
	public void setYText(int y) {yText.setText(Integer.toString(y));}
	public void setWText(int w) {wText.setText(Integer.toString(w));}
	public void setHText(int h) {hText.setText(Integer.toString(h));}
	public void setDirectionRadio(int dir) {directionRadio[dir+1].setSelected(true);}
	public void setSpeed(int speed) {speedSpinner.setValue(speed);}
	public void setDistance(int distance) {distanceSpinner.setValue(distance);}
	public void setAttackRadio(int attack) {attackRadio[attack].setSelected(true);}
	public void setAttackSpeed(int attackSpeed) {attackSpeedSpinner.setValue(attackSpeed);}
	public void setLife(int life) {lifeSpinner.setValue(life);}
	public void setScore(int score) {scoreText.setText(Integer.toString(score));}
	
	public void setBlockLabel(BlockLabel label) {this.selectedBlockLabel = label;}
	
	public Vector getSelectedBlockLabelV() {return selectedBlockLabelV;}
	//public String getBlockImg() {return this.selectedBlockImage;}
}
