/**
 * preStimulus panel representation
 * @author Vahan Babushkin
 */
package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PreStimulusPanel extends JPanel{
	
	public PreStimulusPanel(){
		setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new BorderLayout());
		tempGridPanel.setSize(Main.screenWidth, Main.screenHeight/2);
		JPanel FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.setPreferredSize(this.getSize());
		
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

		
	    JLabel fixImgLabel = new JLabel();
	    fixImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    fixImgLabel.setIcon(newIcon);
	    
	    //add label to the contentPane 
	    tempGridPanel.add(fixImgLabel,BorderLayout.PAGE_START);
	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	    
	    
	    
	}
    
    
}
