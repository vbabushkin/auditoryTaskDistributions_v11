package auditoryTaskPackage;


import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import auditoryTaskPackage.DelayPanel;
import auditoryTaskPackage.PracticeProbePanel.SmoothSoundGenerator;

public class RunExperimentWindow extends JFrame{
	
	static final JPanel panelMain = new JPanel();
	static PracticeProbePanel.SmoothSoundGenerator tempThread = PracticeProbePanel.m_thread;
	static boolean isKeyPressed = false;
	static boolean isProgramEnded = false;
	
	static int numTrials = AdvancedOptionsWindow.numTrials;
	static int numRuns = AdvancedOptionsWindow.numRuns;
	static int numFrequencies = AdvancedOptionsWindow.numFrequencies;
	static String studyId = AdvancedOptionsWindow.studyId;
	static int studyVer = AdvancedOptionsWindow.studyVer;
	static int sessionNum = ExperimentSetupWindow.sessionNum;
	static String subNum = ExperimentSetupWindow.subNum;
	static int numberOfBetsPerTrial = AdvancedOptionsWindow.numberOfBetsPerTrial;
    static int numOfTrialsWithOneBet = AdvancedOptionsWindow.numOfTrialsWithOneBet;
	static int numOfDemoTrialsNotTimed = AdvancedOptionsWindow.numOfDemoTrialsNotTimed; 
	static int numOfDemoTrialsTimed = AdvancedOptionsWindow.numOfDemoTrialsTimed;
	static int numOfPracticeBetTrials  = AdvancedOptionsWindow.numOfPracticeBetTrials; 
	static int stimulusDuration =AdvancedOptionsWindow.stimulusDuration;
	static int preStimulusDuration = AdvancedOptionsWindow.preStimulusDuration;
	static int intertrialDuration = AdvancedOptionsWindow.intertrialDuration;
	static int delayDuration = AdvancedOptionsWindow.delayDuration;
	static int probeDuration = AdvancedOptionsWindow.probeDuration;
	static int maxPointsPerBet = AdvancedOptionsWindow.maxPointsPerBet;
    static float maxFrequency = AdvancedOptionsWindow.maxFrequency;
    static float minFrequency = AdvancedOptionsWindow.minFrequency;
    static float jitter = AdvancedOptionsWindow.jitter;
    static int leftCornerX = AdvancedOptionsWindow.leftCornerX;
    static int leftCornerY = AdvancedOptionsWindow.leftCornerY;
    static int triggerWidth = AdvancedOptionsWindow.triggerWidth;
    static int triggerHeight = AdvancedOptionsWindow.triggerHeight;
	static boolean triggerIsSelected =  AdvancedOptionsWindow.triggerIsSelected;
	static boolean isProbeTaskTimed=AdvancedOptionsWindow.isProbeTaskTimed;
	public static int fontSize = AdvancedOptionsWindow.fontSize;
    public static String fontFamily = AdvancedOptionsWindow.fontFamily;
    public static boolean triggerUpperLeftIsSelected = AdvancedOptionsWindow.triggerUpperLeftIsSelected;
    public static boolean triggerLowerLeftIsSelected = AdvancedOptionsWindow.triggerLowerLeftIsSelected;
    public static boolean triggerLowerRightIsSelected = AdvancedOptionsWindow.triggerLowerRightIsSelected;
    public static boolean triggerUpperRightIsSelected = AdvancedOptionsWindow.triggerUpperRightIsSelected;
    public static boolean triggerOtherIsSelected = AdvancedOptionsWindow.triggerOtherIsSelected;
    static long startBlockTime;
    static long startPracticeTime;
	static double[][] practiceTaskMap;
	static double[][] experimentTaskMap;
	static PrintWriter writer;
	static String resDirectory = ExperimentSetupWindow.resDirectory;
	//we need to fix the start time to calculate the reaction time
    static long startTime =System.currentTimeMillis();
	static int mouseMovementRectWidth;
	static int mouseMovementRectHeight;
	
    static int currentBet = 0;
    
    static int currentPracticeTrial = 0;
    static int currentExperimentTrial = 0;
    
    static double scorePerBet = 0;
    static double cummulativeScorePerTrial=0;
    static double totalScore = 0;
    static int numBetsCompleted = 0;
    static int totalNumTrialsCompleted = 0;
    static double currentFreq;
    static double sigma = Parameters.SIGMA;//AdvancedOptionsWindow.sigma;
    
    final static ArrayList<Double> freqTempList = new ArrayList<Double>();
    final static ArrayList<Double> scoreTempList = new ArrayList<Double>();
    final static ArrayList<Double> freqCoordTempList = new ArrayList<Double>();
    static double [] allReportedFreq = new double[AdvancedOptionsWindow.numberOfBetsPerTrial+1];
    static double [] allReportedCursorPositions = new double[AdvancedOptionsWindow.numberOfBetsPerTrial+1];
    static double [] allReportedScoresPerBet = new double[AdvancedOptionsWindow.numberOfBetsPerTrial+1];
    
	static int numPracticeTrials = numOfTrialsWithOneBet + numOfDemoTrialsNotTimed + numOfDemoTrialsTimed;
	static boolean trainingBetsSection = false;
	static boolean isPracticeTerminated = false;
	static int numOfPracticeSessions = 0;
	static int numOfInstructionSessions = 0;
	
	public static String practiceTaskMapFileName;
    public static String experimentTaskMapFileName;
	public static String taskMapFileName;
	
    public static long timeElapsed;
    
    public static boolean  timedPracticeTrialsAreRunning = false;
    
    public static long startTimeBtwStimAndTrigger;
    public static long endTimeBtwStimAndTrigger;
    public static long stimStartTime;
    public static long stimEndTime;
    
    public static Dimension panelSize;
    
	//class constructor
	public RunExperimentWindow(){
    	setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		BorderLayout mainLayout =new BorderLayout(); //new FlowLayout();
		//to make it fullscreen
		//com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this,true);
		//com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
		getRootPane().putClientProperty("apple.awt.fullscreenable", Boolean.valueOf(true));
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setBounds(0, 0, Main.screenWidth, Main.screenHeight);
		setUndecorated(true);//sets it to fullscreen
		setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLayout(mainLayout);
        panelMain.setSize(1200, 620);
        panelMain.setLayout(mainLayout);
        panelMain.setFocusable(true);
        add(panelMain);
        
        //generate practice and experimental task maps
	 	practiceTaskMap = genPracticeTaskMap();
	 	//recordPracticeTaskMap();
	 	experimentTaskMap = genExperimentTaskMap();
	 	//recordExperimentTaskMap();
	 	panelSize = getRootPane().getSize();

	}//end of constructor
	
