/**
 *  @author WolinLabs modified by Vahan Babushkin
 * 	Code taken from publicly available WolinLabs Site:
 * 	http://www.wolinlabs.com/blog/java.sine.wave.html
 */
package auditoryTaskPackage;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Polygon;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.math.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class ProbePanel extends JComponent {
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
	public static JLabel lblAdjustPitch = new JLabel();
	public static Polygon p = new Polygon();
	public static boolean showTrigger = false;
	public static double heightAtRealTone;
	public static double maxScore;
	
    public ProbePanel(){
    	//setSize(Main.screenWidth-200, 620);
    	setLayout(new BorderLayout(10,10)); 
    	String message = String.format("<html><center>Guess # %d <br>"
	    			 + "Please swipe the touchpad or move the mouse to adjust the pitch.<br><br><br></center></html>"
	    			 ,(RunExperimentWindow.currentBet+1)
	    			);
    	JLabel stringInfoLabel = new JLabel(message);
    	stringInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	stringInfoLabel.setForeground(Color.RED);
    	stringInfoLabel.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
        lblAdjustPitch.setHorizontalAlignment(SwingConstants.CENTER);

        //hide mouse cursor   
  	    if(!Parameters.DISPLAY_CURSOR_ON_PROBE){
  	  		Toolkit toolkit = Toolkit.getDefaultToolkit();
  	  	    Point hotSpot = new Point(0,0);
  	  	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
  	  	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
  	    	setCursor(invisibleCursor);
  	    }
  	    
        add(lblAdjustPitch,BorderLayout.CENTER);
        add(stringInfoLabel, BorderLayout.PAGE_END);
  
      //Non-UI stuff
        m_thread = new SmoothSoundGenerator();
        m_thread.start();
        

    }//end of constructor
    
    
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

//  	 	if(RunExperimentWindow.currentTrial>5 && RunExperimentWindow.currentTrial<2 ){
//  	 		if(RunExperimentWindow.currentBet != 0){
//  	 			g.drawLine(xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1), w-xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1));
//  	 		}
//  	 	}
  	 	

  	 	g.drawLine(xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1), w-xPixToTrim/2, verticalIdent-(Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN+1));

  	 	

  	 	// Draw a sliding gaussian function which appears on mouse movement
  	 	
		 int alpha = 127; // 50% transparent

		//Draw gaussian function
		 Color greyColor = new Color(50, 50, 50, alpha);
		 g.setColor(greyColor);
		 p.reset();
		 

		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 firstBetMu = RunExperimentWindow.allReportedCursorPositions[0];//getCursoreHorizFromFreq(RunExperimentWindow.allReportedFreq[0]);
		 
		 double reportedFreqPixels  = 0;
		 double reportedMuArray[] ;
		 double reportedDiffArray[];
		 
		 reportedMuArray = new double[RunExperimentWindow.currentBet];
		 reportedDiffArray = new double[RunExperimentWindow.currentBet];

		 for(int bet =0;bet<RunExperimentWindow.currentBet;bet++){
			 reportedFreqPixels = RunExperimentWindow.allReportedCursorPositions[bet];
			 reportedMuArray[bet] =  muAtZero - firstBetMu+reportedFreqPixels;
			 reportedDiffArray[bet] = Math.abs(w/2 -(reportedMuArray[bet])); 
		 }
		 
		 double currentMu = muAtZero-firstBetMu+pX;	
		 double diff = Math.abs(w/2 -(currentMu));
		 double mixSum =0;
		 
		 int bet;

		 
		 double realToneMu = muAtZero - firstBetMu+getCursoreHorizFromFreq(RunExperimentWindow.currentFreq);
		 
		 //start to add distributions
		 for (int x = (int) (muAtZero-w/2); x <(int)(muAtZero+w/2+1); x++) {
			 if(RunExperimentWindow.allReportedFreq[0]!=0 && RunExperimentWindow.trainingBetsSection==false)
				 mixSum=2*1000*gaussianPdf((x), w/2, sigma);
			 else
				 mixSum=0;
			
			 for(bet=1;bet<RunExperimentWindow.currentBet;bet++){//changed from bet=1
				 //System.out.println("BET # "+ bet + " freq = "+reportedMuArray[bet]);
				 if(RunExperimentWindow.allReportedFreq[bet]!=0)
					 mixSum += 1000 * gaussianPdf((x), reportedMuArray[bet], sigma)/(1+Math.log10(mixSum+1));
				 else
					 mixSum+=0;
			 }
		
			 if(mouseIsMoving){
				 double y= mixSum+1000 * gaussianPdf((x), currentMu, sigma)/(1+Math.log10(mixSum+1));
				 p.addPoint(x, (int) (verticalIdent-Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN-y));
			 }
			 else{
				 p.addPoint(x, (int) (verticalIdent-Parameters.HEIGHT_ADJUSTMENT_FOR_GAUSSIAN-mixSum));
			 }
			 if(x == (int)Math.round(realToneMu)){
					heightAtRealTone = mixSum;
			}
		 }//end of iterating over all horizontal pixels
		 
		//place the distribution in the center after the first bet
		if(RunExperimentWindow.currentBet != 0 ){
			 g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
		 }
		 
		
		
		//System.out.println("REAL TONE MU = " +realToneMu + "    "+ RunExperimentWindow.currentFreq+ "   "+heightAtRealTone);
		
		//maximum score at the beginning of the trial
		maxScore = 2*1000*gaussianPdf(w/2, w/2, sigma);
		
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
		
		
		//int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-ExperimentSetupWindow.jitter/2-69)/12))/50)*50);
        //int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+ExperimentSetupWindow.jitter/2-69)/12))/50)*50);
		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
