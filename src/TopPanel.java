import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	JButton deleteButton = new JButton("DELETE");
	JButton duplicateButton = new JButton("DUPLICATE");

	public TopPanel(DrawingModel model_) {
		super();
		// set the model
		model = model_;
		model.addView(this);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.registerControllers();
		if (model.enhanced) {
		this.add(duplicateButton);
		}
		this.add(deleteButton);
		this.add(new JLabel("Scale:"));
		this.add(scaleSlider);		
		this.add(scaleLabel);
		this.add(new JLabel("Rotate:"));
		this.add(rotateSlider);
		this.add(rotateLabel);
	}

	@Override
	public void updateView() {
		int curStrokeID = model.getCurSelectedStrokeID();
		if (curStrokeID == -1) {
			scaleSlider.setEnabled(false);
			rotateSlider.setEnabled(false);
			deleteButton.setEnabled(false);
			duplicateButton.setEnabled(false);
			rotateSlider.setValue(0);
			scaleSlider.setValue(10);
		} else {
			scaleSlider.setEnabled(true);
			rotateSlider.setEnabled(true);
			deleteButton.setEnabled(true);
			duplicateButton.setEnabled(true);
			rotateSlider.setValue(model.getShapeRotation(curStrokeID));
			scaleSlider.setValue(((Double)(model.getShapeScale(curStrokeID) * 10)).intValue());
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
		if (model.enhanced) {
		this.duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				model.duplicateStroke();
			}
		});
		}
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
