package auditoryTaskPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
 




import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class PieChart extends JPanel {
 
        private ArrayList values;
        private ArrayList colors;
 
        private ArrayList gradingValues;
        private ArrayList gradingColors;
 
	double percent = 0; //percent is used for simple indicator and graded indicator
 
	
 
	public PieChart(ArrayList values, ArrayList colors) {
 
		this.values = values;
		this.colors = colors;
	}
 
 
	@Override
	protected void paintComponent(Graphics g) {
 
		int width = getSize().width;
 
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				RenderingHints.VALUE_ANTIALIAS_ON);
 
	
 
			int lastPoint = -270;
 
			for (int i = 0; i < values.size(); i++) {
				g2d.setColor((Color) colors.get(i));
 
				Double val = (Double) values.get(i);
				Double angle = (val / 100) * 360+0.5;
 
				g2d.fillArc(0, 0, width, width, lastPoint, -angle.intValue());

 
				lastPoint = lastPoint + -angle.intValue();
			}
		
		
	}


	
	
}
