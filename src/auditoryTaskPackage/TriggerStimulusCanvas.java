package auditoryTaskPackage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

//display white square
 public class TriggerStimulusCanvas extends JComponent {

  public void paint(Graphics g) {
	//hide mouse cursor
	Toolkit toolkit = Toolkit.getDefaultToolkit();
    Point hotSpot = new Point(0,0);
    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
    setCursor(invisibleCursor);
    //
	ImageIcon imgIcon = new ImageIcon(getClass().getResource("fix1.png"));
		
	Image img = imgIcon.getImage();
	Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
	ImageIcon newIcon = new ImageIcon(newimg);
	Image img2 = newIcon.getImage();
	
	Dimension panelSize = getRootPane().getSize();
	g.drawImage(img2, panelSize.width/2-10, panelSize.height/2-10, null);

	if(AdvancedOptionsWindow.triggerIsSelected){
		g.setColor(Parameters.TRIGGER_COLOR);
	    g.fillRect(	AdvancedOptionsWindow.leftCornerX, 
	    			AdvancedOptionsWindow.leftCornerY, 
	    			AdvancedOptionsWindow.triggerWidth, 
	    			AdvancedOptionsWindow.triggerHeight
	    			);
	}//end of if
  }
}
