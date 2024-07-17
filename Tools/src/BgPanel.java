import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class BgPanel extends JPanel {
	private GamePanel gamePanel = null;
	
	private ImageIcon bgImg = null;
	private String filePath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\imgBg";
	private File [] fileList = null;
	private Vector <JLabel> bgImageLabelV = new Vector<>();
	private Vector <ImageIcon> bgImageIconV = new Vector<>();
	
//	private JTextField textField = null;
//	private File [] fileList = null;
	private Vector <String> filesName = new Vector<>();
//	private JComboBox <String> bgCombo = new JComboBox<String>();
	
	
	private String musicPath = "C:\\Users\\hansung\\Desktop\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Tools\\music\\musicBackground";
	private File [] musicList = null;
	private Vector <String> musicName = new Vector<>();
	private JComboBox <String> musicCombo = new JComboBox<String>();
	
	
	private JComboBox <String> typeCombo = new JComboBox<>();
	private TextField text = new TextField("", 20);
	private TextField xText = new TextField("0", 20);
	private TextField yText = new TextField("0", 20);
	private TextField wText = new TextField("0", 20);
	private TextField hText = new TextField("0", 20);
	private JButton colorBtn = new JButton("Color");
	private Color selectedColor = null;
	
	private InfoLabel selectedInfoLabel = null;
	
	private String selectedBgImage = "";
	private String selectedBGMMusic = "";
	private Vector <InfoLabel> selectedInfoLabelV = new Vector<>();
	

	public BgPanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		
		File path = new File(filePath);
		fileList = path.listFiles();
		
		for(int i=0; i<fileList.length; i++) {
			bgImageLabelV.add(changeJLabel(fileList[i].getPath()));
		}
		
		ScrollPanel sPanel = new ScrollPanel();
		JScrollPane scrollPane = new JScrollPane(sPanel);
		scrollPane.setBounds(15, 10, 200, 150);
		this.add(scrollPane);
		
		JLabel BGMLabel = new JLabel ("BGM");
		BGMLabel.setBounds(20, 180, 50, 20);
		add(BGMLabel);
		
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
		musicCombo.setBounds(100, 180, 150, 20);
		int index = musicCombo.getSelectedIndex();
		selectedBGMMusic = musicList[index].getPath();
		add(musicCombo);
		
		JLabel infoLabel = new JLabel("< Info >");
		infoLabel.setBounds(20, 210, 100, 20);
		add(infoLabel);
		
		JLabel typeLabel = new JLabel("종류");
		typeLabel.setBounds(20, 240, 50, 20);
		add(typeLabel);
		typeCombo.addItem("Score");
		typeCombo.addItem("Life");
		typeCombo.addActionListener(new MyActionListener());
		typeCombo.setBounds(150, 240, 100, 20);
		add(typeCombo);
		
		JLabel textLabel = new JLabel("Text");
		textLabel.setBounds(20, 270, 50, 20);
		add(textLabel);
		text.setBounds(150, 270, 100, 20);
		text.setText("Score : ");
		text.setEnabled(false);
		add(text);
		text.addActionListener(new MyActionListener());
		
		JLabel xLabel = new JLabel ("X");
		xLabel.setBounds(20, 300, 20, 20);
		add(xLabel);
		xText.setBounds(50, 300, 80, 20 );
		add(xText);
		xText.addActionListener(new MyActionListener());
				
		JLabel yLabel = new JLabel ("Y");
		yLabel.setBounds(150, 300, 20, 20);
		add(yLabel);
		yText.setBounds(180, 300, 80, 20);
		add(yText);
		yText.addActionListener(new MyActionListener());
		
		JLabel wLabel = new JLabel ("W");
		wLabel.setBounds(20, 330, 20, 20);
		add(wLabel);
		wText.setBounds(50, 330, 80, 20 );
		add(wText);
		wText.addActionListener(new MyActionListener());
				
		JLabel hLabel = new JLabel ("H");
		hLabel.setBounds(150, 330, 20, 20);
		add(hLabel);
		hText.setBounds(180, 330, 80, 20);
		add(hText);
		hText.addActionListener(new MyActionListener());
		
		
		colorBtn.addActionListener(new MyActionListener());
		colorBtn.setBounds(100, 360, 100, 20);
		add(colorBtn);
	}
	
	private JLabel changeJLabel(String filePath) {
		ImageIcon icon = new ImageIcon (filePath);
		Image img = icon.getImage();
		
		Image changeImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		bgImageIconV.add(changeIcon);
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
			
			for(i=0; i<bgImageLabelV.size(); i++) {
				bgImageLabelV.get(i).addMouseListener(new MouseAdapter() {
					private int index = i;
					@Override
					public void mousePressed(MouseEvent e) {
						ImageIcon icon = bgImageIconV.get(index);
						gamePanel.setBgPath(fileList[index].getPath());
						selectedBgImage = fileList[index].getPath();
					}
				});
				this.add(bgImageLabelV.get(i));
			}
		}
	}
	
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {		
			if(e.getSource() == musicCombo) {
				int index = musicCombo.getSelectedIndex();
				selectedBGMMusic = musicList[index].getPath();
			}
			
			else if(e.getSource() == typeCombo) {
				int index = typeCombo.getSelectedIndex();
				selectedInfoLabel.setType(index);
				String answer = (String)typeCombo.getSelectedItem();
				if(answer.equals("Score")) { // Score 일 경우
					text.setText("Score :");
					selectedInfoLabel.setText(text.getText());
				}
				else if(answer.equals("Life")) {
					text.setText("Life : ");
					selectedInfoLabel.setText(text.getText());
				}	
			}
			
			else if(e.getSource() == text) {
				selectedInfoLabel.setText(text.getText());
			}
			
			else if(e.getSource() == xText || e.getSource() == yText) {
				int x = Integer.parseInt(xText.getText());
				int y = Integer.parseInt(yText.getText());
				if(x < 0 || y < 0 
						|| x + selectedInfoLabel.getWidth() > gamePanel.getWidth()
						|| y + selectedInfoLabel.getHeight() > gamePanel.getHeight()) {
					JOptionPane.showMessageDialog(null, "게임 영역 내로 정하세요 \n" + gamePanel.getWidth() + " X " + gamePanel.getHeight(),
							"Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				selectedInfoLabel.setLocation(x, y);
			}
			
			else if(e.getSource() == wText || e.getSource() == hText) {
				int w = Integer.parseInt(wText.getText());
				int h = Integer.parseInt(hText.getText());
					if(w <= 0 || h <= 0) {
						JOptionPane.showMessageDialog(null, "양수로 입력하세요", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
				selectedInfoLabel.setSize(w, h);
			} // end of if(wText||hText)
			
			else if(e.getSource() == colorBtn) {
				selectedColor = JColorChooser.showDialog(null, "Color", Color.BLACK);
				if(selectedColor != null) {
					selectedInfoLabel.setForeground(selectedColor);
				}
			}
			
		}
	}
	
	public void setBGMMusic(String path) {
		this.selectedBGMMusic = path;
		int index=0;
		for(int i=0; i<musicList.length; i++) {
			if(musicList[i].isFile() && musicList[i].getPath().equals(path)) {
				index = i;
				break;
			}
		}
		
		musicCombo.setSelectedIndex(index);
	}
	
	public void setXText(int x) {xText.setText(Integer.toString(x));}
	public void setYText(int y) {yText.setText(Integer.toString(y));}
	public void setWText(int w) {wText.setText(Integer.toString(w));}
	public void setHText(int h) {hText.setText(Integer.toString(h));}
	public void setTypeCombo(int index) {typeCombo.setSelectedIndex(index);}
	public void setText(String str) {text.setText(str);}
	public void setColor(Color color) {this.selectedColor = color;}
	
//	public String getText() {return text.getText();}
	
	public String getSelectedBgImage() {return selectedBgImage;}
	public String getSelectedBGMMusic() {return selectedBGMMusic;}
	public Vector getSelectedInfoLabelV() {return selectedInfoLabelV;}
	
	public void setInfoLabel(InfoLabel label) {this.selectedInfoLabel = label;}
	
	
}
