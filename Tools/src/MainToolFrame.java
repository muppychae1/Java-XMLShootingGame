import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainToolFrame extends JFrame{
	private JSplitPane pane = new JSplitPane();
	private int width,height;
	private GamePanel gamePanel = new GamePanel();
	private BgPanel bgPanel = new BgPanel(gamePanel);
	private BlockPanel blockPanel = new BlockPanel(gamePanel);
	private WallPanel wallPanel = new WallPanel(gamePanel);
	private PlayerPanel playerPanel = new PlayerPanel(gamePanel);
	private BulletPanel bulletPanel = new BulletPanel(gamePanel);
	
//	private JTabbedPane tabPane = createTabbedPane();
	private JTabbedPane tabPane = null;
	
	private JButton newBtn = null;
	private JButton openBtn = null;
	private JButton saveBtn = null;
	
	private JButton infoBtn = null;
	private JButton blockBtn = null;
	private JButton wallBtn = null;
	private JButton playerBtn = null;
	private JButton bulletBtn = null;
	
	private Vector<BlockLabel> blockLabelV = new Vector<>();
	
	private PlayerLabel playerLabel = null;
	private BulletLabel bulletLabel = null;
	
	
	
	public MainToolFrame(int width, int height) {
		super("저작도구");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setLocationRelativeTo(null);
		setResizable(false);
		
		this.width = width;
		this.height = height;
		
		splitPane();
		createToolBar();
		
		this.setSize(width + 300, height);
		this.setVisible(true);
	}
	
	private void splitPane() {
		//
		
		getContentPane().add(pane, BorderLayout.CENTER);
		pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		pane.setDividerLocation(width);
		pane.setDividerSize(0);
		pane.setEnabled(false);
		
		pane.setLeftComponent(gamePanel);
		
		tabPane = createTabbedPane();
		pane.setRightComponent(tabPane);
	}
	
	private void createToolBar() {
		JToolBar bar = new JToolBar("Menu");
		bar.setBackground(Color.LIGHT_GRAY);
		
//		newBtn = new JButton("New");
//		newBtn.setToolTipText("파일 생성");
//		bar.add(newBtn);
		
		openBtn = new JButton(new ImageIcon("img/open.png"));
		openBtn.setToolTipText("파일 열기");
		openBtn.addMouseListener(new MouseListener());
		bar.add(openBtn);
		
		saveBtn = new JButton(new ImageIcon("img/save.png"));
		saveBtn.setToolTipText("파일 저장");
		saveBtn.addMouseListener(new MouseListener());
		bar.add(saveBtn);
		bar.addSeparator();
		
		infoBtn = new JButton("Info");
		infoBtn.setToolTipText("Score, Life를 나타내는 블럭 생성");
		infoBtn.addMouseListener(new MouseListener());
		bar.add(infoBtn);
		bar.addSeparator();
		
		blockBtn = new JButton("Block");
		blockBtn.setToolTipText("장애물 블럭 생성");
		blockBtn.addMouseListener(new MouseListener());
		bar.add(blockBtn);
		bar.addSeparator();
		
		wallBtn = new JButton("Wall");
		wallBtn.setToolTipText("벽 블럭 생성");
		wallBtn.addMouseListener(new MouseListener());
		bar.add(wallBtn);
		bar.addSeparator();
		
		playerBtn = new JButton("Player");
		playerBtn.setToolTipText("사용자 블럭 생성");
		playerBtn.addMouseListener(new MouseListener());
		bar.add(playerBtn);
		bar.addSeparator();
		
		bulletBtn = new JButton("Bullet");
		bulletBtn.setToolTipText("플레이어 총알 생성");
		bulletBtn.addMouseListener(new MouseListener());
		bar.add(bulletBtn);
		bar.addSeparator();
		
		this.add(bar, BorderLayout.NORTH);
	}
	
	class MouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == openBtn) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop\\2022겨울방학\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Game2"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
				chooser.setFileFilter(filter);
				int ret = chooser.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION) {
					File openFile = chooser.getSelectedFile();
					try {
						openXmlFile(openFile.getPath());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			else if(e.getSource() == saveBtn) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop\\2022겨울방학\\동계 학습 프로젝트\\동계 학습 프로젝트\\샘플 코드\\Game2"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
				chooser.setFileFilter(filter);
				int ret = chooser.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION) {
					File saveFile = chooser.getSelectedFile();
					try {
						makeXmlFile(saveFile.getPath());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			else if(e.getSource() == infoBtn) {
				if(gamePanel.getInfoLabelCnt() == 2)
					return;
				InfoLabel infoLabel = new InfoLabel(bgPanel);
				infoLabel.setBounds(10, 10, 150, 30);
				gamePanel.add(infoLabel);
				gamePanel.repaint();
				gamePanel.setInfoLabelCnt(gamePanel.getInfoLabelCnt() + 1);
				bgPanel.getSelectedInfoLabelV().add(infoLabel);
			}
			
			else if(e.getSource() == blockBtn) {
				BlockLabel blockLabel = new BlockLabel(blockPanel);
				blockLabel.setBounds(10, 10, 50, 50);
				gamePanel.add(blockLabel);
				gamePanel.repaint();
				blockPanel.getSelectedBlockLabelV().add(blockLabel);
			}
			else if(e.getSource() == wallBtn) {
				WallLabel wallLabel = new WallLabel(wallPanel);
				wallLabel.setBounds(10, 10, 50, 50);
				gamePanel.add(wallLabel);
				gamePanel.repaint();
				wallPanel.getSelectedWallLabelV().add(wallLabel);
			}
			else if(e.getSource() == playerBtn) {
				if(gamePanel.getPlayerLabelCnt() == 1)
					return;
				playerLabel = new PlayerLabel(playerPanel);
				playerLabel.setBounds(10, 10, 50, 50);
				gamePanel.add(playerLabel);
				gamePanel.repaint();
				gamePanel.setPlayerLabelCnt(gamePanel.getPlayerLabelCnt() + 1);
			}
			else if(e.getSource() == bulletBtn) {
				if(gamePanel.getBulletLabelCnt() == 1)
					return;
				bulletLabel = new BulletLabel(bulletPanel);
				bulletLabel.setBounds(10, 10, 30, 30);
				gamePanel.add(bulletLabel);
				gamePanel.repaint();
				gamePanel.setBulletLabelCnt(gamePanel.getBulletLabelCnt() + 1);
			}
			
		}
	}
	
	private JTabbedPane createTabbedPane() {
		JTabbedPane tabPane = new JTabbedPane();
		
		JScrollPane bgScrollPane = createBgScrollPane();
		JScrollPane blockScrollPane = createBlockScrollPane();
		JScrollPane wallScrollPane = createWallScrollPane();
		JScrollPane playerScrollPane = createPlayerScrollPane();
		JScrollPane bulletScrollPane = createBulletScrollPane();
		
		tabPane.addTab("BG", bgScrollPane);
		tabPane.addTab("Block", blockScrollPane);
		tabPane.addTab("Wall", wallScrollPane);
		tabPane.addTab("Player", playerScrollPane);
		tabPane.addTab("Bullet", bulletScrollPane);
		return tabPane;
		
	}
	
	private JScrollPane createBgScrollPane() {
		JScrollPane bgScroll = new JScrollPane();
		
		bgPanel.setPreferredSize(new Dimension(260, 500));
		bgScroll.setViewportView(bgPanel);
		
		return bgScroll;
	}
	
	private JScrollPane createBlockScrollPane() {
        JScrollPane blockScroll = new JScrollPane();
        
        blockPanel.setPreferredSize(new Dimension(260, 500));
        blockScroll.setViewportView(blockPanel);

        return blockScroll;
	}
	
	private JScrollPane createWallScrollPane() {
        JScrollPane wallScroll = new JScrollPane();
        
        wallPanel.setPreferredSize(new Dimension(260, 350));
        wallScroll.setViewportView(wallPanel);

        return wallScroll;
	}
	
	private JScrollPane createPlayerScrollPane() {
        JScrollPane playerScroll = new JScrollPane();
        
        playerPanel.setPreferredSize(new Dimension(260, 350));
        playerScroll.setViewportView(playerPanel);

        return playerScroll;
	}
	
	private JScrollPane createBulletScrollPane() {
        JScrollPane bulletScroll = new JScrollPane();
        
        bulletPanel.setPreferredSize(new Dimension(260, 350));
        bulletScroll.setViewportView(bulletPanel);

        return bulletScroll;
	}
	
	private void openXmlFile(String path) {
		this.setVisible(false);
		
		XMLReader xml = new XMLReader(path);
		Node blockGameNode = xml.getBlockGameElement();
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		int width = Integer.parseInt(w);
		int height = Integer.parseInt(h);
		
		MainToolFrame newFrame = new MainToolFrame(width, height);
		
		Node gamePanelNode = xml.getGamePanelElement();
		
		Node bgNode = xml.getNode(gamePanelNode, xml.E_BG);
		String bgImgPath = xml.getAttr(bgNode, "img");
		newFrame.gamePanel.setBgPath(bgImgPath);
		
		String bgMusicPath = XMLReader.getAttr(bgNode, "music");
		newFrame.bgPanel.setBGMMusic(bgMusicPath);
		
		Vector <InfoLabel> infoV = newFrame.bgPanel.getSelectedInfoLabelV();
		
		NodeList infoNodeList = bgNode.getChildNodes();
		JLabel [] infoList = new JLabel[infoNodeList.getLength() / 2]; // block 배열의 길이 지정
		Node playerNode = xml.getNode(gamePanelNode, xml.E_PLAYER);
		int life = Integer.parseInt(xml.getAttr(playerNode, "life"));
		
		for(int i=0; i<infoNodeList.getLength(); i++) {
			Node node = infoNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(xml.E_INFO)) {
				int x = Integer.parseInt(xml.getAttr(node, "x"));
				int y = Integer.parseInt(xml.getAttr(node, "y"));
				int w1 = Integer.parseInt(xml.getAttr(node, "w"));
				int h1 = Integer.parseInt(xml.getAttr(node, "h"));
				int r = Integer.parseInt(xml.getAttr(node, "r"));
				int g = Integer.parseInt(xml.getAttr(node, "g"));
				int b = Integer.parseInt(xml.getAttr(node, "b"));
				int type = Integer.parseInt(xml.getAttr(node, "type"));
				String text = xml.getAttr(node, "text");
				
				InfoLabel infoLabel = new InfoLabel(newFrame.bgPanel);
				infoLabel.setBounds(x, y, w1, h1);
				infoLabel.setForeground(new Color(r,g,b));
				infoLabel.setType(type);
				infoLabel.setText(text);
				
				newFrame.gamePanel.add(infoLabel);				
				infoV.add(infoLabel);
			}
			
		}//end of for()
		newFrame.gamePanel.setInfoLabelCnt(infoV.size());
		
		
		// Block 내의 Obj
		Vector <BlockLabel> blockV = newFrame.blockPanel.getSelectedBlockLabelV();
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList blockNodeList = blockNode.getChildNodes(); // block 밑의 Obj 노드들을 저장

		for(int i=0; i<blockNodeList.getLength(); i++) {
			Node node = blockNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w1 = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h1 = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int attack = Integer.parseInt(XMLReader.getAttr(node, "attack"));
				int attackSpeed = Integer.parseInt(XMLReader.getAttr(node, "attackSpeed"));
				int dir = Integer.parseInt(XMLReader.getAttr(node, "dir"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				int hit = Integer.parseInt(XMLReader.getAttr(node, "hit"));
				int score = Integer.parseInt(XMLReader.getAttr(node, "score"));
				int distance = Integer.parseInt(XMLReader.getAttr(node, "distance"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				String imgPath = XMLReader.getAttr(node, "img");
				
				BlockLabel blockLabel = new BlockLabel(newFrame.blockPanel);
				blockLabel.setBounds(x, y, w1, h1);
				blockLabel.setAttack(attack);
				blockLabel.setAttackSpeed(attackSpeed);
				blockLabel.setDirection(dir);
				blockLabel.setSpeed(speed);
				blockLabel.setLife(hit);
				blockLabel.setScore(score);
				blockLabel.setDistance(distance);
				
				blockLabel.setBlockImage(imgPath);
				Image img = icon.getImage();
				Image changeImg = img.getScaledInstance(blockLabel.getWidth(), blockLabel.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon changeIcon = new ImageIcon(changeImg);
				blockLabel.setIcon(changeIcon);
				
				newFrame.gamePanel.add(blockLabel);
				blockV.add(blockLabel);
			}
		} // end of for
		
		// Wall 내의 Obj
		Vector <WallLabel> wallV = newFrame.wallPanel.getSelectedWallLabelV();
		Node wallNode = XMLReader.getNode(gamePanelNode, XMLReader.E_WALL);
		NodeList wallNodeList = wallNode.getChildNodes(); // wall 밑의 Obj 노드들을 저장
		
		for(int i=0; i<wallNodeList.getLength(); i++) {
			Node node = wallNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w1 = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h1 = Integer.parseInt(XMLReader.getAttr(node, "h"));
				int dir = Integer.parseInt(XMLReader.getAttr(node, "dir"));
				int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
				int hit = Integer.parseInt(XMLReader.getAttr(node, "hit"));
				int distance = Integer.parseInt(XMLReader.getAttr(node, "distance"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				String imgPath = XMLReader.getAttr(node, "img");
				
				WallLabel wallLabel = new WallLabel(newFrame.wallPanel);
				wallLabel.setBounds(x, y, w1, h1);
				wallLabel.setDirection(dir);
				wallLabel.setSpeed(speed);
				wallLabel.setLife(hit);
				wallLabel.setDistance(distance);
				
				wallLabel.setWallImage(imgPath);
				Image img = icon.getImage();
				Image changeImg = img.getScaledInstance(wallLabel.getWidth(), wallLabel.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon changeIcon = new ImageIcon(changeImg);
				wallLabel.setIcon(changeIcon);
				
				newFrame.gamePanel.add(wallLabel);
				wallV.add(wallLabel);
			}
		}
		
		// Player
		//playerNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PLAYER);
		int x = Integer.parseInt(XMLReader.getAttr(playerNode, "x"));
		int y = Integer.parseInt(XMLReader.getAttr(playerNode, "y"));
		int w1 = Integer.parseInt(XMLReader.getAttr(playerNode, "w"));
		int h1 = Integer.parseInt(XMLReader.getAttr(playerNode, "h"));
		int dir = Integer.parseInt(XMLReader.getAttr(playerNode, "dir"));
		//int life1 = Integer.parseInt(XMLReader.getAttr(playerNode, "life"));
				
		ImageIcon playerIcon = new ImageIcon(XMLReader.getAttr(playerNode, "img"));
		String imgPath = XMLReader.getAttr(playerNode, "img");
		
		PlayerLabel playerLabel = new PlayerLabel(newFrame.playerPanel);
		playerLabel.setBounds(x, y, w1, h1);
		playerLabel.setDirection(dir);
		
		playerLabel.setPlayerImg(imgPath);
		Image img = playerIcon.getImage();
		Image changeImg = img.getScaledInstance(playerLabel.getWidth(), playerLabel.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		playerLabel.setIcon(changeIcon);
		
		newFrame.gamePanel.add(playerLabel);
		newFrame.gamePanel.setPlayerLabelCnt(1);
		
		
		
		Node bulletNode = xml.getNode(playerNode, xml.E_BULLET);
		int w2 = Integer.parseInt(XMLReader.getAttr(bulletNode, "w"));
		int h2 = Integer.parseInt(XMLReader.getAttr(bulletNode, "h"));
		int speed = Integer.parseInt(XMLReader.getAttr(bulletNode, "speed"));
		int power = Integer.parseInt(XMLReader.getAttr(bulletNode, "power"));
				
		ImageIcon bulletIcon = new ImageIcon(XMLReader.getAttr(bulletNode, "img"));
		String bulletImgPath = XMLReader.getAttr(bulletNode, "img");
		String bulletMusicPath = XMLReader.getAttr(bulletNode, "music");
		
		BulletLabel bulletLabel = new BulletLabel(newFrame.bulletPanel);
		bulletLabel.setBounds(playerLabel.getX() + (playerLabel.getWidth()/2) - (w2/2), playerLabel.getY() - h2, w2, h2);
		bulletLabel.setSpeed(speed);
		bulletLabel.setPower(power);	
		bulletLabel.setBulletMusic(bulletMusicPath);
		
		bulletLabel.setBulletImg(bulletImgPath);
		Image bulletImg = bulletIcon.getImage();
		Image bulletChangeImg = bulletImg.getScaledInstance(bulletLabel.getWidth(), bulletLabel.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon bulletChangeIcon = new ImageIcon(bulletChangeImg);
		bulletLabel.setIcon(bulletChangeIcon);
		
		newFrame.gamePanel.add(bulletLabel);
		newFrame.gamePanel.setBulletLabelCnt(1);
		
		
	}
	
	private void makeXmlFile(String path) {
		try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            // BlockGame 엘리먼트
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
 
            Element BlockGame = doc.createElement("BlockGame");
            doc.appendChild(BlockGame);
 
            // Screen 엘리먼트
            Element Screen = doc.createElement("Screen");
            BlockGame.appendChild(Screen);
            
            // Size 엘리먼트
            Element Size = doc.createElement("Size");
            Screen.appendChild(Size);
            Size.setAttribute("w", Integer.toString(width));
            Size.setAttribute("h", Integer.toString(height));
            
            // GamePanel 엘리먼트
            Element GamePanel = doc.createElement("GamePanel");
            BlockGame.appendChild(GamePanel);
            
            // Bg 엘리먼트
            Element Bg = doc.createElement("Bg");
            GamePanel.appendChild(Bg);
            Bg.setAttribute("img", bgPanel.getSelectedBgImage());
            Bg.setAttribute("music", bgPanel.getSelectedBGMMusic());
            
            // Info 엘리먼트
            Vector <InfoLabel> infoV = bgPanel.getSelectedInfoLabelV();
            for(int i=0; i<infoV.size(); i++) {
            	Element Info = doc.createElement("Info");
            	Bg.appendChild(Info);
            	Info.setAttribute("x", Integer.toString((infoV.get(i)).getLabelX()));
            	Info.setAttribute("y", Integer.toString((infoV.get(i)).getLabelY()));
            	Info.setAttribute("w", Integer.toString((infoV.get(i)).getWidth()));
            	Info.setAttribute("h", Integer.toString((infoV.get(i)).getHeight()));
            	Info.setAttribute("r", Integer.toString(infoV.get(i).getForeground().getRed()));
            	Info.setAttribute("g", Integer.toString(infoV.get(i).getForeground().getGreen()));
            	Info.setAttribute("b", Integer.toString(infoV.get(i).getForeground().getBlue()));
            	Info.setAttribute("type", Integer.toString(infoV.get(i).getType()));
            	Info.setAttribute("text", infoV.get(i).getText());
            }
            
            //Block 엘리먼트
            Element Block = doc.createElement("Block");
            GamePanel.appendChild(Block);
            
            //Obj 엘리먼트
            Vector<BlockLabel> blockV = blockPanel.getSelectedBlockLabelV();
            for(int i=0; i<blockV.size(); i++) {
            	Element Obj = doc.createElement("Obj");
                Block.appendChild(Obj);
                Obj.setAttribute("x", Integer.toString((blockV.get(i)).getLabelX()));
                Obj.setAttribute("y", Integer.toString((blockV.get(i)).getLabelY()));
                Obj.setAttribute("w", Integer.toString((blockV.get(i)).getWidth()));
                Obj.setAttribute("h", Integer.toString((blockV.get(i)).getHeight()));
                Obj.setAttribute("dir", Integer.toString(blockV.get(i).getDirection()));
                Obj.setAttribute("speed", Integer.toString(blockV.get(i).getSpeed()));
                Obj.setAttribute("distance", Integer.toString(blockV.get(i).getDistance()));
                Obj.setAttribute("attack", Integer.toString(blockV.get(i).getAttack()));
                Obj.setAttribute("attackSpeed", Integer.toString(blockV.get(i).getAttackSpeed()));
                Obj.setAttribute("hit", Integer.toString(blockV.get(i).getLife()));
                Obj.setAttribute("score", Integer.toString(blockV.get(i).getScore()));
                Obj.setAttribute("img", blockV.get(i).getBlockImg());
            }
            
            //Wall 엘리먼트
            Element Wall = doc.createElement("Wall");
            GamePanel.appendChild(Wall);
           
          //Obj 엘리먼트
            Vector<WallLabel> wallV = wallPanel.getSelectedWallLabelV();
            for(int i=0; i<blockV.size(); i++) {
            	Element Obj = doc.createElement("Obj");
            	Wall.appendChild(Obj);
                Obj.setAttribute("x", Integer.toString((wallV.get(i)).getLabelX()));
                Obj.setAttribute("y", Integer.toString((wallV.get(i)).getLabelY()));
                Obj.setAttribute("w", Integer.toString((wallV.get(i)).getWidth()));
                Obj.setAttribute("h", Integer.toString((wallV.get(i)).getHeight()));
                Obj.setAttribute("dir", Integer.toString(wallV.get(i).getDirection()));
                Obj.setAttribute("speed", Integer.toString(wallV.get(i).getSpeed()));
                Obj.setAttribute("distance", Integer.toString(wallV.get(i).getDistance()));
                Obj.setAttribute("hit", Integer.toString(wallV.get(i).getLife()));
                Obj.setAttribute("img", wallV.get(i).getWallImg());
            }
            
          //Player 엘리먼트
           Element Player = doc.createElement("Player");
           GamePanel.appendChild(Player);
           Player.setAttribute("x", Integer.toString(playerLabel.getLabelX()));
           Player.setAttribute("y", Integer.toString(playerLabel.getLabelY()));
           Player.setAttribute("w", Integer.toString(playerLabel.getWidth()));
           Player.setAttribute("h", Integer.toString(playerLabel.getHeight()));
           Player.setAttribute("dir", Integer.toString(playerLabel.getDirection()));
           Player.setAttribute("life", Integer.toString(playerLabel.getLife()));
           Player.setAttribute("img", playerLabel.getPlayerImg());
           
           //Bullet 엘리먼트
           Element Bullet = doc.createElement("Bullet");
           Player.appendChild(Bullet);
           Bullet.setAttribute("w", Integer.toString(bulletLabel.getWidth()));
           Bullet.setAttribute("h", Integer.toString(bulletLabel.getHeight()));
           Bullet.setAttribute("power", Integer.toString(bulletLabel.getPower()));
           Bullet.setAttribute("speed", Integer.toString(bulletLabel.getSpeed()));
           Bullet.setAttribute("img", bulletLabel.getBulletImg());
           Bullet.setAttribute("music", bulletLabel.getBulletMusic());
           
            // XML 파일로 쓰기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
 
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
 
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(new File(path)));
 
            transformer.transform(source, result);
 
            System.out.println("=========END=========");
 
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
}
