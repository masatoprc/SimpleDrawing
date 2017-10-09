import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ColorPallete extends JPanel{
	private DrawingModel model;
	private JButton[] buttons;

	public ColorPallete(DrawingModel model_) {
		super();
		model = model_;
		buttons = new JButton[10];
		setBackground(Color.WHITE);
		// create a constraints object
		GridBagConstraints gc = new GridBagConstraints();
		gc.weighty = 0.15;
	    gc.insets = new Insets(10, 5, 10, 5);
		this.setLayout(new GridBagLayout());
		for (int i = 0; i < 2; i++) { 
			for (int j = 0; j < 5; j++) {
				int flatIndex = i * 5 + j;
				JButton color = new JButton("");
				color.setName(String.valueOf(flatIndex));
				buttons[flatIndex] = color;
				color.setMinimumSize(new Dimension(32 ,32));
				color.setPreferredSize(new Dimension(32, 32));
				color.setBackground(model.getColor()[flatIndex]);
				gc.gridx = i;
				gc.gridy = j;
				this.add(color, gc);
			}
		}
		buttons[0].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		this.registerControllers();
		// empty panel to take up space
		JPanel fillerPanel = new JPanel();
		fillerPanel.setBackground(Color.white);
		gc.gridx = 0;
		gc.gridy = 5;
		gc.weighty = 3;
		gc.fill = GridBagConstraints.SOUTH;
		this.add(fillerPanel, gc);
	}

	private void registerControllers() {
		for (int i = 0; i < 10; i ++) {
		buttons[i].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				int curSelect = Integer.parseInt((button.getName()));
				model.setCurColor(curSelect);
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
				for (int j = 0; j < 10; j++) {
					if (j != curSelect) {
						buttons[j].setBorder(BorderFactory.createEmptyBorder());
					}
				}
			}
		});
	}
	}
}
