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
			label.setText("lines");
			this.add(this.label);
		}

		@Override
		public void updateView() {
			// TODO Auto-generated method stub
			this.label.setText("test");
		}
}
