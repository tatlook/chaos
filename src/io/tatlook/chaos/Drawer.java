/**
 * 
 */
package io.tatlook.chaos;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import edu.princeton.cs.algs4.StdRandom;

/**
 * @author Administrator
 *
 */
public class Drawer extends JComponent implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839460539960225035L;

	private Thread drawTheard = new Thread(this);
	
	private final int trials = 100000;	// ��������*�����������������У�����ֱ�Ӹ�ֵ��*
	
	private Graphics graphics;
	
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		graphics = g.create();
		if (graphics == null) {
			System.err.println("EEEE");
		}
	}
	
	public void start() {
		drawTheard.start();
	}
	
	@Override
	public void run() {
		ChaosFileParser parser = ChaosFileParser.getCurrentFileParser();
		parser.readChaos();
		// ÿ���任��ִ�и���
        double[] dist = parser.dist;
        // ����ֵ
        double[][] cx = parser.cx;
        double[][] cy = parser.cy;

        // ��ʼֵ (x, y)
        double x = 0.0, y = 0.0;

        for (int t = 0; t < trials; t++) { 

            // ���ݸ��ʷֲ����ѡ��任
            int r = StdRandom.discrete(dist); 

            // ����
            double x0 = cx[r][0] * x + cx[r][1] * y + cx[r][2]; 
            double y0 = cy[r][0] * x + cy[r][1] * y + cy[r][2]; 
            x = x0; 
            y = y0; 

            // ���ƽ��
            graphics.drawLine((int)x, (int)y, (int)x, (int)y); 

            // ÿ����100����ʾ1��
            if (t % 100 == 0) {
                
            }
        } 
	
	}
}
