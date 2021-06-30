/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * @author Administrator
 *
 */
public class ChaosFileSaver {
	private PrintStream stream;
	
	public ChaosFileSaver(File file) {
		try {
			if (file == null) {
				throw new AssertionError();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			stream = new PrintStream(file);
		} catch (IOException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}
	public void save() {
		double[] dist = ChaosData.current.getDist();
		double[][] cx = ChaosData.current.getCX();
		double[][] cy = ChaosData.current.getCY();
		
		stream.println(dist.length);
		stream.print("    ");
		for (int i = 0; i < dist.length; i++) {
			stream.print(dist[i]);
			stream.print(' ');
		}
		stream.println();
		
		stream.println(cx.length + " " + 3);
		for (int i = 0; i < cx.length; i++) {
			stream.print("   ");
			for (int j = 0; j < 3; j++) {
				stream.print(' ');
				stream.print(cx[i][j]);
			}
			stream.println();
		}
		
		stream.println(cy.length + " " + 3);
		for (int i = 0; i < cy.length; i++) {
			stream.print("   ");
			for (int j = 0; j < 3; j++) {
				stream.print(' ');
				stream.print(cy[i][j]);
			}
			stream.println();
		}

		stream.close();
	}
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
    	fileChooser.chose();
    	File file = fileChooser.getChaosFile();
    	if (file == null) {
			return false;
		}
    	ChaosFileSaver saver = new ChaosFileSaver(file);
    	saver.save();
    	ChaosData.current.setChanged(false);
    	
    	FileHistoryManager.get().add(file);
    	App.mainWindow.setTitle(file);
    	
    	return true;
	}
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean checkFileSave() {
		if (ChaosData.current.isChanged()) {
			int result = ErrorMessageDialog.createSaveDialog();
			if (result == JOptionPane.YES_OPTION) {
				return staticSave();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}
}
