package com.yizhuoyan.model;

import java.io.File;
import java.util.List;

public interface TaskWorkCallback {
	File[] getTaskFiles();
	public void begin(TaskWorker.TaskProcessInfo info);
	void process(List<TaskWorker.TaskProcessInfo> infos);
	void done(TaskWorker.TaskProcessInfo info);
	boolean processError(Exception e,TaskWorker.TaskProcessInfo info);
}
