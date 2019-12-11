package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndOfInstructionPanel extends JPanel{
	public EndOfInstructionPanel(){
		setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new BorderLayout());
		tempGridPanel.setSize(Main.screenWidth, Main.screenHeight/2);
		JPanel FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.setPreferredSize(this.getSize());
		
		
		String message = 
				String.format("<html><center>You're done with the instructions! Ready to practice a few trials?<br>Click the mouse to proceed.</center></html>");
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
