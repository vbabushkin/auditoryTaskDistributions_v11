package auditoryTaskPackage;

import java.awt.Color;

public class Parameters {
	static String VERSION_INFO = "Auditory Task v 11.0";
	static String VERSION_DATE = "24 July, 2017";
	
	// please modify to show the participant how many trials he has to do for the real experiment
	static int NUM_EXPERIMENTAL_TRIALS = 48;
	static int RUN_NUM_DEFAULT = 3;
	
	static int MIN_FREQ_DEFAULT=400;
    static int MAX_FREQ_DEFAULT=1000;
    static int RESPONSE_SCALE_LIM_BOUNDARY_P = 15; 
    
    static int JITTER_DEFAULT=1;
    static String STUDY_ID_DEFAULT="BECA";
    static int STUDY_VER_DEFAULT=1;
    static int SESSION_NUM_DEFAULT=1;
    static String SUB_NUM_DEFAULT="";
    static int SAMPLE_FREQ_NUM_DEFAULT=4;
    
    static int INTERTRIAL_TASK_DURATION = 1000;
    static int PRESTIMULUS_TASK_DURATION = 500;
    static int STIMULUS_TASK_DURATION = 250;
    static int DELAY_TASK_DURATION = 2500;
    static int PROBE_TASK_DURATION_IF_IDLE = 20000;
    static int START_OF_EXPERIMENT_TASK_DURATION_IF_IDLE = 5400000;//90 minutes
    static int INTERBLOCK_TASK_DURATION_IF_IDLE = 5400000;
    static int PROBE_TASK_DURATION_IF_IDLE_SELF_PACED = 5400000;
    static int END_OF_PRACTICE_TASK_DURATION_IF_IDLE = 5400000;
    static int END_OF_EXPERIMENT_TASK_DURATION_IF_IDLE = 15000;
    static int SCORE_REPORT_TASK_DURATION = 2500;
    static int NUMBER_OF_BETS_PER_TRIAL = 6;
    static int MAX_POINTS_PER_BET = 100;
    
    static int TRIALS_ONE_BET_NUM_DEFAULT = 1;
    static int TRIALS_NOT_TIMED_NUM_DEFAULT = 2;
    static int TRIALS_TIMED_NUM_DEFAULT = 2;
    static int TRIALS_PRACTICE_BETS_NUM_DEFAULT = 2;
    static Color TRIGGER_COLOR = Color.WHITE;
    static int TRIGGER_ON_CLICK_DURATION = 250;
    
    static int TRIGGER_LEFT_CORNER_X = 0;
    static int TRIGGER_LEFT_CORNER_Y = 930;
    static int TRIGGER_WIDTH = 150;
    static int TRIGGER_HEIGHT = 150;
    
	final static public int SAMPLING_RATE = 44100;
    final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
    final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer
    
    static int HEIGHT_ADJUSTMENT_FOR_GAUSSIAN = 10;
    
    static double SIGMA = 15;
    
    static boolean DISPLAY_CURSOR_ON_PROBE = false;
    static boolean IS_TRIGGER_SELECTED = true;
    static boolean IS_PROBE_TASK_TIMED = true;
    
    static int FONT_SIZE = 26;
    static String FONT_FAMILY = "Times New Roman";
}
