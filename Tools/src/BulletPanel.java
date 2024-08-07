import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class BulletPanel extends JPanel{
	private GamePanel gamePanel = null;
	
	private String filePath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\imgBullet";
	private File [] fileList = null;
	private Vector <JLabel> bulletImageLabelV = new Vector<>();
	private Vector <ImageIcon> bulletImageIconV = new Vector<>();
	
	private String musicPath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\music\\musicBullet";
	private File [] musicList = null;
	private Vector <String> musicName = new Vector<>();
	private JComboBox <String> musicCombo = new JComboBox<String>();
	private String selectedBulletMusic = "";
	
	private TextField wText = new TextField("0", 20);
	private TextField hText = new TextField("0", 20);
	private JSpinner powerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
	private JSpinner speedSpinner = new JSpinner(new SpinnerNumberModel(20, 1, 40, 1));
	
	private BulletLabel selectedBlockLabel = null;
	
	private String selectedBulletImage = "";
	
	public BulletPanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		File path = new File(filePath);
		fileList = path.listFiles();
		
		for(int i=0; i<fileList.length; i++) {
			bulletImageLabelV.add(changeJLabel(fileList[i].getPath()));
		}
		
		ScrollPanel sPanel = new ScrollPanel();
		JScrollPane scrollPane = new JScrollPane(sPanel);
		scrollPane.setBounds(15, 10, 200, 150);
		this.add(scrollPane);
		
		JLabel wLabel = new JLabel ("W");
		wLabel.setBounds(20, 180, 20, 20);
		add(wLabel);
		wText.setBounds(50, 180, 80, 20 );
		add(wText);
		wText.addActionListener(new MyActionListener());
				
		JLabel hLabel = new JLabel ("H");
		hLabel.setBounds(150, 180, 20, 20);
		add(hLabel);
		hText.setBounds(180, 180, 80, 20);
		add(hText);
		hText.addActionListener(new MyActionListener());
		
		JLabel powerLabel = new JLabel("총알 데미지");
		powerLabel.setBounds(20, 210, 100, 20);
		add(powerLabel);
		powerSpinner.setBounds(150, 210, 50, 20);
		add(powerSpinner);
		powerSpinner.addChangeListener(new MyChangeListener());
		
		JLabel speedLabel = new JLabel("총알 속도");
		speedLabel.setBounds(20, 240, 100, 20);
		add(speedLabel);
		speedSpinner.setBounds(150, 240, 50, 20);
		add(speedSpinner);
		speedSpinner.addChangeListener(new MyChangeListener());
		
		JLabel musicLabel = new JLabel("총알 소리");
		musicLabel.setBounds(20, 270, 100, 20);
		add(musicLabel);
		
		File musicFile = new File(musicPath);
		musicList = musicFile.listFiles();
		
		musicName.removeAllElements();
		for(int i=0; i<musicList.length; i++) {
			if(musicList[i].isFile()) {
				musicName.add(musicList[i].getName());
			}
			else continue;
		}
		
		musicCombo.removeAllItems();
		for(int i=0; i<musicName.size(); i++)
			musicCombo.addItem(musicName.get(i));
		
		musicCombo.addActionListener(new MyActionListener());
		musicCombo.setBounds(150, 270, 100, 20);
		selectedBulletMusic = musicList[0].getPath();
		add(musicCombo);
		
	}
	
	private JLabel changeJLabel(String filePath) {
		ImageIcon icon = new ImageIcon (filePath);
		Image img = icon.getImage();
		
		Image changeImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		bulletImageIconV.add(changeIcon);
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
			
			for(i=0; i<bulletImageLabelV.size(); i++) {
				bulletImageLabelV.get(i).addMouseListener(new MouseAdapter() {
					private int index = i;
					@Override
					public void mousePressed(MouseEvent e) {
						selectedBlockLabel.setBulletImg(fileList[index].getPath());
//						selectedBulletImage = fileList[index].getPath();
						ImageIcon icon = bulletImageIconV.get(index);
						Image img = icon.getImage();
						
						Image changeImg = img.getScaledInstance(selectedBlockLabel.getWidth(), selectedBlockLabel.getHeight(), Image.SCALE_SMOOTH);
						ImageIcon changeIcon = new ImageIcon(changeImg);
						selectedBlockLabel.setIcon(changeIcon);
					}
				});
				this.add(bulletImageLabelV.get(i));
			}
		}
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {			
			if(e.getSource() == wText || e.getSource() == hText) {
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
			} // end of if(wText||hText)
			
			else if(e.getSource() == musicCombo) {
				int index = musicCombo.getSelectedIndex();
				selectedBlockLabel.setBulletMusic(musicList[index].getPath());
//				selectedBulletMusic = musicList[index].getPath();
			}
		}
	}
	
	class MyChangeListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == powerSpinner) {
				int power = (int)powerSpinner.getValue();
				selectedBlockLabel.setPower(power);
			}
			else if(e.getSource() == speedSpinner) {
				int speed = (int)speedSpinner.getValue();
				selectedBlockLabel.setSpeed(speed);
			}
		}
	}
	
	public void setWText(int w) {wText.setText(Integer.toString(w));}
	public void setHText(int h) {hText.setText(Integer.toString(h));}
	public void setPower(int life) {powerSpinner.setValue(life);}

	public void setBulletLabel(BulletLabel label) {this.selectedBlockLabel = label;}
	
//	public String getBulletImg() {return this.selectedBulletImage;}
	public String getBulletMusic() {return this.selectedBulletMusic;}
}
