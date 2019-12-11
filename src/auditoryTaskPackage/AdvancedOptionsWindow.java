package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import java.awt.event.ItemListener;
import java.awt.Point;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.SwingUtilities;

public class AdvancedOptionsWindow  extends JFrame {
	//declare variables
	private JPanel contentPane;
	public JComboBox fontsBox;
	public JPanel triggerPanel = new JPanel(new GridBagLayout());
    public JPanel experimentPanel = new JPanel(new GridBagLayout());
    public JPanel timeIntervalsPanel = new JPanel(new GridBagLayout());
    public JPanel checkerboxPanel =  new JPanel(new GridBagLayout());
    public JPanel triggerPositionPanel =  new JPanel(new GridBagLayout());
    public JPanel practicePanel = new JPanel(new GridBagLayout());
    public JPanel fontSizePanel = new JPanel(new GridBagLayout());
    public final static JCheckBox addTrigger = new JCheckBox("Photodiode trigger");
    
	private static final String REGEX_TEST = "\\d*";
	
	public static int stimulusDuration = Parameters.STIMULUS_TASK_DURATION;
	public static int preStimulusDuration = Parameters.PRESTIMULUS_TASK_DURATION;
	public static int intertrialDuration = Parameters.INTERTRIAL_TASK_DURATION;
	public static int delayDuration = Parameters.DELAY_TASK_DURATION;
	public static int probeDuration = Parameters.PROBE_TASK_DURATION_IF_IDLE;
	public static int numberOfBetsPerTrial = Parameters.NUMBER_OF_BETS_PER_TRIAL;
	public static int maxPointsPerBet = Parameters.MAX_POINTS_PER_BET;
	public static int numTrials = Parameters.NUM_EXPERIMENTAL_TRIALS;
    public static int numRuns = Parameters.RUN_NUM_DEFAULT;
    public static String studyId = Parameters.STUDY_ID_DEFAULT;
    public static int studyVer = Parameters.STUDY_VER_DEFAULT;
    public static int sessionNum = ExperimentSetupWindow.sessionNum;
    public static String subNum = ExperimentSetupWindow.subNum;;
    public static int numOfTrialsWithOneBet = Parameters.TRIALS_ONE_BET_NUM_DEFAULT;
    public static int numOfDemoTrialsNotTimed = Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT ;
	public static int numOfDemoTrialsTimed  = Parameters.TRIALS_TIMED_NUM_DEFAULT;
	public static int numOfPracticeBetTrials = Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT ;
    public static float maxFrequency = Parameters.MAX_FREQ_DEFAULT;
    public static float minFrequency = Parameters.MIN_FREQ_DEFAULT;
    public static int numFrequencies = Parameters.SAMPLE_FREQ_NUM_DEFAULT;
    public static float jitter = Parameters.JITTER_DEFAULT;
    public static int fontSize = Parameters.FONT_SIZE;
    public static String fontFamily = Parameters.FONT_FAMILY;
    
    public static boolean triggerUpperLeftIsSelected = false;
    public static boolean triggerLowerLeftIsSelected = true;
    public static boolean triggerLowerRightIsSelected = false;
    public static boolean triggerUpperRightIsSelected = false;
    public static boolean triggerOtherIsSelected = false;
    
    public static boolean triggerIsSelected = Parameters.IS_TRIGGER_SELECTED;
    public static boolean startPracticeVersion = true;
    public static String practiceLogFileName;
    public static String experimentLogFileName;
    
    
	public static int triggerWidth = Parameters.TRIGGER_WIDTH; //width of the trigger in pixels
	public static int triggerHeight = Parameters.TRIGGER_HEIGHT;//height of the trigger in pixels
	public static int leftCornerX = Parameters.TRIGGER_LEFT_CORNER_X; //coordinate of trigger left corner by default
	public static int leftCornerY =  Main.screenHeight - triggerHeight; //Parameters.TRIGGER_LEFT_CORNER_Y; //coordinate of trigger right corner
	
	public static int ADVANCED_OPTIONS_WIN_HEIGHT = 600;
	public static int ADVANCED_OPTIONS_WIN_WIDTH = 1300;

	public static boolean isProbeTaskTimed = true;
	public static boolean changesOccured=false;
	private static AdvancedOptionsWindow instance = null; //global var
	
