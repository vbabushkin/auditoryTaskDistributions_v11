package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndOfPracticePanel extends JPanel{
	static double totalScore=0;
	static int totalNumBets=0;
	static int totalNumTrialsCompleted=0;
	public EndOfPracticePanel(){
		setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new BorderLayout());
		tempGridPanel.setSize(Main.screenWidth, Main.screenHeight/2);
		JPanel FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.setPreferredSize(this.getSize());
		
		
		String message = 
				String.format("<html><center>You're done with the practice!<br>Click the mouse to exit.</center></html>");
	    JLabel topLbl = new JLabel(message);
	    //set fore ground color to the label
	    topLbl.setForeground(Color.BLACK);
	    //set font to the label
	    topLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    topLbl.setBounds(10, 200, 200, 32);
	    topLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    //add label to the contentPane 
	    tempGridPanel.add(topLbl,BorderLayout.PAGE_START);
	    
	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	}


}
