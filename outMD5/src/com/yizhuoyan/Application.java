package com.yizhuoyan;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.yizhuoyan.view.AppWindow;

public class Application {

	public static void main(String[] args)throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AppWindow win=new AppWindow();
				win.setVisible(true);
			}
		});
	}
}
