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

public class InterPracticeBetPanel extends JPanel{
	public InterPracticeBetPanel(){
		setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new GridLayout(2,1,5,5));
		tempGridPanel.setSize(Main.screenWidth, Main.screenHeight/2);
		JPanel FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.setPreferredSize(this.getSize());
		
		//hide mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	    setCursor(invisibleCursor);
	    
	    String message = String.format("<html><center>Try another set of %d guesses.</center></html>", AdvancedOptionsWindow.numberOfBetsPerTrial);
		JLabel topLbl = new JLabel(message);
	    //set fore ground color to the label
	    topLbl.setForeground(Color.BLACK);
	    //set font to the label
	    topLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    topLbl.setBounds(10, 200, 200, 32);
	    topLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    //add label to the contentPane 
	    tempGridPanel.add(topLbl);
	    
	    JLabel downLbl = new JLabel("Click the mouse to continue.");
	    //set fore ground color to the label
	    downLbl.setForeground(Color.BLACK);
	    //set font to the label
	    downLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, (int)0.8*RunExperimentWindow.fontSize));
	    //set bounds of the label
	    downLbl.setBounds(10, 200, 200, 32);
	    downLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    //add label to the contentPane 
	    tempGridPanel.add(downLbl,BorderLayout.PAGE_END);

	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	    
	}

}
