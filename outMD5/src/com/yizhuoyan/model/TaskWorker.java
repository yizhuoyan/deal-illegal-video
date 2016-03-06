package com.yizhuoyan.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.SwingWorker;

import com.yizhuoyan.model.TaskWorker.TaskProcessInfo;

public class TaskWorker extends SwingWorker<Object, TaskProcessInfo> {
	public static class TaskProcessInfo {
		public File[] taskFiles;
		public int fileIndex;
		public int current;
		public int total;

		public TaskProcessInfo(File[] taskFiles, int fileIndex, int current,
				int total) {
			this.taskFiles = taskFiles;
			this.fileIndex = fileIndex;
			this.current = current;
			this.total = total;
		}

	}

	public static final String SIGN_CHARS = "yizhuoyan";

	private final TaskWorkCallback callback;
	final private File[] taskFiles;

	public TaskWorker(TaskWorkCallback callback) {
		this.callback = callback;
		this.taskFiles = callback.getTaskFiles();
	}

	@Override
	protected void done() {
		this.callback.done(null);
	}
	@Override
	protected Object doInBackground() throws Exception {
		File[] files = this.taskFiles;
		for (int i = 0; i < files.length; i++) {
			try {
				this.handle(i, files[i], "done_" + files[i].getName());
			} catch (Exception e) {
				if(this.callback.processError(e, new TaskProcessInfo(files, i, -1, -1))){
					continue;
				}
			}
		}

		return null;
	}

	public void handle(int fileIndex, File path, String newFileName)
			throws Exception {
		File dest = new File(path.getParentFile(), newFileName);
		try (InputStream in = new FileInputStream(path);
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(dest), 1024 * 1024 * 10);) {
			int len = 0;
			byte[] bs = new byte[1024 * 1024];
			int total = in.available() + 9;
			this.publish(new TaskProcessInfo(this.taskFiles,fileIndex, 0, total));
			int current = 0;
			while ((len = in.read(bs)) != -1) {
				out.write(bs, 0, len);
				current += len;
				this.publish(new TaskProcessInfo(this.taskFiles,fileIndex, current, total));
			}
			out.write(SIGN_CHARS.getBytes());
			this.publish(new TaskProcessInfo(this.taskFiles,fileIndex, total, total));
		}

	}

	@Override
	protected void process(List<TaskProcessInfo> infos) {
			this.callback.process(infos);

	}

}
