/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Administrator
 *
 */
public class ImageConfigurationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6577595387861415962L;
	
	private JPanel panel = new JPanel(new BorderLayout());
	
	class ImageInfo {
		int size;
		double paintingZoom;
		int xOffset;
		int yOffset;
		public ImageInfo() {
			Drawer drawer = App.mainWindow.getDrawer();
			size = drawer.getImageSize();
			paintingZoom = drawer.getPaintingZoom();
			xOffset = drawer.getXOffset();
			yOffset = drawer.getYOffset();
		}
	}
	private ImageInfo info = new ImageInfo();
	
	public ImageConfigurationDialog() {
		super(App.mainWindow, "Image Configuration", true);
		setResizable(false);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setContentPane(panel);
		createConfigurationPanel();
		createConfirmButton();
	}
	
	@FunctionalInterface
	interface SetRunnable {
		public void set(String value) throws NumberFormatException;
	}
	
	@SuppressWarnings("serial")
	class ParameterPanel extends JPanel {
		private JComboBox<? extends Number> comboBox;
		private SetRunnable setRunnable;
		public <T extends Number> ParameterPanel(String name, T[] values, T defaultValue, SetRunnable setRunnable) {
			super(new BorderLayout());
			
			comboBox = new JComboBox<T>(values);
			this.setRunnable = setRunnable;
			
			comboBox.setEditable(true);
			comboBox.setSelectedItem(defaultValue);
			comboBox.addItemListener((e) -> {
				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				chengeValue();
			});
			
			JLabel nameLabel = new JLabel(name);
			nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
			add(nameLabel, BorderLayout.WEST);
			add(comboBox, BorderLayout.CENTER);
			
			setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		}
		
		private void chengeValue() {
			try {
				setRunnable.set(comboBox.getSelectedItem().toString());
				comboBox.setBorder(new JComboBox<>().getBorder());
			} catch (NumberFormatException e) {
				comboBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 255), 3));
			}
		}
	}
	
	private void createConfigurationPanel() {
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		
		box.add(new ParameterPanel("Image Size", new Integer[] {
				300, 500, 1000, 2000, 3000, 4000, 5000
		}, info.size, (value) -> {
			info.size = Integer.parseInt(value);
		}));
		box.add(new ParameterPanel("Painting Zoom", new Double[] {
				0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75
		}, info.paintingZoom, (value) -> {
			double v = Double.parseDouble(value);
			if (v < 0) {
				throw new NumberFormatException();
			}
			info.paintingZoom = v;
		}));
		box.add(new ParameterPanel("X Offset", new Integer[] {
				-1500, -1000, -500, 0, 500, 1000, 1500
		}, info.xOffset, (value) -> {
			info.xOffset = Integer.parseInt(value);
		}));
		box.add(new ParameterPanel("Y Offset", new Integer[] {
				-1500, -1000, -500, 0, 500, 1000, 1500
		}, info.yOffset, (value) -> {
			info.yOffset = Integer.parseInt(value);
		}));
		
		this.panel.add(box , BorderLayout.CENTER);
	}

	private void createConfirmButton() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 200, 5, 0));
		JButton confirmButton = new JButton("   Ok   ");
		JButton cancelButton = new JButton("Cancel");
		confirmButton.addActionListener((e) -> {
			Drawer drawer = App.mainWindow.getDrawer();
			if (drawer.getImageSize() != info.size) {
				drawer.setImageSize(info.size);
			}
			drawer.setPaintingZoom(info.paintingZoom);
			drawer.setXOffset(info.xOffset);
			drawer.setYOffset(info.yOffset);
			super.dispose();
		});
		cancelButton.addActionListener((e) -> {
			super.dispose();
		});
		panel.add(confirmButton);
		panel.add(cancelButton);
		this.panel.add(panel, BorderLayout.SOUTH);
	}
}