	public JWindow errorWindow;
		 
	
	private AdvancedOptionsWindow(){
		loadParams();
		//set icon image
    	setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		//set frame title
		setTitle("Advanced Options");
		//set default close operation
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//set bounds of the frame
		setBounds(ExperimentSetupWindow.WIN_LEFT_X +20,
				ExperimentSetupWindow.WIN_LEFT_Y +20, 
				ADVANCED_OPTIONS_WIN_WIDTH+ExperimentSetupWindow.WIN_LEFT_X + ExperimentSetupWindow.WIN_WIDTH, 
				ADVANCED_OPTIONS_WIN_HEIGHT+ExperimentSetupWindow.WIN_LEFT_Y);
		
		setSize(ADVANCED_OPTIONS_WIN_WIDTH,ADVANCED_OPTIONS_WIN_HEIGHT);
		//create object of JPanel
		contentPane = new JPanel();
		//set border
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//set ContentPane
		setContentPane(contentPane);
		//set grid bag layout	
		contentPane.setLayout(new GridBagLayout());
		//prevent from resizing
		setResizable(true);
		
		//----------------------------------------------------------------------------------
	    JLabel stimTaskDurationLabel = new JLabel("Sample"); 
	    final JTextField stimTaskDurationText = new JTextField(String.valueOf(Parameters.STIMULUS_TASK_DURATION));
	    
	    //----------------------------------------------------------------------------------
	    JLabel delayTaskDurationLabel = new JLabel("Delay");   
	    final JTextField delayTaskDurationText = new JTextField(String.valueOf(Parameters.DELAY_TASK_DURATION));

	    //----------------------------------------------------------------------------------
	    JLabel intertrialTaskDurationLabel = new JLabel("Intertrial interval");    
	    final JTextField intertrialTaskDurationText = new JTextField(String.valueOf(Parameters.INTERTRIAL_TASK_DURATION)); 
	  
	    //----------------------------------------------------------------------------------
	    JLabel preStimTaskDurationLabel = new JLabel("Prestimulus fixation");    
	    final JTextField preStimTaskDurationText = new JTextField(String.valueOf(Parameters.PRESTIMULUS_TASK_DURATION));
   
	    //----------------------------------------------------------------------------------
	    JLabel probeTaskDurationLabel = new JLabel("Probe"); 
	    final JTextField probeTaskDurationText = new JTextField(String.valueOf(Parameters.PROBE_TASK_DURATION_IF_IDLE));

	    //----------------------------------------------------------------------------------
	    JLabel numBetsPerTrialLabel = new JLabel("Number of bets per trial"); 
	    final JTextField numBetsPerTrialText = new JTextField(String.valueOf(Parameters.NUMBER_OF_BETS_PER_TRIAL));

	    //----------------------------------------------------------------------------------
	    JLabel maxPointsPerBetLabel = new JLabel("Max points per bet");   
	    final JTextField maxPointsPerBetText = new JTextField(String.valueOf(Parameters.MAX_POINTS_PER_BET));
	    
	    //----------------------------------------------------------------------------------
	    JLabel studyIdLabel = new JLabel("Study ID");
	    final JTextField studyIdText = new JTextField(Parameters.STUDY_ID_DEFAULT);
	    	    
	    //----------------------------------------------------------------------------------
	    JLabel studyVersionLabel = new JLabel("Study version");
	    final JTextField studyVersionText = new JTextField(String.valueOf(Parameters.STUDY_VER_DEFAULT));
	    
	    //----------------------------------------------------------------------------------
	    JLabel numOfTrialsInExperimentLabel = new JLabel("Number of trials");
	    final JTextField numOfTrialsInExperimentText = new JTextField(String.valueOf(Parameters.NUM_EXPERIMENTAL_TRIALS));
	    
	    //----------------------------------------------------------------------------------
	    JLabel numOfRunsLabel = new JLabel("Number of runs");  
	    final JTextField numOfRunsText = new JTextField(String.valueOf(Parameters.RUN_NUM_DEFAULT));
	   
	    //----------------------------------------------------------------------------------
	    JLabel numOfTrialsWithOneBetLabel = new JLabel("Number of trials with one bet");
	    final JTextField numOfTrialsWithOneBetText = new JTextField(String.valueOf(Parameters.TRIALS_ONE_BET_NUM_DEFAULT));
	   
	    //----------------------------------------------------------------------------------
	    JLabel numOfDemoTrialsNotTimedLabel = new JLabel("Number of untimed trials"); 
	    final JTextField numOfDemoTrialsNotTimedText = new JTextField(String.valueOf(Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT));
	    
	    //----------------------------------------------------------------------------------
	    JLabel numOfDemoTrialsTimedLabel = new JLabel("Number of timed trials");
	    final JTextField numOfDemoTrialsTimedText = new JTextField(String.valueOf(Parameters.TRIALS_TIMED_NUM_DEFAULT));
	    
	    //----------------------------------------------------------------------------------
	    JLabel numOfPracticeBetTrialsLabel = new JLabel("Number of trials to practice bets");
	    final JTextField numOfPracticeBetTrialsText = new JTextField(String.valueOf(Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT));

	    //----------------------------------------------------------------------------------
	    JLabel setFontSizeLabel = new JLabel("Font size");
	    final JTextField setFontSizeText = new JTextField(String.valueOf(Parameters.FONT_SIZE));
	    
	    //----------------------------------------------------------------------------------
	    //define fontBox http://stackoverflow.com/questions/16461454/custom-font-for-jcombobox
	    JLabel fontsBoxLabel = new JLabel("Font family");
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilyNames = ge.getAvailableFontFamilyNames();
        
        fontsBox = new JComboBox(fontFamilyNames);
        int fontIdx = Arrays.asList(fontFamilyNames).indexOf(Parameters.FONT_FAMILY);
        if (fontIdx == -1)
        	fontIdx = 0;
        fontsBox.setSelectedItem(fontFamilyNames[fontIdx]);
        
        setNumberOfComboBlocks(numRuns);
        //fontsBox.setRenderer(new ComboRenderer(fontsBox));
//        fontsBox.addItemListener(new ItemListener() {
//
//
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				if (e.getStateChange() == ItemEvent.SELECTED) {
//                    final String fontName = fontsBox.getSelectedItem().toString();
//                    fontsBox.setFont(new Font(fontName, Font.PLAIN, 16));
//                }
//				
//			}
//        });
//        fontsBox.setSelectedItem(0);
        fontsBox.getEditor().selectAll();


        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                fontsBox.setPopupVisible(true);
                fontsBox.setPopupVisible(false);
            }
        });
	    
	    
	    
	    //----------------------------------------------------------------------------------
	    JLabel minFreqLabel = new JLabel("Min. frequency (Hz)");
	   
	    final JTextField minFreqText = new JTextField(String.valueOf(Parameters.MIN_FREQ_DEFAULT)){
	    	public JToolTip createToolTip() {
	            JToolTip tip = super.createToolTip();
	            tip.setBackground(Color.LIGHT_GRAY.brighter());
	            tip.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	            tip.setFont(new Font("Arial", Font.BOLD,12));
	            return tip;
	          }
	    };
	   
	    //----------------------------------------------------------------------------------
	    JLabel maxFreqLabel = new JLabel("Max. frequency (Hz)");
	   
	    final JTextField maxFreqText = new JTextField(String.valueOf(Parameters.MAX_FREQ_DEFAULT)){
	    	public JToolTip createToolTip() {
	            JToolTip tip = super.createToolTip();
	            //tip.setForeground(Color.BLACK);
	            tip.setBackground(Color.LIGHT_GRAY.brighter());
	            tip.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	            tip.setFont(new Font("Arial", Font.BOLD,12));
	            return tip;
	          }
	    };
	   
	    JLabel numFreqSample = new JLabel("Number of sample frequencies");
	    
	    final JTextField numFreqSampleText = new JTextField(String.valueOf(Parameters.SAMPLE_FREQ_NUM_DEFAULT));
	    
	    JLabel freqJitter = new JLabel("Jitter (p)");
	    
	    final JTextField freqJitterText = new JTextField(String.valueOf(Parameters.JITTER_DEFAULT));
	    
	   
	    //----------------------------------------------------------------------------------
	    
		addTrigger.setSelected(true);

		//----------------------------------------------------------------------------------
	    //declare buttons
	    //----------------------------------------------------------------------------------
	
  		
  		final JButton jbtReset = new JButton("Reset");
  		
  		JButton jbtPreview  = new JButton("Preview");
  		
  		final JButton jbtApply  = new JButton("Apply");

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cButtonPanel = new GridBagConstraints();
		cButtonPanel.fill = GridBagConstraints.HORIZONTAL;
		cButtonPanel.insets = new Insets(7,10,0,10); 
		cButtonPanel.anchor = GridBagConstraints.WEST;
		cButtonPanel.weightx = 1;	
		cButtonPanel.gridwidth=1;
		cButtonPanel.gridx = 0;
		cButtonPanel.gridy = 0;
		buttonPanel.add(jbtReset,cButtonPanel);
		cButtonPanel.gridx = 1;
		cButtonPanel.gridy = 0;
		buttonPanel.add(jbtApply,cButtonPanel);
		


	    //---------------------------------------------------------------------------------------------------//
	    //	trigger panel
	    
	    
	    JPanel p1 = new JPanel(new GridBagLayout());//Layout(2,2,55,8));
		JPanel p2 = new JPanel(new GridLayout(4,2,5,5));
		
		
		TitledBorder comp = new TitledBorder("Size of your screen in pixels: ");
		Border border = comp.getBorder();
		Border margin = new EmptyBorder(0,0,5,5);
		comp.setBorder(new CompoundBorder(border, margin));
		
		
		p1.setBorder(comp);
		
		
		final JLabel leftCornerXLabel = new JLabel("Left corner X");
		
		final JTextField leftCornerXText = new JTextField();
		leftCornerXText.setText(String.valueOf(leftCornerX));
		
		final JLabel leftCornerYLabel = new JLabel("Left corner Y");
		final JTextField leftCornerYText = new JTextField();
		leftCornerYText.setText(String.valueOf(leftCornerY));
		
		final JLabel triggerWidthLabel = new JLabel("Trigger width");
		final JTextField triggerWidthText = new JTextField();
		triggerWidthText.setText(String.valueOf(triggerWidth));
		
		final JLabel triggerHeightLabel = new JLabel("Trigger height");
		final JTextField triggerHeightText = new JTextField();
		triggerHeightText.setText(String.valueOf(triggerHeight));
		
		JLabel screenWidthLabel = new JLabel("Screen width: ");
		JLabel screenWidthValueLabel = new JLabel();
		screenWidthValueLabel.setText(String.format("%d px", Main.screenWidth));
		JLabel screenHeightLabel = new JLabel("Screen height: ");
		JLabel screenHeightValueLabel = new JLabel();
		screenHeightValueLabel.setText(String.format("%d px", Main.screenHeight));
		
		
		ButtonGroup triggerGroup = new ButtonGroup();
	    
	    final JRadioButtonMenuItem jrbTriggerUpperLeft = new JRadioButtonMenuItem("Upper left corner");
	    triggerGroup.add(jrbTriggerUpperLeft);
	    
	    final JRadioButtonMenuItem jrbTriggerLowerLeft = new JRadioButtonMenuItem("Lower left corner");
	    jrbTriggerLowerLeft.setSelected(true);
	    triggerGroup.add(jrbTriggerLowerLeft);

	    final JRadioButtonMenuItem jrbTriggerUpperRight = new JRadioButtonMenuItem("Upper right corner");
	    triggerGroup.add(jrbTriggerUpperRight);
	    
	    final JRadioButtonMenuItem jrbTriggerLowerRight = new JRadioButtonMenuItem("Lower right corner");
	    triggerGroup.add(jrbTriggerLowerRight);
	    
	    final JRadioButtonMenuItem jrbTriggerOther = new JRadioButtonMenuItem("Other");
	    triggerGroup.add(jrbTriggerOther);
	    

	    JPanel  tempTriggerPositionPanel = new JPanel(new GridLayout(5,2,5,5));
	    tempTriggerPositionPanel.add(jrbTriggerUpperLeft);
	    tempTriggerPositionPanel.add(jrbTriggerLowerLeft);
	    tempTriggerPositionPanel.add(jrbTriggerUpperRight);
	    tempTriggerPositionPanel.add(jrbTriggerLowerRight);
	    tempTriggerPositionPanel.add(jrbTriggerOther);
	    
	    
	    GridBagConstraints cTriggerPositionPanel = new GridBagConstraints();
	    cTriggerPositionPanel.fill = GridBagConstraints.HORIZONTAL;
	    cTriggerPositionPanel.anchor = GridBagConstraints.WEST;
	    cTriggerPositionPanel.weightx = 1;
	    cTriggerPositionPanel.gridx = 0;
	    cTriggerPositionPanel.gridy = 0;
	    triggerPositionPanel.add(tempTriggerPositionPanel,cTriggerPositionPanel);
	    
	    TitledBorder triggerPositionPanelBorderComp = new TitledBorder("Specify trigger position");
		Border triggerPositionPanelBorder = triggerPositionPanelBorderComp.getBorder();
		Border triggerPositionPanelBorderMargin = new EmptyBorder(0,0,5,5);
		triggerPositionPanelBorderComp.setBorder(new CompoundBorder(triggerPositionPanelBorder, triggerPositionPanelBorderMargin));
		
		triggerPositionPanel.setBorder(triggerPositionPanelBorderComp);
	
	    
		
		
		
		GridBagConstraints cP1 = new GridBagConstraints();
		cP1.fill = GridBagConstraints.HORIZONTAL;
		cP1.insets = new Insets(6,0,3,0); 
		cP1.anchor = GridBagConstraints.WEST;
		cP1.weightx = 1;
		//c.gridwidth  = 2;
		cP1.gridx = 0;
		cP1.gridy = 0;
		p1.add(screenWidthLabel,cP1);
		cP1.gridx = 1;
		cP1.gridy = 0;
		p1.add(screenWidthValueLabel,cP1);
		cP1.gridx = 0;
		cP1.gridy = 1;
		p1.add(screenHeightLabel,cP1);
		cP1.gridx = 1;
		cP1.gridy = 1;
		p1.add(screenHeightValueLabel,cP1);
		
	
		p2.add(leftCornerXLabel);
		p2.add(leftCornerXText);
		p2.add(leftCornerYLabel);
		p2.add(leftCornerYText);
		p2.add(triggerWidthLabel);
		p2.add(triggerWidthText);
		p2.add(triggerHeightLabel);
		p2.add(triggerHeightText);
		
		
		
	
		
		GridBagConstraints cTriggerPanel = new GridBagConstraints();
		cTriggerPanel.fill = GridBagConstraints.HORIZONTAL;
		cTriggerPanel.insets = new Insets(6,10,6,10); 
		cTriggerPanel.anchor = GridBagConstraints.WEST;
		cTriggerPanel.weightx = 1;
		//c.gridwidth  = 2;
		cTriggerPanel.gridx = 0;
		cTriggerPanel.gridy = 0;
		triggerPanel.add(addTrigger,cTriggerPanel);
		cTriggerPanel.gridx = 0;
		cTriggerPanel.gridy = 1;
		triggerPanel.add(p1,cTriggerPanel);
		cTriggerPanel.gridx = 0;
		cTriggerPanel.gridy = 2;
		triggerPanel.add(p2,cTriggerPanel);
		cTriggerPanel.gridx = 0;
		cTriggerPanel.gridy = 3;
		triggerPanel.add(triggerPositionPanel,cTriggerPanel);
		cTriggerPanel.gridx = 0;
		cTriggerPanel.gridy = 4;
		triggerPanel.add(jbtPreview,cTriggerPanel);
		
		
		TitledBorder triggerPanelBorderComp = new TitledBorder("Trigger parameters ");
		Border triggerPanelBorder = triggerPanelBorderComp.getBorder();
		Border triggerBorderMargin = new EmptyBorder(0,0,5,5);
		triggerPanelBorderComp.setBorder(new CompoundBorder(triggerPanelBorder, triggerBorderMargin));
		
		triggerPanel.setBorder(triggerPanelBorderComp);
		
		//---------------------------------------------------------------------------------------------------//
	    //	experiment parameters panel
		
	    JPanel tempExpP = new JPanel(new GridLayout(10,2,5,8));
	    
		tempExpP.add(studyIdLabel);
		tempExpP.add(studyIdText);
		tempExpP.add(studyVersionLabel);
		tempExpP.add(studyVersionText);
		tempExpP.add(numOfTrialsInExperimentLabel);
		tempExpP.add(numOfTrialsInExperimentText);
		tempExpP.add(numOfRunsLabel);
		tempExpP.add(numOfRunsText);
		tempExpP.add(numFreqSample);
		tempExpP.add(numFreqSampleText);
		tempExpP.add(freqJitter);
		tempExpP.add(freqJitterText);
		tempExpP.add(numBetsPerTrialLabel);
		tempExpP.add(numBetsPerTrialText);
		tempExpP.add(maxPointsPerBetLabel);
		tempExpP.add(maxPointsPerBetText);
		tempExpP.add(minFreqLabel);
		tempExpP.add(minFreqText);
		tempExpP.add(maxFreqLabel);
		tempExpP.add(maxFreqText);
		
		
		
		
		GridBagConstraints cExperimentParametersPanel = new GridBagConstraints();
		cExperimentParametersPanel.fill = GridBagConstraints.HORIZONTAL;
		cExperimentParametersPanel.insets = new Insets(5,0,0,0); 
		cExperimentParametersPanel.anchor = GridBagConstraints.WEST;
		cExperimentParametersPanel.weightx = 1;
		//c.gridwidth  = 2;
		cExperimentParametersPanel.gridx = 0;
		cExperimentParametersPanel.gridy = 0;
		experimentPanel.add(tempExpP,cExperimentParametersPanel);
		
		
		TitledBorder expParamsPanelBorderComp = new TitledBorder("Experiment parameters ");
		Border expParamsPanelBorder = expParamsPanelBorderComp.getBorder();
		Border expParamsBorderMargin = new EmptyBorder(0,0,5,5);
		expParamsPanelBorderComp.setBorder(new CompoundBorder(expParamsPanelBorder, expParamsBorderMargin));
		
		experimentPanel.setBorder(expParamsPanelBorderComp);
		
		//---------------------------------------------------------------------------------------------------//
	    //	experiment parameters panel
		
	    JPanel tempIntervalsP = new JPanel(new GridLayout(5,2,5,5));
	    
	    tempIntervalsP.add(preStimTaskDurationLabel);
	    tempIntervalsP.add(preStimTaskDurationText);
	    tempIntervalsP.add(stimTaskDurationLabel);
	    tempIntervalsP.add(stimTaskDurationText);
	    tempIntervalsP.add(delayTaskDurationLabel);
	    tempIntervalsP.add(delayTaskDurationText);
	    tempIntervalsP.add(probeTaskDurationLabel);
	    tempIntervalsP.add(probeTaskDurationText);
	    tempIntervalsP.add(intertrialTaskDurationLabel);
	    tempIntervalsP.add(intertrialTaskDurationText);

		GridBagConstraints cTimeIntervalsPanel = new GridBagConstraints();
		cTimeIntervalsPanel.fill = GridBagConstraints.HORIZONTAL;
		cTimeIntervalsPanel.insets = new Insets(5,0,0,0); 
		cTimeIntervalsPanel.anchor = GridBagConstraints.WEST;
		cTimeIntervalsPanel.weightx = 1;
		//c.gridwidth  = 2;
		cTimeIntervalsPanel.gridx = 0;
		cTimeIntervalsPanel.gridy = 0;
		timeIntervalsPanel.add(tempIntervalsP,cTimeIntervalsPanel);
		
		
		TitledBorder timeIntervalsPanelBorderComp = new TitledBorder("Tasks duration parameters (msec) ");
		Border timeIntervalsPanelBorder = expParamsPanelBorderComp.getBorder();
		Border timeIntervalsBorderMargin = new EmptyBorder(0,0,5,5);
		timeIntervalsPanelBorderComp.setBorder(new CompoundBorder(timeIntervalsPanelBorder, timeIntervalsBorderMargin));
		
		timeIntervalsPanel.setBorder(timeIntervalsPanelBorderComp);
		
		//---------------------------------------------------------------------------------------------------//
	    //	font size parameters panel
		
	    JPanel setFontSizeP = new JPanel(new GridLayout(2,2,5,5));
	    
	    setFontSizeP.add(setFontSizeLabel);
	    setFontSizeP.add(setFontSizeText);
	    setFontSizeP.add(fontsBoxLabel);
	    setFontSizeP.add(fontsBox);
	    
		
		
		
		GridBagConstraints cSetFontSizePanel = new GridBagConstraints();
		cSetFontSizePanel.fill = GridBagConstraints.HORIZONTAL;
		cSetFontSizePanel.insets = new Insets(5,0,0,0); 
		cSetFontSizePanel.anchor = GridBagConstraints.WEST;
		cSetFontSizePanel.weightx = 1;
		//c.gridwidth  = 2;
		cSetFontSizePanel.gridx = 0;
		cSetFontSizePanel.gridy = 0;
		fontSizePanel.add(setFontSizeP,cSetFontSizePanel);
		
	
		TitledBorder cSetFontSizePanelBorderComp = new TitledBorder("Specify the font of the screen");
		Border cSetFontSizePanelBorder = expParamsPanelBorderComp.getBorder();
		Border cSetFontSizePanelBorderMargin = new EmptyBorder(0,0,5,5);
		cSetFontSizePanelBorderComp.setBorder(new CompoundBorder(cSetFontSizePanelBorder, cSetFontSizePanelBorderMargin));
		
		fontSizePanel.setBorder(cSetFontSizePanelBorderComp);
		
		//---------------------------------------------------------------------------------------------------//
	    //	practice parameters panel
		JPanel tempPractP = new JPanel(new GridLayout(4,2,3,5));

		tempPractP.add(numOfTrialsWithOneBetLabel);
		tempPractP.add(numOfTrialsWithOneBetText);
		tempPractP.add(numOfPracticeBetTrialsLabel);
		tempPractP.add(numOfPracticeBetTrialsText);
		tempPractP.add(numOfDemoTrialsNotTimedLabel);
		tempPractP.add(numOfDemoTrialsNotTimedText);
		tempPractP.add(numOfDemoTrialsTimedLabel);
		tempPractP.add(numOfDemoTrialsTimedText);


		GridBagConstraints cPracticeParametersPanel = new GridBagConstraints();
		cPracticeParametersPanel.fill = GridBagConstraints.HORIZONTAL;
		//cPracticeParametersPanel.insets = new Insets(15,10,0,10); 
		cPracticeParametersPanel.anchor = GridBagConstraints.WEST;
		cPracticeParametersPanel.weightx = 1;
		//c.gridwidth  = 2;
		cPracticeParametersPanel.gridx = 0;
		cPracticeParametersPanel.gridy = 0;
		practicePanel.add(tempPractP,cExperimentParametersPanel);
		
		
		TitledBorder practParamsPanelBorderComp = new TitledBorder("Instruction/Practice session parameters");
		Border practParamsPanelBorder = practParamsPanelBorderComp.getBorder();
		Border practParamsBorderMargin = new EmptyBorder(0,0,5,5);
		practParamsPanelBorderComp.setBorder(new CompoundBorder(practParamsPanelBorder, practParamsBorderMargin));
		
		practicePanel.setBorder(practParamsPanelBorderComp);
	
		//define checkboxes
	    //----------------------------------------------------------------------------------
	    ButtonGroup group = new ButtonGroup();
	    
	    final JRadioButtonMenuItem jrbProbeTaskTimed = new JRadioButtonMenuItem("Timed probe task");
	    jrbProbeTaskTimed.setSelected(true);
	    jrbProbeTaskTimed.setEnabled(false);
	    group.add(jrbProbeTaskTimed);
	    
	    final JRadioButtonMenuItem jrbProbeTaskSelfPaced = new JRadioButtonMenuItem("Self-paced probe task");
	    group.add(jrbProbeTaskSelfPaced);

	    JPanel  tempCheckerboxPanel = new JPanel(new GridLayout(2,2,5,5));
	    tempCheckerboxPanel.add(jrbProbeTaskTimed);
	    tempCheckerboxPanel.add(jrbProbeTaskSelfPaced);
	    
	    GridBagConstraints cCheckerboxPanel = new GridBagConstraints();
	    cCheckerboxPanel.fill = GridBagConstraints.HORIZONTAL;
	    cCheckerboxPanel.anchor = GridBagConstraints.WEST;
	    cCheckerboxPanel.weightx = 1;
	    cCheckerboxPanel.gridx = 0;
	    cCheckerboxPanel.gridy = 0;
	    checkerboxPanel.add(tempCheckerboxPanel,cCheckerboxPanel);
	    
	    TitledBorder checkerboxPanelBorderComp = new TitledBorder("Set timed or untimed response");
		Border checkerboxPanelBorder = checkerboxPanelBorderComp.getBorder();
		Border checkerboxPanelBorderMargin = new EmptyBorder(0,0,5,5);
		checkerboxPanelBorderComp.setBorder(new CompoundBorder(checkerboxPanelBorder, checkerboxPanelBorderMargin));
		
		checkerboxPanel.setBorder(checkerboxPanelBorderComp);
	
	    
	    
		//---------------------------------------------------------------------------------------------------//
		
		JPanel tempPanel = new JPanel(new GridLayout(2,1,5,5));
		tempPanel.add(timeIntervalsPanel);
		tempPanel.add(practicePanel);
		
		
		//place all the panels on the GridBagPanel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,5,0,5); 
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		
		c.gridwidth = 1;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(experimentPanel,c);
		
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		contentPane.add(tempPanel,c);
			
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		contentPane.add(fontSizePanel,c);
		
		c.gridwidth = 1;
		c.gridheight = 3;
		c.gridx = 2;
		c.gridy = 0;
		contentPane.add(triggerPanel,c);

		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy =1;
		contentPane.add(checkerboxPanel,c);

		//c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 2;
		contentPane.add(buttonPanel,c);

		
		
	    //action listener for text fields
	    //----------------------------------------------------------------------------------
		((PlainDocument)stimTaskDurationText.getDocument()).setDocumentFilter(new MyNumberDocFilter(stimTaskDurationText, "Enter a number"));
		
		((PlainDocument)delayTaskDurationText.getDocument()).setDocumentFilter(new MyNumberDocFilter(delayTaskDurationText, "Enter a number"));
		
		((PlainDocument)intertrialTaskDurationText.getDocument()).setDocumentFilter(new MyNumberDocFilter(intertrialTaskDurationText, "Enter a number"));
		
		((PlainDocument)preStimTaskDurationText.getDocument()).setDocumentFilter(new MyNumberDocFilter(preStimTaskDurationText, "Enter a number"));
		
		((PlainDocument)probeTaskDurationText.getDocument()).setDocumentFilter(new MyNumberDocFilter(probeTaskDurationText, "Enter a number"));
		
		((PlainDocument)numBetsPerTrialText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numBetsPerTrialText, "Enter a number"));
		
		((PlainDocument)maxPointsPerBetText.getDocument()).setDocumentFilter(new MyNumberDocFilter(maxPointsPerBetText, "Enter a number"));
		
		((PlainDocument)numOfRunsText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfRunsText, "Enter a number"));
  		
		((PlainDocument)numOfTrialsInExperimentText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfTrialsInExperimentText, "Enter a number"));
  		
  		((PlainDocument)numOfTrialsWithOneBetText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfTrialsWithOneBetText, "Enter a number"));
  		
  		((PlainDocument)numOfDemoTrialsNotTimedText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfDemoTrialsNotTimedText, "Enter a number"));
  		
  		((PlainDocument)numOfDemoTrialsTimedText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfDemoTrialsTimedText, "Enter a number"));
  		
  		((PlainDocument)numOfPracticeBetTrialsText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfPracticeBetTrialsText, "Enter a number"));
  		
  		((PlainDocument)minFreqText.getDocument()).setDocumentFilter(new MyNumberDocFilter(minFreqText, "Enter a number"));
  		 minFreqText.setToolTipText(String.format("Select min frequency between %d and %d Hz", Parameters.MIN_FREQ_DEFAULT, Parameters.MAX_FREQ_DEFAULT));
  		 
  		((PlainDocument)maxFreqText.getDocument()).setDocumentFilter(new MyNumberDocFilter(maxFreqText, "Enter a number"));
 		 maxFreqText.setToolTipText(String.format("Select min frequency between %d and %d Hz", Parameters.MIN_FREQ_DEFAULT, Parameters.MAX_FREQ_DEFAULT));
  		 
 		((PlainDocument)freqJitterText.getDocument()).setDocumentFilter(new MyNumberDocFilter(freqJitterText, "Enter a number"));
 	    
		((PlainDocument)studyVersionText.getDocument()).setDocumentFilter(new MyNumberDocFilter(studyVersionText, "Enter a number"));

  		((PlainDocument)leftCornerXText.getDocument()).setDocumentFilter(new MyNumberDocFilter(leftCornerXText, "Enter a number"));
  		
  		((PlainDocument)leftCornerYText.getDocument()).setDocumentFilter(new MyNumberDocFilter(leftCornerYText, "Enter a number"));

  		((PlainDocument)triggerWidthText.getDocument()).setDocumentFilter(new MyNumberDocFilter(triggerWidthText, "Enter a number"));
  		
  		((PlainDocument)triggerHeightText.getDocument()).setDocumentFilter(new MyNumberDocFilter(triggerHeightText, "Enter a number"));
  		
  		((PlainDocument)setFontSizeText.getDocument()).setDocumentFilter(new MyNumberDocFilter(setFontSizeText, "Enter a number"));
  		
		//set radiobuttons listeners
	    //----------------------------------------------------------------------------------
		jrbProbeTaskTimed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(jrbProbeTaskTimed.isSelected()){
								jrbProbeTaskTimed.setEnabled(false);
								jrbProbeTaskSelfPaced.setEnabled(true);
								probeTaskDurationText.setEditable(true);
								isProbeTaskTimed = true;
							}
							else{
								isProbeTaskTimed = false;
							}
							
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
	    });
	    
	    
	  
	    
		jrbProbeTaskSelfPaced.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(jrbProbeTaskSelfPaced.isSelected()){
								jrbProbeTaskTimed.setEnabled(true);
								jrbProbeTaskSelfPaced.setEnabled(false);
								probeTaskDurationText.setEditable(false);
								isProbeTaskTimed = false;
							}
							else{
								isProbeTaskTimed = true;
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
	    });
	
		 class jrbTriggerUpperLeftActionListener implements ActionListener{

			 public void actionPerformed(ActionEvent arg0) {
					/* It posts an event (Runnable)at the end of Swings event list and is
					processed after all other GUI events are processed.*/
					
					
					EventQueue.invokeLater(new Runnable()
					{
						public void run()
						{
							//try - catch block
							try 
							{
								if(jrbTriggerUpperLeft.isSelected()){
									triggerUpperLeftIsSelected = true;
									triggerUpperRightIsSelected = false;
									triggerLowerLeftIsSelected = false;
									triggerLowerRightIsSelected = false;
									triggerOtherIsSelected = false;
									jrbTriggerUpperLeft.setEnabled(false);
									jrbTriggerUpperRight.setEnabled(true);
									jrbTriggerLowerRight.setEnabled(true);
									jrbTriggerLowerLeft.setEnabled(true);
									jrbTriggerOther.setEnabled(true);
									
									leftCornerX = 0;
									leftCornerY = 0;
									
									
									
									String triggerWidthTextString = triggerWidthText.getText();
									String triggerHeightTextString = triggerHeightText.getText();
									
									
									String message;
									if(triggerWidthTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										triggerWidthText.setBackground(Color.YELLOW);
									}
									else{
										triggerWidthText.setBackground(Color.WHITE);
										triggerWidth =Integer.parseInt(triggerWidthTextString);
										triggerWidthText.setText(String.valueOf(triggerWidth));
									}
									if(triggerHeightTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										triggerHeightText.setBackground(Color.YELLOW);
									}
									else{
										triggerHeightText.setBackground(Color.WHITE);
										triggerHeight =Integer.parseInt(triggerHeightTextString);
										triggerHeightText.setText(String.valueOf(triggerHeight));
									}
									
									leftCornerXText.setText(String.valueOf(leftCornerX));
									leftCornerYText.setText(String.valueOf(leftCornerY));
					
								
									leftCornerXText.setEditable(false);
									leftCornerYText.setEditable(false);
									
								}	
							} 
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
		    };
		
		class jrbTriggerLowerLeftActionListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(jrbTriggerLowerLeft.isSelected()){
								triggerUpperLeftIsSelected = false;
								triggerUpperRightIsSelected = false;
								triggerLowerLeftIsSelected = true;
								triggerLowerRightIsSelected = false;
								triggerOtherIsSelected = false;
								
								jrbTriggerLowerLeft.setEnabled(false);
								jrbTriggerUpperRight.setEnabled(true);
								jrbTriggerLowerRight.setEnabled(true);
								jrbTriggerUpperLeft.setEnabled(true);
								jrbTriggerOther.setEnabled(true);
								
								
								String triggerWidthTextString = triggerWidthText.getText();
								String triggerHeightTextString = triggerHeightText.getText();
								
								
								String message;
								if(triggerWidthTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerWidthText.setBackground(Color.YELLOW);
								}
								else{
									triggerWidthText.setBackground(Color.WHITE);
									triggerWidth =Integer.parseInt(triggerWidthTextString);
									triggerWidthText.setText(String.valueOf(triggerWidth));
								}
								if(triggerHeightTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerHeightText.setBackground(Color.YELLOW);
								}
								else{
									triggerHeightText.setBackground(Color.WHITE);
									triggerHeight =Integer.parseInt(triggerHeightTextString);
									triggerHeightText.setText(String.valueOf(triggerHeight));
								}
								
								leftCornerX = 0;
								leftCornerY =  Main.screenHeight - triggerHeight;
								
								
								leftCornerXText.setText(String.valueOf(leftCornerX));
								leftCornerYText.setText(String.valueOf(leftCornerY));
				
								
								
								leftCornerXText.setEditable(false);
								leftCornerYText.setEditable(false);
								
							}	
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		 };
		 
		class jrbTriggerUpperRightActionListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(jrbTriggerUpperRight.isSelected()){
								
								triggerUpperLeftIsSelected = false;
								triggerUpperRightIsSelected = true;
								triggerLowerLeftIsSelected = false;
								triggerLowerRightIsSelected = false;
								triggerOtherIsSelected = false;
								
								jrbTriggerLowerLeft.setEnabled(true);
								jrbTriggerUpperRight.setEnabled(false);
								jrbTriggerLowerRight.setEnabled(true);
								jrbTriggerUpperLeft.setEnabled(true);
								jrbTriggerOther.setEnabled(true);
								
								
								
								String triggerWidthTextString = triggerWidthText.getText();
								String triggerHeightTextString = triggerHeightText.getText();
								
								
								String message;
								if(triggerWidthTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerWidthText.setBackground(Color.YELLOW);
								}
								else{
									triggerWidthText.setBackground(Color.WHITE);
									triggerWidth =Integer.parseInt(triggerWidthTextString);
									triggerWidthText.setText(String.valueOf(triggerWidth));
								}
								if(triggerHeightTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerHeightText.setBackground(Color.YELLOW);
								}
								else{
									triggerHeightText.setBackground(Color.WHITE);
									triggerHeight =Integer.parseInt(triggerHeightTextString);
									triggerHeightText.setText(String.valueOf(triggerHeight));
								}
								
								leftCornerX = Main.screenWidth - triggerWidth;
								leftCornerY =  0;
								
								
								leftCornerXText.setText(String.valueOf(leftCornerX));
								
								leftCornerYText.setText(String.valueOf(leftCornerY));
				
								leftCornerXText.setEditable(false);
								leftCornerYText.setEditable(false);
								
							}	
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		 };
		
		class jrbTriggerLowerRightActionListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(jrbTriggerLowerRight.isSelected()){
								
								triggerUpperLeftIsSelected = false;
								triggerUpperRightIsSelected = false;
								triggerLowerLeftIsSelected = false;
								triggerLowerRightIsSelected = true;
								triggerOtherIsSelected = false;
								
								jrbTriggerLowerLeft.setEnabled(true);
								jrbTriggerUpperRight.setEnabled(true);
								jrbTriggerLowerRight.setEnabled(false);
								jrbTriggerUpperLeft.setEnabled(true);
								jrbTriggerOther.setEnabled(true);
								
								
								
								
								String triggerWidthTextString = triggerWidthText.getText();
								String triggerHeightTextString = triggerHeightText.getText();
								
								
								String message;
								if(triggerWidthTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerWidthText.setBackground(Color.YELLOW);
								}
								else{
									triggerWidthText.setBackground(Color.WHITE);
									triggerWidth =Integer.parseInt(triggerWidthTextString);
									triggerWidthText.setText(String.valueOf(triggerWidth));
								}
								if(triggerHeightTextString.isEmpty() && triggerIsSelected){
									message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
									JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
									triggerHeightText.setBackground(Color.YELLOW);
								}
								else{
									triggerHeightText.setBackground(Color.WHITE);
									triggerHeight =Integer.parseInt(triggerHeightTextString);
									triggerHeightText.setText(String.valueOf(triggerHeight));
								}
								
								leftCornerX = Main.screenWidth - triggerWidth;
								leftCornerY =  Main.screenHeight - triggerHeight;
								
								
								leftCornerXText.setText(String.valueOf(leftCornerX));
								
								leftCornerYText.setText(String.valueOf(leftCornerY));
				
								
								leftCornerXText.setEditable(false);
								leftCornerYText.setEditable(false);
								
							}	
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		 };
		
		 class jrbTriggerOtherActionListener implements ActionListener{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					/* It posts an event (Runnable)at the end of Swings event list and is
					processed after all other GUI events are processed.*/
					
					
					EventQueue.invokeLater(new Runnable()
					{
						public void run()
						{
							//try - catch block
							try 
							{
								if(jrbTriggerOther.isSelected()){
									triggerUpperLeftIsSelected = false;
									triggerUpperRightIsSelected = false;
									triggerLowerLeftIsSelected = false;
									triggerLowerRightIsSelected = false;
									triggerOtherIsSelected = true;
									
									jrbTriggerLowerLeft.setEnabled(true);
									jrbTriggerUpperRight.setEnabled(true);
									jrbTriggerLowerRight.setEnabled(true);
									jrbTriggerUpperLeft.setEnabled(true);
									jrbTriggerOther.setEnabled(false);
									
									leftCornerX = Parameters.TRIGGER_LEFT_CORNER_X;
									leftCornerY =  Parameters.TRIGGER_LEFT_CORNER_Y;
									leftCornerXText.setText(String.valueOf(leftCornerX));
									leftCornerYText.setText(String.valueOf(leftCornerY));
									
									String triggerWidthTextString = triggerWidthText.getText();
									String triggerHeightTextString = triggerHeightText.getText();
									String leftCornerXTextString = leftCornerXText.getText();
									String leftCornerYTextString = leftCornerYText.getText();
									
									String message;
									if(leftCornerXTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's left corner X coordinate to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										leftCornerXText.setBackground(Color.YELLOW);
									}
									else{
										leftCornerXText.setBackground(Color.WHITE);
										leftCornerX =Integer.parseInt(leftCornerXTextString);
										leftCornerXText.setText(String.valueOf(leftCornerX));
									}
									if(leftCornerYTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's left corner Y coordinate to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										leftCornerYText.setBackground(Color.YELLOW);
									}
									else{
										leftCornerYText.setBackground(Color.WHITE);
										leftCornerY =Integer.parseInt(leftCornerYTextString);
										leftCornerYText.setText(String.valueOf(leftCornerY));
									}
									if(triggerWidthTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										triggerWidthText.setBackground(Color.YELLOW);
									}
									else{
										triggerWidthText.setBackground(Color.WHITE);
										triggerWidth =Integer.parseInt(triggerWidthTextString);
										triggerWidthText.setText(String.valueOf(triggerWidth));
									}
									if(triggerHeightTextString.isEmpty() && triggerIsSelected){
										message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
										JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
										triggerHeightText.setBackground(Color.YELLOW);
									}
									else{
										triggerHeightText.setBackground(Color.WHITE);
										triggerHeight =Integer.parseInt(triggerHeightTextString);
										triggerHeightText.setText(String.valueOf(triggerHeight));
									}
									
									
									leftCornerXText.setText(String.valueOf(leftCornerX));
									
									leftCornerYText.setText(String.valueOf(leftCornerY));
					
									triggerWidthText.setText(String.valueOf(triggerWidth));
									
									triggerHeightText.setText(String.valueOf( triggerHeight));
									
									
									leftCornerXText.setEditable(true);
									leftCornerYText.setEditable(true);
									triggerWidthText.setEditable(true);
									triggerHeightText.setEditable(true);
								}	
							} 
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
			 };
			
		
		jrbTriggerUpperLeft.addActionListener(new jrbTriggerUpperLeftActionListener());
		
		jrbTriggerLowerLeft.addActionListener(new jrbTriggerLowerLeftActionListener());
		
		jrbTriggerLowerRight.addActionListener(new jrbTriggerLowerRightActionListener());
		
		jrbTriggerUpperRight.addActionListener(new jrbTriggerUpperRightActionListener());
		
		jrbTriggerOther.addActionListener(new jrbTriggerOtherActionListener());
		
		//for initializing in constructor:
	    jrbTriggerLowerLeft.doClick();
		//---------------------------------------------------------------------------------------------------//
		
  		
		
 		//action listener for numOfRunsText
  	    //----------------------------------------------------------------------------------
   		((PlainDocument)numOfRunsText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numOfRunsText, "Enter a number"));
   		numOfRunsText.getDocument().addDocumentListener(new DocumentListener() {
      	  public void changedUpdate(DocumentEvent e) {
      		    warn();
      		  }
      		  public void removeUpdate(DocumentEvent e) {
      		  }
      		  public void insertUpdate(DocumentEvent e) { 
     			warn(); 
      		  }
  
      		  public void warn() {
      			 SwingUtilities.invokeLater(new Runnable() {
                      @Override
                      public void run() {
                     	 String runNumTextString = numOfRunsText.getText();
             			 String numOfTrialsTextString = numOfTrialsInExperimentText.getText();
     	  
     	    		     if(runNumTextString.isEmpty()|| numOfTrialsTextString.isEmpty()){
     	    		    	 if(runNumTextString.isEmpty()){
      	    					 runNumTextString ="0";
      	    					 numOfRunsText.setText("");
      	    	     				 //JOptionPane.showMessageDialog(null, "Jitter is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
         	     			 	}
         	    			  
         	    			  if(numOfTrialsTextString.isEmpty()){
         	    				 numOfTrialsTextString ="0";
         	    				 numOfTrialsInExperimentText.setText("");
         	    				  //JOptionPane.showMessageDialog(null, "Number of Sample Frequencies is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
         	     			 	}
     	    		     }
     	    		     else{
     	 	    			  numTrials = Integer.parseInt(numOfTrialsTextString);
     	 	    		      numRuns = Integer.parseInt(runNumTextString);
     	 	    		      if(numTrials % numRuns !=0){
     	 	    		    	  String message = String.format("<html><center> Error: Number of runs must be multiple of number of trials.<br>");
     	 	    		    	  JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
     	 	    		    	  numOfRunsText.setBackground(Color.YELLOW);
     	 	    		      }
     	 	    		      else{
     	 	    		    	  numOfRunsText.setBackground(Color.WHITE);
     	 	    		      }
     	    		     } 
                      }
                  });
      		  }//end of warn()
      	});
 		 
 		 
  		//action listener for numFreqSampleText
 	    //----------------------------------------------------------------------------------
  		((PlainDocument)numFreqSampleText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numFreqSampleText, "Enter a number"));
  		numFreqSampleText.getDocument().addDocumentListener(new DocumentListener() {
  			public void changedUpdate(DocumentEvent e) {
     		    warn();
     		  }
     		  public void removeUpdate(DocumentEvent e) {
     		  }
     		  public void insertUpdate(DocumentEvent e) { 
    			warn(); 	
     		  }
 
     		  public void warn() {
     			 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                    	 String numOfRunsTextString = numOfRunsText.getText();
                    	 String numOfTrialsWithOneBetTextString = numOfTrialsWithOneBetText.getText();
                    	 String numOfDemoTrialsNotTimedTextString = numOfDemoTrialsNotTimedText.getText();
                    	 String numOfDemoTrialsTimedTextString = numOfDemoTrialsTimedText.getText();
                    	 String numOfPracticeBetTrialsTextString = numOfPracticeBetTrialsText.getText();
                    	 String numFreqSampleTextString = numFreqSampleText.getText();
                    	 String numOfTrialsInExperimentTextString = numOfTrialsInExperimentText.getText();
                    	 
    	    		     if(numOfRunsTextString.isEmpty()||numOfTrialsInExperimentTextString.isEmpty()|| numOfTrialsWithOneBetTextString.isEmpty()|| numFreqSampleTextString.isEmpty()||numOfDemoTrialsNotTimedTextString.isEmpty()||numOfDemoTrialsTimedTextString.isEmpty() || numFreqSampleTextString.isEmpty()  ){
    	    		    	 if(numOfTrialsInExperimentTextString.isEmpty()){
    	    		    		 numOfTrialsInExperimentTextString ="0";
    	    		    		 numOfTrialsInExperimentText.setText("");
     	    	     				 //JOptionPane.showMessageDialog(null, "Jitter is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
        	     			 	}
    	    		    	 if(numOfRunsTextString.isEmpty()){
    	    		    		 numOfRunsTextString ="0";
    	    		    		 numOfRunsText.setText("");
     	    	     				 //JOptionPane.showMessageDialog(null, "Jitter is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
        	     			 	}
    	    		    	 if(numOfTrialsWithOneBetTextString.isEmpty()){
    	    		    		 numOfTrialsWithOneBetTextString ="0";
     	    					 numOfTrialsWithOneBetText.setText("");
     	    	     				 //JOptionPane.showMessageDialog(null, "Jitter is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
        	     			 	}
        	    			  
        	    			  if(numFreqSampleTextString.isEmpty()){
        	    				  numFreqSampleTextString ="0";
        	    				  numFreqSampleText.setText("");
        	    				  //JOptionPane.showMessageDialog(null, "Number of Sample Frequencies is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
        	     			 	}
        	    			  if(numOfDemoTrialsNotTimedTextString.isEmpty()){
        	    				  numOfDemoTrialsNotTimedTextString ="0";
        	    				  numOfDemoTrialsNotTimedText.setText("");
         	    				  //JOptionPane.showMessageDialog(null, "Number of Sample Frequencies is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
         	     			 	}
        	    			  if(numOfDemoTrialsTimedTextString.isEmpty()){
        	    				  numOfDemoTrialsTimedTextString ="0";
        	    				  numOfDemoTrialsTimedText.setText("");
         	    				  //JOptionPane.showMessageDialog(null, "Number of Sample Frequencies is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
         	     			 	}
        	    			  if(numOfPracticeBetTrialsTextString.isEmpty()){
        	    				  numOfPracticeBetTrialsTextString ="0";
        	    				  numOfPracticeBetTrialsText.setText("");
         	    				  //JOptionPane.showMessageDialog(null, "Number of Sample Frequencies is empty, assuming 0", "Warning Message",JOptionPane.WARNING_MESSAGE);
         	     			 	}
    	    		     }
    	    		     else{
    	        			  numTrials = Integer.parseInt(numOfTrialsInExperimentTextString);
    	        			  numRuns = Integer.parseInt(numOfRunsTextString);
    	    		    	  numFrequencies = Integer.parseInt(numFreqSampleTextString);
    	    		    	  numOfTrialsWithOneBet = Integer.parseInt(numOfTrialsWithOneBetTextString);
    	    		    	  numOfDemoTrialsNotTimed = Integer.parseInt(numOfDemoTrialsNotTimedTextString); //2 by default
    	    		    	  numOfDemoTrialsTimed =Integer.parseInt(numOfDemoTrialsTimedTextString);
    	    		    	  numOfPracticeBetTrials  = Integer.parseInt(numOfPracticeBetTrialsTextString);
       	 	    		   
    	 	    		      if((numTrials/numRuns)% numFrequencies!=0){
    	 	    		    	  String message = String.format("<html><center> Error: Number of sample frequencies must be multiple of number of trials per each run.<br>"
    	 	    		    			 + "Please set the number of sample frequencies multiple to (number of trials)/(number of runs), which is %d.</center></html>"
    	 	    		    			 ,(int)(numTrials/numRuns)
    	 	    		    			);
    	 	    		    	  JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
    	 	    		    	  numFreqSampleText.setBackground(Color.YELLOW);
    	 	    		      }
    	 	    		      else{
    	 	    		    	  numFreqSampleText.setBackground(Color.WHITE);
    	 	    		      }
    	    		     } 
                     }
                 });
     		  }//end of warn()
     	});

	    
  		
  		
  		//action listener for triggerWidthText
 	    //----------------------------------------------------------------------------------
  		((PlainDocument)triggerWidthText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numFreqSampleText, "Enter a number"));
  		triggerWidthText.getDocument().addDocumentListener(new DocumentListener() {
  			public void changedUpdate(DocumentEvent e) {
  				warn();
     		  }
     		  public void removeUpdate(DocumentEvent e) {
     		  }
     		  public void insertUpdate(DocumentEvent e) { 
     			 warn();
     		  }
 
     		  public void warn() {
     			 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                    	
         				String triggerWidthTextString = triggerWidthText.getText();

                    	if(jrbTriggerUpperLeft.isSelected()){
							
							leftCornerX = 0;
							leftCornerY = 0;
	
							if(triggerWidthTextString.isEmpty() && triggerIsSelected){
								
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerWidth =Integer.parseInt(triggerWidthTextString);
								triggerWidthText.setText(String.valueOf(triggerWidth));
							}
							
							
							leftCornerXText.setText(String.valueOf(leftCornerX));

							
                    	}
                    	
                    	if(jrbTriggerLowerLeft.isSelected()){
							
							if(triggerWidthTextString.isEmpty() && triggerIsSelected){
								
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerWidth =Integer.parseInt(triggerWidthTextString);
								triggerWidthText.setText(String.valueOf(triggerWidth));
							}
							
							
							leftCornerX = 0;
							
							
							leftCornerXText.setText(String.valueOf(leftCornerX));
							
							
						}	
                    	
                    	if(jrbTriggerUpperRight.isSelected()){
							
							if(triggerWidthTextString.isEmpty() && triggerIsSelected){
								
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerWidth =Integer.parseInt(triggerWidthTextString);
								triggerWidthText.setText(String.valueOf(triggerWidth));
							}
							
							leftCornerX = Main.screenWidth - triggerWidth;
							
							leftCornerXText.setText(String.valueOf(leftCornerX));
		
                    	}
                    	
                    	if(jrbTriggerLowerRight.isSelected()){
							
					
							if(triggerWidthTextString.isEmpty() && triggerIsSelected){
								
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerWidth =Integer.parseInt(triggerWidthTextString);
								triggerWidthText.setText(String.valueOf(triggerWidth));
							}
							
							
							leftCornerX = Main.screenWidth - triggerWidth;
							
							
							leftCornerXText.setText(String.valueOf(leftCornerX));
							
							
						}	
 
                     }
                 });
     		  }//end of warn()
     	});
  		

  		//action listener for triggerHeightText
 	    //----------------------------------------------------------------------------------
  		((PlainDocument)triggerHeightText.getDocument()).setDocumentFilter(new MyNumberDocFilter(numFreqSampleText, "Enter a number"));
  		triggerWidthText.getDocument().addDocumentListener(new DocumentListener() {
  			public void changedUpdate(DocumentEvent e) {
  				warn();
     		  }
     		  public void removeUpdate(DocumentEvent e) {
     		  }
     		  public void insertUpdate(DocumentEvent e) { 
     			 warn();
     		  }
 
     		  public void warn() {
     			 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                    	
                    
         				String triggerHeightTextString = triggerHeightText.getText();

                    	if(jrbTriggerUpperLeft.isSelected()){
							
							leftCornerX = 0;
							leftCornerY = 0;
	
							if(triggerHeightTextString.isEmpty() && triggerIsSelected){
								
								triggerHeightText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerHeight =Integer.parseInt(triggerHeightTextString);
								triggerHeightText.setText(String.valueOf(triggerHeight));
							}
							
							
							leftCornerYText.setText(String.valueOf(leftCornerY));

							
                    	}
                    	
                    	if(jrbTriggerLowerLeft.isSelected()){
							
							if(triggerHeightTextString.isEmpty() && triggerIsSelected){
								
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerHeightText.setBackground(Color.WHITE);
								triggerHeight =Integer.parseInt(triggerHeightTextString);
								triggerHeightText.setText(String.valueOf(triggerHeight));
							}
							
							
							leftCornerY =  Main.screenHeight - triggerHeight;
							
							
							leftCornerYText.setText(String.valueOf(leftCornerY));
							
							
						}	
                    	
                    	if(jrbTriggerUpperRight.isSelected()){
							
							if(triggerHeightTextString.isEmpty() && triggerIsSelected){
								
								triggerHeightText.setBackground(Color.YELLOW);
							}
							else{
								triggerHeightText.setBackground(Color.WHITE);
								triggerHeight =Integer.parseInt(triggerHeightTextString);
								triggerHeightText.setText(String.valueOf(triggerHeight));
							}
							
							leftCornerY = 0;
							
							leftCornerYText.setText(String.valueOf(leftCornerY));
		
                    	}
                    	
                    	if(jrbTriggerLowerRight.isSelected()){
							
					
							if(triggerHeightTextString.isEmpty() && triggerIsSelected){
								
								triggerHeightText.setBackground(Color.YELLOW);
							}
							else{
								triggerHeightText.setBackground(Color.WHITE);
								triggerHeight =Integer.parseInt(triggerHeightTextString);
								triggerHeightText.setText(String.valueOf(triggerHeight));
							}
							
							
							leftCornerY = Main.screenHeight - triggerHeight;
							
							
							leftCornerYText.setText(String.valueOf(leftCornerY));
							
							
						}	
 
                     }
                 });
     		  }//end of warn()
     	});
  		
  		
  		
  		
	    addTrigger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* It posts an event (Runnable)at the end of Swings event list and is
				processed after all other GUI events are processed.*/
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						//try - catch block
						try 
						{
							if(addTrigger.isSelected()){
								//set the flag monitoring checkbox condition to true
								triggerIsSelected = true;
								//when setTriggerParamsFrame is activated -- disable the selecting of the checkerbox
								addTrigger.setEnabled(true);
								if(!triggerPanel.isEnabled()){
									triggerPanel.setEnabled(true);
									leftCornerXLabel.setEnabled(true);
									leftCornerXText.setEnabled(true);
									leftCornerYLabel.setEnabled(true);
									leftCornerYText.setEnabled(true);
									triggerWidthLabel.setEnabled(true);
									triggerWidthText.setEnabled(true);
									triggerHeightLabel.setEnabled(true);
									triggerHeightText.setEnabled(true);
								}
							}//end of checking whether the checkbox is selected
							else{
								//if the checkbox is not selected set the flag monitoring checkbox condition to false
								triggerIsSelected = false;
								triggerPanel.setEnabled(false);
								leftCornerXLabel.setEnabled(false);
								leftCornerXText.setEnabled(false);
								leftCornerYLabel.setEnabled(false);
								leftCornerYText.setEnabled(false);
								triggerWidthLabel.setEnabled(false);
								triggerWidthText.setEnabled(false);
								triggerHeightLabel.setEnabled(false);
								triggerHeightText.setEnabled(false);
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
	    });
	    
	    
	    
	    
	    
	    //set listeners to buttons  
	    //----------------------------------------------------------------------------------  
	    //Apply button action listener
  		jbtApply.addActionListener(new ActionListener() {
  	          @Override
  	          public void actionPerformed(ActionEvent e1) {
  	        	revalidate();
  	        	repaint();
  	        	triggerIsSelected = addTrigger.isSelected();
				//get variables from textFileds
  	        	
				//TODO: if the field for subject is empty or entered number already contains the subject data -- show a warning
				String leftCornerXTextString = leftCornerXText.getText();
				String leftCornerYTextString = leftCornerYText.getText();
				String triggerWidthTextString = triggerWidthText.getText();
				String triggerHeightTextString = triggerHeightText.getText();
				String studyIdTextString = studyIdText.getText();
				String studyVerTextString = studyVersionText.getText();
				String numOfTrialsWithOneBetTextString = numOfTrialsWithOneBetText.getText();
				String numOfDemoTrialsNotTimedTextString = numOfDemoTrialsNotTimedText.getText();
				String numOfDemoTrialsTimedTextString = numOfDemoTrialsTimedText.getText();
				String numOfPracticeBetTrialsTextString = numOfPracticeBetTrialsText.getText();
				String numOfTrialsInExperimentTextString = numOfTrialsInExperimentText.getText();
				String numOfRunsTextString = numOfRunsText.getText();
				String maxFreqTextString  = maxFreqText.getText();
				String minFreqTextString  = minFreqText.getText();
				String numFreqSampleTextString = numFreqSampleText.getText();
				String freqJitterTextString = freqJitterText.getText();
				String stimTaskDurationTextString = stimTaskDurationText.getText();
				String preStimTaskDurationTextString = preStimTaskDurationText.getText();
				String intertrialTaskDurationTextString = intertrialTaskDurationText.getText();
				String delayTaskDurationTextString = delayTaskDurationText.getText();
				String probeTaskDurationTextString = probeTaskDurationText.getText();
				String numBetsPerTrialTextString = numBetsPerTrialText.getText();
				String maxPointsPerBetTextString = maxPointsPerBetText.getText();
				String fontSizeTextString = setFontSizeText.getText();
	        	
				boolean startTask = true;

				if(	studyIdTextString.isEmpty()		 					||
					studyVerTextString.isEmpty()     					||
					numOfRunsTextString.isEmpty()						||
					numOfTrialsWithOneBetTextString.isEmpty()       	||
					numOfDemoTrialsNotTimedTextString.isEmpty()       	||
					numOfDemoTrialsTimedTextString.isEmpty()       		||
					numOfPracticeBetTrialsTextString.isEmpty()       	||
					maxFreqTextString.isEmpty()		 					||
					minFreqTextString.isEmpty()		 					||
					numFreqSampleTextString.isEmpty()					||
					freqJitterTextString.isEmpty()						||
					leftCornerXTextString.isEmpty()						||
					leftCornerYTextString.isEmpty()						||
					triggerWidthTextString.isEmpty()					||
					triggerHeightTextString.isEmpty()					||
					numOfTrialsInExperimentTextString.isEmpty()			||
					stimTaskDurationTextString.isEmpty()				||
					preStimTaskDurationTextString.isEmpty()				||
					intertrialTaskDurationTextString.isEmpty()			||
					delayTaskDurationTextString.isEmpty()				||
					probeTaskDurationTextString.isEmpty()				||
					numBetsPerTrialTextString.isEmpty()					||
					maxPointsPerBetTextString.isEmpty()					||
					fontSizeTextString.isEmpty()
					
					){
						String message = new String();
						if(numOfRunsTextString.isEmpty())
							message = String.format("<html><center> Please enter number of runs to proceed.</center></html>");
						
						if(studyIdTextString.isEmpty())
							message = String.format("<html><center> Please enter study ID to proceed.</center></html>");
						
						if(studyVerTextString.isEmpty())
							message = String.format("<html><center> Please enter study version to proceed.</center></html>");
						
						if(numOfTrialsWithOneBetTextString.isEmpty())
							message = String.format("<html><center> Please enter number of trials with one bet to proceed.</center></html>");
						
						if(numOfDemoTrialsNotTimedTextString.isEmpty())
							message = String.format("<html><center> Please enter number of untimed trials to proceed.</center></html>");
						
						if(numOfDemoTrialsTimedTextString.isEmpty())
							message = String.format("<html><center> Please enter number of timed trials to proceed.</center></html>");
						
						if(numOfPracticeBetTrialsTextString.isEmpty())
							message = String.format("<html><center> Please enter number of practice bet trials to proceed.</center></html>");
						
						if(numOfTrialsInExperimentTextString.isEmpty())
							message = String.format("<html><center> Please enter number of trials in real experiment to proceed.</center></html>");
						
						if(maxFreqTextString.isEmpty())
							message = String.format("<html><center> Please enter maximum frequency value to proceed.</center></html>");
						
						if(minFreqTextString.isEmpty())
							message = String.format("<html><center> Please enter minimum frequency value to proceed.</center></html>");
						
						if(numFreqSampleTextString.isEmpty())
							message = String.format("<html><center> Please enter number of sample frequencies to proceed.</center></html>");
						
						if(maxPointsPerBetTextString.isEmpty())
							message = String.format("<html><center> Please enter max points for one bet to proceed.</center></html>");
						
						if(freqJitterTextString.isEmpty())
							message = String.format("<html><center> Please enter jitter value to proceed.</center></html>");

						if(stimTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter stimulus duration in msec to proceed.</center></html>");
						
						if(preStimTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter pre-stimulus duration in msec to proceed.</center></html>");
						
						if(intertrialTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter intertrial duration in msec to proceed.</center></html>");
						
						if(delayTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter delay duration in msec to proceed.</center></html>");
						
						if(probeTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter probe duration in msec to proceed.</center></html>");
						
						if(numBetsPerTrialTextString.isEmpty())
							message = String.format("<html><center> Please enter number of bets in one trial to proceed.</center></html>");
						
						if(leftCornerXTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's left corner X coordinate to proceed.</center></html>");
						
						if(leftCornerYTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's left corner Y coordinate to proceed.</center></html>");
						
						if(triggerWidthTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
						
						if(triggerHeightTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
						
						if(fontSizeTextString.isEmpty())
							message = String.format("<html><center> Please enter font size to proceed.</center></html>");
						
						
		    			JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);
		    			startTask=false;
				}
				else{
					numFreqSampleText.setBackground(Color.WHITE);
					studyId = studyIdTextString.toString();
					studyVer = Integer.parseInt(studyVerTextString);
					sessionNum = ExperimentSetupWindow.sessionNum;
					numRuns = Integer.parseInt(numOfRunsTextString);
					numOfTrialsWithOneBet = Integer.parseInt(numOfTrialsWithOneBetTextString);
					subNum = ExperimentSetupWindow.subNum;
					numOfDemoTrialsNotTimed =Integer.parseInt(numOfDemoTrialsNotTimedTextString);
					numOfDemoTrialsTimed =Integer.parseInt(numOfDemoTrialsTimedTextString);
					numOfPracticeBetTrials = Integer.parseInt(numOfPracticeBetTrialsTextString);
			    	maxFrequency =Integer.parseInt(maxFreqTextString);
			    	minFrequency =Integer.parseInt(minFreqTextString);
			    	numFrequencies = Integer.parseInt(numFreqSampleTextString);
			    	jitter = Integer.parseInt(freqJitterTextString);
			    	numTrials = Integer.parseInt(numOfTrialsInExperimentTextString);		
					numOfTrialsWithOneBet = Integer.parseInt(numOfTrialsWithOneBetTextString);
					leftCornerX = Integer.parseInt(leftCornerXText.getText());
		        	leftCornerY = Integer.parseInt(leftCornerYText.getText());
		        	triggerWidth = Integer.parseInt(triggerWidthText.getText());
		        	triggerHeight = Integer.parseInt(triggerHeightText.getText());
		        	stimulusDuration = Integer.parseInt(stimTaskDurationText.getText());
		        	preStimulusDuration = Integer.parseInt(preStimTaskDurationText.getText());
		        	intertrialDuration = Integer.parseInt(intertrialTaskDurationText.getText());
		        	delayDuration = Integer.parseInt(delayTaskDurationText.getText());
		        	probeDuration = Integer.parseInt(probeTaskDurationText.getText());
		        	numberOfBetsPerTrial=Integer.parseInt(numBetsPerTrialText.getText());
		        	maxPointsPerBet = Integer.parseInt(maxPointsPerBetText.getText());
		        	fontSize = Integer.parseInt(setFontSizeText.getText());
		        	fontFamily = (String) fontsBox.getSelectedItem();
		        	setNumberOfComboBlocks(numRuns);
		        	
		        	if(((numTrials/numRuns)% numFrequencies!=0) && (numTrials % numRuns !=0) ){
						 String message = String.format("<html><center> Error: Number of runs must be multiple of number of trials and <br>"
						 		+ "number of sample frequencies must be multiple of number of trials per each run.");
	    		    	  JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
	    		    	  numOfRunsText.setBackground(Color.YELLOW);
	    		    	  numFreqSampleText.setBackground(Color.YELLOW);
	    		    	  startTask=false;
					 }
		        	if(((numTrials/numRuns)% numFrequencies!=0) && (numTrials % numRuns ==0) ){
	    		    	  String message = String.format("<html><center> Error: Number of sample frequencies must be multiple of number of trials per each run.<br>"
	    		    			 + "Please set the number of sample frequencies multiple to (number of trials)/(number of runs), which is %d.</center></html>"
	    		    			 ,(int)(numTrials/numRuns)
	    		    			);
	    		    	  JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
	    		    	  numOfRunsText.setBackground(Color.WHITE);
	    		    	  numFreqSampleText.setBackground(Color.YELLOW);
	    		    	  startTask=false;
	    		      }
		        	if(((numTrials/numRuns)% numFrequencies==0) && (numTrials % numRuns !=0) ){
		        		String message = String.format("<html><center> Error: Number of runs must be multiple of number of trials.<br>");
	    		    	  JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
	    		    	  numOfRunsText.setBackground(Color.YELLOW);
	    		    	  numFreqSampleText.setBackground(Color.WHITE);
	    		    	  startTask=false;
	    		      }
		        	if(((numTrials/numRuns)% numFrequencies==0) && (numTrials % numRuns ==0) ){
	
	    		    	  numOfRunsText.setBackground(Color.WHITE);
	    		    	  numFreqSampleText.setBackground(Color.WHITE);
	    		    	  startTask=true;
	    		      }
		        	
		        	
		        	//dispatchEvent(new WindowEvent(new AdvancedOptionsWindow(),WindowEvent.WINDOW_CLOSING));
		        	if(startTask){
		        		saveParamChanges();
			        	dispose();
		        	}
		        	 
				}//end of else
  	          }
  	    });

  		
		 
  		//listener for all textfields
		class Listener implements DocumentListener {
			JTextField tmpTextField;
			Listener(JTextField tmpTextField){
				this.tmpTextField = tmpTextField;
			}
		    public void changedUpdate(DocumentEvent e) {
		    	changed();
		    	//changesOccured =true;
		    }

		    public void insertUpdate(DocumentEvent e) {
		    	changed();
		    	
		     
		    }

		    public void removeUpdate(DocumentEvent e) {
		    	changed();
		    	//changesOccured =true;
		     
		    }
		    public void changed() {
			     if (tmpTextField.getText().isEmpty()){
			    	 (tmpTextField).setBackground(Color.YELLOW);
			     }
			     else {
			    	 (tmpTextField).setBackground(Color.WHITE);
			    }
			    
			  }
		  };
		
		
		  
		  
		JTextField[] fields = {
	  				leftCornerXText,
	  				leftCornerYText,
	  				triggerWidthText,
	  				triggerHeightText,
	  				studyIdText,
	  				studyVersionText,
	  				numOfTrialsWithOneBetText,
	  				numOfDemoTrialsNotTimedText,
	  				numOfDemoTrialsTimedText,
	  				numOfPracticeBetTrialsText,
	  				numOfTrialsInExperimentText,
	  				numOfRunsText,
	  				maxFreqText,
	  				minFreqText,
	  				numFreqSampleText,
	  				freqJitterText,
	  				stimTaskDurationText,
	  				preStimTaskDurationText,
	  				intertrialTaskDurationText,
	  				delayTaskDurationText,
	  				probeTaskDurationText,
	  				numBetsPerTrialText,
	  				maxPointsPerBetText,
	  				setFontSizeText
	  		};
		
		//assign the action listener to all textfields
		for(JTextField textfield : fields) { textfield.getDocument().addDocumentListener(new Listener(textfield)); }
  		
		
		//action listener for invoking the pop-up window
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		    	//--------------------------------------------------------------------------------------------------------
		    	
		    	revalidate();
  	        	repaint();
  	        	triggerIsSelected = addTrigger.isSelected();
				//get variables from textFileds
  	        	
				//TODO: if the field for subject is empty or entered number already contains the subject data -- show a warning
				String leftCornerXTextString = leftCornerXText.getText();
				String leftCornerYTextString = leftCornerYText.getText();
				String triggerWidthTextString = triggerWidthText.getText();
				String triggerHeightTextString = triggerHeightText.getText();
				String studyIdTextString = studyIdText.getText();
				String studyVerTextString = studyVersionText.getText();
				String numOfTrialsWithOneBetTextString = numOfTrialsWithOneBetText.getText();
				String numOfDemoTrialsNotTimedTextString = numOfDemoTrialsNotTimedText.getText();
				String numOfDemoTrialsTimedTextString = numOfDemoTrialsTimedText.getText();
				String numOfPracticeBetTrialsTextString = numOfPracticeBetTrialsText.getText();
				String numOfTrialsInExperimentTextString = numOfTrialsInExperimentText.getText();
				String numOfRunsTextString = numOfRunsText.getText();
				String maxFreqTextString  = maxFreqText.getText();
				String minFreqTextString  = minFreqText.getText();
				String numFreqSampleTextString = numFreqSampleText.getText();
				String freqJitterTextString = freqJitterText.getText();
				String stimTaskDurationTextString = stimTaskDurationText.getText();
				String preStimTaskDurationTextString = preStimTaskDurationText.getText();
				String intertrialTaskDurationTextString = intertrialTaskDurationText.getText();
				String delayTaskDurationTextString = delayTaskDurationText.getText();
				String probeTaskDurationTextString = probeTaskDurationText.getText();
				String numBetsPerTrialTextString = numBetsPerTrialText.getText();
				String maxPointsPerBetTextString = maxPointsPerBetText.getText();
				String fontSizeTextString = setFontSizeText.getText();
	        	
				boolean startTask = true;

				if(	studyIdTextString.isEmpty()		 					||
					studyVerTextString.isEmpty()     					||
					numOfRunsTextString.isEmpty()						||
					numOfTrialsWithOneBetTextString.isEmpty()       	||
					numOfDemoTrialsNotTimedTextString.isEmpty()       	||
					numOfDemoTrialsTimedTextString.isEmpty()       		||
					numOfPracticeBetTrialsTextString.isEmpty()       	||
					maxFreqTextString.isEmpty()		 					||
					minFreqTextString.isEmpty()		 					||
					numFreqSampleTextString.isEmpty()					||
					freqJitterTextString.isEmpty()						||
					leftCornerXTextString.isEmpty()						||
					leftCornerYTextString.isEmpty()						||
					triggerWidthTextString.isEmpty()					||
					triggerHeightTextString.isEmpty()					||
					numOfTrialsInExperimentTextString.isEmpty()			||
					stimTaskDurationTextString.isEmpty()				||
					preStimTaskDurationTextString.isEmpty()				||
					intertrialTaskDurationTextString.isEmpty()			||
					delayTaskDurationTextString.isEmpty()				||
					probeTaskDurationTextString.isEmpty()				||
					numBetsPerTrialTextString.isEmpty()					||
					maxPointsPerBetTextString.isEmpty()					||
					fontSizeTextString.isEmpty()
					
					){
						String message = new String();
						if(numOfRunsTextString.isEmpty())
							message = String.format("<html><center> Please enter number of runs to proceed.</center></html>");
						
						if(studyIdTextString.isEmpty())
							message = String.format("<html><center> Please enter study ID to proceed.</center></html>");
						
						if(studyVerTextString.isEmpty())
							message = String.format("<html><center> Please enter study version to proceed.</center></html>");
						
						if(numOfTrialsWithOneBetTextString.isEmpty())
							message = String.format("<html><center> Please enter number of trials with one bet to proceed.</center></html>");
						
						if(numOfDemoTrialsNotTimedTextString.isEmpty())
							message = String.format("<html><center> Please enter number of untimed trials to proceed.</center></html>");
						
						if(numOfDemoTrialsTimedTextString.isEmpty())
							message = String.format("<html><center> Please enter number of timed trials to proceed.</center></html>");
						
						if(numOfPracticeBetTrialsTextString.isEmpty())
							message = String.format("<html><center> Please enter number of practice bet trials to proceed.</center></html>");
						
						if(numOfTrialsInExperimentTextString.isEmpty())
							message = String.format("<html><center> Please enter number of trials in real experiment to proceed.</center></html>");
						
						if(maxFreqTextString.isEmpty())
							message = String.format("<html><center> Please enter maximum frequency value to proceed.</center></html>");
						
						if(minFreqTextString.isEmpty())
							message = String.format("<html><center> Please enter minimum frequency value to proceed.</center></html>");
						
						if(numFreqSampleTextString.isEmpty())
							message = String.format("<html><center> Please enter number of sample frequencies to proceed.</center></html>");
						
						if(maxPointsPerBetTextString.isEmpty())
							message = String.format("<html><center> Please enter max points for one bet to proceed.</center></html>");
						
						if(freqJitterTextString.isEmpty())
							message = String.format("<html><center> Please enter jitter value to proceed.</center></html>");

						if(stimTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter stimulus duration in msec to proceed.</center></html>");
						
						if(preStimTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter pre-stimulus duration in msec to proceed.</center></html>");
						
						if(intertrialTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter intertrial duration in msec to proceed.</center></html>");
						
						if(delayTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter delay duration in msec to proceed.</center></html>");
						
						if(probeTaskDurationTextString.isEmpty())
							message = String.format("<html><center> Please enter probe duration in msec to proceed.</center></html>");
						
						if(numBetsPerTrialTextString.isEmpty())
							message = String.format("<html><center> Please enter number of bets in one trial to proceed.</center></html>");
						
						if(leftCornerXTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's left corner X coordinate to proceed.</center></html>");
						
						if(leftCornerYTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's left corner Y coordinate to proceed.</center></html>");
						
						if(triggerWidthTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
						
						if(triggerHeightTextString.isEmpty() && triggerIsSelected)
							message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
						
						if(fontSizeTextString.isEmpty())
							message = String.format("<html><center> Please enter font size to proceed.</center></html>");
						
						
					    JOptionPane pane = new JOptionPane( message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
    		    	    JDialog dialog = pane.createDialog("Error");
			       	  	dialog.setContentPane(pane);
			       	  	dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE );
			       	  	dialog.pack();
			       	  	dialog.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
			       	  	dialog.setVisible(true);
			       	  	int c = ((Integer)pane.getValue()).intValue();

		    			startTask=false;
				}
				else{
					numFreqSampleText.setBackground(Color.WHITE);
					studyId = studyIdTextString.toString();
					studyVer = Integer.parseInt(studyVerTextString);
					sessionNum = ExperimentSetupWindow.sessionNum;
					numRuns = Integer.parseInt(numOfRunsTextString);
					numOfTrialsWithOneBet = Integer.parseInt(numOfTrialsWithOneBetTextString);
					subNum = ExperimentSetupWindow.subNum;
					numOfDemoTrialsNotTimed =Integer.parseInt(numOfDemoTrialsNotTimedTextString);
					numOfDemoTrialsTimed =Integer.parseInt(numOfDemoTrialsTimedTextString);
					numOfPracticeBetTrials = Integer.parseInt(numOfPracticeBetTrialsTextString);
			    	maxFrequency =Integer.parseInt(maxFreqTextString);
			    	minFrequency =Integer.parseInt(minFreqTextString);
			    	numFrequencies = Integer.parseInt(numFreqSampleTextString);
			    	jitter = Integer.parseInt(freqJitterTextString);
			    	numTrials = Integer.parseInt(numOfTrialsInExperimentTextString);		
					numOfTrialsWithOneBet = Integer.parseInt(numOfTrialsWithOneBetTextString);
					leftCornerX = Integer.parseInt(leftCornerXText.getText());
		        	leftCornerY = Integer.parseInt(leftCornerYText.getText());
		        	triggerWidth = Integer.parseInt(triggerWidthText.getText());
		        	triggerHeight = Integer.parseInt(triggerHeightText.getText());
		        	stimulusDuration = Integer.parseInt(stimTaskDurationText.getText());
		        	preStimulusDuration = Integer.parseInt(preStimTaskDurationText.getText());
		        	intertrialDuration = Integer.parseInt(intertrialTaskDurationText.getText());
		        	delayDuration = Integer.parseInt(delayTaskDurationText.getText());
		        	probeDuration = Integer.parseInt(probeTaskDurationText.getText());
		        	numberOfBetsPerTrial=Integer.parseInt(numBetsPerTrialText.getText());
		        	maxPointsPerBet = Integer.parseInt(maxPointsPerBetText.getText());
		        	fontSize = Integer.parseInt(setFontSizeText.getText());
		        	fontFamily = (String) fontsBox.getSelectedItem();
		        	setNumberOfComboBlocks(numRuns);
		        	
		        	if(((numTrials/numRuns)% numFrequencies!=0) && (numTrials % numRuns !=0) ){
						 String message = String.format("<html><center> Error: Number of runs must be multiple of number of trials and <br>"
						 		+ "number of sample frequencies must be multiple of number of trials per each run.");
						  JOptionPane pane = new JOptionPane( message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
						  JDialog dialog = pane.createDialog("Error");
			       	  	  dialog.setContentPane(pane);
			       	  	  dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE );
			       	  	  dialog.pack();
			       	  	  dialog.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
			       	  	  dialog.setVisible(true);
			       	  	  int c = ((Integer)pane.getValue()).intValue();
	    		    	  numOfRunsText.setBackground(Color.YELLOW);
	    		    	  numFreqSampleText.setBackground(Color.YELLOW);
	    		    	  startTask=false;
					 }
		        	else if(((numTrials/numRuns)% numFrequencies!=0) || (numTrials % numRuns !=0) ){
		        		if((numTrials/numRuns)% numFrequencies!=0){
	 	    		    	  String message = String.format("<html><center> Error: Number of sample frequencies must be multiple of number of trials per each run.<br>"
	 	    		    			 + "Please set the number of sample frequencies multiple to (number of trials)/(number of runs), which is %d.</center></html>"
	 	    		    			 ,(int)(numTrials/numRuns)
	 	    		    			);
	 	    		    	  JOptionPane pane = new JOptionPane( message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
							  JDialog dialog = pane.createDialog("Error");
				       	  	  dialog.setContentPane(pane);
				       	  	  dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE );
				       	  	  dialog.pack();
				       	  	  dialog.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
				       	  	  dialog.setVisible(true);
				       	  	  int c = ((Integer)pane.getValue()).intValue();
	 	    		    	  numFreqSampleText.setBackground(Color.YELLOW);
	 	    		    	 startTask=false;
	 	    		      }
	 	    		      else{
	 	    		    	  numFreqSampleText.setBackground(Color.WHITE);
	 	    		    	  startTask=true;
	 	    		      }
						 if(numTrials % numRuns !=0){
	 	    		    	  String message = String.format("<html><center> Error: Number of runs must be multiple of number of trials.<br>");
	 	    		    	  
	 	    		    	  JOptionPane pane = new JOptionPane( message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
							  JDialog dialog = pane.createDialog("Error");
				       	  	  dialog.setContentPane(pane);
				       	  	  dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE );
				       	  	  dialog.pack();
				       	  	  dialog.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
				       	  	  dialog.setVisible(true);
				       	  	  int c = ((Integer)pane.getValue()).intValue();
	 	    		    	  numOfRunsText.setBackground(Color.YELLOW);
	 	    		    	  startTask=false;
	 	    		      }
	 	    		      else{
	 	    		    	  numOfRunsText.setBackground(Color.WHITE);
	 	    		    	  startTask=true;
	 	    		      }
		        	} 
		        	else{
	    		    	  numOfRunsText.setBackground(Color.WHITE);
	    		    	  numFreqSampleText.setBackground(Color.WHITE);
	    		    	  startTask=true;
	    		      }
				}
		    	//--------------------------------------------------------------------------------------------------------
		    	
		    	
		    	changesOccured = checkIfAnyChanges();
		    	System.out.println("changesOccured  "+changesOccured);
		    	if(changesOccured){
		    		JOptionPane pane = new JOptionPane("Do you want to save the parameters?", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
				    
			        JDialog dialog = pane.createDialog("Warning");

			        dialog.setContentPane(pane);
			        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			        dialog.pack();
			        dialog.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
			        dialog.setVisible(true);
			        
			        //will throw Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
			        // does not affect the performance but allows the user to close the dialog without closing the AdvancedOptionsWindow
			        int c = ((Integer)pane.getValue()).intValue();
			    
			        if(c == JOptionPane.YES_OPTION) {
			        	//Close frame 
			        	//click(jbtApply, 100);
			        	saveParamChanges();
			            dispose(); 
			            
			        }
			        else if (c == JOptionPane.NO_OPTION) {
			        	//Close frame 
			        	//click(jbtReset, 100);
			        	
			        	loadParamsWithoutSettingAnyValues();    
		    		    System.out.println("jitter   "+jitter);
			        	System.out.println("stimulusDuration   "+stimulusDuration);
			        	revalidate();
		  	        	repaint();
			        	studyIdText.setText(studyId);
		        		studyVersionText.setText(String.valueOf(studyVer));
		        		numOfTrialsWithOneBetText.setText(String.valueOf(numOfTrialsWithOneBet));
		        		numOfDemoTrialsNotTimedText.setText(String.valueOf(numOfDemoTrialsNotTimed));
		        		numOfDemoTrialsTimedText.setText(String.valueOf(numOfDemoTrialsTimed));
		        		numOfPracticeBetTrialsText.setText(String.valueOf(numOfPracticeBetTrials));
		        		numOfTrialsInExperimentText.setText(String.valueOf(numTrials));
		        		numOfRunsText.setText(String.valueOf(numRuns));
		        		numFreqSampleText.setText(String.valueOf(numFrequencies));
		        		minFreqText.setText(String.valueOf((int)minFrequency));
		        		maxFreqText.setText(String.valueOf((int)maxFrequency));
		        		freqJitterText.setText(String.valueOf((int)jitter));
		      		  	triggerWidthText.setText(String.valueOf(triggerWidth));
		      		  	triggerHeightText.setText(String.valueOf(triggerHeight));
		      		  	leftCornerXText.setText(String.valueOf(leftCornerX));
		      		  	leftCornerYText.setText(String.valueOf( leftCornerY));
		      		  	addTrigger.setSelected(triggerIsSelected);
		      		  	stimTaskDurationText.setText(String.valueOf(stimulusDuration));
		      		  	preStimTaskDurationText.setText(String.valueOf(preStimulusDuration));
		      		  	intertrialTaskDurationText.setText(String.valueOf(intertrialDuration));
		      		  	delayTaskDurationText.setText(String.valueOf(delayDuration)); 
		      		  	probeTaskDurationText.setText(String.valueOf(probeDuration));
		      		  	numBetsPerTrialText.setText(String.valueOf(numberOfBetsPerTrial));
		      		  	maxPointsPerBetText.setText(String.valueOf(maxPointsPerBet));
		      		  	jrbProbeTaskTimed.setSelected(isProbeTaskTimed);
		      		  	jrbProbeTaskTimed.setEnabled(!isProbeTaskTimed);
		      		  	jrbProbeTaskSelfPaced.setEnabled(isProbeTaskTimed);
		      		  	setFontSizeText.setText(String.valueOf(fontSize));
		      		  	fontsBox.setSelectedItem(fontFamily);
		      		  	if(triggerLowerLeftIsSelected){
			      		  	triggerUpperLeftIsSelected = false;
			      		    triggerLowerLeftIsSelected = true;
			      		    triggerLowerRightIsSelected = false;
			      		    triggerUpperRightIsSelected = false;
			      		    triggerOtherIsSelected = false;
			      		  	jrbTriggerLowerLeft.setSelected(triggerLowerLeftIsSelected);//true
			      		  	jrbTriggerLowerLeft.setEnabled(!triggerLowerLeftIsSelected);//false
							jrbTriggerUpperRight.setEnabled(triggerLowerLeftIsSelected);//true
							jrbTriggerLowerRight.setEnabled(triggerLowerLeftIsSelected);//true
							jrbTriggerUpperLeft.setEnabled(triggerLowerLeftIsSelected);//true
							jrbTriggerOther.setEnabled(triggerLowerLeftIsSelected);//true
		      		  	}
		      		  	else if(triggerLowerRightIsSelected){
			      		  	triggerUpperLeftIsSelected = false;
			      		    triggerLowerLeftIsSelected = false;
			      		    triggerLowerRightIsSelected = true;
			      		    triggerUpperRightIsSelected = false;
			      		    triggerOtherIsSelected = false;
			      		  	jrbTriggerLowerRight.setSelected(triggerLowerRightIsSelected);//true
			      		  	jrbTriggerLowerLeft.setEnabled(triggerLowerRightIsSelected);//false
							jrbTriggerUpperRight.setEnabled(triggerLowerRightIsSelected);//true
							jrbTriggerLowerRight.setEnabled(!triggerLowerRightIsSelected);//true
							jrbTriggerUpperLeft.setEnabled(triggerLowerRightIsSelected);//true
							jrbTriggerOther.setEnabled(triggerLowerRightIsSelected);//true
		      		  	} 
		      		  	else if(triggerUpperRightIsSelected){
			      		  	triggerUpperLeftIsSelected = false;
			      		    triggerLowerLeftIsSelected = false;
			      		    triggerLowerRightIsSelected = false;
			      		    triggerUpperRightIsSelected = true;
			      		    triggerOtherIsSelected = false;
			      		  	jrbTriggerUpperRight.setSelected(triggerUpperRightIsSelected);//true
			      		  	jrbTriggerLowerLeft.setEnabled(triggerUpperRightIsSelected);//false
							jrbTriggerUpperRight.setEnabled(!triggerUpperRightIsSelected);//true
							jrbTriggerLowerRight.setEnabled(triggerUpperRightIsSelected);//true
							jrbTriggerUpperLeft.setEnabled(triggerUpperRightIsSelected);//true
							jrbTriggerOther.setEnabled(triggerUpperRightIsSelected);//true
		      		  	} 
		      		  	else if(triggerUpperLeftIsSelected){
			      		  	triggerUpperLeftIsSelected = true;
			      		    triggerLowerLeftIsSelected = false;
			      		    triggerLowerRightIsSelected = false;
			      		    triggerUpperRightIsSelected = false;
			      		    triggerOtherIsSelected = false;
			      		  	jrbTriggerUpperLeft.setSelected(triggerUpperLeftIsSelected);//true
			      		  	jrbTriggerLowerLeft.setEnabled(triggerUpperLeftIsSelected);//false
							jrbTriggerUpperRight.setEnabled(triggerUpperLeftIsSelected);//true
							jrbTriggerLowerRight.setEnabled(triggerUpperLeftIsSelected);//true
							jrbTriggerUpperLeft.setEnabled(!triggerUpperLeftIsSelected);//true
							jrbTriggerOther.setEnabled(triggerUpperLeftIsSelected);//true
		      		  	} 
		      		  	else if(triggerOtherIsSelected){
			      		  	triggerUpperLeftIsSelected = false;
			      		    triggerLowerLeftIsSelected = false;
			      		    triggerLowerRightIsSelected = false;
			      		    triggerUpperRightIsSelected = false;
			      		    triggerOtherIsSelected = true;
			      		  	jrbTriggerOther.setSelected(triggerOtherIsSelected);//true
			      		  	jrbTriggerLowerLeft.setEnabled(triggerOtherIsSelected);//false
							jrbTriggerUpperRight.setEnabled(triggerOtherIsSelected);//true
							jrbTriggerLowerRight.setEnabled(triggerOtherIsSelected);//true
							jrbTriggerUpperLeft.setEnabled(triggerOtherIsSelected);//true
							jrbTriggerOther.setEnabled(!triggerOtherIsSelected);//true
		      		  	} 
		      		  	//jrbTriggerLowerLeft.doClick();
		      		  	setNumberOfComboBlocks(numRuns);
		      		  	revalidate();
		  	        	repaint();
			        	saveParamChanges();
			            dispose();
		  	        	changesOccured = false;
			        	
			        }
		    	}
		    	else{
		    		dispose();
		    		revalidate();
	  	        	repaint();
		    	}//checking if changes occured
		    	
		    	
		    }
		});
  		
  		
  		//reset button action listener
	  
	    jbtReset.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	    
	        	  	stimulusDuration = Parameters.STIMULUS_TASK_DURATION;
	        	    preStimulusDuration = Parameters.PRESTIMULUS_TASK_DURATION;
	        		intertrialDuration = Parameters.INTERTRIAL_TASK_DURATION;
	        		delayDuration = Parameters.DELAY_TASK_DURATION;
	        		probeDuration = Parameters.PROBE_TASK_DURATION_IF_IDLE;
	        		numberOfBetsPerTrial = Parameters.NUMBER_OF_BETS_PER_TRIAL;
	        		maxPointsPerBet = Parameters.MAX_POINTS_PER_BET;
	        		numTrials = Parameters.NUM_EXPERIMENTAL_TRIALS;
	        	    numRuns = Parameters.RUN_NUM_DEFAULT;
	        	    studyId = Parameters.STUDY_ID_DEFAULT;
	        	    studyVer = Parameters.STUDY_VER_DEFAULT;
	        	    sessionNum = ExperimentSetupWindow.sessionNum;
	        	    subNum = ExperimentSetupWindow.subNum;;
	        	    numOfTrialsWithOneBet = Parameters.TRIALS_ONE_BET_NUM_DEFAULT;
	        	    numOfDemoTrialsNotTimed = Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT ;
	        		numOfDemoTrialsTimed  = Parameters.TRIALS_TIMED_NUM_DEFAULT;
	        		numOfPracticeBetTrials = Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT ;
	        	    maxFrequency = Parameters.MAX_FREQ_DEFAULT;
	        	    minFrequency = Parameters.MIN_FREQ_DEFAULT;
	        	    numFrequencies = Parameters.SAMPLE_FREQ_NUM_DEFAULT;
	        	    jitter = Parameters.JITTER_DEFAULT;
	        	    
	        	    triggerIsSelected = Parameters.IS_TRIGGER_SELECTED;
	        	    startPracticeVersion = true;
	        	    fontSize = Parameters.FONT_SIZE;
	        	    fontFamily = Parameters.FONT_FAMILY;
	        	    
	        	    
	        		triggerWidth = Parameters.TRIGGER_WIDTH; //width of the trigger in pixels
	        		triggerHeight = Parameters.TRIGGER_HEIGHT;//height of the trigger in pixels
	        		leftCornerX = Parameters.TRIGGER_LEFT_CORNER_X; //coordinate of trigger left corner
	        		leftCornerY =  Main.screenHeight - triggerHeight; //coordinate of trigger right corner

	        		studyIdText.setText(Parameters.STUDY_ID_DEFAULT);
	        		studyVersionText.setText(String.valueOf(Parameters.STUDY_VER_DEFAULT));
	        		numOfTrialsWithOneBetText.setText(String.valueOf(Parameters.TRIALS_ONE_BET_NUM_DEFAULT));
	        		numOfDemoTrialsNotTimedText.setText(String.valueOf(Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT));
	        		numOfDemoTrialsTimedText.setText(String.valueOf(Parameters.TRIALS_TIMED_NUM_DEFAULT));
	        		numOfPracticeBetTrialsText.setText(String.valueOf(Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT));
	        		numOfTrialsInExperimentText.setText(String.valueOf(Parameters.NUM_EXPERIMENTAL_TRIALS));
	        		numOfRunsText.setText(String.valueOf(Parameters.RUN_NUM_DEFAULT));
	        		numFreqSampleText.setText(String.valueOf(Parameters.SAMPLE_FREQ_NUM_DEFAULT));
	        		minFreqText.setText(String.valueOf(Parameters.MIN_FREQ_DEFAULT));
	        		maxFreqText.setText(String.valueOf(Parameters.MAX_FREQ_DEFAULT));
	        		freqJitterText.setText(String.valueOf(Parameters.JITTER_DEFAULT));
	      		  	triggerWidthText.setText(String.valueOf(Parameters.TRIGGER_WIDTH));
	      		  	triggerHeightText.setText(String.valueOf(Parameters.TRIGGER_HEIGHT));
	      		  	leftCornerXText.setText(String.valueOf(Parameters.TRIGGER_LEFT_CORNER_X));
	      		  	leftCornerYText.setText(String.valueOf( Main.screenHeight - triggerHeight));
	      		  	addTrigger.setSelected(true);
	      		  	stimTaskDurationText.setText(String.valueOf(stimulusDuration));
	      		  	preStimTaskDurationText.setText(String.valueOf(preStimulusDuration));
	      		  	intertrialTaskDurationText.setText(String.valueOf(intertrialDuration));
	      		  	delayTaskDurationText.setText(String.valueOf(delayDuration)); 
	      		  	probeTaskDurationText.setText(String.valueOf(probeDuration));
	      		  	numBetsPerTrialText.setText(String.valueOf(numberOfBetsPerTrial));
	      		  	maxPointsPerBetText.setText(String.valueOf(maxPointsPerBet));
	      		  	isProbeTaskTimed = true;
	      		  	jrbProbeTaskTimed.setSelected(true);
	      		  	jrbProbeTaskTimed.setEnabled(false);
	      		  	jrbProbeTaskSelfPaced.setEnabled(true);
	      		  	setFontSizeText.setText(String.valueOf(fontSize));
	      		  	fontsBox.setSelectedItem(fontFamily);
	      		  	triggerIsSelected = addTrigger.isSelected();
	      		  	setNumberOfComboBlocks(numRuns);
	      		    //jrbTriggerLowerLeft.doClick();
	      		  	triggerUpperLeftIsSelected = false;
	      		    triggerLowerLeftIsSelected = true;
	      		    triggerLowerRightIsSelected = false;
	      		    triggerUpperRightIsSelected = false;
	      		    triggerOtherIsSelected = false;
	      		  	jrbTriggerLowerLeft.setSelected(triggerLowerLeftIsSelected);//true
	      		  	jrbTriggerLowerLeft.setEnabled(!triggerLowerLeftIsSelected);//false
					jrbTriggerUpperRight.setEnabled(triggerLowerLeftIsSelected);//true
					jrbTriggerLowerRight.setEnabled(triggerLowerLeftIsSelected);//true
					jrbTriggerUpperLeft.setEnabled(triggerLowerLeftIsSelected);//true
					jrbTriggerOther.setEnabled(triggerLowerLeftIsSelected);//true
	      		  	loadParams();
	      		  	saveParamChanges();
	      		    changesOccured=false;
	          }
	    });
	    
	    //Preview button action listener
  		jbtPreview.addActionListener(new ActionListener() {
  	          @Override
  	          public void actionPerformed(ActionEvent e1) {

  	        	  EventQueue.invokeLater(new Runnable()
  		      		{
  	        		
  	        		  
  		      			public void run()
  		      			{
  		      			
	  	        		  	String leftCornerXTextString = leftCornerXText.getText();
							String leftCornerYTextString = leftCornerYText.getText();
							String triggerWidthTextString = triggerWidthText.getText();
							String triggerHeightTextString = triggerHeightText.getText();	
			      			String message;
  		      			
						
	  		      			
							if(leftCornerXTextString.isEmpty() && triggerIsSelected){
								message = String.format("<html><center> Please enter trigger's left corner X coordinate to proceed.</center></html>");
								JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
								leftCornerXText.setBackground(Color.YELLOW);
							}
							else{
								leftCornerXText.setBackground(Color.WHITE);
								leftCornerX =Integer.parseInt(leftCornerXTextString);
								leftCornerXText.setText(String.valueOf(leftCornerX));
							}
							if(leftCornerYTextString.isEmpty() && triggerIsSelected){
								message = String.format("<html><center> Please enter trigger's left corner Y coordinate to proceed.</center></html>");
								JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
								leftCornerYText.setBackground(Color.YELLOW);
							}
							else{
								leftCornerYText.setBackground(Color.WHITE);
								leftCornerY =Integer.parseInt(leftCornerYTextString);
								leftCornerYText.setText(String.valueOf(leftCornerY));
							}
							if(triggerWidthTextString.isEmpty() && triggerIsSelected){
								message = String.format("<html><center> Please enter trigger's width to proceed.</center></html>");
								JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
								triggerWidthText.setBackground(Color.YELLOW);
							}
							else{
								triggerWidthText.setBackground(Color.WHITE);
								triggerWidth =Integer.parseInt(triggerWidthTextString);
								triggerWidthText.setText(String.valueOf(triggerWidth));
							}
							if(triggerHeightTextString.isEmpty() && triggerIsSelected){
								message = String.format("<html><center> Please enter trigger's height to proceed.</center></html>");
								JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);	
								triggerHeightText.setBackground(Color.YELLOW);
							}
							else{
								triggerHeightText.setBackground(Color.WHITE);
								triggerHeight =Integer.parseInt(triggerHeightTextString);
								triggerHeightText.setText(String.valueOf(triggerHeight));
							}
  		      				//try - catch block
  		      				try 
  		      				{
  		      					if(!leftCornerXTextString.isEmpty() && !leftCornerYTextString.isEmpty() && !triggerWidthTextString.isEmpty() && !triggerHeightTextString.isEmpty() && triggerIsSelected){
	  		      					//Create object of InstructionWindow
	  		      					TriggerPreviewWindow frame = new TriggerPreviewWindow();
	  		      					//set frame visible true
	  		      					frame.setVisible(true);		
  		      					}
  		      					
  		      				} 
  		      				catch (Exception e)
  		      				{
  		      					e.printStackTrace();
  		      				}
  		      			}
  		      		});
  	        	  
  	          }
  	    });
  		
//  		//for initializing in constructor:
//	    jrbTriggerLowerLeft.doClick();
//	    changesOccured=false;
	}//end of private constructor
	
	
	
	
	
	public static AdvancedOptionsWindow getInstance()
	{

		if(instance == null)
		{
			instance = new AdvancedOptionsWindow();
		}

		//returns the same instance everytime MySingleFrame.getInstance() is called
		return instance; 


	}

	/**
	 * 
	 * @param textField
	 * @param errorText
	 */
    private void showErrorWin(JTextField textField, String errorText) {     
       if (errorWindow == null) {
          JLabel errorLabel = new JLabel(errorText);
          Window topLevelWin = SwingUtilities.getWindowAncestor(this);
          errorWindow = new JWindow(topLevelWin);
          JPanel contentPane = (JPanel) errorWindow.getContentPane();
          contentPane.add(errorLabel);
          contentPane.setBackground(Color.YELLOW);
          contentPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
          contentPane.setFont(new Font("Arial", Font.BOLD,12));
          errorWindow.pack();
       }

       Point loc = textField.getLocationOnScreen();
       errorWindow.setLocation(loc.x + 20, loc.y + 20);
       errorWindow.setSize(92, 20);
       errorWindow.setVisible(true);
    }

	
	/**
     * 
     * @param text
     * @return
     */
    private boolean textOK(String text) {
       if (text.matches(REGEX_TEST)) {
          return true;
       }
       return false;
    }
    
    /**
     * Click a button on screen
     *
     * @param button Button to click
     * @param millis Time that button will remain "clicked" in milliseconds
     */
    public void click(AbstractButton button, int millis) {
       button.doClick(millis);
    }
    
    /**
     * 
     * @author Wild
     *
     */
    private class MyNumberDocFilter extends DocumentFilter {
    	JTextField textField = new JTextField();
    	String ERROR_TEXT = "";
    	MyNumberDocFilter(JTextField textField, String ERROR_TEXT){
    		this.textField = textField;
    		this.ERROR_TEXT = ERROR_TEXT;
    	}
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                 AttributeSet attr) throws BadLocationException {
           if (textOK(string)) {
              super.insertString(fb, offset, string, attr);
              if (errorWindow != null && errorWindow.isVisible()) {
                 errorWindow.setVisible(false);
              }
           } else {
              showErrorWin(textField,ERROR_TEXT);
           }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                 AttributeSet attrs) throws BadLocationException {
           if (textOK(text)) {
              super.replace(fb, offset, length, text, attrs);
              if (errorWindow != null && errorWindow.isVisible()) {
                 errorWindow.setVisible(false);
              }
           } else {
              showErrorWin(textField,ERROR_TEXT);
           }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length)
                 throws BadLocationException {
           super.remove(fb, offset, length);
           if (errorWindow != null && errorWindow.isVisible()) {
              errorWindow.setVisible(false);
           }
        }
     }

  //helper functions
  	/**
  	 * Sets the number of blocks in combo box on the main panel
  	 * @param runs
  	 */
  	public void setNumberOfComboBlocks(int runs){
  		  ExperimentSetupWindow.blockSelectionCombo.removeAllItems();
  		  String[] description = new String[runs];
  		  for(int i =0;i<runs;i++){
  			description[i] = "Block "+(i+1);
  		  }
  		  for (int i = 0; i < runs; i++)
  			ExperimentSetupWindow.blockSelectionCombo.addItem(description[i]);
  	  }
  	
  	/**
  	 * Saves program properties
  	 */
  	public void saveParamChanges() {
  	    try {
  	        Properties props = new Properties();
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
		  	props.setProperty("TriggerUpperLeftIsSelected", ""+triggerUpperLeftIsSelected);
		  	props.setProperty("TriggerLowerLeftIsSelected", ""+triggerLowerLeftIsSelected);
		  	props.setProperty("TriggerLowerRightIsSelected", ""+triggerLowerRightIsSelected);
		  	props.setProperty("TriggerUpperRightIsSelected", ""+triggerUpperRightIsSelected);
		  	props.setProperty("TriggerOtherIsSelected", ""+triggerOtherIsSelected);
  	        File f = new File("program.properties");
  	        OutputStream out = new FileOutputStream( f );
  	        props.store(out, "Properties for the Auditory Task program");
  	    }
  	    catch (Exception e ) {
  	        e.printStackTrace();
  	    }
  	}
  	
  	
  	/**
	 * load parameters from properties file
	 * @throws IOException 
	 */
	public static void loadParamsWithoutSettingAnyValues()  {
	    Properties props = new Properties();
	    InputStream is = null;
		try {
		  
	        File f = new File("program.properties");

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
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  	
	
  	/**
	 * load parameters from properties file
	 * @throws IOException 
	 */
	public static void loadParams()  {
	    Properties props = new Properties();
	    InputStream is = null;
		try {
		  
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
			  	props.setProperty("TriggerUpperLeftIsSelected", ""+triggerUpperLeftIsSelected);
			  	props.setProperty("TriggerLowerLeftIsSelected", ""+triggerLowerLeftIsSelected);
			  	props.setProperty("TriggerLowerRightIsSelected", ""+triggerLowerRightIsSelected);
			  	props.setProperty("TriggerUpperRightIsSelected", ""+triggerUpperRightIsSelected);
			  	props.setProperty("TriggerOtherIsSelected", ""+triggerOtherIsSelected);

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
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  	/**
  	 * 
  	 * @return
  	 */
  	public boolean checkIfAnyChanges(){
  		Properties props = new Properties();
 	    InputStream is = null;
 	    
 	    boolean anyTextValueChanged = false;
 	  
         File f = new File("program.properties");
         try {
			is = new FileInputStream( f );
			// Try loading properties from the file (if found)
	        props.load( is );
	        int stimulusDurationTemp=new Integer(props.getProperty("StimulusDuration", String.valueOf(Parameters.STIMULUS_TASK_DURATION)));
	  	    int preStimulusDurationTemp=new Integer(props.getProperty("PreStimulusDuration", String.valueOf(Parameters.PRESTIMULUS_TASK_DURATION)));
	  	    int intertrialDurationTemp=new Integer(props.getProperty("IntertrialDuration", String.valueOf(Parameters.INTERTRIAL_TASK_DURATION)));
	  	    int delayDurationTemp=new Integer(props.getProperty("DelayDuration", String.valueOf(Parameters.DELAY_TASK_DURATION)));
	  	    int probeDurationTemp=new Integer(props.getProperty("ProbeDuration", String.valueOf(Parameters.PROBE_TASK_DURATION_IF_IDLE)));
	  	    int numberOfBetsPerTrialTemp=new Integer(props.getProperty("NumberOfBetsPerTrial", String.valueOf(Parameters.NUMBER_OF_BETS_PER_TRIAL)));
	  	    int maxPointsPerBetTemp=new Integer(props.getProperty("MaxPointsPerBet", String.valueOf(Parameters.MAX_POINTS_PER_BET)));
	  	    int numTrialsTemp=new Integer(props.getProperty("NumTrials", String.valueOf(Parameters.NUM_EXPERIMENTAL_TRIALS)));
	  	    int numRunsTemp=new Integer(props.getProperty("NumRuns", String.valueOf(Parameters.RUN_NUM_DEFAULT)));
	  	    String studyIdTemp = props.getProperty("StudyId",  String.valueOf(Parameters.STUDY_ID_DEFAULT));
	  	    int studyVerTemp = new Integer(props.getProperty("StudyVer",  String.valueOf(Parameters.STUDY_VER_DEFAULT)));
	  	    int numOfTrialsWithOneBetTemp=new Integer(props.getProperty("NumOfTrialsWithOneBet", String.valueOf(Parameters.TRIALS_ONE_BET_NUM_DEFAULT)));
	  	    int numOfDemoTrialsNotTimedTemp=new Integer(props.getProperty("NumOfDemoTrialsNotTimed", String.valueOf(Parameters.TRIALS_NOT_TIMED_NUM_DEFAULT)));
	  	    int numOfDemoTrialsTimedTemp=new Integer(props.getProperty("NumOfDemoTrialsTimed", String.valueOf(Parameters.TRIALS_TIMED_NUM_DEFAULT)));
	  	    int numOfPracticeBetTrialsTemp=new Integer(props.getProperty("NumOfPracticeBetTrials", String.valueOf(Parameters.TRIALS_PRACTICE_BETS_NUM_DEFAULT)));
	  	    float maxFrequencyTemp=new Float(props.getProperty("MaxFrequency", String.valueOf(Parameters.MAX_FREQ_DEFAULT)));
	  	    float minFrequencyTemp=new Float(props.getProperty("MinFrequency", String.valueOf(Parameters.MIN_FREQ_DEFAULT)));
	  	    int numFrequenciesTemp=new Integer(props.getProperty("NumFrequencies", String.valueOf(Parameters.SAMPLE_FREQ_NUM_DEFAULT)));
	  	    float jitterTemp=new Float(props.getProperty("Jitter", String.valueOf(Parameters.JITTER_DEFAULT)));
	  	    boolean triggerIsSelectedTemp=new Boolean(props.getProperty("TriggerIsSelected", String.valueOf(Parameters.IS_TRIGGER_SELECTED)));
	  	    int leftCornerXTemp=new Integer(props.getProperty("LeftCornerX", String.valueOf(Parameters.TRIGGER_LEFT_CORNER_X)));
	  	    int leftCornerYTemp=new Integer(props.getProperty("LeftCornerY", String.valueOf(Main.screenHeight - Parameters.TRIGGER_HEIGHT)));
	  	    int triggerWidthTemp=new Integer(props.getProperty("TriggerWidth", String.valueOf(Parameters.TRIGGER_WIDTH)));
	  	    int triggerHeightTemp=new Integer(props.getProperty("TriggerHeight", String.valueOf(Parameters.TRIGGER_HEIGHT)));
	  	    boolean isProbeTaskTimedTemp=new Boolean(props.getProperty("IsProbeTaskTimed", String.valueOf(Parameters.IS_PROBE_TASK_TIMED)));  
	  	    int fontSizeTemp=new Integer(props.getProperty("FontSize", String.valueOf(Parameters.FONT_SIZE)));  
	  	    String fontFamilyTemp=new String(props.getProperty("FontFamily", String.valueOf(Parameters.FONT_FAMILY)));
	  	    boolean triggerUpperLeftIsSelectedTemp = new Boolean(props.getProperty("TriggerUpperLeftIsSelected", String.valueOf(false)));  
	  	    boolean triggerLowerLeftIsSelectedTemp = new Boolean(props.getProperty("TriggerLowerLeftIsSelected", String.valueOf(true)));  
	  	    boolean triggerUpperRightIsSelectedTemp = new Boolean(props.getProperty("TriggerUpperRightIsSelected", String.valueOf(false)));  
	  	    boolean triggerLowerRightIsSelectedTemp = new Boolean(props.getProperty("TriggerLowerRightIsSelected", String.valueOf(false)));  
	  	    boolean triggerOtherIsSelectedTemp = new Boolean(props.getProperty("TriggerOtherIsSelected", String.valueOf(false))); 
	  	    if(
	  	    		stimulusDurationTemp != stimulusDuration ||
	  	    		preStimulusDurationTemp != preStimulusDuration||
	  	    		intertrialDurationTemp!=intertrialDuration||
	  	    		delayDurationTemp !=delayDuration||
	  	    		probeDurationTemp!= probeDuration||
	  	    		numberOfBetsPerTrialTemp!=numberOfBetsPerTrial||
	  	    		maxPointsPerBetTemp!= maxPointsPerBet||
	  	    		numTrialsTemp!=numTrials||
	  	    		numRunsTemp!=numRuns||
	  	    		!studyIdTemp.equals(studyId) ||
	  	    		studyVerTemp != studyVer ||
	  	    		numOfTrialsWithOneBetTemp != numOfTrialsWithOneBet||
	  	    		numOfDemoTrialsNotTimedTemp != numOfDemoTrialsNotTimed||
	  	    		numOfDemoTrialsTimedTemp != numOfDemoTrialsTimed||
	  	    		numOfPracticeBetTrialsTemp != numOfPracticeBetTrials ||
	  	    		maxFrequencyTemp !=maxFrequency||
	  	    		minFrequencyTemp!=minFrequency||
	  	    		numFrequenciesTemp!=numFrequencies||
	  	    		jitterTemp!=jitter||
	  	    		triggerIsSelectedTemp!=triggerIsSelected||
	  	    		leftCornerXTemp!=leftCornerX||
	  	    		leftCornerYTemp!=leftCornerY||
	  	    		triggerWidthTemp!=triggerWidth||
	  	    		triggerHeightTemp!=triggerHeight||
	  	    		isProbeTaskTimedTemp != isProbeTaskTimed||
	  	    		fontSizeTemp!= fontSize ||
	  	    		!fontFamilyTemp.equals(fontFamily) ||
	  	    		triggerUpperLeftIsSelectedTemp 	!= triggerUpperLeftIsSelected ||
	  	    		triggerLowerLeftIsSelectedTemp 	!= triggerLowerLeftIsSelected ||
	  	    		triggerUpperRightIsSelectedTemp != triggerUpperRightIsSelected||
	  	    		triggerLowerRightIsSelectedTemp != triggerLowerRightIsSelected||
	  	    		triggerOtherIsSelectedTemp != triggerOtherIsSelected
	  	    		)
	  	    	anyTextValueChanged = true;
	  	    else
	  	    	anyTextValueChanged = false;
	  	    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return anyTextValueChanged;
 	
  	}
  	
  	/**
  	 * 
  	 * @author Wild
  	 *
  	 */
  	 private class ComboRenderer extends BasicComboBoxRenderer {

         private static final long serialVersionUID = 1L;
         private JComboBox comboBox;
         final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
         private int row;

         private ComboRenderer(JComboBox fontsBox) {
             comboBox = fontsBox;
         }

         private void manItemInCombo() {
             if (comboBox.getItemCount() > 0) {
                 final Object comp = comboBox.getUI().getAccessibleChild(comboBox, 0);
                 if ((comp instanceof JPopupMenu)) {
                     final JList list = new JList(comboBox.getModel());
                     final JPopupMenu popup = (JPopupMenu) comp;
                     final JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
                     final JViewport viewport = scrollPane.getViewport();
                     final Rectangle rect = popup.getVisibleRect();
                     final Point pt = viewport.getViewPosition();
                     row = list.locationToIndex(pt);
                 }
             }
         }

         @Override
         public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
             super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
             if (list.getModel().getSize() > 0) {
                 manItemInCombo();
             }
             final JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, row, isSelected, cellHasFocus);
             final Object fntObj = value;
             final String fontFamilyName = (String) fntObj;
             setFont(new Font(fontFamilyName, Font.PLAIN, 16));
             return this;
         }
     }
  	 
  	 
    
}
