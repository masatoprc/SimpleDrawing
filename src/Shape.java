/*
*  Shape: See ShapeDemo for an example how to use this class.
*
*/
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.vecmath.*;


// simple shape model class
class Shape {

    // shape points
    ArrayList<Point2d> points;
    @SuppressWarnings("unchecked")
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Shape clone = new Shape();
		clone.id = id;
		clone.points = (ArrayList<Point2d>) points.clone();
		clone.isClosed = isClosed;
		clone.isFilled = isFilled;
		clone.npoints = npoints;
		clone.pointsChanged = pointsChanged;
		clone.xpoints = xpoints.clone();
		clone.ypoints = ypoints.clone();
		clone.transform = transform;
		clone.colour = colour;
		clone.strokeThickness = strokeThickness;
		clone.prevRotation = prevRotation;
		return clone;
	}


	int id;
    Double prevRotation = 0d;
    int prevX = -1;
    int prevY = -1;
    double scale = 1d;
	
    public void setID(int id_) {
    	id = id_;
    }
    
    public int getID() {
    	return id;
    }

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
    	Point2d clickPoint = new Point2d(x, y);
    	Point2D p;
		try {
			p = transform.inverseTransform(new Point2D.Double(x, y), null);
	    	clickPoint.x = p.getX();
	    	clickPoint.y = p.getY();
	    	if (points != null) {
	    		for (int i = 0; i < points.size() - 1; i++) {
	    			Point2d closest = closestPoint(clickPoint, points.get(i), points.get(i+1));
	    			if (Math.sqrt(Math.pow(closest.x - p.getX(), 2) + Math.pow(closest.y - p.getY(), 2)) <= 5) {
	            		return true;
	            	}
	    		}
	        }    	
		} catch (NoninvertibleTransformException e) {
			System.out.println("non invertible transformation");
		}
    	return false;
    }
    
    public Point2d centerBoundingBox() {
    	Point2d center = new Point2d();
    	double largestY = 0;
    	double largestX = 0;
    	double smallestX = Integer.MAX_VALUE;
    	double smallestY = Integer.MAX_VALUE;
    	for (Point2d point : points) {
    		smallestX = Math.min(smallestX, point.x);
    		smallestY = Math.min(smallestY, point.y);
    		largestX = Math.max(largestX, point.x);
    		largestY = Math.max(largestY, point.y);
    	}
    	center.x = (smallestX + largestX) / 2;
    	center.y = (smallestY + largestY) / 2;
    	return center;
    }
    
    // find closest point using projection method 
    static Point2d closestPoint(Point2d M, Point2d P0, Point2d P1) {
    	Vector2d v = new Vector2d();
    	v.sub(P1,P0); // v = P2 - P1
    	
    	// early out if line is less than 1 pixel long
    	if (v.lengthSquared() < 0.5)
    		return P0;
    	
    	Vector2d u = new Vector2d();
    	u.sub(M,P0); // u = M - P1

    	// scalar of vector projection ...
    	double s = u.dot(v) / v.dot(v); 
    	
    	// find point for constrained line segment
    	if (s < 0) 
    		return P0;
    	else if (s > 1)
    		return P1;
    	else {
    		Point2d I = P0;
        	Vector2d w = new Vector2d();
        	w.scale(s, v); // w = s * v
    		I.add(w); // I = P1 + w
    		return I;
    	}
    }
    
    public void manualTranslate(int x_, int y_) {
    	for (Point2d point : points) {
    		point.setX(point.getX() + x_);
    		point.setY(point.getY() + y_);
    	}
    	pointsChanged = true;
    }
}
