/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		JMenu fileMenu = new JMenu("�ļ�");
		fileMenu.setMnemonic('F');
		
		JMenuItem newMenuItem = new JMenuItem("�½�");
        JMenuItem openMenuItem = new JMenuItem("��");
        JMenuItem exitMenuItem = new JMenuItem("�˳�");
        // �Ӳ˵���ӵ�һ���˵�
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();       // ���һ���ָ���
        fileMenu.add(exitMenuItem);
        
        exitMenuItem.addActionListener((e) -> {
        	System.exit(0);
        });
        openMenuItem.addActionListener((e) -> {
        	ChaosFileChooser fileChooser = new ChaosFileChooser();
        	fileChooser.chose();
        	File file = fileChooser.getChaosFile();
        	try {
				ChaosFileParser parser = new ChaosFileParser(file);
				parser.readChaos();
				App.mainWindow.UI();
        	} catch (ChaosFileDataException e1) {
        		e1.openDialog();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        });
		
		add(fileMenu);
	}
}
