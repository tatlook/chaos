/**
 * 
 */
package io.tatlook.chaos;

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
		
		add(fileMenu);
	}
}
