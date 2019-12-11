/**
 *  @author WolinLabs modified by Vahan Babushkin
 * 	Code taken from publicly available WolinLabs Site:
 * 	http://www.wolinlabs.com/blog/java.sine.wave.html
 */
package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class AdjustVolumePanel extends JPanel {
	public static JSlider m_sliderPitch;
	public static int fFreq;
	public static boolean mouseClickedFirstTime = false;//controls whether the sound is played at the beginning of each trial
	public static SmoothSoundGenerator m_thread;
	
    public AdjustVolumePanel() {
    	setSize(Main.screenWidth-200, 620);
    	setLayout(new BorderLayout(10,10)); //GridLayout(2,1));
    	JLabel lblAdjustPitch = new JLabel("<html><center> Please slide to hear tones playing.<br>Use your system volume control to ajdust the loudness.<br>Press 'ESC' or 'Q' to exit (or just close the window).</center></html>");
        lblAdjustPitch.setHorizontalAlignment(SwingConstants.CENTER);
        //set fore ground color to the label
        lblAdjustPitch.setForeground(Color.RED);
        //set font to the label
        lblAdjustPitch.setFont(new Font(RunExperimentWindow.fontFamily, Font.BOLD | Font.ITALIC, RunExperimentWindow.fontSize));
        System.out.println("MAX_FREQUENCY "+(69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2))));
        System.out.println("MIN_FREQUENCY "+(69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2))));
        long maxFreqP = 69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2));
		long minFreqP = 69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2));
		int sliderMinVal = (int) (Math.floor((440*Math.pow(2, (minFreqP-AdvancedOptionsWindow.jitter/2-69)/12))/50)*50);
        int sliderMaxVal =(int) (Math.ceil((440*Math.pow(2, (maxFreqP+AdvancedOptionsWindow.jitter/2-69)/12))/50)*50);
        m_sliderPitch = new JSlider();
        m_sliderPitch.setName("");
        m_sliderPitch.setMinimum(sliderMinVal);
        m_sliderPitch.setPaintLabels(false);
        m_sliderPitch.setPaintTicks(false);
        m_sliderPitch.setMajorTickSpacing(50);
        m_sliderPitch.setMinorTickSpacing(5);
        m_sliderPitch.setMaximum(sliderMaxVal);
        m_sliderPitch.setValue((int)(sliderMinVal+(sliderMaxVal-sliderMinVal)/2));
        m_sliderPitch.setFocusable(false);

        add(m_sliderPitch,BorderLayout.CENTER);
        //add label to the contentPane 
        add(lblAdjustPitch,BorderLayout.NORTH);
        m_sliderPitch.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me)
            {
            	mouseClickedFirstTime = true;
            }
          });
      //Non-UI stuff
        m_thread = new SmoothSoundGenerator();
        m_thread.start();
        

    }
    /**
     * 
     */
    public void stopThread(){
    	if(PracticeProbePanel.m_thread!=null)
    		if(PracticeProbePanel.m_thread.isAlive()){
    			System.out.println("Now temp thread is:   "+PracticeProbePanel.m_thread);
    			PracticeProbePanel.m_thread.exit();
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
        public double fFreq;                                    //Set from the pitch slider
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
           while (bExitThread==false) {
        	   if(mouseClickedFirstTime)
        		   fFreq = m_sliderPitch.getValue();

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
        }
     }

}