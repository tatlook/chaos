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

package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import io.tatlook.chaos.MainWindow.MainWindowListener;
import io.tatlook.chaos.parser.NullFileParser;
import io.tatlook.chaos.saver.AbstractFileSaver;

/**
 * @author Administrator
 *
 */
public class MenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7347092499428362250L;
	
	public MenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		
		fileMenu.setMnemonic('F');
		viewMenu.setMnemonic('V');
		
		JMenuItem newMenuItem = new JMenuItem("New");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem openMenuItem = new JMenuItem("Open");
		JMenu openRecentMenu = FileHistoryManager.get().getMenu();
		JMenuItem saveImageMenuItem = new JMenuItem("Save Image");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		newMenuItem.setMnemonic('N');
		saveMenuItem.setMnemonic('S');
		openMenuItem.setMnemonic('O');
		openRecentMenu.setMnemonic('R');
		saveImageMenuItem.setMnemonic('I');
		exitMenuItem.setMnemonic('E');
		
		fileMenu.add(newMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(openRecentMenu);
		fileMenu.add(saveImageMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		
		newMenuItem.addActionListener((e) -> {
			if (AbstractFileSaver.checkFileSave() == false) {
				return;
			} 
			
			new NullFileParser().parse();
			App.setCurrentFile(null);
			App.mainWindow.updateToolPanel();
			App.mainWindow.getDrawer().setChange();
		});
		saveMenuItem.addActionListener((e) -> {
			AbstractFileSaver.staticSave();
		});
		openMenuItem.addActionListener((e) -> {
			AbstractFileSaver.checkFileSave();
			
			ChaosFileChooser fileChooser = new ChaosFileChooser();
			fileChooser.choose();
			File file = fileChooser.getFile();
			if (file == null || !file.exists() || !file.canRead()) {
				return;
			}
			try {
				ChaosFileChooser.staticOpen(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		saveImageMenuItem.addActionListener((e) -> {
			ImageFileChooser fileChooser = new ImageFileChooser();
			fileChooser.choose();
			File file = fileChooser.getImageFile();
			if (file == null) {
				return;
			}
			String imageType = fileChooser.getImageType();
			Drawer drawer = App.mainWindow.getDrawer();
			
			BufferedImage image = new BufferedImage(
					drawer.getImageSize(), drawer.getImageSize(), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			graphics.drawImage(drawer.getImage(), 0, 0, null);
			graphics.dispose();
			
			try {
				ImageIO.write(image, imageType, file);
			} catch (IOException e1) {
				ErrorMessageDialog.createExceptionDialog(e1);
			}
		});
		exitMenuItem.addActionListener((e) -> {
			MainWindowListener mainWindowListener = MainWindow.getWindowListener();
			mainWindowListener.windowClosing(App.mainWindow);
			mainWindowListener.windowClosed(App.mainWindow);
		});
		
		JMenuItem cleanImageMenuItem = new JMenuItem("Clean Display");
		JMenuItem intoMiddleMenuItem = new JMenuItem("Into the Middle");
		JMenuItem chooseColorMenuItem = new JMenuItem("Select Color");
		JMenuItem imageConfigurationMenuItem = new JMenuItem("Image Configuration");
		JMenuItem fullScreenMenuItem = new JMenuItem("Full Screen (F11)");
		cleanImageMenuItem.setMnemonic('C');
		intoMiddleMenuItem.setMnemonic('M');
		chooseColorMenuItem.setMnemonic('O');
		imageConfigurationMenuItem.setMnemonic('I');
		fullScreenMenuItem.setMnemonic('F');
		
		viewMenu.add(cleanImageMenuItem);
		viewMenu.add(intoMiddleMenuItem);
		viewMenu.add(chooseColorMenuItem);
		viewMenu.add(imageConfigurationMenuItem);
		viewMenu.addSeparator();
		viewMenu.add(fullScreenMenuItem);
		
		cleanImageMenuItem.addActionListener((e) -> App.mainWindow.getDrawer().clean());
		intoMiddleMenuItem.addActionListener((e) -> App.mainWindow.getDrawer().intoMiddle());
		chooseColorMenuItem.addActionListener((e) -> {
			Color color = JColorChooser.showDialog(
					App.mainWindow, 
					"Color Chooser", 
					App.mainWindow.getDrawer().getPenColor());
			if (color != null) {
				App.mainWindow.getDrawer().setPenColor(color);				
			}
		});
		imageConfigurationMenuItem.addActionListener((e) -> {
			new ImageConfigurationDialog().setVisible(true);
		});
		fullScreenMenuItem.addActionListener((e) -> {
			App.mainWindow.fullScreen();
		});
		
		add(fileMenu);
		add(viewMenu);
	}
}
