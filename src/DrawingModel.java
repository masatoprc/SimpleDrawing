import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

public class DrawingModel implements DeleteCallback, DuplicateCallback
   , ScaleSlideCallback, RotateSlideCallback, ThicknessCallback {
	public static Color[] colorArray = new Color[10];
	private int curColorIndex;
	private List<Shape> shapes = new ArrayList<>();
	private ArrayList<IView> views = new ArrayList<IView>();
	private int curSelectedStrokeID = -1;
	private Canvas canvas;
	
	public void setCurSelectedStrokeID(int id_) {
		curSelectedStrokeID = id_;
		updateAllViews();
	}
	
	public int getCurSelectedStrokeID() {
		return curSelectedStrokeID;
	}
	
	public Shape getCurSelectedStroke() {
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getID() == curSelectedStrokeID) {
				return shapes.get(i);
			}
		}
		return null;
	}

	public DrawingModel() {
		super();
		for (int i = 0; i < 10; i++) {
			colorArray[i] = Color.getHSBColor((float) (i*0.1),
					(float) 0.5, (float) 0.5);
		}
	}
	
	public Color[] getColor() {
		return colorArray;
	}

	public void setCurColor(int curColor) {
		this.curColorIndex = curColor;
		if (curSelectedStrokeID != -1) {
			for (int i = 0; i < shapes.size(); i++) {
				if (shapes.get(i).getID() == curSelectedStrokeID) {
				shapes.get(i).setColour(colorArray[curColor]);
				break;
				}
			}
		}
		updateAllViews();
	}
	
	public Color getCurColor() {
		return colorArray[curColorIndex];
	}
	
	public void addNewShape(Shape shape) {
			shapes.add(shape);
			updateAllViews();
	}
	
	public int getLineCount() {
		return shapes.size();
	}
	
	/** Add a new view */
	public void addView(IView view) {
		this.views.add(view);
		view.updateView(); // update Views!
	}
	
	/** Update all the views that are viewing this */
	public void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public void deleteStroke() {
		if (curSelectedStrokeID != -1) {
			canvas.deleteStroke();
				int index = -1;
				for (int i = 0; i < shapes.size(); i++) {
					if (shapes.get(i).getID() == curSelectedStrokeID) {
						index = i;
						break;
					}
				}
				if (index != -1) {
				shapes.remove(index);
				}
				setCurSelectedStrokeID(-1);
				updateAllViews();
		}		
	}
	
	public int getShapeRotation(int id) {
		for (int i = 0; i < shapes.size(); i++) {
			if (id == shapes.get(i).getID()) {
				return shapes.get(i).prevRotation.intValue();
			}
		}
		return 0;
	}
	
	public int getShapeThick(int id) {
		for (int i = 0; i < shapes.size(); i++) {
			if (id == shapes.get(i).getID()) {
				return (int) shapes.get(i).getStrokeThickness();
			}
		}
		return 2;
	}
	
	public Double getShapeScale(int id) {
		for (int i = 0; i < shapes.size(); i++) {
			if (id == shapes.get(i).getID()) {
				return shapes.get(i).scale;
			}
		}
		return 1d;
	}
	
	public void setCanvas(Canvas canvas_) {
		canvas = canvas_;
	}

	@Override
	public void duplicateStroke() {
		if (curSelectedStrokeID != -1) {
			canvas.duplicateStroke();
				int index = -1;
				for (int i = 0; i < shapes.size(); i++) {
					if (shapes.get(i).getID() == curSelectedStrokeID) {
						index = i;
						break;
					}
				}
				try {
					Shape dupShape = (Shape) shapes.get(index).clone();
					dupShape.setID(canvas.getSetStrokeIDCounter());
					setCurSelectedStrokeID(dupShape.getID());
					AffineTransform T = (AffineTransform) dupShape.getTransform().clone();
					T.concatenate(AffineTransform.getTranslateInstance(10, 10));
	                dupShape.setTransform(T);
	                addNewShape(dupShape);
	                canvas.setCurSelectedShape(dupShape);
				} catch (CloneNotSupportedException e) {
					System.out.println("dupShape clone failed");
				}
				updateAllViews();
		}	
	}

	// Assuming some stroke is already selected
	@Override
	public void modifyScale(double scale) {
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getID() == curSelectedStrokeID) {
				shapes.get(i).scale = scale;
				AffineTransform T = (AffineTransform) shapes.get(i).getTransform();
				Point2d center = shapes.get(i).centerBoundingBox();
				double temp1 = 1 / T.getScaleX(); Double temp2 = 1 / T.getScaleY();
				T.concatenate(AffineTransform.getTranslateInstance(center.x, center.y));
				T.concatenate(AffineTransform.getScaleInstance(scale, scale));
				T.concatenate(AffineTransform.getScaleInstance(temp1, temp2));
				T.concatenate(AffineTransform.getTranslateInstance(-center.x,-center.y));				
                shapes.get(i).setTransform(T);
				break;
			}
		}
		updateAllViews();
	}

	@Override
	public void modifyRotation(double degree) {
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getID() == curSelectedStrokeID) {
				AffineTransform T = (AffineTransform) shapes.get(i).getTransform();
				Point2d center = shapes.get(i).centerBoundingBox();
				Double prevRotation = shapes.get(i).prevRotation;
				T.rotate(Math.toRadians(degree - prevRotation), center.x, center.y);
				shapes.get(i).prevRotation = degree;
                shapes.get(i).setTransform(T);
				break;
			}
		}
		updateAllViews();
	}

	@Override
	public void changeThickness(int val) {
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).getID() == curSelectedStrokeID) {
				shapes.get(i).setStrokeThickness(val);
				break;
			}
		}
		updateAllViews();
	}
}
