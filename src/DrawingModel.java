import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DrawingModel {
	public static Color[] colorArray = new Color[10];
	private int curColorIndex;
	private List<Shape> shapes = new ArrayList<>();
	private ArrayList<IView> views = new ArrayList<IView>();

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
	private void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
	
	
}
