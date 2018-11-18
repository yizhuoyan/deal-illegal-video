package com.yizhuoyan;

import com.yizhuoyan.view.AppWindow;

import javax.swing.*;

public class Application {

	public static void main(String[] args)throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AppWindow win=new AppWindow();
				win.setLocationRelativeTo(null);
				win.setVisible(true);
			}
		});
	}
}
