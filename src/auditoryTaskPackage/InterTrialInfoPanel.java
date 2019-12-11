package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import auditoryTaskPackage.PracticeProbePanel.SmoothSoundGenerator;

public class InterTrialInfoPanel extends JComponent{
	
	public static double fFreq;
	public static double fFreqCursorX;
	public static boolean mouseIsMoving = false;//controls whether the sound is played at the beginning of each trial
	public static SmoothSoundGenerator m_thread;
	public static int pX, pY;
	public static int prevX, prevY;
	public static int X0, Y0;
	public static int xPixToTrim = 50;
	public static int yPixToTrim = Main.screenHeight/3;
	public static double[] tempGaussian = new double [401];
	public static double[] prevBetGaussian = new double [401];
	public static boolean isAnswerSubmitted;
	public static double cursorAllowedIntervalX;
	public static double cursorAllowedIntervalY;
	public static double lowerMarginX = xPixToTrim-1;
	public static double firstBetMu;
	
	static double cummulativeScorePerTrial =0;
	static boolean invoke = true;
	static int currentTrial =0;
	public static boolean showTrigger = false;
	public  InterTrialInfoPanel(){
		//setLayout(new BorderLayout(10,10)); 
		String tempMsg;
		
		
		if((int)cummulativeScorePerTrial == 1)
	    	 tempMsg = "point";
	    else
	    	 tempMsg = "points";
	
		String message =  "";

		message = String.format("<html><center>Please compare your <font  color=\"#505050\">response</font > vs. the <font  color=\"#00E600\">actual tone</font>. <br>"
    			 + "Click the mouse to proceed.<br><br></center></html>");
	
    		JLabel stringInfoLabel = new JLabel(message);
	    	stringInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    	stringInfoLabel.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
	    	
	    	JLabel lblAdjustPitch = new JLabel();
        lblAdjustPitch.setHorizontalAlignment(SwingConstants.CENTER);

        //hide mouse cursor   
  	    if(!Parameters.DISPLAY_CURSOR_ON_PROBE){
  	  		Toolkit toolkit = Toolkit.getDefaultToolkit();
  	  	    Point hotSpot = new Point(0,0);
  	  	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
  	  	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
  	    	setCursor(invisibleCursor);
  	    }
  	    

  	 
  	    double earnedPercents = (cummulativeScorePerTrial*100)/(AdvancedOptionsWindow.numberOfBetsPerTrial*AdvancedOptionsWindow.maxPointsPerBet);
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
  	  	
  	  	gbc.gridheight=2;
	  	gbc.gridy=0;
	  	gbc.gridx = 0;
	  	gbc.insets = new Insets(0,0,50,0);
	  	add(stringInfoLabel, gbc);
  	  	
  	  	gbc.gridx = 0;
  	  	gbc.gridy = 2;
  	  	gbc.insets = new Insets(0,0,300,0);
  	  	gbc.gridheight = 3;
  	  	gbc.fill = GridBagConstraints.VERTICAL;
  	  	gbc.anchor = GridBagConstraints.CENTER;
  	  	add(pieChart, gbc);

  	  	gbc.gridwidth=1;
  	  	gbc.gridy=10;
  	  	gbc.gridx = 0;
  	  	gbc.insets = new Insets(100,0,0,0);
  	  	add(lblAdjustPitch, gbc);
  	  	
  	  	
        
	}
    	
	  /**
     * return pdf(x) = standard Gaussian pdf
  	 * credit to:
  	 * http://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
     * @param x
     * @return pdf(x) = standard Gaussian pdf
     */
  	 double pdf(double x) {
  	        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
  	    }
      	 
