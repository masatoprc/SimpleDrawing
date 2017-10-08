import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TopPanel extends JPanel implements IView {
	
	private DrawingModel model;
	private JSlider scaleSlider = new JSlider(5, 20);
	private JSlider rotateSlider = new JSlider(-180, 180);
	JLabel scaleLabel = new JLabel("1.0");
	JLabel rotateLabel = new JLabel("0");

	public TopPanel(DrawingModel model_) {
		super();
		// set the model
		model = model_;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.registerControllers();
		// create a constraints object
//		GridBagConstraints gc = new GridBagConstraints();
//		// stretch the widget horizontally and vertically
//		gc.fill = GridBagConstraints.HORIZONTAL;
//		gc.gridwidth = 1; // 1 grid cell wide
//		gc.gridheight = 1; // 1 grid cells tall
//		gc.weightx = 0.15; // the proportion of space to give this column
//		gc.weighty = 0.8; // the proportion of space to give this row
		this.add(new JButton("DUPLICATE"));
		this.add(new JButton("DELETE"));
		this.add(new JLabel("Scale:"));
		this.add(scaleSlider);		
		this.add(scaleLabel);
		this.add(new JLabel("Rotate:"));
		this.add(rotateSlider);
		this.add(rotateLabel);
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}

	private void registerControllers() {
		this.scaleSlider.addChangeListener(new ScaleController());
		this.rotateSlider.addChangeListener(new RotateController());
	}

	private class ScaleController implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			double scale = scaleSlider.getValue();
			scaleLabel.setText(String.valueOf(scale / 10.0));
			//model.setBase(base);
		}
	}

	private class RotateController implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			double rotation = rotateSlider.getValue();
			rotateLabel.setText(String.valueOf(rotation));
			//model.setHeight(height);
		}
	}
}
