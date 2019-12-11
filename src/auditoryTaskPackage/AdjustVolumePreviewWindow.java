package auditoryTaskPackage;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AdjustVolumePreviewWindow extends JFrame{
	public AdjustVolumePreviewWindow(){
		setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		//to make it fullscreen
		//com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this,true);
		//com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
		getRootPane().putClientProperty("apple.awt.fullscreenable", Boolean.valueOf(true));
		
		
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setBounds(0, 0, Main.screenWidth, 200);//Main.screenHeight);
		//setUndecorated(true);//sets it to fullscreen
		setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final AdjustVolumePanel volumePanel =  new AdjustVolumePanel();
        getContentPane().add(volumePanel);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                volumePanel.m_thread.exit();
                e.getWindow().dispose();
            }
        });
        
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            	
            }

            @Override
            public void keyReleased(KeyEvent e) {
           
            }

            @Override
            public void keyPressed(KeyEvent e) {
    		//if the 'q' key is pressed -- exit
    		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
    			volumePanel.m_thread.exit();
    			dispose();
    			}
            }
        });
	}//end of class

	
	
}
