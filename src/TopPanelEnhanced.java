import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TopPanelEnhanced extends JPanel implements IView {
	
	private DrawingModel model;
	private JSlider scaleSlider = new JSlider(5, 20);
	private JSlider rotateSlider = new JSlider(-180, 180);
	JLabel scaleLabel = new JLabel("1.0");
	JLabel rotateLabel = new JLabel("0");
	JButton deleteButton = new JButton("DELETE");
	JButton duplicateButton = new JButton("DUPLICATE");
	// The spinner requires a number model that specifies the starting
	// value, the minimum, maximum, and step size.
	private JSpinner thickSpinner = new JSpinner(new SpinnerNumberModel(2, 1,
			4, 1));

	public TopPanelEnhanced(DrawingModel model_) {
		super();
		// set the model
		model = model_;
		model.addView(this);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.registerControllers();
		this.add(duplicateButton);
		this.add(deleteButton);
		this.add(new JLabel("Scale:"));
		this.add(scaleSlider);		
		this.add(scaleLabel);
		this.add(new JLabel("Rotate:"));
		this.add(rotateSlider);
		this.add(rotateLabel);
		this.add(new JLabel("Thickness:"));
		this.add(thickSpinner);
	}

	@Override
	public void updateView() {
		int curStrokeID = model.getCurSelectedStrokeID();
		if (curStrokeID == -1) {
			scaleSlider.setEnabled(false);
			rotateSlider.setEnabled(false);
			deleteButton.setEnabled(false);
			duplicateButton.setEnabled(false);
			thickSpinner.setEnabled(false);
			rotateSlider.setValue(0);
			scaleSlider.setValue(10);
			thickSpinner.setValue(2);
		} else {
			scaleSlider.setEnabled(true);
			rotateSlider.setEnabled(true);
			deleteButton.setEnabled(true);
			duplicateButton.setEnabled(true);
			thickSpinner.setEnabled(true);
			rotateSlider.setValue(model.getShapeRotation(curStrokeID));
			thickSpinner.setValue(model.getShapeThick(curStrokeID));
		}
	}

	private void registerControllers() {
		this.scaleSlider.addChangeListener(new ScaleController());
		this.rotateSlider.addChangeListener(new RotateController());
		this.deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model.deleteStroke();
			}
		});
		this.duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model.duplicateStroke();
			}
		});
		this.thickSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner src = (JSpinner) e.getSource();
				int val = (int) src.getValue();
				model.changeThickness(val);
			}
		});
	}

	private class ScaleController implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			double scale = scaleSlider.getValue();
			scaleLabel.setText(String.valueOf(scale / 10.0));
			model.modifyScale(scale / 10.0);
		}
	}

	private class RotateController implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			double rotation = rotateSlider.getValue();
			rotateLabel.setText(String.valueOf(rotation));
			model.modifyRotation(rotation);
		}
	}
}
