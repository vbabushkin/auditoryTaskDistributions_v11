/**
 * Stimulus panel representation
 * @author Vahan Babushkin
 */
package auditoryTaskPackage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class StimulusPanel extends JPanel {

	static long startTime;
	

    //You can play with the size of this buffer if you want.  Making it smaller speeds up
    //the response to the slider movement, but if you make it too small you will get 
    //noise in your output from buffer underflows, etc...
    // final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer

    // Size in bytes of sine wave samples we'll create on each loop pass 
    final static public int SINE_PACKET_SIZE = (int)(Parameters.BUFFER_DURATION*Parameters.SAMPLING_RATE*Parameters.SAMPLE_SIZE); 
   
    static SourceDataLine line;
    public double fFreq;                                    //TODO: generate randomly
    public boolean bExitThread = false;
    
    
   /**
    * 
    */
    public StimulusPanel(double fFreq) {
    	setLayout(new BorderLayout());
		JPanel tempGridPanel = new JPanel(new BorderLayout());
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
		
		ImageIcon imgIcon = new ImageIcon(getClass().getResource("fix1.png"));
		
		Image img = imgIcon.getImage();
		Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);

		
	    JLabel fixImgLabel = new JLabel();
	    fixImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    fixImgLabel.setIcon(newIcon);
	    
	    //add label to the contentPane 
	    tempGridPanel.add(fixImgLabel,BorderLayout.PAGE_START);
	    FixedPanel.add(tempGridPanel);
	    add(FixedPanel,BorderLayout.CENTER);
	    
    	this.fFreq = fFreq;
        startTime= System.currentTimeMillis();

        generateSound(this.fFreq);

    }
    /**
     * 
     * @param fFreq
     */
    public void set(double fFreq){
    	this.fFreq = fFreq;
    	
    }
    /**
     * Continually fill the audio output buffer whenever it starts to get empty, SINE_PACKET_SIZE/2
     * samples at a time, until we tell the thread to exit
     */
    public void generateSound(double fFreq) {
    	//System.out.println(System.currentTimeMillis());

       //Position through the sine wave as a percentage (i.e. 0-1 is 0-2*PI)
       double fCyclePosition = 0;
       
       //Open up the audio output, using a sampling rate of 44100hz, 16 bit samples, mono, and big 
       // endian byte ordering.   Ask for a buffer size of at least 2*SINE_PACKET_SIZE
       try {
          //AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
    	  AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16,1,2,44100, true);
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
       //System.out.println("Generated frequency " +fFreq);

       ByteBuffer cBuf = ByteBuffer.allocate(SINE_PACKET_SIZE);

       double TOTAL = (double) AdvancedOptionsWindow.stimulusDuration/1000 * Parameters.SAMPLING_RATE; // number of samples
       double FRAME_COUNT = 0;
       
       double FRAME_LENGTH_START = 128;
       double FRAME_LENGTH_END = 1024;
       
       double INTENSITY = 1.00;

       double fCycleInc = fFreq/Parameters.SAMPLING_RATE;   //Fraction of cycle between samples

       //On each pass main loop fills the available free space in the audio buffer
       //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
       //Each sample is spaced 1/SAMPLING_RATE apart in time
       while (System.currentTimeMillis()-startTime<AdvancedOptionsWindow.stimulusDuration) {

          cBuf.clear();                             //Toss out samples from previous pass

          //Generate SINE_PACKET_SIZE samples based on the current fCycleInc from fFreq
          for (int i=0; i < SINE_PACKET_SIZE/Parameters.SAMPLE_SIZE; i++) {

        	  if (FRAME_COUNT <= FRAME_LENGTH_START) {
         		 cBuf.putShort((short)(Short.MAX_VALUE * INTENSITY * Math.sin(Math.PI/2 * FRAME_COUNT/FRAME_LENGTH_START)* Math.sin(2*Math.PI * fCyclePosition)));        		 
         	 } else if ((TOTAL - FRAME_COUNT) <= FRAME_LENGTH_END) {
         		 short value;
         		 
         		 if (TOTAL - FRAME_COUNT < 0) {
         			 break;
         		 }
         		 else {
         			 value = (short) (Short.MAX_VALUE * INTENSITY * Math.sin(Math.PI/2 * (TOTAL - FRAME_COUNT)/FRAME_LENGTH_END) * Math.sin(2*Math.PI * fCyclePosition));
         		 }
         		 
         		 //System.out.println(TOTAL - FRAME_COUNT + " | " + value);
         		 
         		 cBuf.putShort(value);        		 
         		 
         	 } else {
         		 cBuf.putShort((short)(Short.MAX_VALUE * INTENSITY * Math.sin(2*Math.PI * fCyclePosition)));
         	 }
             
             FRAME_COUNT += 1;
           
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
    
    /**
     * Get the number of queued samples in the SourceDataLine buffer
     * @return
     */
    private static int getLineSampleCount() {
       return line.getBufferSize() - line.available();
    }
    
}
