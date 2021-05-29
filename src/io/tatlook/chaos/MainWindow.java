/**
 * 
 */
package io.tatlook.chaos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

/**
 * @author Administrator
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480434536614023106L;
	
	private static final String NAME = "Iterated function system";

	public MainWindow() {
		super.setTitle(NAME);
		super.setExtendedState(JFrame.MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(900, 600));
		super.addWindowListener(new WindowAdapter() {
			int result = JOptionPane.NO_OPTION;
			@Override
			public void windowClosing(WindowEvent e) {
				if (!ChaosData.current.isChanged()) {
					return;
				}
				result = JOptionPane.showConfirmDialog(
                        App.mainWindow,
                        "If you don't save, your changes will be lost.",
                        "Save the changes?",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
				if (result == JOptionPane.CANCEL_OPTION) {
					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				} else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
					System.exit(0);
				} else if (result == JOptionPane.YES_OPTION) {
					ChaosFileSaver.staticSave();
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				if (result == JOptionPane.CANCEL_OPTION) {
					setVisible(true);
					setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				}
			}
		});
	}
	
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private Drawer drawer;
	
	public Drawer getDrawer() {
		return drawer;
	}

	private JPanel toolPanel;
	private JSplitPane splitPane;
	
	public void updateToolPanel() {
		toolPanel = new ToolPanel();
		splitPane.setLeftComponent(toolPanel);
		File file = ChaosFileParser.getCurrentFileParser().getFile();
		if (file == null) {
			super.setTitle("untitled - " + NAME);
		} else {
			super.setTitle(file.getName() + " - " + NAME);			
		}
	}
	
	public void UI() {
		super.setContentPane(mainPanel);
		
		JMenuBar menuBar = new MenuBar();
		setJMenuBar(menuBar);
		
		toolPanel = new ToolPanel();
		drawer = new Drawer();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(drawer);
		splitPane.setLeftComponent(toolPanel);
		
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
			int stal = 0;
			boolean full = false;
			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				stal++;
				if (stal != 1) {
					stal = 0;
					return true;
				}
				if (e.getKeyCode() != KeyEvent.VK_F11) {
					return true;
				}
				System.out.println(
						"MainWindow.UI().new KeyEventPostProcessor() {...}.postProcessKeyEvent()");
				if (full == true) {
					setJMenuBar(menuBar);
					setContentPane(mainPanel);
					splitPane.setRightComponent(drawer);
					update(getGraphics());
					full = false;
				} else {
					setJMenuBar(null);
					setContentPane(drawer);
					update(getGraphics());
					setExtendedState(JFrame.NORMAL);
					setExtendedState(JFrame.MAXIMIZED_BOTH);
					full = true;
				}
				return true; 
			}
		});
	}
	
	@Override
	public void setVisible(boolean rootPaneCheckingEnabled) {
		super.setVisible(rootPaneCheckingEnabled);
		// Ennen kuin "thread" lähtee, pitä odotta ikkunan valmis
		new Thread(() -> {
			try {
				// Ei saa tapahtu mitää, ennen kuin odotaminen loppu
				synchronized (this) {
					wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Nyt ikkuna on suurin pirtein valmis.
			drawer.start();
		}).start();
	}
}
