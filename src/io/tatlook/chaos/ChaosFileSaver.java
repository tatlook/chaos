/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import javax.swing.JFileChooser;

/**
 * @author Administrator
 *
 */
public class ChaosFileSaver {
	private PrintStream stream;
	
	public ChaosFileSaver(File file) {
		try {
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
	
	public static void staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
    	fileChooser.chose();
    	File file = fileChooser.getChaosFile();
    	ChaosFileSaver saver = new ChaosFileSaver(file);
    	saver.save();
	}
}