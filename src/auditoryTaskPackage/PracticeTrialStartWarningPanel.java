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

public class PracticeTrialStartWarningPanel extends JPanel{
	static double cummulativeScorePerTrial =0;
	static boolean invoke = true;
	public PracticeTrialStartWarningPanel(){
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

	    String message = "";
	    message =String.format("<html><center>The next trial will start shortly.</center></html>");
//	    if(AdvancedOptionsWindow.isProbeTaskTimed)
//		    if(!RunExperimentWindow.timedPracticeTrialsAreRunning)//currentPracticeTrial<=(AdvancedOptionsWindow.numOfDemoTrialsNotTimed))
//		    	message =String.format("<html><center>The next trial will start shortly.</center></html>");
//		    else
//		    	message =String.format("<html><center>The next trial will start shortly.<br>For each guess you will have 20 seconds to submit your responce.</center></html>");
//	    else
//	    	message =String.format("<html><center>The next trial will start shortly.</center></html>");
//	    
	    
	    JLabel downLbl = new JLabel(message);
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