  	 // 
  	 /**
  	  * return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
  	  * credit to:
  	  * http://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
  	  * @param x
  	  * @param mu
  	  * @param sigma
  	  * @return double gaussian Pdf with given mu and sigma
  	  */
  	 double gaussianPdf(double x, double mu, double sigma) {
  	        return pdf((x - mu) / sigma) / sigma;
  	 }
    	
    	
	//override the paint component function so it draws on top of the ProbePanel
    @Override
	public void paintComponent(Graphics g) {
    	 super.paintComponent(g);
    	
	   	 Dimension panelSize = getRootPane().getSize();
		 
		 int h = (int) panelSize.getHeight();
		 int w = (int) panelSize.getWidth();
		 int  verticalIdent=8*h/10;

		 double muAtZero=w/2;

		 double cursorInitX = getCursoreHorizFromFreq(RunExperimentWindow.currentFreq);

   	 	 double sigma = Parameters.SIGMA;

   	 	
   	 	
		g.drawLine(xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1), w-xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1));
		 

   	 	// Draw a sliding gaussian function which appears on mouse movement
		 Polygon p = new Polygon();
		 int alpha = 127; // 50% transparent
    	 	Color greenColor = new Color(0, 255, 0, alpha);

			 
    	 	//System.out.println("CURRENT BET   "+RunExperimentWindow.currentBet);

		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 firstBetMu = RunExperimentWindow.allReportedCursorPositions[0];//getCursoreHorizFromFreq(RunExperimentWindow.allReportedFreq[0]);
		 //if(RunExperimentWindow.currentBet!=0){
			 double reportedFreqPixels  =0;
			 double reportedMuArray[] = new double[RunExperimentWindow.currentBet];
			 double reportedDiffArray[] = new double[RunExperimentWindow.currentBet];
			 
			 for(int bet =0;bet<RunExperimentWindow.currentBet;bet++){
				 reportedFreqPixels = RunExperimentWindow.allReportedCursorPositions[bet];
				 reportedMuArray[bet] =  muAtZero - firstBetMu+reportedFreqPixels;
				 reportedDiffArray[bet] = Math.abs(w/2 -(reportedMuArray[bet])); 
				
			 }
	
			//show the actual tone position
			 double realToneMu = muAtZero - firstBetMu+getCursoreHorizFromFreq(RunExperimentWindow.currentFreq);
			 
			 int maxFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2)));
			 int minFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2)));
			
			 int sliderMinVal = (int) getCursoreHorizFromFreq((Math.floor((440*Math.pow(2, (minFreqP-Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50));
	         int sliderMaxVal =(int) getCursoreHorizFromFreq((Math.ceil((440*Math.pow(2, (maxFreqP+Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50));

			
			 
			 if (realToneMu>= sliderMaxVal)
				 realToneMu = sliderMaxVal;
			 if (realToneMu<= sliderMinVal)
				 realToneMu = sliderMinVal;
		 
			 g.setColor(greenColor);
			 p.reset();
			 
			 g.fillRect((int)realToneMu-2, (int)(verticalIdent-Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN-1000* gaussianPdf(realToneMu, realToneMu, sigma)), 4, (int)(1000* gaussianPdf(realToneMu, realToneMu, sigma))); //(int)(verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1)));
			 //g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
		
			 Color greyColor = new Color(50, 50, 50, alpha);
			 g.setColor(greyColor);
			 p.reset();
			 
			 //for the first trial make response to appear as one grey line only
			 if(RunExperimentWindow.currentPracticeTrial < AdvancedOptionsWindow.numOfTrialsWithOneBet){
				 g.fillRect((int)muAtZero-2, (int)(verticalIdent-Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN-1000* gaussianPdf(muAtZero, w/2, sigma)), 4, (int)(1000* gaussianPdf(muAtZero, w/2, sigma))); 
			 }
			 else{
				 g.fillPolygon(PracticeProbePanel.p.xpoints, PracticeProbePanel.p.ypoints, PracticeProbePanel.p.npoints);
			 }//end of else
			 
			 if(showTrigger){
					if(AdvancedOptionsWindow.triggerIsSelected){
							g.setColor(Parameters.TRIGGER_COLOR);
							g.fillRect(	AdvancedOptionsWindow.leftCornerX, 
					    			AdvancedOptionsWindow.leftCornerY, 
					    			AdvancedOptionsWindow.triggerWidth, 
					    			AdvancedOptionsWindow.triggerHeight
					    			);
			    			
			    		}//end of if
					
		        }
        }
    /**
     * converting frequency into mouse x coordinate 
     * @param frequency
     * @return mouse x coordinate
     */
    public double  getCursoreHorizFromFreq(double frequency){
    	
    	Dimension panelSize = this.getSize();//getRootPane().getSize();
    	int maxFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2)));
		int minFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2)));
		
		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);

        double convFactor = (double)(sliderMaxVal-sliderMinVal)/(panelSize.width-2*xPixToTrim);
        
        //we trim 50 pixels from each end of the screen
        double x = (frequency-sliderMinVal)/convFactor+xPixToTrim;
        return x;
    }
    
   
    /**
     * converting  mouse x coordinate into frequency
     * @param x
     * @return frequency
     */
    public  double getFreqFromCursor(double x){
    	Dimension panelSize = this.getSize();//getRootPane().getSize();
    	int maxFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2)));
		int minFreqP = (int) (69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2)));
        
		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        
        
        //we trim 50 pixels from each end of the screen
        double convFactor = (double)(sliderMaxVal-sliderMinVal)/(panelSize.width-2*xPixToTrim);
        
        double frequency = convFactor*(x-xPixToTrim)+sliderMinVal;
       
        return frequency;
    }
	
}
