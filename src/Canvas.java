import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel implements IView, DeleteCallback, DuplicateCallback {
	
	List<Shape> shapes = new ArrayList<> ();
	Shape curDrawingShape;
	Shape curSelectedShape;
	private DrawingModel model;
	int strokeIDCounter = 0;
	
	public Canvas(DrawingModel model_) {
		super();
		model = model_;
		model.addView(this);
		setBackground(Color.WHITE);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				boolean select = false;
				for (Shape shape : model.getShapes()) {
					if (shape.hittest(e.getX(), e.getY())) {
						select = true;
						model.setCurSelectedStrokeID(shape.getID());
						try {
							curSelectedShape = (Shape) shape.clone();
						} catch (CloneNotSupportedException e1) {
							System.out.println("shape clone failed");
						}
					}
				}
				if (select) {
					curSelectedShape.prevX = -1;
					curSelectedShape.prevY = -1;
					repaint();
				} else {
				model.setCurSelectedStrokeID(-1);
			    curSelectedShape = null;
				Shape shape = new Shape();
				shape.setID(strokeIDCounter);
				strokeIDCounter++;
                shape.setIsClosed(false);
                shape.setIsFilled(false);
                shape.setColour(model.getCurColor());
                shape.setStrokeThickness(2.0f);
                model.addNewShape(shape);
                curDrawingShape = shape;
                repaint();
				} 
			}	
		});	
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
		@Override
			public void mouseDragged(MouseEvent e) {
			    if (curSelectedShape != null) {
			    	if (curSelectedShape.prevX == -1) {
			    		curSelectedShape.prevX = e.getX();
			    		curSelectedShape.prevY = e.getY();
			    	} else {
			    		AffineTransform T = curSelectedShape.getTransform();
			    		double unrotatedX = (e.getX() - curSelectedShape.prevX) / curSelectedShape.scale;
			    		double unrotatedY = (e.getY() - curSelectedShape.prevY) / curSelectedShape.scale;
			    		T.concatenate(AffineTransform.getTranslateInstance(
			    				Math.cos(Math.toRadians(-curSelectedShape.prevRotation))*unrotatedX - 
			    				Math.sin(Math.toRadians(-curSelectedShape.prevRotation))*unrotatedY, 
			    				Math.sin(Math.toRadians(-curSelectedShape.prevRotation))*unrotatedX + 
			    				Math.cos(Math.toRadians(-curSelectedShape.prevRotation))*unrotatedY));
		                curSelectedShape.setTransform(T);
		                curSelectedShape.prevX = e.getX();
			    		curSelectedShape.prevY = e.getY();
			    	}
			    } else {
				curDrawingShape.addPoint(e.getX(), e.getY());
			    }
	            repaint();    
			}			
		});	

	}

	public void paintComponent(Graphics g){
        super.paintComponent(g);  // VERY IMPORTANT!
        Graphics2D a = (Graphics2D)g;
        a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (curSelectedShape != null) {
			curSelectedShape.setStrokeThickness(6.0f);
			curSelectedShape.setColour(Color.YELLOW);
			curSelectedShape.draw(a);
        }
        for (Shape shape : model.getShapes()) {
        if (shape != null) {
            shape.draw(a);
        	}
        }
    }

	public void setCurSelectedShape(Shape curSelectedShape) {
		try {
			this.curSelectedShape = (Shape) curSelectedShape.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateView() {
		repaint();
	}

	@Override
	public void deleteStroke() {
		if (curSelectedShape != null) {
			curSelectedShape = null;
		}		
		repaint();
	}

	@Override
	public void duplicateStroke() {
		// TODO Auto-generated method stub
		repaint();
	}

	public int getSetStrokeIDCounter() {
		this.strokeIDCounter += 1;
		return strokeIDCounter - 1;
	}
	
	
}
