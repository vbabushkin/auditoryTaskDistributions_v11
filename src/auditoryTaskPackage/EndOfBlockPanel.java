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

public class EndOfBlockPanel extends JPanel{
	
	public EndOfBlockPanel(){
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
	    String lblMessage = "";
	    if( (ExperimentSetupWindow.currentBlockRunning+1) == AdvancedOptionsWindow.numRuns)
	    	lblMessage = String.format("<html><center>Thanks for your participation!<br>Click the mouse to exit.</center></html>", (ExperimentSetupWindow.currentBlockRunning+1), AdvancedOptionsWindow.numRuns);
	    else
	    	lblMessage = String.format("<html><center>The block # %d of %d blocks is over.<br>Click the mouse to exit.</center></html>", (ExperimentSetupWindow.currentBlockRunning+1), AdvancedOptionsWindow.numRuns);
		   
	     
	    
	    JLabel topLabel = new JLabel(lblMessage);
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