//        System.out.println("maxFreqP " +maxFreqP);
//        System.out.println("minFreqP " +minFreqP);
//        System.out.println("sliderMinVal "+sliderMinVal);
//        System.out.println("sliderMaxVal "+sliderMaxVal);
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
		
		
//		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-ExperimentSetupWindow.jitter/2-69)/12))/50)*50);
//      int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+ExperimentSetupWindow.jitter/2-69)/12))/50)*50);
        
		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+Parameters.RESPONSE_SCALE_LIM_BOUNDARY_P-69)/12))/50)*50);
        
//        System.out.println("maxFreqP " +maxFreqP);
//        System.out.println("minFreqP " +minFreqP);
//        System.out.println("sliderMinVal "+sliderMinVal);
//        System.out.println("sliderMaxVal "+sliderMaxVal);
        
        //we trim 50 pixels from each end of the screen
        double convFactor = (double)(sliderMaxVal-sliderMinVal)/(panelSize.width-2*xPixToTrim);
        
        double frequency = convFactor*(x-xPixToTrim)+sliderMinVal;
       
        return frequency;
    }
    
    
    /**
     * 
     */
    public static void stopThread(){
    	if(ProbePanel.m_thread!=null)
    		if(ProbePanel.m_thread.isAlive()){
    			//System.out.println("Now temp thread is:   "+ProbePanel.m_thread);
    			ProbePanel.m_thread.exit();
    		}
    }
    
    /**
     * 
     * @author WolinLabs modified by Vahan Babushkin
     * Code taken from publicly available WolinLabs Site:
     * http://www.wolinlabs.com/blog/java.sine.wave.html
     * Class extending thread to generate the samples 
     * Keeps refilling the audio output buffer, 100ms worth of sine samples at a time. 
     * 
     */
    class SmoothSoundGenerator extends Thread {

        final static public int SAMPLING_RATE = 44100;
        final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
        final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer

        //You can play with the size of this buffer if you want.  Making it smaller speeds up
        //the response to the slider movement, but if you make it too small you will get 
        //noise in your output from buffer underflows, etc...
        //final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer

        // Size in bytes of sine wave samples we'll create on each loop pass 
        final static public int SINE_PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE); 
       
        SourceDataLine line;
        //public double fFreq;                                    //Set from the pitch slider
        public boolean bExitThread = false;
        
        
        //Get the number of queued samples in the SourceDataLine buffer
        private int getLineSampleCount() {
           return line.getBufferSize() - line.available();
        }
        

        //Continually fill the audio output buffer whenever it starts to get empty, SINE_PACKET_SIZE/2
        //samples at a time, until we tell the thread to exit
        public void run() {
        	
        	
           //Position through the sine wave as a percentage (i.e. 0-1 is 0-2*PI)
           double fCyclePosition = 0;
           
           //Open up the audio output, using a sampling rate of 44100hz, 16 bit samples, mono, and big 
           // endian byte ordering.   Ask for a buffer size of at least 2*SINE_PACKET_SIZE
           try {
              AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
              DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, SINE_PACKET_SIZE*2);

              if (!AudioSystem.isLineSupported(info))
                 throw new LineUnavailableException();

              line = (SourceDataLine)AudioSystem.getLine(info);
              line.open(format);  
              line.start();
           }
           catch (LineUnavailableException e) {
              System.out.println("Line of that type is not available");
              e.printStackTrace();            
              System.exit(-1);
           }

           //System.out.println("Requested line buffer size = " + SINE_PACKET_SIZE*2);            
           //System.out.println("Actual line buffer size = " + line.getBufferSize());


           ByteBuffer cBuf = ByteBuffer.allocate(SINE_PACKET_SIZE);

           //On each pass main loop fills the available free space in the audio buffer
           //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
           //Each sample is spaced 1/SAMPLING_RATE apart in time
           
           mouseIsMoving=false;
           //System.out.println("Is mouse moving "+mouseIsMoving);
           while (bExitThread==false) {
        	   fFreqCursorX = pX;
        	   if(mouseIsMoving){
        		   fFreq = getFreqFromCursor(pX);   
        	   }
        	   else{
        		   fFreq=0;
        	   }
        	   
        	   double fCycleInc = fFreq/SAMPLING_RATE;   //Fraction of cycle between samples

        	   cBuf.clear();                             //Toss out samples from previous pass

        	   //Generate SINE_PACKET_SIZE samples based on the current fCycleInc from fFreq
        	   for (int i=0; i < SINE_PACKET_SIZE/SAMPLE_SIZE; i++) {
                 cBuf.putShort((short)(Short.MAX_VALUE * Math.sin(2*Math.PI * fCyclePosition)));

                 fCyclePosition += fCycleInc;
                 if (fCyclePosition > 1)
                    fCyclePosition -= 1;
        	   
              }

              //Write sine samples to the line buffer
              // If the audio buffer is full, this would block until there is enough room,
              // but we are not writing unless we know there is enough space.
              line.write(cBuf.array(), 0, cBuf.position());    


              //Wait here until there are less than SINE_PACKET_SIZE samples in the buffer
              //(Buffer size is 2*SINE_PACKET_SIZE at least, so there will be room for 
              // at least SINE_PACKET_SIZE samples when this is true)
              try {
                 while (getLineSampleCount() > SINE_PACKET_SIZE) 
                    Thread.sleep(1);                          // Give UI a chance to run 
              }
              catch (InterruptedException e) {                // We don't care about this
              }
           }

           line.drain();
           line.close();
        }
        
        
        
        public void exit() {
           bExitThread=true;
           mouseIsMoving = false;
        }
     }

}