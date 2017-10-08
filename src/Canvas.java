import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Canvas extends JPanel{
	public void paintComponent(Graphics g){
        super.paintComponent(g);  // VERY IMPORTANT!
        Graphics2D a = (Graphics2D)g;
        a.setColor(Color.RED);
        a.drawLine(50, 50, 100, 100);
    }
}
