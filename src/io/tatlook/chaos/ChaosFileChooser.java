/**
 * 
 */
package io.tatlook.chaos;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Administrator
 *
 */
public class ChaosFileChooser {
	private File chaosFile;
	private int dialogMode = JFileChooser.OPEN_DIALOG;
	public ChaosFileChooser(int dialogMode) {
		this.dialogMode = dialogMode;
	}

	public ChaosFileChooser() {
	}

	public void chose() {
        JFileChooser fileChooser = new JFileChooser();

        // ����Ĭ����ʾ���ļ���Ϊ��ǰ�ļ���
        fileChooser.setCurrentDirectory(new File("."));

        // �����ļ�ѡ���ģʽ��ֻѡ�ļ���ֻѡ�ļ��С��ļ����ļ�����ѡ��
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // �����Ƿ������ѡ
        fileChooser.setMultiSelectionEnabled(false);

        // ��ӿ��õ��ļ���������FileNameExtensionFilter �ĵ�һ������������, ��������Ҫ���˵��ļ���չ�� �ɱ������
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plain text(*.txt)", "txt"));
        // ����Ĭ��ʹ�õ��ļ�������
        fileChooser.setFileFilter(new FileNameExtensionFilter("Chaos file(*.ch)", "ch"));

        
        int result;
        if (dialogMode == JFileChooser.OPEN_DIALOG) {
            result = fileChooser.showOpenDialog(App.mainWindow);
        } else {
        	result = fileChooser.showSaveDialog(App.mainWindow);
        }
        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            // ��������"ȷ��", ���ȡѡ����ļ�·��
            file = fileChooser.getSelectedFile();

            // �������ѡ�����ļ�, ��ͨ�����淽����ȡѡ��������ļ�
            // File[] files = fileChooser.getSelectedFiles();

            System.out.println("���ļ�: " + file.getAbsolutePath() + "\n\n");
        } else if (result == JFileChooser.CANCEL_OPTION || result == JFileChooser.ERROR_OPTION) {
			file = new File("bin/sysrule.ch");
		}
        chaosFile = file;
	}
	
	/**
	 * @return the chaosFile
	 */
	public File getChaosFile() {
		return chaosFile;
	}
}
