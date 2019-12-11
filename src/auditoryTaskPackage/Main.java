package auditoryTaskPackage;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
 
//create class and extend with JFrame
public class Main  extends JFrame 
{
	
	/**
	 * Experiments parameters
	 */
	public static int count = 1;
	public static int screenHeight;
	public static int screenWidth;
	public static Rectangle rect;
	
	
	/**
	 * Launch the application.
	 */
	//main method
	public static void main(String[] args)
	{
		//get the size of the screen
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		GraphicsDevice curGs=gs[0];
		DisplayMode dm = curGs.getDisplayMode();
		rect = curGs.getDefaultConfiguration().getBounds();
		System.out.println(dm.getWidth() + " x " + dm.getHeight());
		screenHeight = dm.getHeight();
		screenWidth = dm.getWidth();
		/* It posts an event (Runnable)at the end of Swings event list and is
		processed after all other GUI events are processed.*/
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				//try - catch block
				try 
				{
					//Create object of InstructionWindow
					ExperimentSetupWindow frame = new ExperimentSetupWindow();
					//set frame visible true
					frame.setVisible(true);					
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		try { 
	         File file = new File("program.properties");
	         if(file.delete()) { 
	            System.out.println(file.getName() + " is deleted!");
	         } else {
	            System.out.println("Delete operation is failed.");
	    		}
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}


}
