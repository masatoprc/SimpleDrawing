import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomPanel extends JPanel implements IView {
	// the model that this view is showing
		private DrawingModel model;
		private JLabel label = new JLabel();
		
		public BottomPanel(DrawingModel model_) {
			// create UI
			setBackground(Color.WHITE);
			setLayout(new FlowLayout(FlowLayout.LEFT));
			
			// set the model
			model = model_;
			model.addView(this);
			label.setText("0 lines");
			this.add(this.label);
		}

		@Override
		public void updateView() {
			Shape curShape = model.getCurSelectedStroke();
			if (curShape != null) { 
				this.label.setText(model.getLineCount() + " Strokes, Selection ("
						+ curShape.npoints + " points, scale: " + curShape.scale + 
						", rotation " + curShape.prevRotation + ")");
			} else {
				this.label.setText(model.getLineCount() + " Strokes");
			}			
		}
}
