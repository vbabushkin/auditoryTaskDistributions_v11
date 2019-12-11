package auditoryTaskPackage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class InstructionsPanel8 extends JPanel{
	
	public InstructionsPanel8(){
		
		setLayout(new GridBagLayout());
		
		//hide mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	    setCursor(invisibleCursor);
	    //
	    String topLblMessage = String.format("<html><center>Be strategic about how you place your %d guesses<br> in order to earn more points.<br>"
	    		+ "For example, if you're  certain that your memory is correct,<br>"
	    		+ "you should select responses that are close together, like this:</center></html>"
	    		+ "Click the mouse to continue.</center></html>", AdvancedOptionsWindow.numberOfBetsPerTrial);
	    String downLblMessage = String.format("<html><center>Click the mouse to continue.</center></html>");
	    
	    JLabel topLabel = new JLabel(topLblMessage);
	    //set fore ground color to the label
	    topLabel.setForeground(Color.BLACK);
	    //set font to the label
	    topLabel.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    topLabel.setBounds(10, 200, 200, 32);
	    topLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    
	    JLabel downLabel = new JLabel(downLblMessage);
	    //set fore ground color to the label
	    downLabel.setForeground(Color.BLACK);
	    //set font to the label
	    downLabel.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    downLabel.setBounds(10, 200, 200, 32);
	    downLabel.setHorizontalAlignment(SwingConstants.CENTER);
	  
 
	    ImageIcon imgIcon = new ImageIcon(getClass().getResource("distribStacked.png"));
	    JLabel fixImgLabel = new JLabel();
	    fixImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    fixImgLabel.setIcon(imgIcon);
 
  	  	GridBagConstraints gbc = new GridBagConstraints();
  	  	gbc.gridwidth=3;
  	  	gbc.gridheight=2;
	  	gbc.gridy=0;
	  	gbc.gridx = 0;
	  	gbc.insets = new Insets(0,0,0,0);
	  	add(topLabel, gbc);
  	  	
	  	gbc.gridwidth=1;
  	  	gbc.gridx = 0;
  	  	gbc.gridy = 2;
  	  	gbc.insets = new Insets(0,0,0,0);
  	  	gbc.gridheight = 3;
  	  	gbc.fill = GridBagConstraints.VERTICAL;
  	  	gbc.anchor = GridBagConstraints.CENTER;
  	  	add(fixImgLabel, gbc);

  	  	gbc.gridwidth=3;
  	  	gbc.gridy=10;
  	  	gbc.gridx = 0;
  	  	gbc.insets = new Insets(0,0,0,0);
  	  	add(downLabel, gbc);

	}
}
