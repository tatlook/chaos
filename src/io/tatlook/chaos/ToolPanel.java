/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Vector;

/**
 * @author Administrator
 *
 */
public class ToolPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3016839462507037732L;

	private static final Border STD_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private static final Border VERCTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(2, 0, 2, 0);
	private static final Border HORIZONTAL_SPACING_BORDER = BorderFactory.createEmptyBorder(0, 1, 0, 2);
	private static final Border BROAD_SPACING_BORDER_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	
	private Box contentBox;
	private Vector<JPanel> rulePanels;
	private JButton createRuleButton = new JButton("Create a rule");
	
	public ToolPanel() {
		super(new BorderLayout());
		try {
			ChaosFileParser.getCurrentFileParser().readChaos();
		} catch (ChaosFileDataException e) {
			e.openDialog();
		}
		contentBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(contentBox,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentBox.setBorder(BROAD_SPACING_BORDER_BORDER);
		add(scrollPane, BorderLayout.CENTER);
		createSpeedControl();
		createRulePanel();
	}
	
	private void createRulePanel() {
		rulePanels = new Vector<>();
		
		for (int i = 0; i < ChaosData.current.getDist().length; i++) {
			createRule(false);
		}

		createRuleButton.addActionListener((e) -> createRule(true));
		createRuleButton.setBorder(BROAD_SPACING_BORDER_BORDER);
		contentBox.add(createRuleButton);
	}
	
	private void createSpeedControl() {
		JPanel speedControlPanel = new JPanel();
		speedControlPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
		speedControlPanel.setMaximumSize(new Dimension(speedControlPanel.getMaximumSize().width, 90));
		
		JSlider slider = new JSlider(0, 10);
		slider.setValue(0);
		slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener((e) -> {
			int sliderValue = slider.getValue();
			int waitTime = (Byte.MAX_VALUE) / (sliderValue + 1);
			if (sliderValue == 10) {
				waitTime = 0;
			}
			App.mainWindow.getDrawer().setWaitTime(waitTime);
		});
		
		speedControlPanel.add(slider);
		
		contentBox.add(speedControlPanel);
	}
	
	private JLabel createSpacing() {
		JLabel spacing = new JLabel();
		spacing.setBorder(HORIZONTAL_SPACING_BORDER);
		return spacing;
	}
	
	private void createRule(boolean itIsNew) {
		JPanel panel = new JPanel(new BorderLayout());
		
		if (itIsNew) {
			ChaosData.current.addRule();
			App.mainWindow.getDrawer().setChange();
		}
		
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, 110));
		System.out.println("ToolPanel.createRule()");
		int panelIndex = rulePanels.size();
		
		Border border = BorderFactory.createTitledBorder("Rule" + (panelIndex + 1));
		panel.setBorder(border);
		{
			JLabel label = new JLabel("Possibility");
			JTextField textField = new JTextField("" + ChaosData.current.getDist()[panelIndex]);
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() != KeyEvent.VK_ENTER) {
						return;
					}
					Double value;
					try {
						value = Double.parseDouble(textField.getText());
					} catch (NumberFormatException e2) {
						return;
					}
					Vector<Double> vector = ChaosData.current.getDistVector();
					Double dists = vector.get(panelIndex);
					dists = value;
					vector.set(panelIndex, dists);
					App.mainWindow.getDrawer().setChange();
				}
			});
			Box box = Box.createHorizontalBox();
			box.add(label);
			box.add(textField);
			box.setBorder(STD_SPACING_BORDER);
			panel.add(box, BorderLayout.NORTH);
		}
		final int fieldMinimumWidth = 100;
		final int fieldMaximumHeight = 22;
		{
			Box xBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CX");
			xBox.add(label);
			for (int i = 0; i < ChaosData.current.getCX()[0].length; i++) {
				JTextField field = new JTextField("" + ChaosData.current.getCX()[panelIndex][i]);
				field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
				field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
				final int theI = i;
				field.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() != KeyEvent.VK_ENTER) {
							return;
						}
						Double value;
						try {
							value = Double.parseDouble(field.getText());
						} catch (NumberFormatException e2) {
							return;
						}
						Vector<Double[]> vector = ChaosData.current.getCXVector();
						Double[] cxs = vector.get(panelIndex);
						cxs[theI] = value;
						vector.set(panelIndex, cxs);
						App.mainWindow.getDrawer().setChange();
					}
				});
				xBox.add(createSpacing());
				xBox.add(field);
			}
			xBox.setBorder(STD_SPACING_BORDER);
			panel.add(xBox);
		}
		{
			Box yBox = Box.createHorizontalBox();
			JLabel label = new JLabel("CY");
			yBox.add(label);
			for (int i = 0; i < ChaosData.current.getCY()[0].length; i++) {
				JTextField field = new JTextField("" + ChaosData.current.getCY()[panelIndex][i]);
				field.setMinimumSize(new Dimension(fieldMinimumWidth, 0));
				field.setMaximumSize(new Dimension(field.getMaximumSize().width, fieldMaximumHeight));
				final int theI = i;
				field.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() != KeyEvent.VK_ENTER) {
							return;
						}
						Double value;
						try {
							value = Double.parseDouble(field.getText());
						} catch (NumberFormatException e2) {
							return;
						}
						Vector<Double[]> vector = ChaosData.current.getCYVector();
						Double[] cys = vector.get(panelIndex);
						cys[theI] = value;
						vector.set(panelIndex, cys);
						App.mainWindow.getDrawer().setChange();
					}
				});
				yBox.add(createSpacing());
				yBox.add(field);
			}
			yBox.setBorder(STD_SPACING_BORDER);
			panel.add(yBox, BorderLayout.SOUTH);
		}
		System.out.print(rulePanels.size());
		
		contentBox.remove(createRuleButton);
		contentBox.add(panel);
		contentBox.add(createRuleButton);
		panel.updateUI();
		rulePanels.add(panel);
	}
}
