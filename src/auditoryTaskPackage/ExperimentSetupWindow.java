package auditoryTaskPackage;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class ExperimentSetupWindow extends JFrame{
	//declare variable
	private JPanel contentPane;
	private JLabel contact;
    private JLabel website;
    private JFrame about = new JFrame();
    public JPanel triggerPanel = new JPanel(new GridBagLayout());
    public JPanel experimentPanel = new JPanel(new GridBagLayout());
    public JPanel practicePanel = new JPanel(new GridBagLayout());


    public static int sessionNum;
    public static String subNum;
    
 
    
    public static String resDirectory = System.getProperty("user.dir");
    
    
    public static boolean startPracticeVersion = true;
    public static String practiceLogFileName;
    public static String experimentLogFileName;
    
    static final int WIN_WIDTH = 700;
    static final int WIN_HEIGHT = 280;
    static final int WIN_LEFT_X = 20;
    static final int WIN_LEFT_Y = 5;
    private static final String REGEX_TEST = "\\d*";

    ArrayList <Integer> allAccomplishedBlocks = new ArrayList<Integer>();
    public static int numRunsInstructions = 0; 
    public static int numRunsPractice = 0; 
	public static int currentBlockRunning;
	//----------------------------------------------------------------------------------
	final static JComboBox blockSelectionCombo = new JComboBox();
	
	//to prevent running several blocks in the meantime
	public static boolean startBlock = true;
	
	
	//to prevent running several instructions screens in the meantime
	public static boolean startInstructions = true;
	
	//to prevent running several practice screens in the meantime
	public static boolean startPractice = true;
	
	
    //private JTextField textField = new JTextField(10);
    public JWindow errorWindow;
   
	/**
	 * Create the frame.
	 * @throws FileNotFoundException 
	 */
	public ExperimentSetupWindow() throws FileNotFoundException//constructor 
	{
    	//set icon image
    	setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		//set frame title
		setTitle("Experiment Setup");
		//set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set bounds of the frame
		setBounds(WIN_LEFT_X, WIN_LEFT_Y, WIN_LEFT_X + WIN_WIDTH, WIN_LEFT_Y + WIN_HEIGHT);
		setSize(WIN_WIDTH,WIN_HEIGHT);
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

		
		//INIT COMBO BOX	
  		//----------------------------------------------------------------------------------   
  		setNumberOfComboBlocks(AdvancedOptionsWindow.numRuns);
		
  		currentBlockRunning = blockSelectionCombo.getSelectedIndex();
  		
		
	    //----------------------------------------------------------------------------------
	    JLabel sessionNumLabel = new JLabel("Session number");
	   
	    final JTextField sessionNumText = new JTextField(String.valueOf(Parameters.SESSION_NUM_DEFAULT));
	   
	    //----------------------------------------------------------------------------------
	    JLabel subNumLabel = new JLabel("Subject number");
	   
	    final JTextField subNumText = new JTextField(String.valueOf(Parameters.SUB_NUM_DEFAULT));
	    

		//----------------------------------------------------------------------------------
	    //declare buttons
	    //----------------------------------------------------------------------------------	
  		
  		JButton jbtReset = new JButton("Reset");
  		
  		JButton jbtInstructions  = new JButton("Instructions");
  		
  		JButton jbtPractice  = new JButton("Practice");
  		
  		JButton jbtVolume = new JButton("Set Volume");
		
  		JButton jbtRunBlock = new JButton("Run Block");
  		
  		JPanel tempButtonPanel = new JPanel(new GridLayout(1,2,15,5));
  		tempButtonPanel.add(jbtReset);
  		tempButtonPanel.add(jbtVolume);
  		
  		//---------------------------------------------------------------------------------------------------//
	    //	main button panel
			
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cButtonPanel = new GridBagConstraints();
		cButtonPanel.fill = GridBagConstraints.HORIZONTAL;
		cButtonPanel.insets = new Insets(5,5,0,5); 
		cButtonPanel.anchor = GridBagConstraints.NORTH;
		cButtonPanel.weightx = 1;	
		cButtonPanel.weighty = 1;
		cButtonPanel.gridwidth  = 1;

		cButtonPanel.gridx = 0;
		cButtonPanel.gridy = 0;
		buttonPanel.add(tempButtonPanel,cButtonPanel);

		
		//---------------------------------------------------------------------------------------------------//
	    //	practice button panel
			
		JPanel tempPracticeButtonPanel = new JPanel(new GridLayout(2,1,5,5));
		tempPracticeButtonPanel.add(jbtInstructions);
		tempPracticeButtonPanel.add(jbtPractice);
		
		JPanel practiceButtonPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cPracticeButtonPanel = new GridBagConstraints();
		cPracticeButtonPanel.fill = GridBagConstraints.HORIZONTAL;
		cPracticeButtonPanel.insets = new Insets(5,5,0,5); 
		cPracticeButtonPanel.anchor = GridBagConstraints.NORTH;
		cPracticeButtonPanel.weightx = 1;
		//cPracticeButtonPanel.weighty = 1;
		cPracticeButtonPanel.gridwidth  = 1;

		cPracticeButtonPanel.gridx = 0;
		cPracticeButtonPanel.gridy = 0;
		practiceButtonPanel.add(tempPracticeButtonPanel,cPracticeButtonPanel);
		
		TitledBorder comp = new TitledBorder("Press to run practice session");
		Border border = comp.getBorder();
		Border margin = new EmptyBorder(5,5,10,5);
		comp.setBorder(new CompoundBorder(border, margin));
		practiceButtonPanel.setBorder(comp);
		
		
		//---------------------------------------------------------------------------------------------------//
	    //	blocks panel
			
		JPanel tempBlockComboPanel = new JPanel(new GridLayout(2,1,5,5));
		tempBlockComboPanel.add(blockSelectionCombo);
		tempBlockComboPanel.add(jbtRunBlock);

		
		JPanel blockComboPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints cBlockComboPanel = new GridBagConstraints();
		cBlockComboPanel.fill = GridBagConstraints.HORIZONTAL;
		cBlockComboPanel.insets = new Insets(5,5,0,5); 
		cBlockComboPanel.anchor = GridBagConstraints.NORTH;
		cBlockComboPanel.weightx = 1;	
		cBlockComboPanel.weighty = 1;
		cBlockComboPanel.gridwidth  = 1;

		cBlockComboPanel.gridx = 0;
		cBlockComboPanel.gridy = 0;
		blockComboPanel.add(tempBlockComboPanel,cBlockComboPanel);
		
		TitledBorder blockComponentPanel = new TitledBorder("Select the block to run");
		Border blockCompPanelBorder = comp.getBorder();
		Border blockCompPanelMargin = new EmptyBorder(5,5,5,5);
		blockComponentPanel.setBorder(new CompoundBorder(blockCompPanelBorder, blockCompPanelMargin));
		blockComboPanel.setBorder(blockComponentPanel);
		
		
		
		//---------------------------------------------------------------------------------------------------//
	    //	experiment parameters panel
		
	    JPanel tempExpP = new JPanel(new GridLayout(2,2,5,5));
	    
	    tempExpP.add(subNumLabel);
		tempExpP.add(subNumText);
		tempExpP.add(sessionNumLabel);
		tempExpP.add(sessionNumText);
		
		GridBagConstraints cExperimentParametersPanel = new GridBagConstraints();
		cExperimentParametersPanel.fill = GridBagConstraints.HORIZONTAL;
		cExperimentParametersPanel.insets = new Insets(5,5,0,5); 
		cExperimentParametersPanel.anchor = GridBagConstraints.NORTH;
		cExperimentParametersPanel.weightx = 1;
		cExperimentParametersPanel.gridx = 0;
		cExperimentParametersPanel.gridy = 0;
		experimentPanel.add(tempExpP,cExperimentParametersPanel);
		
		
		TitledBorder expParamsPanelBorderComp = new TitledBorder("Experiment parameters ");
		Border expParamsPanelBorder = expParamsPanelBorderComp.getBorder();
		Border expParamsBorderMargin = new EmptyBorder(5,5,15,5);
		expParamsPanelBorderComp.setBorder(new CompoundBorder(expParamsPanelBorder, expParamsBorderMargin));
		
		experimentPanel.setBorder(expParamsPanelBorderComp);
		
	
		//---------------------------------------------------------------------------------------------------//
		//place all the panels on the GridBagPanel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,5,0,5); 
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		//c.weighty = 1;
		
		c.gridheight = 3;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(experimentPanel,c);
		
		c.gridheight = 2;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 0;
		contentPane.add(practiceButtonPanel,c);
		
		c.gridheight = 2;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 2;
		contentPane.add(blockComboPanel,c);
		
		c.gridheight = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 3;
		contentPane.add(buttonPanel,c);
	
		//---------------------------------------------------------------------------------------------------//
		
  		//action listener for text fields
	    //----------------------------------------------------------------------------------
		((PlainDocument)sessionNumText.getDocument()).setDocumentFilter(new MyNumberDocFilter(sessionNumText, "Enter a number"));

  		
	  		
	  		
	    //Instructions button action listener
	    
	    jbtInstructions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean runInstructions = false;
				if(numRunsInstructions==0 && startInstructions)
					runInstructions = true;
				if(numRunsInstructions>0){
					String timesMsg = "";
        		  	String message = String.format("<html><center> You have already run instructions module %d %s.<br>"
					 		+ "Would you like to run it again?",(numRunsInstructions), (numRunsInstructions ==1? timesMsg = "time":"times"));
					  
        		  	JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
				    
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
			        	runInstructions = true;
			        }
			        else if (c == JOptionPane.NO_OPTION) {
			        	runInstructions = false;
			        }
				
				}
				
				if(startInstructions && runInstructions){
					//get variables from textFileds
					//TODO: if the field for subject is empty or entered number already contains the subject data -- show a warning
					String subNumTextString = subNumText.getText();
					String sessionNumTextString = sessionNumText.getText();

					boolean startTask = true;
				
					if(	subNumTextString.isEmpty()		 					||
						sessionNumTextString.isEmpty()
						){
							String message = new String();
							
							if(subNumTextString.isEmpty())
								message = String.format("<html><center> Please enter the subject ID to proceed.</center></html>");
							
							if(sessionNumTextString.isEmpty())
								message = String.format("<html><center> Please enter study version to proceed.</center></html>");
							
			    			JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);
			    			startTask=false;
					}
					else{
						subNum = subNumTextString.toString();
						sessionNum = Integer.parseInt(sessionNumTextString);	
					}//end of else
				    	
				    if(startTask){
				    	numRunsInstructions +=1;
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
						        
					        String logDirectoryName = subDirectoryName+"//Logs";
	  						 File logFile = new File(logDirectoryName);
	  					        if (!logFile.exists()) {
	  					            if (logFile.mkdir()) {
	  					                System.out.println("Directory "+logDirectoryName+" is created!");
	  					            } else {
	  					                System.out.println("Failed to create directory "+ logDirectoryName);
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
						        
					        String tmDirectoryName = subDirectoryName+"//TaskMaps";
							 File tmFile = new File(tmDirectoryName);
						        if (!tmFile.exists()) {
						            if (tmFile.mkdir()) {
						                System.out.println("Directory "+tmDirectoryName+" is created!");
						            } else {
						                System.out.println("Failed to create directory "+ tmDirectoryName);
						            }
						        }
						RunExperimentWindow.practiceTaskMapFileName = tmDirectoryName+"//p_"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_tm.txt";
						RunExperimentWindow.experimentTaskMapFileName = tmDirectoryName+"//"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_block"+(currentBlockRunning+1)+"_tm.txt";

						
			            // Create a new Thread to prevent GUI freezing
			            Thread t = new Thread() {
			               @Override
			               public void run() {
			            	   // TODO Auto-generated method stub
			            	   try {
			            		   //dispose();
			            		 
			            		   RunExperimentWindow.runInstructionsModule();
			            		   } catch (InterruptedException | AWTException | IOException e) {
			            			   // TODO Auto-generated catch block
			            			   e.printStackTrace();
			            			   } 
			            	   // Suspend this thread via sleep() and yield control to other threads.
			            	   // Also provide the necessary delay.
			            	   try {
			            		   sleep(10);  // milliseconds
			            		   } catch (InterruptedException ex) {}
			            	   }
			               };
			               t.start();  // call back run()
			               }	
				    }//end of if
				}
			});   	
	  		
	    
	     
	    
	    //Practice button action listener
	    
	    jbtPractice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				boolean runPractice = false;
				if(numRunsPractice==0 && startPractice)
					runPractice = true;
				if(numRunsPractice>0){
					String timesMsg = "";
        		  	String message = String.format("<html><center> You have already run practice module %d %s.<br>"
					 		+ "Would you like to run it again?",(numRunsPractice),(numRunsPractice ==1? timesMsg = "time":"times"));
					  
        		  	JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
				    
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
			        	runPractice = true;
			        }
			        else if (c == JOptionPane.NO_OPTION) {
			        	runPractice = false;
			        }
				
				}

				if(startPractice && runPractice){
					
					//get variables from textFileds
					
					String subNumTextString = subNumText.getText();
					String sessionNumTextString = sessionNumText.getText();
					
					boolean startTask = true;
				
					if(	subNumTextString.isEmpty()		 					||
						sessionNumTextString.isEmpty()
						){
							String message = new String();
							
							if(subNumTextString.isEmpty())
								message = String.format("<html><center> Please enter the subject ID to proceed.</center></html>");
							
							if(sessionNumTextString.isEmpty())
								message = String.format("<html><center> Please enter study version to proceed.</center></html>");
							
			    			JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);
			    			startTask=false;
					}
					else{
						subNum = subNumTextString.toString();
						sessionNum = Integer.parseInt(sessionNumTextString);	
					}//end of else
					
				    if(startTask){
				    	 numRunsPractice+=1;
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
						        
						     String logDirectoryName = subDirectoryName+"//Logs";
		  						 File logFile = new File(logDirectoryName);
		  					        if (!logFile.exists()) {
		  					            if (logFile.mkdir()) {
		  					                System.out.println("Directory "+logDirectoryName+" is created!");
		  					            } else {
		  					                System.out.println("Failed to create directory "+ logDirectoryName);
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
						        
					        String tmDirectoryName = subDirectoryName+"//TaskMaps";
							 File tmFile = new File(tmDirectoryName);
						        if (!tmFile.exists()) {
						            if (tmFile.mkdir()) {
						                System.out.println("Directory "+tmDirectoryName+" is created!");
						            } else {
						                System.out.println("Failed to create directory "+ tmDirectoryName);
						            }
						        }
						practiceLogFileName = logDirectoryName+"//p_"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_log.txt";
						RunExperimentWindow.practiceTaskMapFileName = tmDirectoryName+"//p_"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_tm.txt";
						RunExperimentWindow.experimentTaskMapFileName = tmDirectoryName+"//"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_block"+(currentBlockRunning+1)+"_tm.txt";
						
						
						writeToLogFile(practiceLogFileName);
						//writeToLogFile(experimentLogFileName);
				        
			            // Create a new Thread to prevent GUI freezing
			            Thread t = new Thread() {
			               @Override
			               public void run() {
			            	   // TODO Auto-generated method stub
			            	   try {
			            		   //dispose();
			            		
			            		   RunExperimentWindow.runPracticeTrialsModule();
			            		   } catch (InterruptedException | AWTException | IOException e) {
			            			   // TODO Auto-generated catch block
			            			   e.printStackTrace();
			            			   } 
			            	   // Suspend this thread via sleep() and yield control to other threads.
			            	   // Also provide the necessary delay.
			            	   try {
			            		   sleep(10);  // milliseconds
			            		   } catch (InterruptedException ex) {}
			            	   }
			               };
			               t.start();  // call back run()
			               }	
				    }
				}// end of checking whether the startPractice is true

			});   	
	  	
	    
	    
	    //jbtRunBlock button action listener
	  	
	    jbtRunBlock.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  currentBlockRunning = blockSelectionCombo.getSelectedIndex();
	        	  System.out.println(blockSelectionCombo.getSelectedItem() + "   index # " + currentBlockRunning);
	        	 
	        	  boolean runBlock = false;
				  if(startBlock)
					  	runBlock = true;
	        	  if(allAccomplishedBlocks.contains(currentBlockRunning)){
	        		  	
	        		  	String message = String.format("<html><center> You have already run block # %d.<br>"
						 		+ "Would you like to run it again?",(currentBlockRunning+1));
						  
	        		  	JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
					    
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
				        	runBlock = true;
				        }
				        else if (c == JOptionPane.NO_OPTION) {
				        	runBlock = false;
				        }
	        	  }
	        		  

	        	  if(startBlock && runBlock){
	        		
	        		String subNumTextString = subNumText.getText();
					String sessionNumTextString = sessionNumText.getText();
					
					boolean startTask = true;
				
					if(	subNumTextString.isEmpty()		 					||
						sessionNumTextString.isEmpty()
						){
							String message = new String();
							
							if(subNumTextString.isEmpty())
								message = String.format("<html><center> Please enter the subject ID to proceed.</center></html>");
							
							if(sessionNumTextString.isEmpty())
								message = String.format("<html><center> Please enter study version to proceed.</center></html>");
							
			    			JOptionPane.showMessageDialog(null, message, "Error Message",JOptionPane.ERROR_MESSAGE);
			    			startTask=false;
					}
					else{
						subNum =subNumTextString.toString();
						sessionNum = Integer.parseInt(sessionNumTextString);	
					}//end of else
	  			    	
	  			    if(startTask){
	  			    	allAccomplishedBlocks.add(currentBlockRunning);
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
	  					        
	  					      String logDirectoryName = subDirectoryName+"//Logs";
		  						 File logFile = new File(logDirectoryName);
		  					        if (!logFile.exists()) {
		  					            if (logFile.mkdir()) {
		  					                System.out.println("Directory "+logDirectoryName+" is created!");
		  					            } else {
		  					                System.out.println("Failed to create directory "+ logDirectoryName);
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
	  					        
	  				        String tmDirectoryName = subDirectoryName+"//TaskMaps";
	  						 File tmFile = new File(tmDirectoryName);
	  					        if (!tmFile.exists()) {
	  					            if (tmFile.mkdir()) {
	  					                System.out.println("Directory "+tmDirectoryName+" is created!");
	  					            } else {
	  					                System.out.println("Failed to create directory "+ tmDirectoryName);
	  					            }
	  					        }
	  					experimentLogFileName = logDirectoryName+"//"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_block"+(currentBlockRunning+1)+"_log.txt";
	  					RunExperimentWindow.practiceTaskMapFileName = tmDirectoryName+"//p_"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_tm.txt";
						RunExperimentWindow.experimentTaskMapFileName = tmDirectoryName+"//"+AdvancedOptionsWindow.studyId+AdvancedOptionsWindow.studyVer+subNumStr+sessionNum+"_"+dateFormat.format(date)+"_block"+(currentBlockRunning+1)+"_tm.txt";
						
						 //writeToLogFile(practiceLogFileName);
						 writeToLogFile(experimentLogFileName);
	        	  
						 // Create a new Thread to prevent GUI freezing
						 Thread t = new Thread() {
						 @Override
						 public void run() {
		            	   // TODO Auto-generated method stub
		            	   try {
		            		   //dispose();
		            		   
		            		   RunExperimentWindow.runBlockModule();
		            		   } catch (InterruptedException | AWTException | IOException e) {
		            			   // TODO Auto-generated catch block
		            			   e.printStackTrace();
		            			   } 
		            	   // Suspend this thread via sleep() and yield control to other threads.
		            	   // Also provide the necessary delay.
		            	   try {
		            		   sleep(10);  // milliseconds
		            		   } catch (InterruptedException ex) {}
		            	   }
						 };
						 t.start();  // call back run()
		               	}
	        	 }
	          }//check whether the startBlock is true 
	    });
	    
	    
	    //reset button action listener
	  	
	    jbtReset.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  
				  sessionNumText.setText(String.valueOf(Parameters.SESSION_NUM_DEFAULT));
	        	  subNumText.setText(String.valueOf(Parameters.SUB_NUM_DEFAULT));
	        	  sessionNum = Parameters.SESSION_NUM_DEFAULT;
	        	  subNum = Parameters.SUB_NUM_DEFAULT;
	        	  numRunsInstructions = 0;
	        	  numRunsPractice = 0;
	        	  allAccomplishedBlocks.clear();
	        	  setNumberOfComboBlocks(Parameters.RUN_NUM_DEFAULT);
	        	  
	          }
	    });

	    //adjust volume action listener
	  	
	    jbtVolume.addActionListener(new ActionListener() {
	    	
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	 
					EventQueue.invokeLater(new Runnable()
		      		{
		      			public void run()
		      			{
		      				
		      				//try - catch block
		      				try 
		      				{
		      					//first load program.properties
			      				RunExperimentWindow.loadParams();
		      					//Create object of InstructionWindow
		      					AdjustVolumePreviewWindow frame = new AdjustVolumePreviewWindow();
		      					//set frame visible true
		      					frame.setVisible(true);					
		      				} 
		      				catch (Exception e)
		      				{
		      					e.printStackTrace();
		      				}
		      			}
		      		});

	        	  
	        	  
	          }
	    });
	    
	    
	 
	  		
		//add a menu bar
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem chooseItem = new JMenuItem("Choose Folder");
        fileMenu.add(chooseItem);
        
        JMenu editMenu = new JMenu("Edit");
		JMenuItem advancedOptions = new JMenuItem("Advanced Options");
		editMenu.add(advancedOptions);
        
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpContentsItem = new JMenuItem("Help Contents");
		JMenuItem aboutItem = new JMenuItem("About");
		
		
		
		//specify action listeners
		//----------------------------------------------------------------------------------------------//
		
		chooseItem.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  JFileChooser chooser = new JFileChooser();
	        	  chooser.setCurrentDirectory(new java.io.File("."));
	        	  chooser.setDialogTitle("select folder");
	        	  chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        	  chooser.setAcceptAllFileFilterUsed(false);
	        	  if (chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) { 
	        	      resDirectory = chooser.getSelectedFile().toString();
	        	      }
	        	    else {
	        	      System.out.println("No Selection ");
	        	      resDirectory = System.getProperty("user.dir");
	        	      }
	        	     
	        	  System.out.println("Resulting directory: "+resDirectory);
	          }
		});
		

		
		advancedOptions.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  EventQueue.invokeLater(new Runnable()
					{
						public void run()
						{
							System.out.println("Advanced Options");
							//Create object of InstructionWindow
							final AdvancedOptionsWindow setAdvancedOptionsFrame = AdvancedOptionsWindow.getInstance();
							//set frame visible true
							setAdvancedOptionsFrame.setVisible(true);	
							//on closing the setTriggerParamsFrame --  enable the checkbox
							setAdvancedOptionsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
							    @Override
							    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							        System.out.println("Closing setTriggerParameters frame");
							        setAdvancedOptionsFrame.dispose();
							        AdvancedOptionsWindow.addTrigger.setEnabled(true);
							    }
							});
						}
					});		
	          }
		});

		aboutItem.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  JPanel finalFrame=new JPanel(null);
		          ImageIcon img = new ImageIcon(getClass().getResource("MainLogo.png"));
	              JLabel imgLabel = new JLabel();
	              imgLabel.setBounds(10,5, 430, 75);
	              imgLabel.setIcon(img);
	              
	              JLabel progName = new JLabel(Parameters.VERSION_INFO);
	              progName.setBounds(10, 100, 430, 25);
	              JLabel progDate = new JLabel(Parameters.VERSION_DATE);
	              progDate.setBounds(10, 125, 430, 25);
	              contact = new JLabel();
	              website = new JLabel();
	              contact.setBounds(10,150,430, 25);
	              website.setBounds(10,175,430, 25);
	            
	              contact.setText("<html> Contact : <a href=\"\">vahanbabushkin@gmail.com</a></html>");
	              contact.setCursor(new Cursor(Cursor.HAND_CURSOR));
	
	              website.setText("<html> Website : <a href=\"\">http://sites.nyuad.nyu.edu/faculty/sreenivasan/</a></html>");
	              website.setCursor(new Cursor(Cursor.HAND_CURSOR));
	              
	              JLabel copyrightText = new JLabel("Copyright: Sreenivasan Lab @nyuad, 2016");
	              copyrightText.setBounds(10,200,430, 25);
	              
	              JLabel rightsReservedText = new JLabel("Licensed under GNU General Public License (GPL) version 3");
	              rightsReservedText.setBounds(10,225,430, 25);
	              
	              finalFrame.add(imgLabel);
	              finalFrame.add(progName);
	              finalFrame.add(progDate);
	              finalFrame.add(contact);
	              finalFrame.add(website);
	              finalFrame.add(copyrightText);
	              finalFrame.add(rightsReservedText);
	              about.add(finalFrame);
	              about.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
	              about.setTitle( "About" );
	              about.setSize( 450, 300 );
	              about.setResizable(false);
	              about.setLocationRelativeTo(null);  
	              about.setVisible(true);
	              sendMail(contact);
	              goWebsite(website);
	              
	          }
	      });

		//this piece of code enables using internal jar files as resources
		
		File file = null;
        String resource = "/helpFiles/audioTaskHelp.txt" ;
        URL res = getClass().getResource(resource);
        System.out.println(res.toString());
        if (res.toString().startsWith("jar:")) {
            try {
                InputStream input = getClass().getResourceAsStream(resource);
                file = File.createTempFile(new Date().getTime()+"", ".html");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
                input.close();
                file.deleteOnExit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            //this will probably work in your IDE, but not from a JAR
            file = new File(res.getFile());
        }
		
       
        Scanner scanner = new Scanner(file);
		final String content=scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		//listener for help browser
		helpContentsItem.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	// create jeditorpane
	              JEditorPane editor = new JEditorPane();
	              
	              // make it read-only
	              editor.setEditable(false);
	              
	              // create a scrollpane; modify its attributes as desired
	              JScrollPane scrollPane = new JScrollPane(editor);
	              
	              // add an html editor kit
	              HTMLEditorKit kit = new HTMLEditorKit();
	              editor.setEditorKit(kit);
	              
	              // add some styles to the html
	              StyleSheet styleSheet = (StyleSheet) kit.getStyleSheet();
	              ((javax.swing.text.html.StyleSheet) styleSheet).addRule("body {color:#000; font-family:times; margin: 4px; }");
	              ((javax.swing.text.html.StyleSheet) styleSheet).addRule("h1 {color: blue;}");
	              ((javax.swing.text.html.StyleSheet) styleSheet).addRule("h2 {color: #ff0000;}");
	              ((javax.swing.text.html.StyleSheet) styleSheet).addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

	             
	              // create a document, set it on the jeditorpane, then add the html
	              Document doc = kit.createDefaultDocument();
	              editor.setDocument(doc);
	              editor.setText(content);
	              editor.setCaretPosition(0);

	              editor.addHyperlinkListener(new HyperlinkListener() {
	            	    public void hyperlinkUpdate(HyperlinkEvent e) {
	            	        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	            	           // Do something with e.getURL() here
	            	        	if(Desktop.isDesktopSupported()) {
	            	        	    try {
										Desktop.getDesktop().browse(e.getURL().toURI());
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (URISyntaxException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
	            	        	}
	            	        }
	            	    }
	            	});
	              
	              // now add it all to a frame
	              JFrame j = new JFrame("Help Browser");
	              ImageIcon img = new ImageIcon(getClass().getResource("icon.png"));
	              
	              j.getContentPane().add(scrollPane, BorderLayout.CENTER);
	              
	              j.setIconImage(img.getImage());
	              
	              // make it easy to close the application
	             // j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	              
	              // display the frame
	              j.setSize(new Dimension(Main.screenWidth/2, Main.screenHeight-40));
	              int x = (int) Main.rect.getMaxX() - Main.screenWidth/2;
	              int y = 0;
	              j.setLocation(x, y);
	              
	              // center the jframe, then make it visible
	              //j.setLocationRelativeTo(null);
	              j.setVisible(true);
	        	  
	          }
	      });
		
		//Add menue to main panel
		//----------------------------------------------------------------------------------------------//
		
		helpMenu.add(helpContentsItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);

        
	}//end of constructor
	
	
	//helper functions
	/**
	 * 
	 * @param logFileName
	 */
	public void writeToLogFile(String logFileName){
		 Date date = new Date() ;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyHHmmss");//_HH_mm_ss") ;
		 String subNumStr = subNum;

		//practice log file writer
		PrintWriter writer;
		try {
			writer = new PrintWriter(logFileName, "UTF-8");
			writer.printf("%-35s%-5s%n", "Date", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date));
			writer.printf("%-35s%-5s%n", "Study ID", String.valueOf(AdvancedOptionsWindow.studyId));
			writer.printf("%-35s%-5s%n", "Study version", String.valueOf(AdvancedOptionsWindow.studyVer));
			writer.printf("%-35s%-5s%n", "Session number", String.valueOf(sessionNum));
			writer.printf("%-35s%-5s%n", "Subject number", subNumStr);
			writer.printf("%-35s%-5s%n", "Min frequency, Hz", String.valueOf(AdvancedOptionsWindow.minFrequency));
			writer.printf("%-35s%-5s%n", "Min frequency p index", String.valueOf(69+Math.round(12*Math.log(AdvancedOptionsWindow.minFrequency/440)/Math.log(2))));
			writer.printf("%-35s%-5s%n", "Max frequency, Hz", String.valueOf(AdvancedOptionsWindow.maxFrequency));
			writer.printf("%-35s%-5s%n", "Max frequency p index", String.valueOf(69+Math.round(12*Math.log(AdvancedOptionsWindow.maxFrequency/440)/Math.log(2))));
			writer.printf("%-35s%-5s%n", "Number of frequencies", String.valueOf(AdvancedOptionsWindow.numFrequencies));
			writer.printf("%-35s%-5s%n", "Jitter, p", String.valueOf(AdvancedOptionsWindow.jitter));
			writer.printf("%-35s%-5s%n", "Stimulus task duration, ms", String.valueOf(AdvancedOptionsWindow.stimulusDuration));
			writer.printf("%-35s%-5s%n", "Pre-stimulus task duration, ms", String.valueOf(AdvancedOptionsWindow.preStimulusDuration));
			writer.printf("%-35s%-5s%n", "Delay task duration, ms", String.valueOf(AdvancedOptionsWindow.delayDuration));
			writer.printf("%-35s%-5s%n", "Intertrial task duration, ms", String.valueOf(AdvancedOptionsWindow.intertrialDuration));
			writer.printf("%-35s%-5s%n", "Is probe task timed", String.valueOf(AdvancedOptionsWindow.isProbeTaskTimed));
			writer.printf("%-35s%-5s%n", "Number of bets per trial", String.valueOf(AdvancedOptionsWindow.numberOfBetsPerTrial));
			writer.printf("%-35s%-5s%n", "Highest score per one bet", String.valueOf(Parameters.MAX_POINTS_PER_BET));
			writer.printf("%-35s%-5s%n", "Number of trials with one bet", String.valueOf(AdvancedOptionsWindow.numOfTrialsWithOneBet));
			writer.printf("%-35s%-5s%n", "Number of untimed trials", String.valueOf(AdvancedOptionsWindow.numOfDemoTrialsNotTimed));
			writer.printf("%-35s%-5s%n", "Number of timed trials", String.valueOf(AdvancedOptionsWindow.numOfDemoTrialsTimed));
			writer.printf("%-35s%-5s%n", "Number of trials for bet practice", String.valueOf(AdvancedOptionsWindow.numOfPracticeBetTrials));
			writer.printf("%-35s%-5s%n", "Times practice repeated", String.valueOf(numRunsPractice));
			writer.printf("%-35s%-5s%n", "Times instructions repeated", String.valueOf(numRunsInstructions));
			if(AdvancedOptionsWindow.isProbeTaskTimed){
				writer.printf("%-35s%-5s%n", "Probe task timer value", String.valueOf(AdvancedOptionsWindow.probeDuration));
			}
			writer.printf("%-35s%-5s%n", "Display screen width, px", String.valueOf(Main.screenWidth));
			writer.printf("%-35s%-5s%n", "Display screen height, px", String.valueOf(Main.screenHeight));
			writer.printf("%-35s%-5s%n", "Is photodiode trigger set", String.valueOf(AdvancedOptionsWindow.triggerIsSelected));
			if(AdvancedOptionsWindow.triggerIsSelected){
				writer.printf("%-35s%-5s%n", "Trigger upper left corner X, px", String.valueOf(AdvancedOptionsWindow.leftCornerX));
				writer.printf("%-35s%-5s%n", "Trigger upper left corner Y, px", String.valueOf(AdvancedOptionsWindow.leftCornerY));
				writer.printf("%-35s%-5s%n", "Trigger width, px", String.valueOf(AdvancedOptionsWindow.triggerWidth));
				writer.printf("%-35s%-5s%n", "Trigger height, px", String.valueOf(AdvancedOptionsWindow.triggerHeight));
			}
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Sets the number of blocks in combo box on the main panel
	 * @param runs
	 */
	public void setNumberOfComboBlocks(int runs){
		  blockSelectionCombo.removeAllItems();
		  String[] description = new String[runs];
		  for(int i =0;i<runs;i++){
			description[i] = "Block "+(i+1);
		  }
		  for (int i = 0; i < runs; i++)
			  blockSelectionCombo.addItem(description[i]);
	  }
	
	/**
	 * 
	 * @param website
	 */
	private void goWebsite(JLabel website) {
        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("http://sites.nyuad.nyu.edu/faculty/sreenivasan/"));
                } catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
                }
            }
        });
    }

	/**
	 * 
	 * @param contact
	 */
    private void sendMail(JLabel contact) {
        contact.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:vahanbabushkin@gmail.com?subject=TEST"));
                } catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
                }
            }
        });
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

    
    
}
