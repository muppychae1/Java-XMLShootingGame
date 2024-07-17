import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PlayerPanel extends JPanel {
	private GamePanel gamePanel = null;
	
	private String filePath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\imgPlayer";
	private File [] fileList = null;
	private Vector <JLabel> playerImageLabelV = new Vector<>();
	private Vector <ImageIcon> playerImageIconV = new Vector<>();
	
	private TextField xText = new TextField("0", 20);
	private TextField yText = new TextField("0", 20);
	private TextField wText = new TextField("0", 20);
	private TextField hText = new TextField("0", 20);
	private JRadioButton[] directionRadio = new JRadioButton[3];
	private JSpinner lifeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
	
	private PlayerLabel selectedBlockLabel = null;
	
	private String selectedPlayerImage = "";
	
	public PlayerPanel (GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		File path = new File(filePath);
		fileList = path.listFiles();
		
		for(int i=0; i<fileList.length; i++) {
			playerImageLabelV.add(changeJLabel(fileList[i].getPath()));
		}
		
		ScrollPanel sPanel = new ScrollPanel();
		JScrollPane scrollPane = new JScrollPane(sPanel);
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
		directionRadio[1].setEnabled(false);
		
		
		JLabel lifeLabel = new JLabel("목숨 개수");
		lifeLabel.setBounds(20, 290, 100, 20);
		add(lifeLabel);
		
		lifeSpinner.setBounds(150, 290, 50, 20);
		add(lifeSpinner);
		lifeSpinner.addChangeListener(new MyChangeListener());
		
	}
	
	private JLabel changeJLabel(String filePath) {
		ImageIcon icon = new ImageIcon (filePath);
		Image img = icon.getImage();
		
		Image changeImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		playerImageIconV.add(changeIcon);
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
			
			for(i=0; i<playerImageLabelV.size(); i++) {
				playerImageLabelV.get(i).addMouseListener(new MouseAdapter() {
					private int index = i;
					@Override
					public void 
					mousePressed(MouseEvent e) {
						selectedBlockLabel.setPlayerImg(fileList[index].getPath());
						selectedPlayerImage = fileList[index].getPath();
						ImageIcon icon = playerImageIconV.get(index);
						Image img = icon.getImage();
						
						Image changeImg = img.getScaledInstance(selectedBlockLabel.getWidth(), selectedBlockLabel.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon changeIcon = new ImageIcon(changeImg);
						selectedBlockLabel.setIcon(changeIcon);
					}
				});
				this.add(playerImageLabelV.get(i));
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
		}
	}
	
	class directionItemListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.DESELECTED)
				return;
			if(directionRadio[0].isSelected())
				selectedBlockLabel.setDirection(-1);
			else if(directionRadio[1].isSelected())
				selectedBlockLabel.setDirection(0);
			else
				selectedBlockLabel.setDirection(1);
		}
	}
	
	class MyChangeListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == lifeSpinner) {
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
	public void setLife(int life) {lifeSpinner.setValue(life);}
	
	public void setPlayerLabel(PlayerLabel label) {this.selectedBlockLabel = label;}
	
	public String getPlayerImg() {return this.selectedPlayerImage;}

}
