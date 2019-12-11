package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class TriggerPreviewWindow extends JFrame {
	
	public TriggerPreviewWindow(){
		setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		//to make it fullscreen
		//com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this,true);
		//com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
		getRootPane().putClientProperty("apple.awt.fullscreenable", Boolean.valueOf(true));
		
		//hide mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");        
	    setCursor(invisibleCursor);
	    //
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setBounds(0, 0, Main.screenWidth, Main.screenHeight);
		setUndecorated(true);//sets it to fullscreen
		setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(new TriggerStimulusCanvas());
        
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
    			
    			dispose();
    			}
            }
        });
	}//end of class
	


}
