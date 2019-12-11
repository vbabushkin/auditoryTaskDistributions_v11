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

public class InstructionsPanel11 extends JPanel{
	
	public InstructionsPanel11(){
		
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
	    String lblMessage = String.format("<html><center>Now you're ready to do %d practice trials.<br>"
	    		+ "Let the experimenter know if you have any questions before you start.<br>"
	    		+ "Click the mouse to start the practice.</center></html>", (AdvancedOptionsWindow.numOfDemoTrialsNotTimed+AdvancedOptionsWindow.numOfDemoTrialsTimed));
	    JLabel welcomeLbl = new JLabel(lblMessage);
	    //set fore ground color to the label
	    welcomeLbl.setForeground(Color.BLACK);
	    //set font to the label
	    welcomeLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    welcomeLbl.setBounds(10, 200, 200, 32);
	    welcomeLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    //add label to the contentPane 
	    tempGridPanel.add(welcomeLbl);
 
	    
	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	}


}
