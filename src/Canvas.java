import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel implements IView {
	
	List<Shape> shapes = new ArrayList<> ();
	Shape curShape;
	private DrawingModel model;
	
	public Canvas(DrawingModel model_) {
		super();
		model = model_;
		model.addView(this);
		setBackground(Color.WHITE);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Shape shape = new Shape();
                shape.setIsClosed(false);
                shape.setIsFilled(false);
                shape.setColour(model.getCurColor());
                shape.setStrokeThickness(2.0f);
                shapes.add(shape);
                model.addNewShape(shape);
                curShape = shape;
                repaint();
			}	
		});	
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
		@Override
			public void mouseDragged(MouseEvent e) {
				curShape.addPoint(e.getX(), e.getY());
	            repaint();    
			}			
		});	

	}

	public void paintComponent(Graphics g){
        super.paintComponent(g);  // VERY IMPORTANT!
        Graphics2D a = (Graphics2D)g;
        a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (Shape shape : shapes) {
        if (shape != null) {
            shape.draw(a);
        	}
        }
    }

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}
}
