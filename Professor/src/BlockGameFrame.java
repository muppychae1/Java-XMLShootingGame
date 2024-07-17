import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BlockGameFrame extends JFrame {
	BlockGameFrame() {
		XMLReader xml = new XMLReader("block.xml");
		Node blockGameNode = xml.getBlockGameElement();
		Node sizeNode = XMLReader.getNode(blockGameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		setSize(Integer.parseInt(w), Integer.parseInt(h));
		
		setContentPane(new GamePanel(xml.getGamePanelElement()));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	public static void main(String[] args) {
		new BlockGameFrame();
	}

}

class Block extends JLabel{
	Image img;
	public Block(int x, int y, int w, int h, ImageIcon icon) {
		this.setBounds(x,y,w,h);
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}

class GamePanel extends JPanel {
	ImageIcon bgImg;
	public GamePanel(Node gamePanelNode) {
		setLayout(null);
		Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);
		bgImg = new ImageIcon(bgNode.getTextContent());
		
		// read <Fish><Obj>s from the XML parse tree, make Food objects, and add them to the FishBowl panel. 
		Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
		NodeList nodeList = blockNode.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			// found!!, <Obj> tag
			if(node.getNodeName().equals(XMLReader.E_OBJ)) {
				int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
				int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
				int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
				int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
				
				ImageIcon icon = new ImageIcon(XMLReader.getAttr(node, "img"));
				Block block = new Block(x,y,w,h, icon);
				add(block);
			}
		}
	}
	public void paintComponent(Graphics g) {
		g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}