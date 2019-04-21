package com.yizhuoyan.model;

import com.yizhuoyan.model.TaskWorker.TaskProcessInfo;
import com.yizhuoyan.service.OutMD5Service;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class TaskWorker extends SwingWorker<Object, TaskProcessInfo> {
    public static class TaskProcessInfo {
        public int current;
        public int total;

        public TaskProcessInfo(int current,
                               int total) {
            this.current = current;
            this.total = total;
        }

    }


    private final TaskWorkCallback callback;
    final private List<FileItemView> taskFiles;

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
        List<FileItemView> fileItems = this.taskFiles;

        OutMD5Service service = new OutMD5Service();

        for (int i = 0; i < fileItems.size(); i++) {
            try {
                service.doOneFile(fileItems.get(i).getFile());
                this.publish(new TaskProcessInfo(i, fileItems.size()));
            } catch (Exception e) {
                if (this.callback.processError(e, new TaskProcessInfo(i, -1))) {
                    continue;
                }
            }
        }

        return null;
    }


    @Override
    protected void process(List<TaskProcessInfo> infos) {
        this.callback.process(infos);

    }

}
