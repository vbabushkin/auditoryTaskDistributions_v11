package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class InterBlockPanel extends JPanel{
	static double cummulativeScorePerTrial = 0;
	static int currentExperimentTrial = 0;
	public InterBlockPanel(){
		//hide mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	    setCursor(invisibleCursor);
	    //
	    String tempMsg = new String();
	    if((int)cummulativeScorePerTrial == 1)
	    	 tempMsg = "point";
	    else
	    	 tempMsg = "points";
	    String message = String.format("<html><center>You have earned <font  color=\"#00E600\">%d</font > %s for trial # %d.<br>Please take a break.</center></html>"
	   			, (int)cummulativeScorePerTrial,tempMsg, currentExperimentTrial+1
	   			);
		JLabel welcomeLbl = new JLabel(message);
	    //set fore ground color to the label
	    welcomeLbl.setForeground(Color.BLACK);
	    //set font to the label
	    welcomeLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    //set bounds of the label
	    welcomeLbl.setBounds(10, 200, 200, 32);
	    welcomeLbl.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    JLabel downLbl = new JLabel("Click the mouse when you are ready to resume.");
	    //set fore ground color to the label
	    downLbl.setForeground(Color.BLACK);
	    //set font to the label
	    downLbl.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, (int)0.8*RunExperimentWindow.fontSize));
	    //set bounds of the label
	    //downLbl.setBounds(10, 200, 200, 32);
	    downLbl.setHorizontalAlignment(SwingConstants.CENTER);
	  
	    
	    double earnedPercents =cummulativeScorePerTrial*100/(AdvancedOptionsWindow.numberOfBetsPerTrial*AdvancedOptionsWindow.maxPointsPerBet);
	    double lostPercents = 100-earnedPercents;
	    
	    ArrayList<Double> values = new ArrayList<Double>();
	    values.add(new Double(earnedPercents));
	    values.add(new Double(lostPercents));
 
	    ArrayList<Color> colors = new ArrayList<Color>();
	    colors.add(Color.green);
	    //set the piece for incorrect responses grey as background
	    Color defaultGray = UIManager.getColor("Panel.background"); 
	    colors.add(new Color((int)(0.9*defaultGray.getRed()),(int)(0.9*defaultGray.getGreen()),(int)(0.9*defaultGray.getBlue())));
	    
	    PieChart pieChart = new PieChart(values, colors);
    
	    pieChart.setPreferredSize(new Dimension(200,200));

	    setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(60,0,0,0);
        
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(pieChart, gbc);
     
        gbc.gridwidth=1;
        gbc.gridy=5;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(40,0,0,0);
        add(downLbl, gbc);

	    
	}

}