	/**
	 * This is the main module that controls Instructions version
	 * @throws InterruptedException
	 * @throws AWTException
	 * @throws IOException 
	 */
	 public static void runInstructionsModule() throws InterruptedException, AWTException, IOException {
		 loadParams();
		 if (ExperimentSetupWindow.startInstructions){

			System.out.println("numOfTrialsWithOneBet  "+numOfTrialsWithOneBet);
				
			ExperimentSetupWindow.startInstructions = false;
			ExperimentSetupWindow.startBlock = false;
			ExperimentSetupWindow.startPractice = false;
			
			//create an instance of the main class
	        final RunExperimentWindow frame = new RunExperimentWindow();
	       
	        //class for instructions panel 1 activity
	        final class InstructionsPanel1Task implements Runnable {
	            @Override
	            public void run() {
	            	System.out.println("Running instructions panel 1 task.");
	            	frame.getContentPane().removeAll();
	                frame.runInstructionsPanel1();
	                frame.revalidate();
	                frame.repaint();
	            }
	        };
	        
	        //class for end of practice panel activity
	        final class EndOfInstructionsTask implements Runnable {
	            @Override
	            public void run() {
	                System.out.println("Running eopi panel task.");
	                frame.getContentPane().removeAll();
	                PracticeProbePanel.stopThread();
	                frame.runEndOfInstructionPanel();
	                //timeElapsed =  System.currentTimeMillis() - startPracticeTime;
	                //recordToPracticeLogFileOnInterruption();
	                frame.revalidate();
	                frame.repaint();
	            }
	        };
	        
	        
	        
	        // declarations of tasks' instances
	        InstructionsPanel1Task instructionsPanel1TaskInst = new InstructionsPanel1Task();
	        EndOfInstructionsTask endOfInstructionsTaskInst = new EndOfInstructionsTask();
	        
	        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	        // 								STARTING INSTRUCTIONS
	        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	    		//-------------------------------------------------------------------------------------------------------//
			// 									Start instructions panel 
			//-------------------------------------------------------------------------------------------------------// 
			changePanelAppearance(frame, instructionsPanel1TaskInst, false);
	       

			//reinitialize the variables
		 	isKeyPressed = false;
		 	isProgramEnded = false;
		 	currentBet = 0;
		 	currentPracticeTrial = 0;
		 	scorePerBet = 0;
		 	cummulativeScorePerTrial=0;
		 	totalScore = 0;
		 	numBetsCompleted=0;
		 	totalNumTrialsCompleted=0;
		 	trainingBetsSection = false;
		 	timedPracticeTrialsAreRunning  = false;
//		 	startPracticeTime = System.currentTimeMillis();
//	        timeElapsed = 0;
	        
		 	//run the instruction module 
		 	runInstructions(frame);
		 
		 	//initiate end of practice services
	        //redefine a new instance of executor for each new trial
	        final ExecutorService endOfPracticeService = Executors.newCachedThreadPool();
	        //invoke the end of experiment services
	        //---------------------------------------------------------------------------------------------------//
	        endOfPracticeService.submit(endOfInstructionsTaskInst);
	        frame.setVisible(true);
	        frame.revalidate();
		    	frame.repaint();
		    	frame.setFocusable(true);
		    	frame.requestFocusInWindow();
		    	
		    	frame.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						isPracticeTerminated = true;
						ExperimentSetupWindow.startPractice = true;
						ExperimentSetupWindow.startBlock = true;
						ExperimentSetupWindow.startInstructions = true;
			    			frame.dispose();
			    			//stopAllThreads();
			    			//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
			    			//recordToPracticeLogFileOnInterruption();
			            	endOfPracticeService.shutdownNow();
				        System.out.println("DONE WITH PRACTICE TASK");
					}
	
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
	
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
	
					@Override
					public void mousePressed(MouseEvent e) {
						
						
					}
	
					@Override
					public void mouseReleased(MouseEvent e) {
						
					}
		        });
	
			    	System.out.println(numOfInstructionSessions + " INSTRUCTION SESSIONS ACCOMPLISHED");
				System.out.println("END OF INSTRUCTIONS.");
		        numOfInstructionSessions++;
		        endOfPracticeService.awaitTermination(Parameters.END_OF_EXPERIMENT_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
		        frame.dispose();
		        
		        ExperimentSetupWindow.startPractice = true;
				ExperimentSetupWindow.startBlock = true;
				ExperimentSetupWindow.startInstructions = true;
				
				
			 }//end of if checking whether the startInstructions is true 
			 
        
	 }//end runInstructions
	 
	 
	 /**
		 * This is the main module that controls practice trials versions
		 * @throws InterruptedException
		 * @throws AWTException
		 * @throws IOException 
		 */
		 public static void runPracticeTrialsModule() throws InterruptedException, AWTException, IOException {
			 loadParams();
			 if (ExperimentSetupWindow.startPractice){
				 
				ExperimentSetupWindow.startPractice = false;
				ExperimentSetupWindow.startBlock = false;
				ExperimentSetupWindow.startInstructions = false;
				
				//create an instance of the main class
		        final RunExperimentWindow frame = new RunExperimentWindow();
		       
		        recordPracticeTaskMap();
		     
		        //class for end of practice panel activity
		        final class EndOfInstructionsTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running eopi panel task.");
		                frame.getContentPane().removeAll();
		                PracticeProbePanel.stopThread();
		                frame.runEndOfPracticePanel();
		                timeElapsed =  System.currentTimeMillis() - startPracticeTime;
		                recordToPracticeLogFileOnInterruption();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        
		        
		        // declarations of tasks' instances
		        EndOfInstructionsTask endOfInstructionsTaskInst = new EndOfInstructionsTask();
		        
		       
		        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		        // 								STARTING THE PRACTICE
		        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		        
		        //reinitialize the variables
			 	isKeyPressed = false;
			 	isProgramEnded = false;
			 	currentBet = 0;
			 	currentPracticeTrial = 0;
			 	scorePerBet = 0;
			 	cummulativeScorePerTrial=0;
			 	totalScore = 0;
			 	numBetsCompleted = 0;
			 	totalNumTrialsCompleted = 1;
			 	trainingBetsSection = false;
			 	timedPracticeTrialsAreRunning  = false;
			 	startPracticeTime = System.currentTimeMillis();
		        timeElapsed = 0;
			 	//run the practice
			 	runPracticeTrials(frame);
			 
			 	//initiate end of practice services
		        //redefine a new instance of executor for each new trial
		        final ExecutorService endOfPracticeService = Executors.newCachedThreadPool();
		        //invoke the end of experiment services
		        //---------------------------------------------------------------------------------------------------//
		        endOfPracticeService.submit(endOfInstructionsTaskInst);
		        frame.setVisible(true);
		        frame.revalidate();
			    	frame.repaint();
			    	frame.setFocusable(true);
			    	frame.requestFocusInWindow();
			    	
			    	frame.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							isPracticeTerminated = true;
							ExperimentSetupWindow.startPractice = true;
 							ExperimentSetupWindow.startBlock = true;
 							ExperimentSetupWindow.startInstructions = true;
			    			frame.dispose();
			    			//stopAllThreads();
			            	endOfPracticeService.shutdownNow();
					        System.out.println("DONE WITH PRACTICE TASK");
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent e) {
							
							
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							
						}
			        });

			        numOfInstructionSessions++;
			        endOfPracticeService.awaitTermination(Parameters.END_OF_EXPERIMENT_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
			        frame.dispose();
			        System.out.println(numOfInstructionSessions + " INSTRUCTION SESSIONS ACCOMPLISHED");
					System.out.println("END OF PRACTICE TRIALS.");
					 
					
					ExperimentSetupWindow.startPractice = true;
					ExperimentSetupWindow.startBlock = true;
					ExperimentSetupWindow.startInstructions = true;
					
			 }//end of checking whether is the startPractice is true

			
		 }//end runPracticeTrialsModule
		

	
	 	/**
		 * This is the main module that controls both experiment and practice versions
		 * @throws InterruptedException
		 * @throws AWTException
	 	 * @throws IOException 
		 */
		 public static void runBlockModule() throws InterruptedException, AWTException, IOException {
			 loadParams();
			 if (ExperimentSetupWindow.startBlock){
				 
				ExperimentSetupWindow.startPractice = false;
				ExperimentSetupWindow.startBlock = false;
				ExperimentSetupWindow.startInstructions = false;
				
				//create an instance of the main class
		        final RunExperimentWindow frame = new RunExperimentWindow();
		       
		        recordExperimentTaskMap();
		       
		        //class for start of experiment panel activity
		        final class StartOfExperimentPanelTask implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running soe panel task.");
		            	frame.getContentPane().removeAll();
		                frame.runStartOfExperimentPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        //class for end of experiment panel activity
		        final class EndOfBlockPanelTask implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running eoe panel task.");
		            	frame.getContentPane().removeAll();
		                frame.runEndOfBlockPanel();
		                timeElapsed =  System.currentTimeMillis() - startBlockTime;
		                recordToExperimentLogFileOnInterruption();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        // declarations of tasks' instances
		   
		        StartOfExperimentPanelTask startOfExperimentPanelTaskInst = new StartOfExperimentPanelTask();
		        EndOfBlockPanelTask endOfBlockPanelTaskInst = new EndOfBlockPanelTask();
	
		        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		        // 								STARTING THE BLOCK
		        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
				 
		        //reinitialize the variables
				isKeyPressed = false;
	 		 	isProgramEnded = false;
	 		 	currentBet = 0;
	 		 	currentExperimentTrial = 0;
	 		 	scorePerBet = 0;
	 		 	cummulativeScorePerTrial=0;
	 		 	totalScore = 0;
	 		 	numBetsCompleted=0;
	 		 	totalNumTrialsCompleted=0;
	 		 	startBlockTime = System.currentTimeMillis();
		        timeElapsed = 0;
				//-------------------------------------------------------------------------------------------------------//
				// 									Start experiment panel 
				//-------------------------------------------------------------------------------------------------------//  
				
 		 	
 		 		changePanelAppearance(frame, startOfExperimentPanelTaskInst, false);

 			 	//run the practice
 			 	runBlock(frame);
 			 	
 				final ExecutorService endOfExperimentService = Executors.newCachedThreadPool();
 				 //invoke the end of experiment services
 				 //---------------------------------------------------------------------------------------------------//
 				 endOfExperimentService.submit(endOfBlockPanelTaskInst);
 				 frame.setVisible(true);
 				 frame.revalidate();
 				 frame.repaint();
 				 frame.setFocusable(true);
 				 frame.requestFocusInWindow();	
 				 
 				 frame.addMouseListener(new MouseListener() {
 		          	 
 						@Override
 						public void mouseClicked(MouseEvent e) {
 							ExperimentSetupWindow.startPractice = true;
 							ExperimentSetupWindow.startBlock = true;
 							ExperimentSetupWindow.startInstructions = true;
 							endOfExperimentService.shutdownNow();
 							frame.dispose();
 							ExperimentSetupWindow.startBlock = true;
 						}

 						@Override
 						public void mouseEntered(MouseEvent e) {
 							// TODO Auto-generated method stub
 							
 						}

 						@Override
 						public void mouseExited(MouseEvent e) {
 							// TODO Auto-generated method stub
 							
 						}

 						@Override
 						public void mousePressed(MouseEvent e) {
 							// TODO Auto-generated method stub
 							
 						}

 						@Override
 						public void mouseReleased(MouseEvent e) {
 							// TODO Auto-generated method stub
 							
 						}
 			        });
 			    	
 				 //if nothing is pressed for some time -- close the window
 				 endOfExperimentService.awaitTermination(Parameters.END_OF_EXPERIMENT_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
 				 System.out.println("END OF EXPERIMENT");
 				 frame.dispose();
 				 
 				 ExperimentSetupWindow.startPractice = true;
 				 ExperimentSetupWindow.startBlock = true;
 				 ExperimentSetupWindow.startInstructions = true;
 				 
 				 
	 	}//end of checking whether the startBlock ==  true;
			
 		 	
	 }//end runBlockModule
	 
	
	/**
	 * generates PRACTICE task map, including the stimulus frequencies, jitter, and the main frequencies together with its p values
	 * for i = 0 to numFrequences - 1
	 * @return
	 */
	public  static double[][] genPracticeTaskMap(){
		//the number of practice frequencies has to be lcm of numOfTrialsWithOneBet,numOfDemoTrialsNotTimed and numOfDemoTrialsTimed
		int totalNumOfPracticeTrials = numOfTrialsWithOneBet + numOfDemoTrialsNotTimed + numOfDemoTrialsTimed;
		
		float jitter = AdvancedOptionsWindow.jitter;
		float minFrequency = AdvancedOptionsWindow.minFrequency;
		float maxFrequency = AdvancedOptionsWindow.maxFrequency;
		double taskMap[][] = new double[totalNumOfPracticeTrials][4];

		int maxFreqP = (int) (69+Math.round(12*Math.log(maxFrequency/440)/Math.log(2)));
		int minFreqP = (int) (69+Math.round(12*Math.log(minFrequency/440)/Math.log(2)));
		
		ArrayList <Double> tempFreqValuesP = new ArrayList <Double>();
		for (int trial = 0; trial < totalNumOfPracticeTrials; trial++){
			tempFreqValuesP.add(minFreqP+Math.ceil((double)(trial)*(maxFreqP-minFreqP)/(totalNumOfPracticeTrials+1)));
		}
		
		//set the seed
		long seed = System.nanoTime();
		
		//shuffle the array list
		Collections.shuffle(tempFreqValuesP, new Random(seed));
		
		//generate randomly jittered frequencies
		for (int trial = 0; trial < totalNumOfPracticeTrials; trial++){
			
				double tempP = tempFreqValuesP.get(trial);
				double minValP =  tempP-jitter/2;
				double maxValP =  tempP+jitter/2;
				
				double genFreqP = minValP + (Math.random() * (maxValP - minValP));
			
				taskMap[trial][0] = 440*Math.pow(2, (genFreqP-69)/12);
				taskMap[trial][1] = genFreqP;
				taskMap[trial][2] = 440*Math.pow(2, (tempP-69)/12);
				taskMap[trial][3] = tempP;
		}

		taskMap = shuffle2DArrayRows(taskMap);
		
		return taskMap;
	}

	
	/**
	 * generates EXPERIMENT task map, including the stimulus frequencies, jitter, and the main frequencies together with its p values
	 * for i = 0 to numFrequences - 1
	 * @return
	 */
	public  static double[][] genExperimentTaskMap(){
		int numFrequencies = AdvancedOptionsWindow.numFrequencies;
		int numTrials = AdvancedOptionsWindow.numTrials;
		int numRuns = AdvancedOptionsWindow.numRuns;
		int numTrialsPerBlock = numTrials/numRuns;                        //how many trials are within each block
		int timesFreqRepeatedPerBlock = numTrialsPerBlock/numFrequencies; //number of times the sequence of sample frequencies repeated in block
		float jitter = AdvancedOptionsWindow.jitter;
		float minFrequency = AdvancedOptionsWindow.minFrequency;
		float maxFrequency = AdvancedOptionsWindow.maxFrequency;
		double taskMap[][] = new double[numTrials][5];
		
		int j=0;
		int maxFreqP = (int) (69+Math.round(12*Math.log(maxFrequency/440)/Math.log(2)));
		int minFreqP = (int) (69+Math.round(12*Math.log(minFrequency/440)/Math.log(2)));
		
		//generate randomly jittered frequencies
		for(int run =0;run<numRuns;run++){	
			for(int k = 0; k<timesFreqRepeatedPerBlock;k++){
				for(int  i = 1; i<=numFrequencies;i++){
					
					double tempP = minFreqP+Math.ceil((double)(i)*(maxFreqP-minFreqP)/(numFrequencies+1));
					double minValP =  tempP-jitter/2;
					double maxValP =  tempP+jitter/2;
					
					double genFreqP = minValP + (Math.random() * (maxValP - minValP));
				
					taskMap[j][0] = 440*Math.pow(2, (genFreqP-69)/12);
					taskMap[j][1] = genFreqP;
					taskMap[j][2] = 440*Math.pow(2, (tempP-69)/12);
					taskMap[j][3] = tempP;
					taskMap[j][4] = 0;
					//System.out.println(taskMap[j][0]+"  "+ taskMap[j][1] + "  "+ taskMap[j][2] +" "+taskMap[j][3]+" "+taskMap[j][4]);
					j++;
				}
			}
		}
			
		//taskMap = shuffle2DArrayRows(taskMap);	
		for(int run = 0;run < numRuns;run++){	
			double tempTaskMap[][] = new double[numTrialsPerBlock][5];
			for ( j = 0;j < numTrialsPerBlock;j++) {
				tempTaskMap[j][0] = taskMap[run * numTrialsPerBlock + j][0];
				tempTaskMap[j][1] = taskMap[run * numTrialsPerBlock + j][1];
				tempTaskMap[j][2] = taskMap[run * numTrialsPerBlock + j][2];
				tempTaskMap[j][3] = taskMap[run * numTrialsPerBlock + j][3];
				tempTaskMap[j][4] = taskMap[run * numTrialsPerBlock + j][4];
				//System.out.println(tempTaskMap[j][0]+"  "+ tempTaskMap[j][1] + "  "+ tempTaskMap[j][2] +" "+tempTaskMap[j][3]+" "+tempTaskMap[j][4]);
			}
			tempTaskMap = shuffle2DArrayRows(tempTaskMap);
			//System.out.println("AFTER SHUFFLING");
			
			for ( j = 0;j<numTrialsPerBlock;j++) {
				taskMap[run * numTrialsPerBlock + j][0] = tempTaskMap[j][0];
				taskMap[run * numTrialsPerBlock + j][1] = tempTaskMap[j][1];
				taskMap[run * numTrialsPerBlock + j][2] = tempTaskMap[j][2];
				taskMap[run * numTrialsPerBlock + j][3] = tempTaskMap[j][3];
				taskMap[run * numTrialsPerBlock + j][4] = tempTaskMap[j][4];
				//System.out.println(tempTaskMap[j][0]+"  "+ tempTaskMap[j][1] + "  "+ tempTaskMap[j][2] +" "+tempTaskMap[j][3]+" "+tempTaskMap[j][4]);
			}
		}
		
		
		j=0;
		for(int run =0;run<numRuns;run++){	
			for(int k = 0; k<timesFreqRepeatedPerBlock;k++){
				for(int  i = 1; i<=numFrequencies;i++){
					taskMap[j][4] = run;
					j++;
				}
			}
		}
		
		return taskMap;
	}
	
	
	/**
	 *  efficient Fisher�Yates shuffle array function
	 *  http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	 * @param array
	 */
	public static double[][] shuffle2DArrayRows(double[][] array)
	{
	    int index;
	    double temp[] = new double[array[0].length];
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        
	        for(int j=0;j<array[0].length;j++){
	        	temp[j] = array[index][j];
	        	array[index][j] = array[i][j];
	        	array[i][j] = temp[j];
	        }
	       
	    }
	    return array;
	}
	
	/**
	 * invoking the InstructionsPanel1 panel 
	 */
	public void runInstructionsPanel1(){
		panelMain.removeAll();
        InstructionsPanel1 panel = new InstructionsPanel1();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 1");
	}
	
	/**
	 * invoking the InstructionsPanel2 panel 
	 */
	public void runInstructionsPanel2(){
		panelMain.removeAll();
        InstructionsPanel2 panel = new InstructionsPanel2();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 2");
	}
	
	/**
	 * invoking the InstructionsPanel3 panel 
	 */
	public void runInstructionsPanel3(){
		panelMain.removeAll();
        InstructionsPanel3 panel = new InstructionsPanel3();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 3");
	}
	
	/**
	 * invoking the InstructionsPanel4 panel 
	 */
	public void runInstructionsPanel4(){
		panelMain.removeAll();
        InstructionsPanel4 panel = new InstructionsPanel4();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 4");
	}
	/**
	 * invoking the InstructionsPanel5 panel 
	 */
	public void runInstructionsPanel5(){
		panelMain.removeAll();
        InstructionsPanel5 panel = new InstructionsPanel5();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 5");
	}
	
	/**
	 * invoking the InstructionsPanel6 panel 
	 */
	public void runInstructionsPanel6(){
		panelMain.removeAll();
        InstructionsPanel6 panel = new InstructionsPanel6();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 6");
	}
	
	/**
	 * invoking the InstructionsPanel7 panel 
	 */
	public void runInstructionsPanel7(){
		panelMain.removeAll();
        InstructionsPanel7 panel = new InstructionsPanel7();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 7");
	}
	
	/**
	 * invoking the InstructionsPanel8 panel 
	 */
	public void runInstructionsPanel8(){
		panelMain.removeAll();
        InstructionsPanel8 panel = new InstructionsPanel8();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 8");
	}
	
	/**
	 * invoking the InstructionsPanel9 panel 
	 */
	public void runInstructionsPanel9(){
		panelMain.removeAll();
        InstructionsPanel9 panel = new InstructionsPanel9();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 9");
	}
	
	/**
	 * invoking the InstructionsPanel10 panel 
	 */
	public void runInstructionsPanel10(){
		panelMain.removeAll();
        InstructionsPanel10 panel = new InstructionsPanel10();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 10");
	}
	
	/**
	 * invoking the InstructionsPanel11 panel 
	 */
	public void runInstructionsPanel11(){
		panelMain.removeAll();
        InstructionsPanel11 panel = new InstructionsPanel11();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening screen 10");
	}
	
	/**
	 * invoking the prestimulus panel 
	 */
	public void runPreStimulusPanel(){
        panelMain.removeAll();
        PreStimulusPanel panel = new PreStimulusPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening pre-stimulus panel");

	}
	
	
	/**
	 * invoking the stimulus panel 
	 */
	public void runStimulusPanel(double currentFreq){
        //panelMain.removeAll();
		StimulusPanel panel = new StimulusPanel(currentFreq);
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening stimulus panel");

	}

	
	/**
	 * invoking the delay panel 
	 */
	public void runDelayPanel(){
        panelMain.removeAll();
		DelayPanel panel = new DelayPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening delay panel");

	}
	
	
	/**
	 *  invoking the probe panel 
	 * @throws AWTException 
	 */
	public void runPracticeProbePanel(){
        panelMain.removeAll();
		PracticeProbePanel panel = new PracticeProbePanel();
        panelMain.add(panel);
        add(panelMain);
	}
	
	/**
	 * invoking the intertrial panel 
	 */
	public void runIntertrialPanel(){
        panelMain.removeAll();
		IntertrialPanel panel = new IntertrialPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening intertrial panel");

	}
	
	/**
	 * invoking the intertrial info panel 
	 */
	public void runInterTrialInfoPanel(){
        panelMain.removeAll();
        InterTrialInfoPanel panel = new InterTrialInfoPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening intertrial info panel");

	}
	
	/**
	 * invoking the interPracticeBet panel 
	 */
	public void runInterPracticeBetPanel(){
        panelMain.removeAll();
        InterPracticeBetPanel panel = new InterPracticeBetPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening interblock panel");

	}
	
	/**
	 * invoking the endOfPractice panel 
	 */
	public void runEndOfPracticePanel(){
        panelMain.removeAll();
        EndOfPracticePanel panel = new EndOfPracticePanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening eoe panel");

	}
	
	/**
	 * invoking the endOfInstruction panel 
	 */
	public void runEndOfInstructionPanel(){
        panelMain.removeAll();
        EndOfInstructionPanel panel = new EndOfInstructionPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening eoi panel");

	}
	/**
	 * invoking the runPracticeTrialStartWarningPanel panel 
	 */
	public void runPracticeTrialStartWarningPanel(){
        panelMain.removeAll();
        PracticeTrialStartWarningPanel panel = new PracticeTrialStartWarningPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening pre trial warning panel");

	}

	/**
	 * invoking the StartOfExperiment panel 
	 */
	public void runStartOfExperimentPanel(){
		panelMain.removeAll();
		StartOfExperimentPanel panel = new StartOfExperimentPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening soe panel");
	}
	
	/**
	 * invoking the EndOfExperiment panel 
	 */
	public void runEndOfExperimentPanel(){
		panelMain.removeAll();
		EndOfExperimentPanel panel = new EndOfExperimentPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening eoe panel");
	}
	
	/**
	 * invoking the EndOfBlockPanel panel 
	 */
	public void runEndOfBlockPanel(){
		panelMain.removeAll();
		EndOfBlockPanel panel = new EndOfBlockPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening eob panel");
	}
	
	
	/**
	 * invoking the interblock panel 
	 */
	public void runInterBlockPanel(){
        panelMain.removeAll();
        InterBlockPanel panel = new InterBlockPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening interblock panel");

	}
	
	/**
	 *  invoking the probe panel 
	 * @throws AWTException 
	 */
	public void runProbePanel(){
        panelMain.removeAll();
		ProbePanel panel = new ProbePanel();
        panelMain.add(panel);
        add(panelMain);
	}
	
	
	/**
	 * invoking the scoreReport panel 
	 */
	public void runScoreReportPanel(){
        panelMain.removeAll();
        ScoreReportPanel panel = new ScoreReportPanel();
        panelMain.add(panel);
        add(panelMain);
        System.out.println("Opening score report panel");

	}
	
	 
	 /**
		 * main routine for running current block
		 * @param arguments
		 * @throws InterruptedException 
		 * @throws UnsupportedEncodingException 
		 * @throws FileNotFoundException 
		 * @throws AWTException 
		 */
		 public static void runBlock(final RunExperimentWindow frame) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException, AWTException {
			 Date date = new Date() ;
			 SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyHHmmss");//_HH_mm_ss") ;
			 String subNumStr = subNum;

			 
			 String topDirectoryName = resDirectory+"//"+AdvancedOptionsWindow.studyId;
			 File file = new File(topDirectoryName);
		        if (!file.exists()) {
		            if (file.mkdir()) {
		                System.out.println("Directory "+topDirectoryName+" is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        }
		        
		        String subDirectoryName = topDirectoryName+"//"+subNumStr;
				 File subFile = new File(subDirectoryName);
			        if (!subFile.exists()) {
			            if (subFile.mkdir()) {
			                System.out.println("Directory "+subDirectoryName+" is created!");
			            } else {
			                System.out.println("Failed to create directory "+ subDirectoryName);
			            }
			        }
			        
		        String expDirectoryName = subDirectoryName+"//Data";
				File expFile = new File(expDirectoryName);
			        if (!expFile.exists()) {
			            if (expFile.mkdir()) {
			                System.out.println("Directory "+expDirectoryName+" is created!");
			            } else {
			                System.out.println("Failed to create directory "+ expDirectoryName);
			            }
			        }     
			 String filename = expDirectoryName+"//"+studyId+studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_block"+(ExperimentSetupWindow.currentBlockRunning+1)+".txt";
			 System.out.println(filename);
			 writer = new PrintWriter(filename, "UTF-8");
			 writer.println("trial,bet,block,sampleFrequency,reportedFrequency,reactionTime,error, errorP, sampleFreqP,reportedFrequencyP,mainFrequency,mainFreqP,cummulativeScore,stimulusDuration,timeBtwTriggerAndStim");
		        
			 //class for pre-stimulus panel activity
	         final class PreStimulusPanelTask implements Runnable {
	             @Override
	             public void run() {
	             	System.out.println("Running pre stimulus panel task.");
	             	frame.getContentPane().removeAll();
	                 frame.runPreStimulusPanel();
	                 frame.revalidate();
	                 frame.repaint();
	             }
	         };
	         
	         
	       //class for delay panel activity
	         final class DelayPanelTask implements Runnable {
	             @Override
	             public void run() {
	                 System.out.println("Running delay panel task.");
	                 frame.getContentPane().removeAll();
	                 frame.runDelayPanel();
	                 frame.revalidate();
	                 frame.repaint();
	             }
	         };
	         
	         
	       //class for probe intertrial panel activity
	         final class IntertrialPanelTask implements Runnable {
	             @Override
	             public void run() {
	                 System.out.println("Running intertrial panel task.");
	                 frame.getContentPane().removeAll();
	                 frame.runIntertrialPanel();
	                 frame.revalidate();
	                 frame.repaint();
	             }
	         };
	         
	         
	      
	         
	         
	       //class for probe panel activity
	         final class ProbePanelTask implements Runnable {
	            @Override
	            public void run() {
	        			System.out.println("Running probe panel task.");
	            		frame.getContentPane().removeAll();
						frame.runProbePanel();
						frame.revalidate();
	                    frame.repaint();
	                    startTime = System.currentTimeMillis();
	            }
	            
	         };

	  
	         
	       //class for probe intertrial panel activity
	         final class ScoreReportPanelTask implements Runnable {
	             @Override
	             public void run() {
	                 System.out.println("Running score report panel task.");
	                 frame.getContentPane().removeAll();
	                 frame.runScoreReportPanel();
	                 frame.revalidate();
	                 frame.repaint();
	             }
	         };
	         
	         //define instances of all four classes
	         DelayPanelTask delayTaskInst = new DelayPanelTask();
	         PreStimulusPanelTask preStimulusPanelTaskInst = new PreStimulusPanelTask();
	         IntertrialPanelTask intertrialTaskInst = new IntertrialPanelTask();
	         ScoreReportPanelTask scoreReportPanelTaskInst = new ScoreReportPanelTask();

	         //////////////////////////////////////////////////////////////////////////////////////////////////////////
	         int numTrialsPerBlock = numTrials/numRuns; 
	         int [] idxArray = new int[numTrialsPerBlock];
	         int k = 0;
	         for( int i = 0; i < experimentTaskMap.length;i++){
	        	 if(experimentTaskMap[i][4] == ExperimentSetupWindow.currentBlockRunning){
	        		 //System.out.println(experimentTaskMap[i][4]+"      "+ ExperimentSetupWindow.currentBlockRunning);
	        		 idxArray[k] = i;
	        		 k++;
	        	 }	 
	         }

	         startBlockTime = System.currentTimeMillis();
	         timeElapsed = 0;
	         //iterate over trials in this block
	         for (int i : idxArray) {
	         	cummulativeScorePerTrial=0;
	         	currentExperimentTrial=i;
	         
	         	//ArrayList to keep reaction time
	         	final ArrayList<Integer> timeTempList = new ArrayList<Integer>();
	         	//ArrayList to keep frequencies
	         	freqTempList.clear();
	         	freqCoordTempList.clear();
	         	
	   
	         	//do not play the sound at the beginning of the trial
	         	System.out.println("Running trial number "+i);	
	         	currentFreq = experimentTaskMap[i][0];
	         	final double stimulusFreqP = experimentTaskMap[i][1];
	         	final double mainFrequency = experimentTaskMap[i][2];
	         	final double mainFreqP = experimentTaskMap[i][3];
	         	
	         	//redefine a new instance of executor for each new trial
	         	final ExecutorService service = Executors.newSingleThreadExecutor();//.newCachedThreadPool();
	         	
	         	
	         	
	         	
	         	//invoke the intertrial panel routines
	         	//---------------------------------------------------------------------------------------------------//
	         	frame.setFocusable(false);
	         	ProbePanel.stopThread();
	            service.submit(intertrialTaskInst);
	            frame.setVisible(true);
	             
	             //hide mouse cursor
	     		Toolkit toolkit = Toolkit.getDefaultToolkit();
	     	    Point hotSpot = new Point(0,0);
	     	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	     	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	     	    frame.setCursor(invisibleCursor);
	     	    //
	     	    
	             service.awaitTermination(AdvancedOptionsWindow.intertrialDuration, TimeUnit.MILLISECONDS);// 3 sec
	             
	             //invoke the pre-stimulus panel routines
	             //---------------------------------------------------------------------------------------------------//
	             
	             service.submit(preStimulusPanelTaskInst);
	             frame.setVisible(true);
	             service.awaitTermination(AdvancedOptionsWindow.preStimulusDuration, TimeUnit.MILLISECONDS);// 3 sec
	             
	             
	             //invoke the stimulus panel routines
	             //---------------------------------------------------------------------------------------------------//

	             frame.getContentPane().removeAll();
	             stimStartTime = System.currentTimeMillis();
	             startTimeBtwStimAndTrigger  = System.currentTimeMillis();
	             Thread thread1 = new Thread () {
	            	  public void run () {
		                  System.out.println("Running stimulus panel task.");
		                  frame.getContentPane().add(new TriggerStimulusCanvas());
		                  frame.repaint();
		                  frame.revalidate();
	            	  }
	            	};
	            	
	            	Thread thread2 = new Thread () {
	            		public void run () {
	            			endTimeBtwStimAndTrigger = System.currentTimeMillis() - startTimeBtwStimAndTrigger;
	            			frame.runStimulusPanel(currentFreq);
	            		}
	            	};
	            	
	            	thread1.start();
	         	thread2.start();
	            	while(System.currentTimeMillis()-stimStartTime < AdvancedOptionsWindow.stimulusDuration){
	            		continue;
	            	}
	            	
	            	thread1.stop();
	            	thread2.stop();
	            	
	            	stimEndTime =  System.currentTimeMillis() - stimStartTime;
	            	
	         	System.out.println("Time btw stimulus and trigger ---------------------------------------------------   "+endTimeBtwStimAndTrigger+" ms");
	         	System.out.println("Total stimulus presentation time ---------------------------------------------------   "+stimEndTime+" ms");
	
	             //invoke the delay panel routines
	             //---------------------------------------------------------------------------------------------------//
	             service.submit(delayTaskInst);
	             frame.setVisible(true);
	             service.awaitTermination(AdvancedOptionsWindow.delayDuration, TimeUnit.MILLISECONDS);//500
	             //---------------------------------------------------------------------------------------------------//
	             
	             
	             
	             if(Parameters.DISPLAY_CURSOR_ON_PROBE){
	             	frame.setCursor(Cursor.getDefaultCursor());
	             }
	             
	             
	             //first move a cursor to randomly generated position
	             Random random = new Random();
	             mouseMovementRectWidth = frame.getWidth()-2*ProbePanel.xPixToTrim;
	             mouseMovementRectHeight = frame.getHeight()-2*ProbePanel.yPixToTrim;
	             
	             int xCoord =  random.nextInt(mouseMovementRectWidth-1)+ProbePanel.xPixToTrim;
	             int yCoord =  random.nextInt(mouseMovementRectHeight-1)+ProbePanel.yPixToTrim;

	             double heightAtRealTone = 0;
	             
	             //iterate over all bets
	             for(int bet = 0;bet<numberOfBetsPerTrial;bet++) {
	            	 
	            	 	final long probeStartTime = System.currentTimeMillis();
	                 
	             	numBetsCompleted+=1;
	             	currentBet = bet;
	             	
	             	ActionListener animate = new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			            	frame.repaint();
			            	ProbePanel.showTrigger = false;
			            }
			        };
			        Timer timer = new Timer(Parameters.TRIGGER_ON_CLICK_DURATION,animate);
			       
	             	final ExecutorService betsPoolService = Executors.newSingleThreadExecutor();//.newCachedThreadPool();
	             	ProbePanelTask probeTaskInst = new ProbePanelTask();
	             	//============================================================================================================//
	                //invoke the probe panel routines
	                //---------------------------------------------------------------------------------------------------//
	             	
	             	betsPoolService.submit(probeTaskInst);
	             	
	                 frame.setVisible(true);
	                 // Move the cursor
	                 //for the first bet place the cursor randomly, starting from the second bet the cursor X coord coincides with the last bet's X
	                 if(bet == 0)
	                 	ProbePanel.X0=xCoord;
	                 else{
	                 	ProbePanel.X0=(int) allReportedCursorPositions[bet-1];
	                 	}

	                 ProbePanel.Y0=yCoord;  
	                 ProbePanel.pX = ProbePanel.X0;
	                 ProbePanel.pY = ProbePanel.Y0;
	                 
	                 Robot robot = new Robot();
	                 robot.mouseMove(ProbePanel.X0, ProbePanel.Y0);
	                
	                 frame.revalidate();
	             	 frame.repaint();
	             	 frame.setFocusable(true);
	             	 frame.requestFocusInWindow();
	             	
	             	 isKeyPressed = false;
	             	 //Monitor mouse motion
	             	 ProbePanel.lblAdjustPitch.addMouseMotionListener(new MouseAdapter() {
	             		public void mouseMoved(MouseEvent me) {
	                    	Robot robot;
	                        // Get x,y and store them
	                    	ProbePanel.pX = me.getX();
	                    	ProbePanel.pY = me.getY();
	                        //if mouse moves from the vicinity of its initial position (X0,Y0) -- sounds starts playing
	                        //it is more then enough to check horizontal vicinity only
	                        if((ProbePanel.pX>Math.abs(ProbePanel.X0+1) || ProbePanel.pX<Math.abs(ProbePanel.X0-1))){
	                        	ProbePanel.mouseIsMoving = true;
	                        }
	                        
	                        double lowerMarginX = ProbePanel.xPixToTrim-1;
	                        double lowerMarginY = ProbePanel.yPixToTrim-1;
	                        
	                        if(RunExperimentWindow.currentBet != 0){
	                        	if(ProbePanel.firstBetMu<(RunExperimentWindow.mouseMovementRectWidth)/2+ProbePanel.xPixToTrim-1){
	                        		lowerMarginX = ProbePanel.xPixToTrim-1;
	                        		ProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth/2+ProbePanel.firstBetMu - (ProbePanel.xPixToTrim-1);
	                        		ProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(ProbePanel.firstBetMu>(RunExperimentWindow.mouseMovementRectWidth)/2+ProbePanel.xPixToTrim-1){
	                        		lowerMarginX = ProbePanel.firstBetMu - RunExperimentWindow.mouseMovementRectWidth/2;
	                        		ProbePanel.cursorAllowedIntervalX=3*RunExperimentWindow.mouseMovementRectWidth/2-ProbePanel.firstBetMu + (ProbePanel.xPixToTrim-1);
	                        		ProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(ProbePanel.firstBetMu==(RunExperimentWindow.mouseMovementRectWidth)/2+ProbePanel.xPixToTrim-1){
	                        		lowerMarginX = ProbePanel.xPixToTrim-1;
	                        		ProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
	                        		ProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	
	               		 	 }
	        	       		 else{
	        	       			lowerMarginX = ProbePanel.xPixToTrim-1;
	        	       			ProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
	        	       			ProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	        	       		 }
	                        
	                        
	                        
                        //this is to prevent the cursor from leaving a rectangular area
                        if(	(ProbePanel.pX<= lowerMarginX)||(ProbePanel.pX>=(lowerMarginX+ProbePanel.cursorAllowedIntervalX))||
	                        	(ProbePanel.pY<=lowerMarginY)||(ProbePanel.pY>=(lowerMarginY+ProbePanel.cursorAllowedIntervalY))){
                        	if ((ProbePanel.pX<=lowerMarginX)||(ProbePanel.pX>=(lowerMarginX+ProbePanel.cursorAllowedIntervalX))){
	                        		ProbePanel.pX=ProbePanel.prevX;
	                        		// Move the cursor
	            					try {
	            						robot = new Robot();
	            						robot.mouseMove(ProbePanel.pX, ProbePanel.pY);
	            					} catch (AWTException e) {
	            						// TODO Auto-generated catch block
	            						e.printStackTrace();
	            					}
	                        	}
                        	if((ProbePanel.pY<=lowerMarginY)||(ProbePanel.pY>=(lowerMarginY+ProbePanel.cursorAllowedIntervalY))){
	                        			ProbePanel.pY=ProbePanel.prevY;
		                        		try {
		            						robot = new Robot();
		            						robot.mouseMove(ProbePanel.pX, ProbePanel.pY);
		            					} catch (AWTException e) {
		            						// TODO Auto-generated catch block
		            						e.printStackTrace();
		            					}
	                        		}
	                        }
	                        //save the previous coordinates
	                        ProbePanel.prevX=ProbePanel.pX;
	                        ProbePanel.prevY=ProbePanel.pY;
	                        if(!ProbePanel.showTrigger){
	                        		//frame.revalidate();
	        						frame.repaint();
	        					}
	                    }
	                });
	             	ProbePanel.lblAdjustPitch.addMouseListener(new MouseAdapter() {
	         			@Override
	         			public void mouseClicked(MouseEvent e) {

	         			}

	         			@Override
	         			public void mouseEntered(MouseEvent e) {
	         				
	         				
	         			}

	         			@Override
	         			public void mouseExited(MouseEvent e) {
	         				
	         				
	         			}

	         			@Override
	         			public void mousePressed(MouseEvent e) {
	         				
	         				
	         			}

	         			@Override
	         			public void mouseReleased(MouseEvent e) {
	 
	         				isKeyPressed = true;
		        				ProbePanel.showTrigger = true;
		        				
		        				long timeToRespond= System.currentTimeMillis()-probeStartTime;
		 	            		timeTempList.add((int) timeToRespond);
		 	            		freqTempList.add((double) ProbePanel.fFreq);
		 	            		scoreTempList.add((double) ProbePanel.heightAtRealTone);
		 	            		freqCoordTempList.add(ProbePanel.fFreqCursorX);
		 	   
		 	            		ProbePanel.stopThread();
			            		timer.setRepeats(false);
		        				timer.start();
			            		betsPoolService.shutdownNow();
			            		
	         			}
                 	});
	             	
	                 frame.addKeyListener(new KeyListener() {
	       		            @Override
	       		            public void keyTyped(KeyEvent e) {
	       		            	
	       		            }

	       		            @Override
	       		            public void keyReleased(KeyEvent e) {
	       		           
	       		            }

	       		            @Override
	       		            public void keyPressed(KeyEvent e) {
	       		            	isKeyPressed = true;
	     	            		frame.revalidate();
	     	            		frame.repaint();
	     	            		//if the 'q' key is pressed -- exit
	     	            		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	     	            			isProgramEnded = true;
	     	            			writer.close();
	     	            			betsPoolService.shutdownNow();
	     	            			timeElapsed =  System.currentTimeMillis() - startBlockTime;
	     	            			recordToExperimentLogFileOnInterruption();
	     	            			frame.dispose();
	     	            			betsPoolService.shutdownNow();
	     	            			ProbePanel.stopThread();
	     	            			stopAllThreads();
	     	            			
	     	            			}

	       		            }
	       		        });
	                 
	                 //check whether the probe task is timed
	                 if(AdvancedOptionsWindow.isProbeTaskTimed){
	                 	betsPoolService.awaitTermination(AdvancedOptionsWindow.probeDuration, TimeUnit.MILLISECONDS);
	                 	betsPoolService.shutdownNow();
	                    frame.requestFocusInWindow();
	                 }
	                 else
	                 {
	                 	long startTime = System.currentTimeMillis();
	     				//if nothing is pressed for some time -- wait
	                 	betsPoolService.awaitTermination(Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED, TimeUnit.MILLISECONDS);
	     				long estimatedTime = System.currentTimeMillis() - startTime;
	     				if(estimatedTime > Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED){
	     					isProgramEnded = true;
	     					ProbePanel.showTrigger = true;
	     					timer.setRepeats(false);
	     					timer.start();
	             			writer.close();
	             			betsPoolService.shutdownNow();
	             			
	             			String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
	        		    			 + "The program is terminated.</center></html>"
	        		    			 ,(int)(estimatedTime/(60000))
	        		    			);
	             			JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
	             			
	             			frame.dispose();
	             			stopAllThreads();
	     				}
	                 }
	                 
	                 
	                 System.out.println("trial = "+i+ " bet = "+bet);
	                 if(!isProgramEnded){
	     	            if(isKeyPressed){
		     	            	double reportedFrequency = freqTempList.get(freqTempList.size()-1-bet);
		     	            	allReportedFreq[bet]=reportedFrequency;
		     	            	allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-bet);
		     	            	allReportedScoresPerBet[bet]=scoreTempList.get(scoreTempList.size()-1-bet);
		     	            	
		     	            	double reportedFrequencyP=69+12*Math.log(reportedFrequency/440)/Math.log(2);  
		     	            	
		     	            	String reportedFrequencyStr = String.valueOf(reportedFrequency);
		     	            	String reportedErrorStr = String.valueOf(currentFreq-freqTempList.get(freqTempList.size()-1-bet));
		     	            	String reportedFrequencyPStr = String.valueOf(reportedFrequencyP);
			    	            
		    	            	
		     	            	double errorP=12*Math.log(currentFreq/reportedFrequency)/Math.log(2);  
		    	            	
		     	            	String reportedErrorStrP = String.valueOf(errorP);
		    	           
			    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////   	
			    	            //
			    	            	//
			    	            	//			RECALCULATE ALL THE DISTRIBUTIONS
			    	            	//
			    	            	//
			    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	            	
			    	            	 Dimension panelSize = frame.getSize();
			    	       		 
			    	       		 int h = (int) panelSize.getHeight();
			    	       		 int w = (int) panelSize.getWidth();
			    	  
			    	       		 double muAtZero=w/2;
		 
			    	            	 double 	firstBetMu = allReportedCursorPositions[0];//getCursoreHorizFromFreq(RunExperimentWindow.allReportedFreq[0]);
			    	       		 
			    	       		 double reportedFreqPixels  = 0;
			    	       		 double reportedMuArray[] ;
			    	       		 double reportedDiffArray[];
			    	       		 
			    	       		 reportedMuArray = new double[RunExperimentWindow.currentBet+1];
			    	       		 reportedDiffArray = new double[RunExperimentWindow.currentBet+1];
			    	       		 
			    	       		 for(int b =0;b<=RunExperimentWindow.currentBet;b++){
			    	       			 reportedFreqPixels = allReportedCursorPositions[b];
			    	       			 reportedMuArray[b] =  muAtZero - firstBetMu+reportedFreqPixels;
			    	       			 reportedDiffArray[b] = Math.abs(w/2 -(reportedMuArray[b])); 
			    	       		 }
			    	       		 
			    	       		 double currentMu = muAtZero-firstBetMu+ProbePanel.pX;	
			    	       		 double diff = Math.abs(w/2 -(currentMu));
			    	       		 double mixSum =0;
			    	 
			    	       		 double realToneMu = muAtZero - firstBetMu+getCursoreHorizFromFreq(RunExperimentWindow.currentFreq, panelSize);
			    	       		 
			    	       		 //start to add distributions
			    	       		 for (int x = (int) (muAtZero-w/2); x <(int)(muAtZero+w/2+1); x++) {
			    	       			 if(allReportedFreq[0]!=0 && trainingBetsSection==false)
			    	       				 mixSum=2*1000*gaussianPdf((x), w/2, sigma);
			    	       			 else
			    	       				 mixSum=0;
			    	       			
			    	       			 for(int b=1;b<=RunExperimentWindow.currentBet;b++){//changed from bet=1
			    	       				 //System.out.println("BET # "+ bet + " freq = "+reportedMuArray[bet]);
			    	       				 if(RunExperimentWindow.allReportedFreq[b]!=0)
			    	       					 mixSum += 1000 * gaussianPdf((x), reportedMuArray[b], sigma)/(1+Math.log10(mixSum+1));
			    	       				 else
			    	       					 mixSum+=0;
			    	       			 }
			    	       		
			    	       			 
			    	       			 if(x == (int)Math.round(realToneMu)){
			    	       					 heightAtRealTone = mixSum;
			    	       					
			    	       			}
			    	       			 //tmOut.printf("%-15d%-22.2f%n",x,mixSum);
			    	       		 }//end of iterating over all horizontal pixels
			    	       		 
			    	       		 
			    	       		allReportedScoresPerBet[bet] = heightAtRealTone;
			    	       		
			    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			   
		     	            	if(reportedFrequency == 0 ){
		     	            		scorePerBet = 0;
		     	            		reportedFrequencyStr = "NaN";
		     	            		reportedErrorStr = "NaN";
		     	            		reportedErrorStrP = "NaN";
		     	            		reportedFrequencyPStr ="NaN";
		     	            	}
		     	            	else{
		     	            		double pBinSize = Math.abs(reportedFrequencyP-stimulusFreqP);
		     	            		//scorePerBet=(AdvancedOptionsWindow.maxPointsPerBet/Math.exp(pBinSize));
		     	            		scorePerBet=Math.round(Math.pow(allReportedScoresPerBet[bet]/ProbePanel.maxScore,.93) * AdvancedOptionsWindow.maxPointsPerBet );
	
		     	            	}
		     	            	System.out.println("For bet # "+currentBet +" you have earned "+scorePerBet+" points.");
		     	            	cummulativeScorePerTrial =cummulativeScorePerTrial+scorePerBet;
		     	            	
	
		     	            	writer.println((i+1)+","+(bet+1)+","+(experimentTaskMap[i][4]+1)+","+currentFreq+","+ reportedFrequencyStr
		    	            			+","+timeTempList.get(timeTempList.size()-1)+","+reportedErrorStr+","+reportedErrorStrP
		    	            			+","+stimulusFreqP+ ","+reportedFrequencyPStr+","+mainFrequency+","+mainFreqP+","+scorePerBet+","+stimEndTime+","+endTimeBtwStimAndTrigger);
		    	    	        
		     	            }
		     	            else{
			     	            	System.out.println("Key is pressed  "+isKeyPressed);
			     	            	System.out.println("real frequency is  "+0);
			     	            	System.out.println("real reaction time "+0);
			     	            	writer.println((i+1)+","+(bet+1)+","+(experimentTaskMap[i][4]+1)+","+currentFreq+",NaN,NaN,NaN,NaN"+","
			     	            					+stimulusFreqP+ ",NaN,"+mainFrequency+","+mainFreqP+","+"0"+","+stimEndTime+","+endTimeBtwStimAndTrigger);
			     	            	cummulativeScorePerTrial =cummulativeScorePerTrial+0;
			     	            	allReportedFreq[bet]=0;
			     	            	if(freqCoordTempList.size() == 0)
			     	            		allReportedCursorPositions[bet]=xCoord;
			     	            	else {
			     	            		try {
			     	            			System.out.println("ArrayList Size "+ freqCoordTempList.size() +" index "+(freqCoordTempList.size()-1-bet));
				     	            		allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-bet);
				     	            		} catch (ArrayIndexOutOfBoundsException e) {
				     	            			System.out.println("\n\nEXCEPTION THROWN\n\nArrayList Size "+ freqCoordTempList.size() +" index "+(freqCoordTempList.size()-1-bet));
					     	            		allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1);
				     	            		} 
			     	            		}
			     	            	ProbePanel.stopThread();
		     	            }
		                 }
	   
	             }//end of iterating over all bets
	             
	             //============================================================================================================//
	             ActionListener showTriggerBeforeIntertrialInfoScreen = new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			            	frame.repaint();
			            	ScoreReportPanel.showTrigger = false;
			            }
			        };
			    
			    Timer intertrialInfoTriggerTimer = new Timer(Parameters.TRIGGER_ON_CLICK_DURATION,showTriggerBeforeIntertrialInfoScreen);
	            
			   
	             ScoreReportPanel.invoke=true;
	             ScoreReportPanel.cummulativeScorePerTrial=cummulativeScorePerTrial;
	             System.out.println("cummulativeScorePerTrial    "+ cummulativeScorePerTrial);

	 			
	             ScoreReportPanel.showTrigger = true;
	             intertrialInfoTriggerTimer.setRepeats(false);
	             
	             service.submit(scoreReportPanelTaskInst);
	             intertrialInfoTriggerTimer.start();
          		 
                 service.awaitTermination(Parameters.SCORE_REPORT_TASK_DURATION, TimeUnit.MILLISECONDS);
                 
	             
	             totalScore+=cummulativeScorePerTrial;
	             totalNumTrialsCompleted+=1;
	            
	         }//end of iterating over all trials

	         writer.close();
	         //////////////////////////////////////////////////////////////////////////////////////////////////////////  
	         
		 }//end of runBlock

	 /**
	  * 
	  * @param frame
	  * @throws InterruptedException
	  * @throws FileNotFoundException
	  * @throws UnsupportedEncodingException
	  * @throws AWTException
	  */
	 public static void runPracticeTrials(final RunExperimentWindow frame) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException, AWTException {

		 int xCoord = 0;
		 int yCoord = 0;
		 Robot robot = new Robot();
		 Random random = new Random();
		 
		 Date date = new Date() ;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyHHmmss");//_HH_mm_ss") ;
		 String subNumStr = subNum;

		 
		 String topDirectoryName = resDirectory+"//"+AdvancedOptionsWindow.studyId;
		 File file = new File(topDirectoryName);
	        if (!file.exists()) {
	            if (file.mkdir()) {
	                System.out.println("Directory "+topDirectoryName+" is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
	        
	        String subDirectoryName = topDirectoryName+"//"+subNumStr;
			 File subFile = new File(subDirectoryName);
		        if (!subFile.exists()) {
		            if (subFile.mkdir()) {
		                System.out.println("Directory "+subDirectoryName+" is created!");
		            } else {
		                System.out.println("Failed to create directory "+ subDirectoryName);
		            }
		        }
	        String practiceDirectoryName = subDirectoryName+"//Practice";
			 File practFile = new File(practiceDirectoryName);
		        if (!practFile.exists()) {
		            if (practFile.mkdir()) {
		                System.out.println("Directory "+practiceDirectoryName+" is created!");
		            } else {
		                System.out.println("Failed to create directory "+ practiceDirectoryName);
		            }
		        }
		 String filename = practiceDirectoryName+"//p_"+studyId+studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+".txt";
		 //System.out.println(filename);
		 writer = new PrintWriter(filename, "UTF-8");
	     writer.println("trial,bet,sampleFrequency,reportedFrequency,reactionTime,error, errorP, sampleFreqP,reportedFrequencyP,mainFrequency,mainFreqP,cummulativeScore,stimulusDuration,timeBtwTriggerAndStim");
        
         
         //class for probe panel activity
         final class PracticeProbePanelTask implements Runnable {
            @Override
            public void run() {
        			System.out.println("Running practice probe panel task.");
            		frame.getContentPane().removeAll();
					frame.runPracticeProbePanel();
					frame.revalidate();
                    frame.repaint();
                    startTime = System.currentTimeMillis();
            }
         };
         
        //class for pre-stimulus panel activity
         final class PreStimulusPanelTask implements Runnable {
             @Override
             public void run() {
             	System.out.println("Running stimulus panel task.");
             	frame.getContentPane().removeAll();
                 frame.runPreStimulusPanel();
                 frame.revalidate();
                 frame.repaint();
             }
         };
 
        
      //class for instructions panel 10 activity
        final class InstructionsPanel10Task implements Runnable {
            @Override
            public void run() {
            	System.out.println("Running instructions panel  10 task.");
            	frame.getContentPane().removeAll();
                frame.runInstructionsPanel10();
                frame.revalidate();
                frame.repaint();
            }
        };
        
      //class for instructions panel 11 activity
        final class InstructionsPanel11Task implements Runnable {
            @Override
            public void run() {
            	System.out.println("Running instructions panel  10 task.");
            	frame.getContentPane().removeAll();
                frame.runInstructionsPanel11();
                frame.revalidate();
                frame.repaint();
            }
        };
        
        //class for probe intertrial panel activity
        final class IntertrialPanelTask implements Runnable {
            @Override
            public void run() {
                System.out.println("Running intertrial panel task.");
                frame.getContentPane().removeAll();
                frame.runIntertrialPanel();
                frame.revalidate();
                frame.repaint();
            }
        };
        
        //class for delay panel activity
        final class DelayPanelTask implements Runnable {
            @Override
            public void run() {
                System.out.println("Running delay panel task.");
                frame.getContentPane().removeAll();
                frame.runDelayPanel();
                frame.revalidate();
                frame.repaint();
            }
        };
        
      //class for end of block panel activity
        final class InterTrialInfoPanelTask implements Runnable {
            @Override
            public void run() {
                System.out.println("Running interblock panel task.");
                InterTrialInfoPanel.cummulativeScorePerTrial = Math.ceil(cummulativeScorePerTrial);
                InterTrialInfoPanel.currentTrial =currentPracticeTrial;
                frame.getContentPane().removeAll();
                frame.runInterTrialInfoPanel();
                frame.revalidate();
                frame.repaint();
            }
        };
        
      
        
      //class for probe intertrial panel activity
        final class PracticeTrialStartWarningPanelTask implements Runnable {
            @Override
            public void run() {
                System.out.println("Running score report panel task.");
                frame.getContentPane().removeAll();
                frame.runPracticeTrialStartWarningPanel();
                frame.revalidate();
                frame.repaint();
            }
        };
      
        //define instances of all four classes
        DelayPanelTask delayTaskInst = new DelayPanelTask();
        PreStimulusPanelTask preStimulusPanelTaskInst = new PreStimulusPanelTask();
        IntertrialPanelTask intertrialTaskInst = new IntertrialPanelTask();
        InterTrialInfoPanelTask interTrialInfoPanelTaskInst = new InterTrialInfoPanelTask();
        
        
       
        InstructionsPanel11Task instructionsPanel11TaskInst = new InstructionsPanel11Task();
        PracticeTrialStartWarningPanelTask practiceTrialStartWarningPanelTaskInst = new PracticeTrialStartWarningPanelTask();
       
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //hide mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	    frame.setCursor(invisibleCursor);
	    //////////////////////////////////////////////////////////////////////////////////////////////////////////
      
		cummulativeScorePerTrial=0;
    		currentPracticeTrial=0;
    		long startTrialTime = startTime;
		long estimatedTime;
		long timeToTerminate;
    	
	    
    		//ArrayList to keep reaction time
        final ArrayList<Integer> timeTempList = new ArrayList<Integer>();

    		double stimulusFreqP ;
    		double mainFrequency;
    		double mainFreqP ;
	   
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		///// Instructions panel 11
		////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    		changePanelAppearance(frame, instructionsPanel11TaskInst, false);
        
        
        
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        // THIRD TRIAL (3 "REAL-LIFE" PRACTICE TRIALS)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        trainingBetsSection = false;
        
		currentPracticeTrial = totalNumTrialsCompleted;
		
		int numOfTrialsCompletedBeforeTask3=totalNumTrialsCompleted;
		
        

        //iterate over all trials
       
        
        for (int i = numOfTrialsCompletedBeforeTask3; i <numOfTrialsCompletedBeforeTask3+ (numOfDemoTrialsTimed + numOfDemoTrialsNotTimed); i++) {
        	
        	//for half of the practice trials make the probe task untimed and for the other half -- timed
        if(AdvancedOptionsWindow.isProbeTaskTimed){
            	if(i<=numOfDemoTrialsNotTimed){
                	isProbeTaskTimed = false;
            		timedPracticeTrialsAreRunning  = false;
            	}
                else{
                	isProbeTaskTimed = true;
                	timedPracticeTrialsAreRunning = true;
                }
            }
            else{
            		isProbeTaskTimed = false;
            		timedPracticeTrialsAreRunning  = false;
            }
            
            
	        	cummulativeScorePerTrial=0;
	        
	        	//ArrayList to keep reaction time
	        	final ArrayList<Integer> timeTempList1 = new ArrayList<Integer>();
	        //ArrayList to keep frequencies
	        freqTempList.clear();
	        freqCoordTempList.clear();
	        scoreTempList.clear();
	        	
	        	//System.out.println("Mouse moved first time at trial#  "+i+"   "+" current Trial is "+currentPracticeTrial);
	        	//do not play the sound at the beginning of the trial
	        	System.out.println("Running trial number "+(i));	
	        	currentFreq = practiceTaskMap[i][0];
		    	stimulusFreqP = practiceTaskMap[i][1];
		    	mainFrequency = practiceTaskMap[i][2];
		    	mainFreqP = practiceTaskMap[i][3];
	        	
	        	//redefine a new instance of executor for each new trial
	        	final ExecutorService service = Executors.newFixedThreadPool(1);//.newCachedThreadPool();
	        	
	        	
	        	if (i!=0 && PracticeTrialStartWarningPanel.invoke){
	        		//invoke the Score Report Panel routines
	                //---------------------------------------------------------------------------------------------------//
	                service.submit(practiceTrialStartWarningPanelTaskInst);
	                frame.setVisible(true);
	                service.awaitTermination(Parameters.SCORE_REPORT_TASK_DURATION, TimeUnit.MILLISECONDS);
	        	}
	        	
	        	
	        	//invoke the intertrial panel routines
	        	//---------------------------------------------------------------------------------------------------//
	        	frame.setFocusable(false);
	        	PracticeProbePanel.stopThread();
	        service.submit(intertrialTaskInst);
	        frame.setVisible(true);
	        
            
    	    
            service.awaitTermination(AdvancedOptionsWindow.intertrialDuration, TimeUnit.MILLISECONDS);// 3 sec
            
            //invoke the pre-stimulus panel routines
            //---------------------------------------------------------------------------------------------------//
            
            service.submit(preStimulusPanelTaskInst);
            frame.setVisible(true);
            service.awaitTermination(AdvancedOptionsWindow.preStimulusDuration, TimeUnit.MILLISECONDS);// 3 sec
            
            

            
            //invoke the stimulus panel routines
            //---------------------------------------------------------------------------------------------------//
            frame.getContentPane().removeAll();
            stimStartTime = System.currentTimeMillis();
            startTimeBtwStimAndTrigger  = System.currentTimeMillis();
            Thread thread1 = new Thread () {
           	  public void run () {
	                  System.out.println("Running stimulus panel task.");
	                  frame.getContentPane().add(new TriggerStimulusCanvas());
	                  frame.repaint();
	                  frame.revalidate();
           	  }
           	};
	       	Thread thread2 = new Thread () {
	       		public void run () {
	       			endTimeBtwStimAndTrigger = System.currentTimeMillis() - startTimeBtwStimAndTrigger;
	       			frame.runStimulusPanel(currentFreq);
	       	  }
	       	};
	       	
	       	thread1.start();
	        thread2.start();
	       	while(System.currentTimeMillis()-stimStartTime < AdvancedOptionsWindow.stimulusDuration){
	       		continue;
	       	}
	       	
	       	thread1.stop();
	       	thread2.stop();
	       	
	       	stimEndTime =  System.currentTimeMillis() - stimStartTime;
       	
	       	System.out.println("Time btw stimulus and trigger ---------------------------------------------------   "+endTimeBtwStimAndTrigger+" ms");
	       	System.out.println("Total stimulus presentation time ---------------------------------------------------   "+stimEndTime+" ms");
             
            frame.setVisible(true);
            //for the first two trials make longer stimulus and response without any bets
            timeToTerminate = 0;
            
            
            frame.setVisible(true);
            //for the first two trials make longer stimulus and response without any bets
            timeToTerminate = 0;
      
         	AdvancedOptionsWindow.stimulusDuration = Parameters.STIMULUS_TASK_DURATION;
         	timeToTerminate = Parameters.STIMULUS_TASK_DURATION;
         	service.awaitTermination(timeToTerminate, TimeUnit.MILLISECONDS);//250
         	
            //invoke the delay panel routines
            //---------------------------------------------------------------------------------------------------//
            service.submit(delayTaskInst);
            frame.setVisible(true);
            service.awaitTermination(AdvancedOptionsWindow.delayDuration, TimeUnit.MILLISECONDS);//500
            
            
            
            //---------------------------------------------------------------------------------------------------//
            
            
            //first move a cursor to randomly generated position
            
            mouseMovementRectWidth = frame.getWidth()-2*PracticeProbePanel.xPixToTrim;
            mouseMovementRectHeight = frame.getHeight()-2*PracticeProbePanel.yPixToTrim;
            
            xCoord =  random.nextInt(mouseMovementRectWidth-1)+PracticeProbePanel.xPixToTrim;
            yCoord =  random.nextInt(mouseMovementRectHeight-1)+PracticeProbePanel.yPixToTrim;
            
            
           
        		double heightAtRealTone = 0;
           
            //iterate over all bets
            for(int bet = 0;bet<numberOfBetsPerTrial;bet++) {
            		
            		final long tempStartTime = System.currentTimeMillis();
          
            		
	            	currentBet = bet;
	            	
	            	ActionListener animate = new ActionListener() {
			            public void actionPerformed(ActionEvent e) {
			            	frame.repaint();
			            	PracticeProbePanel.showTrigger = false;
			            }
			        };
			       Timer timer = new Timer(Parameters.TRIGGER_ON_CLICK_DURATION,animate);
	            	
	            	
	            	final ExecutorService betsPoolService = Executors.newSingleThreadExecutor();//.newCachedThreadPool();
	            	PracticeProbePanelTask practiceProbeTaskInst3 = new PracticeProbePanelTask();
	            	//============================================================================================================//
                //invoke the probe panel routines
                //---------------------------------------------------------------------------------------------------//
	            	
	            	betsPoolService.submit(practiceProbeTaskInst3);
	        	
	            frame.setVisible(true);
	            // Move the cursor
	            //for the first bet place the cursor randomly, starting from the second bet the cursor X coord coincides with the last bet's X
	            if(bet == 0)
	            		PracticeProbePanel.X0=xCoord;
	            else{
	            		PracticeProbePanel.X0=(int) allReportedCursorPositions[bet-1];
	            	
	            }
	
	            PracticeProbePanel.Y0=yCoord;  
	            PracticeProbePanel.pX = PracticeProbePanel.X0;
	            PracticeProbePanel.pY = PracticeProbePanel.Y0;
	            
	             
	            robot.mouseMove(PracticeProbePanel.X0, PracticeProbePanel.Y0);
	           
	            frame.revalidate();
	            	frame.repaint();
	            	frame.setFocusable(true);
	            	frame.requestFocusInWindow();
	            	
	            	isKeyPressed = false;
	            	
	            	
	            	//Monitor mouse motion
                PracticeProbePanel.lblAdjustPitch.addMouseMotionListener(new MouseAdapter() {
                    public void mouseMoved(MouseEvent me) {
                    		Robot robot;
                        // Get x,y and store them
                    		PracticeProbePanel.pX = me.getX();
                    		PracticeProbePanel.pY = me.getY();
                        //if mouse moves from the vicinity of its initial position (X0,Y0) -- sounds starts playing
                        //it is more then enough to check horizontal vicinity only
                        if((PracticeProbePanel.pX>Math.abs(PracticeProbePanel.X0+1) || PracticeProbePanel.pX<Math.abs(PracticeProbePanel.X0-1))){
                        		PracticeProbePanel.mouseIsMoving = true;
                        }
                        
                        double lowerMarginX = PracticeProbePanel.xPixToTrim-1;
                        double lowerMarginY = PracticeProbePanel.yPixToTrim-1;
                        
                        if(RunExperimentWindow.currentBet != 0){
	                        	if(PracticeProbePanel.firstBetMu<(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	                        		PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth/2+PracticeProbePanel.firstBetMu - (PracticeProbePanel.xPixToTrim-1);
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(PracticeProbePanel.firstBetMu>(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.firstBetMu - RunExperimentWindow.mouseMovementRectWidth/2;
	                        		PracticeProbePanel.cursorAllowedIntervalX=3*RunExperimentWindow.mouseMovementRectWidth/2-PracticeProbePanel.firstBetMu + (PracticeProbePanel.xPixToTrim-1);
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(PracticeProbePanel.firstBetMu==(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	                        		PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
               		 	 }
        	       		 else{
        	       			lowerMarginX = PracticeProbePanel.xPixToTrim-1;
        	       			PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
        	       			PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
        	       		 	}
                        
                        
                        
                        //this is to prevent the cursor from leaving a rectangular area
                        if(	(PracticeProbePanel.pX<= lowerMarginX)||(PracticeProbePanel.pX>=(lowerMarginX+PracticeProbePanel.cursorAllowedIntervalX))||
                        	(PracticeProbePanel.pY<=lowerMarginY)||(PracticeProbePanel.pY>=(lowerMarginY+PracticeProbePanel.cursorAllowedIntervalY))){
                        	if ((PracticeProbePanel.pX<=lowerMarginX)||(PracticeProbePanel.pX>=(lowerMarginX+PracticeProbePanel.cursorAllowedIntervalX))){
                        		PracticeProbePanel.pX=PracticeProbePanel.prevX;
                        		// Move the cursor
            					try {
            						robot = new Robot();
            						robot.mouseMove(PracticeProbePanel.pX, PracticeProbePanel.pY);
            					} catch (AWTException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
                        	}
                        	if((PracticeProbePanel.pY<=lowerMarginY)||(PracticeProbePanel.pY>=(lowerMarginY+PracticeProbePanel.cursorAllowedIntervalY))){
	                        		PracticeProbePanel.pY=PracticeProbePanel.prevY;
	                        		try {
	            						robot = new Robot();
	            						robot.mouseMove(PracticeProbePanel.pX, PracticeProbePanel.pY);
	            					} catch (AWTException e) {
	            						// TODO Auto-generated catch block
	            						e.printStackTrace();
	            					}
	                        	}
                        }
                        //save the previous coordinates
                        PracticeProbePanel.prevX=PracticeProbePanel.pX;
                        PracticeProbePanel.prevY=PracticeProbePanel.pY;
                        if(!PracticeProbePanel.showTrigger){
                        		//frame.revalidate();
        						frame.repaint();
        					}
                        
                    }

                });
            
            	
            	
            	//isKeyPressed = false;
            	
            	
            	PracticeProbePanel.lblAdjustPitch.addMouseListener(new MouseAdapter() {
        			@Override
        			public void mouseClicked(MouseEvent e)  {
						
        			}

        			@Override
        			public void mouseEntered(MouseEvent e) {
        				// TODO Auto-generated method stub
        				
        			}

        			@Override
        			public void mouseExited(MouseEvent e) {
        				// TODO Auto-generated method stub
        				
        			}

        			@Override
        			public void mousePressed(MouseEvent e) {
        				
        			}

        			@Override
        			public void mouseReleased(MouseEvent e) {
        				isKeyPressed = true;
        				PracticeProbePanel.showTrigger = true;
        				long timeToRespond= System.currentTimeMillis()-tempStartTime;
	            		timeTempList1.add((int) timeToRespond);
	            		freqTempList.add((double) PracticeProbePanel.fFreq);
	            		freqCoordTempList.add(PracticeProbePanel.fFreqCursorX);   	
	            		PracticeProbePanel.stopThread();
	            		timer.setRepeats(false);
        				timer.start();
	            		betsPoolService.shutdownNow();
        				}
                });
            	
            	
                frame.addKeyListener(new KeyListener() {
  		            @Override
  		            public void keyTyped(KeyEvent e) {
  		            	
  		            }

  		            @Override
  		            public void keyReleased(KeyEvent e) {
  		           
  		            }

  		            @Override
  		            public void keyPressed(KeyEvent e) {
  		            	isKeyPressed = true;
    	            		frame.revalidate();
    	            		frame.repaint();
    	            		//if the 'q' key is pressed -- exit
    	            		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
    	            			isProgramEnded = true;
    	            			writer.close();
    	            			betsPoolService.shutdownNow();
    	            			timeElapsed =  System.currentTimeMillis() - startPracticeTime;
    	            			recordToPracticeLogFileOnInterruption();
    	            			frame.dispose();
    	            			PracticeProbePanel.stopThread();
    	            			stopAllThreads();
    	            			}
  		            }
      		    });
                
                //check whether the probe task is timed
                if(isProbeTaskTimed){
	                	betsPoolService.awaitTermination(AdvancedOptionsWindow.probeDuration, TimeUnit.MILLISECONDS);
	                	
	                PracticeProbePanel.showTrigger = true;
	                	timer.setRepeats(false);
	    				timer.start();
	    				
	    				betsPoolService.shutdownNow();
	                frame.requestFocusInWindow();
                }
                else
                {
                	 	startTime = System.currentTimeMillis();
                	 	//if nothing is pressed for some time -- wait
                	 	betsPoolService.awaitTermination(Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED, TimeUnit.MILLISECONDS);
                	 	estimatedTime = System.currentTimeMillis() - startTime;
                	 	PracticeProbePanel.showTrigger = true;
                 	timer.setRepeats(false);
     				timer.start();
     				if(estimatedTime > Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED){
     					isProgramEnded = true;
     					writer.close();
     					betsPoolService.shutdownNow();
            			
     					String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
       		    			 + "The program is terminated.</center></html>"
       		    			 ,(int)(estimatedTime/(60000))
       		    			);
     					JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
            			
     					frame.dispose();
     					stopAllThreads();
     				}
                }
                
                if(!isProgramEnded){
    	            		if(isKeyPressed){
		    	            	double reportedFrequency = freqTempList.get(freqTempList.size()-1-bet);
		    	            	allReportedFreq[bet]=reportedFrequency;
		    	            	allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-bet);
		    	 
		    	            	double reportedFrequencyP=69+12*Math.log(reportedFrequency/440)/Math.log(2);  
		    	            	double errorP=12*Math.log(currentFreq/reportedFrequency)/Math.log(2);  
		    	            	
		    	            	String reportedFrequencyStr = String.valueOf(reportedFrequency);
		    	            	String reportedErrorStr = String.valueOf(currentFreq-freqTempList.get(freqTempList.size()-1-bet));
		    	            	String reportedErrorStrP = String.valueOf(errorP);
		    	            	String reportedFrequencyPStr = String.valueOf(reportedFrequencyP);

		    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////   	
		    	            //
		    	            	//
		    	            	//			RECALCULATE ALL THE DISTRIBUTIONS
		    	            	//
		    	            	//
		    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    	            	
		    	            	 Dimension panelSize = frame.getSize();
		    	       		 
		    	       		 int h = (int) panelSize.getHeight();
		    	       		 int w = (int) panelSize.getWidth();
		    	       		
		    	       		 double muAtZero=w/2;

		    	            	 double 	firstBetMu = allReportedCursorPositions[0];//getCursoreHorizFromFreq(RunExperimentWindow.allReportedFreq[0]);
		    	       		 
		    	       		 double reportedFreqPixels  = 0;
		    	       		 double reportedMuArray[] ;
		    	       		 double reportedDiffArray[];
		    	       		 
		    	       		 reportedMuArray = new double[RunExperimentWindow.currentBet+1];
		    	       		 reportedDiffArray = new double[RunExperimentWindow.currentBet+1];
		    	       		 
		    	       		 for(int b =0;b<=RunExperimentWindow.currentBet;b++){
		    	       			 reportedFreqPixels = allReportedCursorPositions[b];
		    	       			 reportedMuArray[b] =  muAtZero - firstBetMu+reportedFreqPixels;
		    	       			 reportedDiffArray[b] = Math.abs(w/2 -(reportedMuArray[b])); 
		    	       		 }
		    	       		 
		    	       		 double currentMu = muAtZero-firstBetMu+PracticeProbePanel.pX;	
		    	       		 double diff = Math.abs(w/2 -(currentMu));
		    	       		 double mixSum =0;
		    	 
		    	       		 double realToneMu = muAtZero - firstBetMu+getCursoreHorizFromFreq(RunExperimentWindow.currentFreq, panelSize);
		    	       		 
		    	       		 //start to add distributions
		    	       		 for (int x = (int) (muAtZero-w/2); x <(int)(muAtZero+w/2+1); x++) {
		    	       			 if(allReportedFreq[0]!=0 && trainingBetsSection==false)
		    	       				 mixSum=2*1000*gaussianPdf((x), w/2, sigma);
		    	       			 else
		    	       				 mixSum=0;
		    	       			
		    	       			 for(int b=1;b<=RunExperimentWindow.currentBet;b++){//changed from bet=1
		    	       				 //System.out.println("BET # "+ bet + " freq = "+reportedMuArray[bet]);
		    	       				 if(RunExperimentWindow.allReportedFreq[b]!=0)
		    	       					 mixSum += 1000 * gaussianPdf((x), reportedMuArray[b], sigma)/(1+Math.log10(mixSum+1));
		    	       				 else
		    	       					 mixSum+=0;
		    	       			 }
		    	       		
		    	       			 
		    	       			 if(x == (int)Math.round(realToneMu)){
		    	       					 heightAtRealTone = mixSum;
		    	       					
		    	       			}
		    	       			 //tmOut.printf("%-15d%-22.2f%n",x,mixSum);
		    	       		 }//end of iterating over all horizontal pixels
		    	       		 
		    	       		 
		    	       		allReportedScoresPerBet[bet] = heightAtRealTone;
		    	       		
		    	            	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		   
		    	            	if(reportedFrequency == 0 ){
		    	            		scorePerBet = 0;
		    	            		reportedFrequencyStr = "NaN";
		    	            		reportedErrorStr = "NaN";
		    	            		reportedErrorStrP = "NaN";
		    	            		reportedFrequencyPStr ="NaN";
		    	            	}
		    	            	else{
		    	            		double pBinSize = Math.abs(reportedFrequencyP-stimulusFreqP);
		    	            		scorePerBet=Math.round(Math.pow(allReportedScoresPerBet[bet]/PracticeProbePanel.maxScore,.93) * AdvancedOptionsWindow.maxPointsPerBet );//(AdvancedOptionsWindow.maxPointsPerBet/Math.exp(pBinSize));
		
		    	            	}
		    	            	System.out.println("For bet # "+(currentBet+1) +" you have earned "+scorePerBet+" points ");
		    	            	cummulativeScorePerTrial =cummulativeScorePerTrial+scorePerBet;
		    	           
		    	            	writer.println((i+1)+","+(bet+1)+","+currentFreq+","+ reportedFrequencyStr
		    	            			+","+timeTempList1.get(timeTempList1.size()-1)+","+reportedErrorStr+","+reportedErrorStrP
		    	            			+","+stimulusFreqP+ ","+reportedFrequencyPStr+","+mainFrequency+","+mainFreqP+","+scorePerBet+","+stimEndTime+","+endTimeBtwStimAndTrigger);
		    	    	        
	    	            }
    	            		else{
		    	            	System.out.println("Key is pressed  "+isKeyPressed);
		    	            	System.out.println("real frequency is  "+0);
		    	            	System.out.println("real reaction time "+0);
		    	            	writer.println((i+1)+","+(bet+1)+","+currentFreq+",NaN,NaN,NaN,NaN"+","
		    	            					+stimulusFreqP+ ",NaN,"+mainFrequency+","+mainFreqP+","+"0"+","+stimEndTime+","+endTimeBtwStimAndTrigger);
		    	            	cummulativeScorePerTrial =cummulativeScorePerTrial+0;
		    	            	allReportedFreq[bet]=0;
		    	            	if(freqCoordTempList.size() == 0)
		    	            		allReportedCursorPositions[bet]=xCoord;
		    	            	else
		    	            		allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-bet);
		    	            	PracticeProbePanel.stopThread();
	    	            }
                }
                numBetsCompleted+=1;
            }//end of iterating over all bets
            

            ActionListener showTriggerBeforeIntertrialInfoScreen = new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	frame.repaint();
		            	InterTrialInfoPanel.showTrigger = false;
		            }
		        };
		    
		    Timer intertrialInfoTriggerTimer = new Timer(Parameters.TRIGGER_ON_CLICK_DURATION,showTriggerBeforeIntertrialInfoScreen);
            
		   
            
            //============================================================================================================//
            
            InterTrialInfoPanel.cummulativeScorePerTrial=cummulativeScorePerTrial;
            System.out.println("cummulativeScorePerTrial    "+ cummulativeScorePerTrial);
            
            //start interBlock activity
            
            // show them report about their performance 
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			//redefine a new instance of executor for each new trial
			final ExecutorService interTrialInfoService1 = Executors.newCachedThreadPool();
			
			//invoke the start of experiment service
			//---------------------------------------------------------------------------------------------------//
			interTrialInfoService1.submit(interTrialInfoPanelTaskInst);
			PracticeProbePanel.stopThread();
			frame.setVisible(true);
			frame.revalidate();
			frame.repaint();
			frame.setFocusable(true);
			frame.requestFocusInWindow();
			
			InterTrialInfoPanel.showTrigger = true;
            intertrialInfoTriggerTimer.setRepeats(false);
            intertrialInfoTriggerTimer.start();
            
			frame.addMouseListener(new MouseListener() {
	          	 
				@Override
				public void mouseClicked(MouseEvent e) {
					interTrialInfoService1.shutdownNow();
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	        });
	    	
			frame.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {}
			
				@Override
				public void keyReleased(KeyEvent e) {}
			
				@Override
				public void keyPressed(KeyEvent e) {
					//if the 'q' key is pressed -- exit
	        		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	        			isProgramEnded = true;
	        			writer.close();
	        			interTrialInfoService1.shutdownNow();
	        			timeElapsed =  System.currentTimeMillis() - startPracticeTime;
	        			recordToPracticeLogFileOnInterruption();
	        			frame.dispose();
	        			stopAllThreads();
	        		}
				}
			});
			 startTime = System.currentTimeMillis();
			//if nothing is pressed for some time -- wait
			interTrialInfoService1.awaitTermination(Parameters.INTERBLOCK_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
			 estimatedTime = System.currentTimeMillis() - startTime;
			if(estimatedTime > Parameters.INTERBLOCK_TASK_DURATION_IF_IDLE){
				isProgramEnded = true;
				writer.close();
				interTrialInfoService1.shutdownNow();
				String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
		    			 + "The program is terminated.</center></html>"
		    			 ,(int)(estimatedTime/(60000))
		    			);
	   			JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
	   			timeElapsed =  System.currentTimeMillis() - startPracticeTime;
	   			recordToPracticeLogFileOnInterruption();
				frame.dispose();
				stopAllThreads();
				
				}

	       
            totalScore+=cummulativeScorePerTrial;
            currentPracticeTrial++;
            totalNumTrialsCompleted+=1;
           
        }//end of iterating over all trials

        EndOfPracticePanel.totalScore=Math.ceil(totalScore);
        EndOfPracticePanel.totalNumBets=numBetsCompleted;
        EndOfPracticePanel.totalNumTrialsCompleted=totalNumTrialsCompleted;
        
        writer.close();


	 }//end of runPracticeTrials
	 
	 
	 
	 
	
	
	 /**
		 * main routine for running instructions
		 * @param arguments
		 * @throws InterruptedException 
		 * @throws UnsupportedEncodingException 
		 * @throws FileNotFoundException 
		 * @throws AWTException 
		 */
		
		 public static void runInstructions(final RunExperimentWindow frame) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException, AWTException {

				 int xCoord = 0;
				 int yCoord = 0;
				 Robot robot = new Robot();
				 Random random = new Random();

		         
		         //class for probe panel activity
		         final class PracticeProbePanelTask implements Runnable {
		            @Override
		            public void run() {
		        			System.out.println("Running practice probe panel task.");
		            		frame.getContentPane().removeAll();
							frame.runPracticeProbePanel();
							frame.revalidate();
		                    frame.repaint();
		                    startTime = System.currentTimeMillis();
		            }
		            
		         };
		         
		        //class for pre-stimulus panel activity
		         final class PreStimulusPanelTask implements Runnable {
		             @Override
		             public void run() {
		             	System.out.println("Running stimulus panel task.");
		             	frame.getContentPane().removeAll();
		                 frame.runPreStimulusPanel();
		                 frame.revalidate();
		                 frame.repaint();
		             }
		         };
		         
		         
		        
		        
		      //class for instructions panel 2 activity
		        final class InstructionsPanel2Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel 2 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel2();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 3 activity
		        final class InstructionsPanel3Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel 3 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel3();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 4 activity
		        final class InstructionsPanel4Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  4 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel4();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 5 activity
		        final class InstructionsPanel5Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  5 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel5();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        
		      //class for instructions panel 6 activity
		        final class InstructionsPanel6Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  4 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel6();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 7 activity
		        final class InstructionsPanel7Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  7 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel7();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 8 activity
		        final class InstructionsPanel8Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel 8 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel8();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 9 activity
		        final class InstructionsPanel9Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  9 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel9();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for instructions panel 10 activity
		        final class InstructionsPanel10Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  10 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel10();
		                frame.revalidate();
		                frame.repaint();
		            }
		        }; 
		        
		      //class for instructions panel 11 activity
		        final class InstructionsPanel11Task implements Runnable {
		            @Override
		            public void run() {
		            	System.out.println("Running instructions panel  11 task.");
		            	frame.getContentPane().removeAll();
		                frame.runInstructionsPanel11();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        //class for probe intertrial panel activity
		        final class IntertrialPanelTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running intertrial panel task.");
		                frame.getContentPane().removeAll();
		                frame.runIntertrialPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		        //class for delay panel activity
		        final class DelayPanelTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running delay panel task.");
		                frame.getContentPane().removeAll();
		                frame.runDelayPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      //class for end of block panel activity
		        final class InterTrialInfoPanelTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running interblock panel task.");
		                InterTrialInfoPanel.cummulativeScorePerTrial = Math.ceil(cummulativeScorePerTrial);
		                InterTrialInfoPanel.currentTrial =currentPracticeTrial;
		                frame.getContentPane().removeAll();
		                frame.runInterTrialInfoPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        
		      
		        
		      //class for probe intertrial panel activity
		        final class PracticeTrialStartWarningPanelTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running score report panel task.");
		                frame.getContentPane().removeAll();
		                frame.runPracticeTrialStartWarningPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        //class for end of block panel activity
		        final class InterPracticeBetPanelTask implements Runnable {
		            @Override
		            public void run() {
		                System.out.println("Running interpracticebet panel task.");
		                frame.getContentPane().removeAll();
		                frame.runInterPracticeBetPanel();
		                frame.revalidate();
		                frame.repaint();
		            }
		        };
		        //define instances of all four classes
		        DelayPanelTask delayTaskInst = new DelayPanelTask();
		        PreStimulusPanelTask preStimulusPanelTaskInst = new PreStimulusPanelTask();
		        IntertrialPanelTask intertrialTaskInst = new IntertrialPanelTask();
		        InterTrialInfoPanelTask interTrialInfoPanelTaskInst = new InterTrialInfoPanelTask();
		        
		        
		        InstructionsPanel2Task instructionsPanel2TaskInst = new InstructionsPanel2Task();
		        InstructionsPanel3Task instructionsPanel3TaskInst = new InstructionsPanel3Task();
		        InstructionsPanel4Task instructionsPanel4TaskInst = new InstructionsPanel4Task();
		        InstructionsPanel5Task instructionsPanel5TaskInst = new InstructionsPanel5Task();
		        InstructionsPanel6Task instructionsPanel6TaskInst = new InstructionsPanel6Task();
		        InstructionsPanel7Task instructionsPanel7TaskInst = new InstructionsPanel7Task();
		        InstructionsPanel8Task instructionsPanel8TaskInst = new InstructionsPanel8Task();
		        InstructionsPanel9Task instructionsPanel9TaskInst = new InstructionsPanel9Task();
		        InstructionsPanel10Task instructionsPanel10TaskInst = new InstructionsPanel10Task();
		        InstructionsPanel11Task instructionsPanel11TaskInst = new InstructionsPanel11Task();
		        PracticeTrialStartWarningPanelTask practiceTrialStartWarningPanelTaskInst = new PracticeTrialStartWarningPanelTask();
		        InterPracticeBetPanelTask interPracticeBetTaskInst = new InterPracticeBetPanelTask();
		        
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        //hide mouse cursor
				Toolkit toolkit = Toolkit.getDefaultToolkit();
			    Point hotSpot = new Point(0,0);
			    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
			    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
			    frame.setCursor(invisibleCursor);
			    //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        
		        
		        
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 2
				//////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel2TaskInst, false);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 3
				//////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel3TaskInst, false);
		        
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 4
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////  
		        changePanelAppearance(frame, instructionsPanel4TaskInst, true);
		        
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        ///// Instructions panel 5
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel5TaskInst, false);
				
				
				//go to trials
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
				// FIRST TRIAL
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				//start with first trial
				//have them do one trial (without multiple bets) with a sample tone that plays for longer than the standard sample tone in the experiment]
		
				cummulativeScorePerTrial=0;
		    	currentPracticeTrial=0;
		    	long startTrialTime = startTime;
				long estimatedTime;
				long timeToTerminate;
		    	
			    
		    	//ArrayList to keep reaction time
		        final ArrayList<Integer> timeTempList = new ArrayList<Integer>();
		
		    	double stimulusFreqP ;
		    	double mainFrequency;
		    	double mainFreqP ;
			   
		    	//repeat it numOfPracticeBetTrials times
			    for(int k=0;k<numOfTrialsWithOneBet;k++){
			    	System.out.println("Running trial number "+k);	
			    	currentFreq = practiceTaskMap[k][0];
			    	stimulusFreqP = practiceTaskMap[k][1];
			    	mainFrequency = practiceTaskMap[k][2];
			    	mainFreqP = practiceTaskMap[k][3];
			    	//ArrayList to keep frequencies
			        freqTempList.clear();
			        freqCoordTempList.clear();
			        
			        
			        
			    	//redefine a new instance of executor for each new trial
			    	final ExecutorService firstTrialService = Executors.newSingleThreadExecutor();//.newCachedThreadPool();
			    	
			    	//invoke the Score Report Panel routines
		            //---------------------------------------------------------------------------------------------------//
		        	firstTrialService.submit(practiceTrialStartWarningPanelTaskInst);
		            frame.setVisible(true);
		            firstTrialService.awaitTermination(Parameters.SCORE_REPORT_TASK_DURATION, TimeUnit.MILLISECONDS);
		        
			        
			    	
			    	//invoke the intertrial panel routines
			    	//---------------------------------------------------------------------------------------------------//
			    	frame.setFocusable(false);
			    	PracticeProbePanel.stopThread();
			    	firstTrialService.submit(intertrialTaskInst);
			        frame.setVisible(true);
			        
			        firstTrialService.awaitTermination(AdvancedOptionsWindow.intertrialDuration, TimeUnit.MILLISECONDS);// 3 sec
			 
			        //invoke the pre-stimulus panel routines
			        //---------------------------------------------------------------------------------------------------//
			        firstTrialService.submit(preStimulusPanelTaskInst);
			        frame.setVisible(true);
			        firstTrialService.awaitTermination(AdvancedOptionsWindow.preStimulusDuration, TimeUnit.MILLISECONDS);// 3 sec
			        
			        //invoke the stimulus panel routines
			        //---------------------------------------------------------------------------------------------------//
			        firstTrialService.submit(new Runnable() {
			            @Override
			            public void run() {
			            	frame.getContentPane().removeAll();
			            	System.out.println("Running stimulus panel task.");
			            	frame.getContentPane().add(new TriggerStimulusCanvas());
			            	frame.revalidate();
//			            	frame.repaint();
			            	frame.runStimulusPanel(currentFreq);

			                
			            }
			        });
			        frame.setVisible(true);
			        //for the first two trials make longer stimulus and response without any bets
			        timeToTerminate = 0;
			        
			        isProbeTaskTimed  = false;
			    	
			    	
			    	timeToTerminate = 2*AdvancedOptionsWindow.stimulusDuration;
			    	firstTrialService.awaitTermination(timeToTerminate, TimeUnit.MILLISECONDS);//500
			        	
			       
			    	//invoke the delay panel routines
			        //---------------------------------------------------------------------------------------------------//
			    	firstTrialService.submit(delayTaskInst);
			        frame.setVisible(true);
			        firstTrialService.awaitTermination(AdvancedOptionsWindow.delayDuration, TimeUnit.MILLISECONDS);//500
			
			        //---------------------------------------------------------------------------------------------------//
			        
			        //first move a cursor to randomly generated position
			        
			        mouseMovementRectWidth = frame.getWidth()-2*PracticeProbePanel.xPixToTrim;
			        mouseMovementRectHeight = frame.getHeight()-2*PracticeProbePanel.yPixToTrim;
			        
			        xCoord =  random.nextInt(mouseMovementRectWidth-1)+PracticeProbePanel.xPixToTrim;
			        yCoord =  random.nextInt(mouseMovementRectHeight-1)+PracticeProbePanel.yPixToTrim;
					
					
			        PracticeProbePanelTask practiceProbeTaskInst1 = new PracticeProbePanelTask();
			    	//============================================================================================================//
			        //invoke the probe panel routines
			        //---------------------------------------------------------------------------------------------------//
			    	
			        firstTrialService.submit(practiceProbeTaskInst1);
			    	
			        frame.setVisible(true);
			        // Move the cursor
			
			        PracticeProbePanel.X0=xCoord;
			        PracticeProbePanel.Y0=yCoord;
			        
			        PracticeProbePanel.pX = PracticeProbePanel.X0;
			        PracticeProbePanel.pY = PracticeProbePanel.Y0;
			        
			        
			        robot.mouseMove(PracticeProbePanel.X0, PracticeProbePanel.Y0);
			       
			        frame.revalidate();
			    	frame.repaint();
			    	frame.setFocusable(true);
			    	frame.requestFocusInWindow();
			    	
			    	isKeyPressed = false;
			   
	            	
	            	
	            	//Monitor mouse motion
	                PracticeProbePanel.lblAdjustPitch.addMouseMotionListener(new MouseAdapter() {
	                    public void mouseMoved(MouseEvent me) {
	                    	Robot robot;
	                        // Get x,y and store them
	                    	PracticeProbePanel.pX = me.getX();
	                    	PracticeProbePanel.pY = me.getY();
	                        //if mouse moves from the vicinity of its initial position (X0,Y0) -- sounds starts playing
	                        //it is more then enough to check horizontal vicinity only
	                        if((PracticeProbePanel.pX>Math.abs(PracticeProbePanel.X0+1) || PracticeProbePanel.pX<Math.abs(PracticeProbePanel.X0-1))){
	                        	PracticeProbePanel.mouseIsMoving = true;
	                        }
	                        
	                        double lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	                        double lowerMarginY = PracticeProbePanel.yPixToTrim-1;
	                        
	                        if(RunExperimentWindow.currentBet != 0){
	                        	if(PracticeProbePanel.firstBetMu<(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	                        		PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth/2+PracticeProbePanel.firstBetMu - (PracticeProbePanel.xPixToTrim-1);
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(PracticeProbePanel.firstBetMu>(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.firstBetMu - RunExperimentWindow.mouseMovementRectWidth/2;
	                        		PracticeProbePanel.cursorAllowedIntervalX=3*RunExperimentWindow.mouseMovementRectWidth/2-PracticeProbePanel.firstBetMu + (PracticeProbePanel.xPixToTrim-1);
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	if(PracticeProbePanel.firstBetMu==(RunExperimentWindow.mouseMovementRectWidth)/2+PracticeProbePanel.xPixToTrim-1){
	                        		lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	                        		PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
	                        		PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	                        	}
	                        	
	               		 	 }
	        	       		 else{
	        	       			lowerMarginX = PracticeProbePanel.xPixToTrim-1;
	        	       			PracticeProbePanel.cursorAllowedIntervalX=RunExperimentWindow.mouseMovementRectWidth;
	        	       			PracticeProbePanel.cursorAllowedIntervalY=RunExperimentWindow.mouseMovementRectHeight;
	        	       		 }
	                        
	                        
	                        
	                        //this is to prevent the cursor from leaving a rectangular area
	                        if(	(PracticeProbePanel.pX<= lowerMarginX)||(PracticeProbePanel.pX>=(lowerMarginX+PracticeProbePanel.cursorAllowedIntervalX))||
	                        	(PracticeProbePanel.pY<=lowerMarginY)||(PracticeProbePanel.pY>=(lowerMarginY+PracticeProbePanel.cursorAllowedIntervalY))){
	                        	if ((PracticeProbePanel.pX<=lowerMarginX)||(PracticeProbePanel.pX>=(lowerMarginX+PracticeProbePanel.cursorAllowedIntervalX))){
	                        		PracticeProbePanel.pX=PracticeProbePanel.prevX;
	                        		// Move the cursor
	            					try {
	            						robot = new Robot();
	            						robot.mouseMove(PracticeProbePanel.pX, PracticeProbePanel.pY);
	            					} catch (AWTException e) {
	            						// TODO Auto-generated catch block
	            						e.printStackTrace();
	            					}
	                        	}
	                        	if((PracticeProbePanel.pY<=lowerMarginY)||(PracticeProbePanel.pY>=(lowerMarginY+PracticeProbePanel.cursorAllowedIntervalY))){
	                        		PracticeProbePanel.pY=PracticeProbePanel.prevY;
	                        		try {
	            						robot = new Robot();
	            						robot.mouseMove(PracticeProbePanel.pX, PracticeProbePanel.pY);
	            					} catch (AWTException e) {
	            						// TODO Auto-generated catch block
	            						e.printStackTrace();
	            					}
	                        	}
	                        }
	                        //save the previous coordinates
	                        PracticeProbePanel.prevX=PracticeProbePanel.pX;
	                        PracticeProbePanel.prevY=PracticeProbePanel.pY;
	                        if(!PracticeProbePanel.showTrigger){
	                        	//frame.revalidate();
	        					frame.repaint();
	        				}
	                        
	                    }

	                });
			    	
			    	
			    	PracticeProbePanel.lblAdjustPitch.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							isKeyPressed = true;
							long timeToRespond= System.currentTimeMillis()-startTime;
			        		timeTempList.add((int) timeToRespond);
			        		freqTempList.add((double) PracticeProbePanel.fFreq);
			        		freqCoordTempList.add(PracticeProbePanel.fFreqCursorX);
			        		firstTrialService.shutdownNow();
							PracticeProbePanel.stopThread();
						}
			
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			
						@Override
						public void mousePressed(MouseEvent e) {
							//System.out.println("MOUSE KEY PRESSED");
							RunExperimentWindow.isKeyPressed = true;
							
						}
			
						@Override
						public void mouseReleased(MouseEvent e) {
							//System.out.println("MOUSE KEY RELEASED");
							RunExperimentWindow.isKeyPressed = true;
						}
			        });
			    	
			    	
			    	
			        frame.addKeyListener(new KeyListener() {
					            @Override
					            public void keyTyped(KeyEvent e) {
					            	
					            }
			
					            @Override
					            public void keyReleased(KeyEvent e) {
					           
					            }
			
					            @Override
					            public void keyPressed(KeyEvent e) {
					            	isKeyPressed = true;
				            		frame.revalidate();
				            		frame.repaint();
				            		//if the 'q' key is pressed -- exit
				            		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				            			isProgramEnded = true;
				            			firstTrialService.shutdownNow();
				            			//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
				            			//recordToPracticeLogFileOnInterruption();
				            			frame.dispose();
				            			PracticeProbePanel.stopThread();
				            			stopAllThreads();
				            			
				            		}
					            }
					        });
			        
			        //check whether the probe task is timed
			        
			 
			    	startTrialTime = System.currentTimeMillis();
					//if nothing is pressed for some time -- wait
			    	firstTrialService.awaitTermination(Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED, TimeUnit.MILLISECONDS);
					estimatedTime = System.currentTimeMillis() - startTrialTime;
					if(estimatedTime > Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED){
						isProgramEnded = true;
						writer.close();
						firstTrialService.shutdownNow();
						
						String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
				    			 + "The program is terminated.</center></html>"
				    			 ,(int)(estimatedTime/(60000))
				    			);
						JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
						
						frame.dispose();
						stopAllThreads();
					}
			
			        
			        
			        System.out.println("trial = "+0+ " bet = "+0);
			        if(!isProgramEnded){
			            if(isKeyPressed){
			            	double reportedFrequency = freqTempList.get(freqTempList.size()-1);
			            	allReportedFreq[0]=reportedFrequency;
			            	allReportedCursorPositions[0]=freqCoordTempList.get(freqCoordTempList.size()-1);
			            
			            	double reportedFrequencyP=69+12*Math.log(reportedFrequency/440)/Math.log(2);  
			            	
			            	String reportedFrequencyStr = String.valueOf(reportedFrequency);
			            	String reportedErrorStr = String.valueOf(currentFreq-freqTempList.get(freqTempList.size()-1));
			            	String reportedFrequencyPStr = String.valueOf(reportedFrequencyP);
			            	
			            	if(reportedFrequency == 0 ){
			            		scorePerBet = 0;
			            		reportedFrequencyStr = "NaN";
			            		reportedErrorStr = "NaN";
			            		reportedFrequencyPStr ="NaN";
			            	}
			            	else{
			            		double pBinSize = Math.abs(reportedFrequencyP-stimulusFreqP);
			            		scorePerBet=(AdvancedOptionsWindow.maxPointsPerBet/Math.exp(pBinSize));
			
			            	}
			            	System.out.println("For bet # "+currentBet +" you have earned "+scorePerBet+" points.");
			            	cummulativeScorePerTrial =cummulativeScorePerTrial+scorePerBet;
			            }
			            else{
			            	System.out.println("Key is pressed  "+isKeyPressed);
			            	System.out.println("real frequency is  "+0);
			            	System.out.println("real reaction time "+0);
			            	//writer.println((1)+","+(1)+","+currentFreq+",NaN,NaN,NaN"+","+stimulusFreqP+ ",NaN,"+mainFrequency+","+mainFreqP+","+"0");
			            	cummulativeScorePerTrial =cummulativeScorePerTrial+0;
			            	allReportedFreq[0]=0;
			            	if(freqCoordTempList.size() == 0)
			            		allReportedCursorPositions[0]=xCoord;
			            	else
			            		allReportedCursorPositions[0]=freqCoordTempList.get(freqCoordTempList.size()-1);
			            	PracticeProbePanel.stopThread();
			            }
			        }
					
			        //============================================================================================================//

			        cummulativeScorePerTrial=(cummulativeScorePerTrial*AdvancedOptionsWindow.numberOfBetsPerTrial);
			        InterTrialInfoPanel.cummulativeScorePerTrial = cummulativeScorePerTrial;
			        //start interBlock activity
			        
			        //for the first trial show them report about their performance 
					//ScoreReportPanel.invoke=false;
					//////////////////////////////////////////////////////////////////////////////////////////////////////////
					//redefine a new instance of executor for each new trial
					final ExecutorService interTrialInfoService = Executors.newCachedThreadPool();
					
					//invoke the start of experiment service
					//---------------------------------------------------------------------------------------------------//
					interTrialInfoService.submit(interTrialInfoPanelTaskInst);
					PracticeProbePanel.stopThread();
					frame.setVisible(true);
					frame.revalidate();
					frame.repaint();
					frame.setFocusable(true);
					frame.requestFocusInWindow();
					
					frame.addMouseListener(new MouseListener() {
			         	 
						@Override
						public void mouseClicked(MouseEvent e) {
							interTrialInfoService.shutdownNow();
							
						}
			
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			
						@Override
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			
						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
			        });
			    	
					
					frame.addKeyListener(new KeyListener() {
						@Override
						public void keyTyped(KeyEvent e) {}
					
						@Override
						public void keyReleased(KeyEvent e) {}
					
						@Override
						public void keyPressed(KeyEvent e) {
							//if the 'q' key is pressed -- exit
				    		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				    			isProgramEnded = true;
				    			//writer.close();
				    			interTrialInfoService.shutdownNow();
				    			//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
				    			//recordToPracticeLogFileOnInterruption();
				    			frame.dispose();
				    			stopAllThreads();	
				    		}
						}
					});
					startTrialTime = System.currentTimeMillis();
					//if nothing is pressed for some time -- wait
					interTrialInfoService.awaitTermination(Parameters.INTERBLOCK_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
					estimatedTime = System.currentTimeMillis() - startTrialTime;
					if(estimatedTime > Parameters.INTERBLOCK_TASK_DURATION_IF_IDLE){
						isProgramEnded = true;
						//writer.close();
						interTrialInfoService.shutdownNow();
						String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
				    			 + "The program is terminated.</center></html>"
				    			 ,(int)(estimatedTime/(60000))
				    			);
						JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
						//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
						//recordToPracticeLogFileOnInterruption();
						frame.dispose();
						stopAllThreads();
						
						}
					totalNumTrialsCompleted+=1;
				}//end iterating over the number of times to present one-betted trial
			    
		        
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        ///// Instructions panel 6
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////  
		        changePanelAppearance(frame, instructionsPanel6TaskInst, false);
		        
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        ///// Instructions panel 7
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////  
		        changePanelAppearance(frame, instructionsPanel7TaskInst, false);
		
		        
		        
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        // SECOND TRIAL (PRACTICE PLACING BETS ONLY) 
		        //////////////////////////////////////////////////////////////////////////////////////////////////////////
		        
		        trainingBetsSection = true;
		        currentPracticeTrial = 1; //temporary to produce proper output
		        numberOfBetsPerTrial = AdvancedOptionsWindow.numberOfBetsPerTrial;
		        xCoord = frame.getWidth()/2;
		        allReportedCursorPositions[0] = xCoord;
		        
		        //repeat it numOfPracticeBetTrials times
		        for(int k=0;k<numOfPracticeBetTrials;k++){
		        	//iterate over all bets
			        for(int bet = 1;bet<=numberOfBetsPerTrial;++bet) {
			        	numBetsCompleted+=1;
			        	currentBet = bet;
			        	final ExecutorService betsPoolService = Executors.newSingleThreadExecutor();//.newCachedThreadPool();
			        	PracticeProbePanelTask practiceProbeTaskInst2 = new PracticeProbePanelTask();
			        	//============================================================================================================//
			            //invoke the probe panel routines
			            //---------------------------------------------------------------------------------------------------//
			        	
			        	betsPoolService.submit(practiceProbeTaskInst2);
			        	
			            frame.setVisible(true);
			            // Move the cursor
			            //for the first bet place the cursor randomly, starting from the second bet the cursor X coord coincides with the last bet's X
			            if(bet == 0)
			            	PracticeProbePanel.X0=xCoord;
			            else{
			            	PracticeProbePanel.X0=(int) allReportedCursorPositions[bet-1];
			            	
			            }
			
			            PracticeProbePanel.Y0=yCoord;  
			            PracticeProbePanel.pX = PracticeProbePanel.X0;
			            PracticeProbePanel.pY = PracticeProbePanel.Y0;
			            
			             
			            robot.mouseMove(PracticeProbePanel.X0, PracticeProbePanel.Y0);
			           
			            frame.revalidate();
			        	frame.repaint();
			        	frame.setFocusable(true);
			        	frame.requestFocusInWindow();
			        	
			        	isKeyPressed = false;
		            	
		            	PracticeProbePanel.lblAdjustPitch.addMouseListener(new MouseAdapter() {
		        			@Override
		        			public void mouseClicked(MouseEvent e) {
		        				freqTempList.add((double) PracticeProbePanel.fFreq);
				            	freqCoordTempList.add(PracticeProbePanel.fFreqCursorX);
				            	betsPoolService.shutdownNow();
								PracticeProbePanel.stopThread();
		        			}
		
		        			@Override
		        			public void mouseEntered(MouseEvent e) {
		        				// TODO Auto-generated method stub
		        				
		        			}
		
		        			@Override
		        			public void mouseExited(MouseEvent e) {
		        				// TODO Auto-generated method stub
		        				
		        			}
		
		        			@Override
		        			public void mousePressed(MouseEvent e) {
		        				//System.out.println("MOUSE KEY PRESSED");
		        				RunExperimentWindow.isKeyPressed = true;
		        				
		        			}
		
		        			@Override
		        			public void mouseReleased(MouseEvent e) {
		        				//System.out.println("MOUSE KEY RELEASED");
		        				RunExperimentWindow.isKeyPressed = true;
		        			}
		                });
			        	
			            frame.addKeyListener(new KeyListener() {
			  		            @Override
			  		            public void keyTyped(KeyEvent e) {
			  		            	
			  		            }
			
			  		            @Override
			  		            public void keyReleased(KeyEvent e) {
			  		           
			  		            }
			
			  		            @Override
			  		            public void keyPressed(KeyEvent e) {
			  		            	isKeyPressed = true;
				            		frame.revalidate();
				            		frame.repaint();
				            		//if the 'q' key is pressed -- exit
				            		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				            			isProgramEnded = true;
				            			//writer.close();
				            			betsPoolService.shutdownNow();
				            			//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
				            			//recordToPracticeLogFileOnInterruption();
				            			frame.dispose();
				            			PracticeProbePanel.stopThread();
				            			stopAllThreads();
				            			
				            		}
				            		if (e.getKeyCode() == KeyEvent.VK_SPACE){
			  		            		freqTempList.add((double) PracticeProbePanel.fFreq);
			  		            		freqCoordTempList.add(PracticeProbePanel.fFreqCursorX);
			  		            		betsPoolService.shutdownNow();
										PracticeProbePanel.stopThread();
				            		}
			  		            }
			  		        });
			            
			            	startTrialTime = System.currentTimeMillis();
							//if nothing is pressed for some time -- wait
			            	betsPoolService.awaitTermination(Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED, TimeUnit.MILLISECONDS);
							estimatedTime = System.currentTimeMillis() - startTrialTime;
							if(estimatedTime > Parameters.PROBE_TASK_DURATION_IF_IDLE_SELF_PACED){
								isProgramEnded = true;
			        			//writer.close();
			        			betsPoolService.shutdownNow();
			        			
			        			String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
			   		    			 + "The program is terminated.</center></html>"
			   		    			 ,(int)(estimatedTime/(60000))
			   		    			);
			        			JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
			        			
			        			frame.dispose();
			        			stopAllThreads();
							}
			
			
			            if(!isProgramEnded){
				            if(isKeyPressed){
				            	double reportedFrequency = freqTempList.get(freqTempList.size()-1-(bet));
				            	allReportedFreq[bet]=reportedFrequency;
				            	allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-(bet));
		
				            }
				            else{
				            	System.out.println("Key is pressed  "+isKeyPressed);
				            	System.out.println("real frequency is  "+0);
				            	System.out.println("real reaction time "+0);
				            	
				            	cummulativeScorePerTrial =cummulativeScorePerTrial+0;
				            	allReportedFreq[bet]=0;
				            	if(freqCoordTempList.size() == 0)
				            		allReportedCursorPositions[bet]=xCoord;
				            	else
				            		allReportedCursorPositions[bet]=freqCoordTempList.get(freqCoordTempList.size()-1-(bet));
				            	PracticeProbePanel.stopThread();
				            }
			            }
		
			        }//end of iterating over all bets
			       
			        
		        	//////////////////////////////////////////////////////////////////////////////////////////////////////////
		        	///// Interblock activity
		        	//////////////////////////////////////////////////////////////////////////////////////////////////////////  
		        	if(k!=(numOfPracticeBetTrials-1))
			        	changePanelAppearance(frame, interPracticeBetTaskInst, false);
			
		        }
		        //we do not count currentTrial for bets practice
		        
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 8
				//////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel8TaskInst, false);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 9
				//////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel9TaskInst, false);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				///// Instructions panel 10
				//////////////////////////////////////////////////////////////////////////////////////////////////////////  
				changePanelAppearance(frame, instructionsPanel10TaskInst, false);


	    }//end of run instructions
		 
		 
	 /**
	  * stops all currently running threads
	  */
	 public static void stopAllThreads(){
		 for (Thread t : Thread.getAllStackTraces().keySet()) 
		 {  if (t.getState()==Thread.State.RUNNABLE) 
		      t.interrupt(); 
		 }
		 System.exit(0);
	 }

	 /**
	  * 
	  */
	 public static void recordToPracticeLogFileOnInterruption(){
		 try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ExperimentSetupWindow.practiceLogFileName, true)))) {
			 	out.printf("%-35s%-5s%n", "Sigma", String.valueOf(Parameters.SIGMA));
				out.printf("%-35s%-5s%n", "Total score", String.valueOf(totalScore));
				out.printf("%-35s%-5s%n", "Number of bets completed", String.valueOf(numBetsCompleted));
				out.printf("%-35s%-5s%n", "Number of trials completed", String.valueOf(totalNumTrialsCompleted));
				out.printf("%-35s%-5s%n", "Number of practice sessions", String.valueOf(numOfPracticeSessions));
				out.printf("%-35s%-5s%n", "Time elapsed, ms", String.valueOf(timeElapsed));	
				out.println("--------------------------------------------------------------------------------");
	            out.println("The generated frequencies are: ");
	            out.printf("%-10s%-10s%-10s%n","#","P-index","Frequency, Hz");
	            int totalNumOfPracticeTrials = numOfTrialsWithOneBet + numOfDemoTrialsNotTimed + numOfDemoTrialsTimed;
	            for(int k=0;k<totalNumOfPracticeTrials;k++){
	                out.printf("%-10d%-10.0f%-10.2f%n",(k+1),practiceTaskMap[k][3],practiceTaskMap[k][2]);
	            }
	            out.println("--------------------------------------------------------------------------------");
			}catch (IOException e) {
			    System.err.println(e);
			}
	 }
	 /**
	  * 
	  */
	 public static void recordToExperimentLogFileOnInterruption(){
		 try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ExperimentSetupWindow.experimentLogFileName, true)))) {
			 	out.printf("%-35s%-5s%n", "Sigma", String.valueOf(Parameters.SIGMA));
				out.printf("%-35s%-5s%n", "Total score", String.valueOf(totalScore));
				out.printf("%-35s%-5s%n", "Number of bets completed", String.valueOf(numBetsCompleted));
				out.printf("%-35s%-5s%n", "Number of trials completed", String.valueOf(totalNumTrialsCompleted));
				out.printf("%-35s%-5s%n", "Time elapsed, ms", String.valueOf(timeElapsed));
				 out.println("--------------------------------------------------------------------------------");
		            out.println("The generated frequencies are: ");
		            out.printf("%-10s%-10s%-10s%n","#","P-index","Frequency, Hz");
		            for(int k=0;k<numFrequencies;k++)
		                out.printf("%-10d%-10.0f%-10.2f%n",(k+1),experimentTaskMap[k][3],experimentTaskMap[k][2]);
		            out.println("--------------------------------------------------------------------------------");
			}catch (IOException e) {
			    System.err.println(e);
			}
	 }
	 /**
	  * 
	  */
	 public static void recordExperimentTaskMap(){
		 try(PrintWriter tmOut = new PrintWriter(new BufferedWriter(new FileWriter(experimentTaskMapFileName, true)))) {
				tmOut.printf("%-15s%-15s%-22s%-22s%-22s%-22s%n","trial #","run #","Gen. Frequency, Hz","Gen. Frequency, P","Main Frequency, Hz","Main Frequency, P");
			    for(int k=0;k<experimentTaskMap.length;k++){
			    	tmOut.printf("%-15d%-15.0f%-22.2f%-22.2f%-22.2f%-22.2f%n",(k+1),(experimentTaskMap[k][4]+1),experimentTaskMap[k][0],experimentTaskMap[k][1],experimentTaskMap[k][2],experimentTaskMap[k][3]);
			    }
			    tmOut.println("--------------------------------------------------------------------------------");
			    
			}catch (IOException e) {
			    System.err.println(e);
			}
	 }
	 
	 public static void recordPracticeTaskMap(){
		 try(PrintWriter tmOut = new PrintWriter(new BufferedWriter(new FileWriter(practiceTaskMapFileName, true)))) {
				tmOut.printf("%-15s%-22s%-22s%-22s%-22s%n","trial #","Gen. Frequency, Hz","Gen. Frequency, P","Main Frequency, Hz","Main Frequency, P");
			    for(int k=0;k<practiceTaskMap.length;k++){
			    	tmOut.printf("%-15d%-22.2f%-22.2f%-22.2f%-22.2f%n",(k+1),practiceTaskMap[k][0],practiceTaskMap[k][1],practiceTaskMap[k][2],practiceTaskMap[k][3]);
			    }
			    tmOut.println("--------------------------------------------------------------------------------");
			}catch (IOException e) {
			    System.err.println(e);
			}
	 }
	 
	
	 /**
	  * To show a corresponding window. playSampleTone = true if required to play a tone on pressing the T key
	  * @param frame
	  * @param PanelTaskInstance
	  * @param playSampleTone
	  * @throws InterruptedException
	  */
	public static void changePanelAppearance(final JFrame frame,Runnable PanelTaskInstance, final boolean playSampleTone) throws InterruptedException{
		//redefine a new instance of executor for each new trial
        final ExecutorService panelService = Executors.newSingleThreadExecutor();
        
        //invoke the start of experiment service
        //---------------------------------------------------------------------------------------------------//
        panelService.submit(PanelTaskInstance);
        frame.setVisible(true);
        frame.revalidate();
    	frame.repaint();
    	frame.setFocusable(true);
    	frame.requestFocusInWindow();
    	
    	// Create a variable holding the listener
    	KeyAdapter keyAdapter = new KeyAdapter()
    	{
    	  public void keyPressed(KeyEvent e)
    	  {
    		//if the 'q' key is pressed -- exit
      		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      			panelService.shutdownNow();
      			isProgramEnded = true;

      			if(writer!=null)
      				writer.close();
      			//timeElapsed =  System.currentTimeMillis() - startPracticeTime;
      			//recordToPracticeLogFileOnInterruption();
      			frame.dispose();
      			stopAllThreads();
      			
      		}

          	//if sample tone needs to be demonstrated for InstructionsPanel2
          	if(playSampleTone){
          		if (e.getKeyCode() == KeyEvent.VK_T){
              		float minFrequency = AdvancedOptionsWindow.minFrequency;
              		float maxFrequency = AdvancedOptionsWindow.maxFrequency;
              		double tempToneFrequency = (maxFrequency-minFrequency)*Math.random()+minFrequency;
              		((RunExperimentWindow) frame).runStimulusPanel(tempToneFrequency);
              	}
          	}

    	  }
    	};
    	frame.addKeyListener(keyAdapter);
    	
    	frame.addMouseListener(new MouseListener() {
          	 
			@Override
			public void mouseClicked(MouseEvent e) {
				panelService.shutdownNow();
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
    	
        long serviceStartTime = System.currentTimeMillis();
        
		//if nothing is pressed for some time -- wait
        panelService.awaitTermination(Parameters.START_OF_EXPERIMENT_TASK_DURATION_IF_IDLE, TimeUnit.MILLISECONDS);
        
		long serviceEstimatedTime = System.currentTimeMillis() - serviceStartTime;
		if(serviceEstimatedTime > Parameters.START_OF_EXPERIMENT_TASK_DURATION_IF_IDLE){
			isProgramEnded = true;
			writer.close();
			panelService.shutdown();
			
			String message = String.format("<html><center> No key has been pressed for the last %d minutes <br>"
		    			 + "The program is terminated.</center></html>"
		    			 ,(int)(serviceEstimatedTime/(60000))
		    			);
   			JOptionPane.showMessageDialog(null, message, "Termination Message",JOptionPane.ERROR_MESSAGE);
   			
			frame.dispose();
			stopAllThreads();
		}
		frame.removeKeyListener(keyAdapter);
	}
	 
	
	/**
	 * load parameters from properties file
	 * @throws IOException 
	 */
	public static void loadParams() throws IOException {
	    Properties props = new Properties();
	    InputStream is = null;
	 
	  
        File f = new File("program.properties");
        if(!f.exists() && !f.isDirectory())
        {
            f.createNewFile();
            
  	        props.setProperty("StimulusDuration", String.valueOf(stimulusDuration));
  	        props.setProperty("PreStimulusDuration", ""+String.valueOf(preStimulusDuration));
  	        props.setProperty("IntertrialDuration", ""+String.valueOf(intertrialDuration));
  	        props.setProperty("DelayDuration", ""+String.valueOf(delayDuration));
  	        props.setProperty("ProbeDuration", ""+String.valueOf(probeDuration));
		  	props.setProperty("NumberOfBetsPerTrial", ""+String.valueOf(numberOfBetsPerTrial));
		  	props.setProperty("MaxPointsPerBet", ""+String.valueOf(maxPointsPerBet));
		  	props.setProperty("NumTrials", ""+String.valueOf(numTrials));
		  	props.setProperty("NumRuns", ""+String.valueOf(numRuns));
		  	props.setProperty("StudyId", ""+String.valueOf(studyId));
		  	props.setProperty("StudyVer", ""+String.valueOf(studyVer));
		  	props.setProperty("NumOfTrialsWithOneBet", ""+String.valueOf(numOfTrialsWithOneBet));
		  	props.setProperty("NumOfDemoTrialsNotTimed", ""+String.valueOf(numOfDemoTrialsNotTimed));
		  	props.setProperty("NumOfDemoTrialsTimed", ""+String.valueOf(numOfDemoTrialsTimed));
		  	props.setProperty("NumOfPracticeBetTrials", ""+String.valueOf(numOfPracticeBetTrials));
		  	props.setProperty("MaxFrequency", ""+String.valueOf(maxFrequency));
		  	props.setProperty("MinFrequency", ""+String.valueOf(minFrequency));
		  	props.setProperty("NumFrequencies", ""+String.valueOf(numFrequencies));
		  	props.setProperty("Jitter", ""+String.valueOf(jitter));
		  	props.setProperty("TriggerIsSelected", ""+String.valueOf(triggerIsSelected));
		  	props.setProperty("LeftCornerX", ""+String.valueOf(leftCornerX));
		  	props.setProperty("LeftCornerY", ""+String.valueOf(leftCornerY));
		  	props.setProperty("TriggerWidth", ""+String.valueOf(triggerWidth));
		  	props.setProperty("TriggerHeight", ""+String.valueOf(triggerHeight));
		  	props.setProperty("IsProbeTaskTimed", ""+String.valueOf(isProbeTaskTimed));
		  	props.setProperty("FontSize", ""+String.valueOf(fontSize));
		  	props.setProperty("FontFamily", ""+fontFamily);

  	        OutputStream out = new FileOutputStream( f );
  	        props.store(out, "Properties for the Auditory Task program");
        }
      
        is = new FileInputStream( f );
	  
	    // Try loading properties from the file (if found)
        props.load( is );
	    
	    
	    stimulusDuration=new Integer(props.getProperty("StimulusDuration", String.valueOf(Parameters.STIMULUS_TASK_DURATION)));
	    preStimulusDuration=new Integer(props.getProperty("PreStimulusDuration", String.valueOf(Parameters.PRESTIMULUS_TASK_DURATION)));
	    intertrialDuration=new Integer(props.getProperty("IntertrialDuration", String.valueOf(Parameters.INTERTRIAL_TASK_DURATION)));
	    delayDuration=new Integer(props.getProperty("DelayDuration", String.valueOf(Parameters.DELAY_TASK_DURATION)));
	    probeDuration=new Integer(props.getProperty("ProbeDuration", String.valueOf(Parameters.PROBE_TASK_DURATION_IF_IDLE)));
	    numberOfBetsPerTrial=new Integer(props.getProperty("NumberOfBetsPerTrial", String.valueOf(Parameters.NUMBER_OF_BETS_PER_TRIAL)));
	    maxPointsPerBet=new Integer(props.getProperty("MaxPointsPerBet", String.valueOf(Parameters.MAX_POINTS_PER_BET)));
	    numTrials=new Integer(props.getProperty("NumTrials", String.valueOf(Parameters.NUM_EXPERIMENTAL_TRIALS)));
	    numRuns=new Integer(props.getProperty("NumRuns", String.valueOf(Parameters.RUN_NUM_DEFAULT)));
	    studyId = props.getProperty("StudyId",  String.valueOf(Parameters.STUDY_ID_DEFAULT));
	    studyVer = new Integer(props.getProperty("StudyVer",  String.valueOf(Parameters.STUDY_VER_DEFAULT)));
	    numOfTrialsWithOneBet=new Integer(props.getProperty("NumOfTrialsWithOneBet", String.valueOf(Parameters.TRIALS_ONE_BET_NUM_DEFAULT)));
	    numOfDemoTrialsNotTimed=new Integer(props.getProperty("NumOfDemoTrialsNotTimed", String.valueOf(Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT)));
	    numOfDemoTrialsTimed=new Integer(props.getProperty("NumOfDemoTrialsTimed", String.valueOf(Parameters.TRIALS_TIMED_NUM_DEFAULT)));
	    numOfPracticeBetTrials=new Integer(props.getProperty("NumOfPracticeBetTrials", String.valueOf(Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT)));
	    maxFrequency=new Float(props.getProperty("MaxFrequency", String.valueOf(Parameters.MAX_FREQ_DEFAULT)));
	    minFrequency=new Float(props.getProperty("MinFrequency", String.valueOf(Parameters.MIN_FREQ_DEFAULT)));
	    numFrequencies=new Integer(props.getProperty("NumFrequencies", String.valueOf(Parameters.SAMPLE_FREQ_NUM_DEFAULT)));
	    jitter=new Float(props.getProperty("Jitter", String.valueOf(Parameters.JITTER_DEFAULT)));
	    triggerIsSelected=new Boolean(props.getProperty("TriggerIsSelected", String.valueOf(Parameters.IS_TRIGGER_SELECTED)));
	    leftCornerX=new Integer(props.getProperty("LeftCornerX", String.valueOf(Parameters.TRIGGER_LEFT_CORNER_X)));
	    leftCornerY=new Integer(props.getProperty("LeftCornerY", String.valueOf(Main.screenHeight - Parameters.TRIGGER_HEIGHT)));
	    triggerWidth=new Integer(props.getProperty("TriggerWidth", String.valueOf(Parameters.TRIGGER_WIDTH)));
	    triggerHeight=new Integer(props.getProperty("TriggerHeight", String.valueOf(Parameters.TRIGGER_HEIGHT)));
	    isProbeTaskTimed=new Boolean(props.getProperty("IsProbeTaskTimed", String.valueOf(Parameters.IS_PROBE_TASK_TIMED)));  
	    fontSize=new Integer(props.getProperty("FontSize", String.valueOf(Parameters.FONT_SIZE)));  
	    fontFamily=new String(props.getProperty("FontFamily", String.valueOf(Parameters.FONT_FAMILY)));
	    triggerUpperLeftIsSelected = new Boolean(props.getProperty("TriggerUpperLeftIsSelected", String.valueOf(false)));  
	    triggerLowerLeftIsSelected = new Boolean(props.getProperty("TriggerLowerLeftIsSelected", String.valueOf(true)));  
	    triggerUpperRightIsSelected = new Boolean(props.getProperty("TriggerUpperRightIsSelected", String.valueOf(false)));  
	    triggerLowerRightIsSelected = new Boolean(props.getProperty("TriggerLowerRightIsSelected", String.valueOf(false)));  
	    triggerOtherIsSelected = new Boolean(props.getProperty("TriggerOtherIsSelected", String.valueOf(false))); 
	}
	
	 /**
     * converting frequency into mouse x coordinate 
     * @param frequency
     * @return mouse x coordinate
     */
     static double  getCursoreHorizFromFreq(double frequency, Dimension panelSize){
    	 
    	 	
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
        double convFactor = (double)(sliderMaxVal-sliderMinVal)/(panelSize.width-2*PracticeProbePanel.xPixToTrim);
        
        //we trim 50 pixels from each end of the screen
        double x = (frequency-sliderMinVal)/convFactor+PracticeProbePanel.xPixToTrim;
        return x;
    }
    
    
    /**
     * return pdf(x) = standard Gaussian pdf
  	 * credit to:
  	 * http://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
     * @param x
     * @return pdf(x) = standard Gaussian pdf
     */
  	 static double pdf(double x) {
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
  	 static double gaussianPdf(double x, double mu, double sigma) {
  	        return pdf((x - mu) / sigma) / sigma;
  	 }
  	 
	
}//end of class
