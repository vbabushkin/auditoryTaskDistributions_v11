package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InstructionsPanel5 extends JPanel{
	
	public InstructionsPanel5(){
		
		setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new GridLayout(1,1,5,5));
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
	    
	    JLabel topLabel = new JLabel("<html><center>After a brief delay you'll hear another tone play.<br>"
	    		+ "Use the mouse to adjust the pitch of this tone up or down <br> "
	    		+ "by scrolling right and left.<br><br>"
	    		+ "Adjust the tone until it matches the tone you're holding onto<br>"
	    		+ "in your memory as closely as possible.<br>"
	    		+ "Do your best even if you're not sure.<br><br>"
	    		+ "It helps to recreate the tone silently in your mind, but don't hum or sing the tone out loud.<br>"
	    		+ "Once you're done, click the mouse to \"lock in\" your answer.<br><br>"
	    		+ "Please click the mouse to try out one trial.</center></html>");
	    //set fore ground color to the label
	    topLabel.setForeground(Color.BLACK);
	    //set font to the label
	    topLabel.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    topLabel.setBounds(10, 200, 200, 32);
	    topLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    //add label to the contentPane 
	    tempGridPanel.add(topLabel);
 
	    
	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	}

}
