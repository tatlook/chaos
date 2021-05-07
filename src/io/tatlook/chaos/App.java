package io.tatlook.chaos;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class App {
    public static void main(String[] args) {
    	MainWindow mainWindow = new MainWindow();
    	
    	
    	// ����һ��Ĭ�ϵ��ļ�ѡȡ��
        JFileChooser fileChooser = new JFileChooser();

        // ����Ĭ����ʾ���ļ���Ϊ��ǰ�ļ���
        fileChooser.setCurrentDirectory(new File("."));

        // �����ļ�ѡ���ģʽ��ֻѡ�ļ���ֻѡ�ļ��С��ļ����ļ�����ѡ��
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // �����Ƿ������ѡ
        fileChooser.setMultiSelectionEnabled(true);

        // ��ӿ��õ��ļ���������FileNameExtensionFilter �ĵ�һ������������, ��������Ҫ���˵��ļ���չ�� �ɱ������
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Plain text(*.txt)", "txt"));
        // ����Ĭ��ʹ�õ��ļ�������
        fileChooser.setFileFilter(new FileNameExtensionFilter("Chaos file(*.ch)", "ch"));

        // ���ļ�ѡ����߳̽�������, ֱ��ѡ��򱻹رգ�
        int result = fileChooser.showOpenDialog(mainWindow);

        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            // ��������"ȷ��", ���ȡѡ����ļ�·��
            file = fileChooser.getSelectedFile();

            // �������ѡ�����ļ�, ��ͨ�����淽����ȡѡ��������ļ�
            // File[] files = fileChooser.getSelectedFiles();

            System.out.println("���ļ�: " + file.getAbsolutePath() + "\n\n");
        } else if (result == JFileChooser.CANCEL_OPTION) {
			file = new File("D:/Documents/p.txt");
		}
    	try {
			new ChaosFileParser(file).readChaos();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	mainWindow.UI();
    	mainWindow.setVisible(true);
    } 
} 