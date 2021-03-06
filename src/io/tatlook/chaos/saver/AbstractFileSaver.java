/*
 * Chaos - simple 2D iterated function system plotter and editor.
 * Copyright (C) 2021 YouZhe Zhen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.tatlook.chaos.saver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import io.tatlook.chaos.App;
import io.tatlook.chaos.ChaosData;
import io.tatlook.chaos.ChaosFileChooser;
import io.tatlook.chaos.ErrorMessageDialog;
import io.tatlook.chaos.FileHistoryManager;

/**
 * The parent class of all file savers.
 * 
 * @author YouZhe Zhen
 */
public abstract class AbstractFileSaver {
	protected PrintStream out;
	protected File file;
	protected ChaosData data;
	
	/**
	 * Constructs a new file saver with the target file.
	 * 
	 * @param file the target file
	 * @param data the data to output to the target file
	 */
	public AbstractFileSaver(File file, ChaosData data) {
		this.file = file;
		this.data = data;
		try {
			if (file == null) {
				throw new AssertionError();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new PrintStream(file);
		} catch (IOException e) {
			ErrorMessageDialog.createExceptionDialog(e);
		}
	}
	
	/**
	 * Key file saving steps.
	 * The data is provided by {@link #data}
	 */
	public abstract void save();
	
	/**
	 * 
	 * @return false älä tee joatin
	 */
	public static boolean staticSave() {
		ChaosFileChooser fileChooser = new ChaosFileChooser(JFileChooser.SAVE_DIALOG);
		fileChooser.choose();
		File file = fileChooser.getFile();
		if (file == null) {
			return false;
		}
		AbstractFileSaver saver;
		switch (getFileExtension(file)) {
			case "ifs":
				saver = new FractintFileSaver(file, ChaosData.current);
				break;
			case "ch":
			default: 
				saver = new ChaosFileSaver(file,  ChaosData.current);
				break;
		};
		saver.save();
		ChaosData.current.setCurrentToOrigin();
		ChaosData.current.checkChanged();
		
		FileHistoryManager.get().add(file);
		App.setCurrentFile(file);
		App.mainWindow.updateTitle();
		
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
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex != -1 && lastIndex != 0) {
			return fileName.substring(lastIndex + 1).toLowerCase();
		} else {
			return "";
		}
	}
}
