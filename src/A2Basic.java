import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class A2Basic {
	public static void main(String[] args) {
		
		DrawingModel model_ = new DrawingModel();
		
		BottomPanel bottomPanel = new BottomPanel(model_);
		bottomPanel.setPreferredSize(new Dimension(bottomPanel.getWidth(), 30));
		TopPanel topPanel = new TopPanel(model_);
		topPanel.setPreferredSize(new Dimension(topPanel.getWidth(), 40));
		ColorPallete westPanel = new ColorPallete(model_);
		westPanel.setPreferredSize(new Dimension(80, westPanel.getHeight()));
		
		Canvas panel = new Canvas(model_);
		model_.setCanvas(panel);
        panel.setPreferredSize(new Dimension(780, 480));
        JScrollPane scroll = new JScrollPane(panel);
		
	    JFrame frame = new JFrame("SimpleDrawing");
	    frame.getContentPane().setLayout(new BorderLayout());	
	    frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	    frame.getContentPane().add(topPanel, BorderLayout.NORTH);
	    frame.getContentPane().add(westPanel, BorderLayout.WEST);
	    frame.getContentPane().add(scroll, BorderLayout.CENTER);
	    
	    frame.setPreferredSize(new Dimension(900,600));
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
}
