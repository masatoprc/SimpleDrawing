/*
*  Shape: See ShapeDemo for an example how to use this class.
*
*/
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.*;


// simple shape model class
class Shape {

    // shape points
    ArrayList<Point2d> points;

    public void clearPoints() {
        points = new ArrayList<Point2d>();
        pointsChanged = true;
    }
  
    // add a point to end of shape
    public void addPoint(Point2d p) {
        if (points == null) clearPoints();
        points.add(p);
        pointsChanged = true;
    }    

    // add a point to end of shape
    public void addPoint(double x, double y) {
        addPoint(new Point2d(x, y));  
    }

    public int npoints() {
        return points.size();
    }
 

    // shape is polyline or polygon
    Boolean isClosed = true; 

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }    

    // if polygon is filled or not
    Boolean isFilled = true; 

    public Boolean getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Boolean isFilled) {
        this.isFilled = isFilled;
    }    

    // drawing attributes
    Color colour = Color.BLACK;
	float strokeThickness = 3.0f;

    public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

    public float getStrokeThickness() {
		return strokeThickness;
	}

	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

    // the model to world transform (default is identity)
    AffineTransform transform = new AffineTransform();

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    // some optimization to cache points for drawing
    Boolean pointsChanged = false; // dirty bit
    int[] xpoints, ypoints;
    int npoints = 0;

    void cachePointsArray() {
        xpoints = new int[points.size()];
        ypoints = new int[points.size()];
        for (int i=0; i < points.size(); i++) {
            xpoints[i] = (int)points.get(i).x;
            ypoints[i] = (int)points.get(i).y;
        }
        npoints = points.size();
        pointsChanged = false;
    }
	
    
    // let the shape draw itself
    public void draw(Graphics2D g2) {

        // don't draw if points are empty (not shape)
        if (points == null) return;

        // see if we need to update the cache
        if (pointsChanged) cachePointsArray();

        // save the current g2 transform matrix 
        AffineTransform M = g2.getTransform();

        // multiply in this shape's transform
        g2.transform(transform);

        // call drawing functions
        g2.setColor(colour);            
        if (isFilled) {
            g2.fillPolygon(xpoints, ypoints, npoints);
        } else {
        	g2.setStroke(new BasicStroke(strokeThickness)); 
        	if (isClosed)
                g2.drawPolygon(xpoints, ypoints, npoints);
            else
                g2.drawPolyline(xpoints, ypoints, npoints);
        }

        // reset the transform to what it was before we drew the shape
        g2.setTransform(M);            
        
    }
    
   
    // let shape handle it's own hit testing
    // (x,y) is the point to test against
    // (x,y) needs to be in same coordinate frame as shape, you could add
    // a work to shape transform as an extra parater to this function
    public boolean hittest(double x, double y)
    {   
    	if (points != null) {

            // TODO Implement

    	}
    	
    	return false;
    }
}
