package com.yizhuoyan.view;

import com.yizhuoyan.model.FileItemView;
import com.yizhuoyan.model.TaskWorkCallback;
import com.yizhuoyan.model.TaskWorker;
import com.yizhuoyan.model.TaskWorker.TaskProcessInfo;
import com.yizhuoyan.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AppWindow extends JFrame implements ActionListener, TaskWorkCallback {

    private JFileChooser chooser;
    private List<FileItemView> selectedFiles;




    private JList<Object> fileListView;
    private JProgressBar progressBar;
    private JButton beginBtn;
    private JLabel selectFileTitleView;
    private JDialog messageDialog;

    public AppWindow() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("文件md5签名修改");
        this.setSize(960, 640);
        initLayout();

    }

    private void initLayout() {

        {
            JPanel selectFileActionPanel=new JPanel();

            JButton selectFileBtn = new JButton("选择文件");
            selectFileBtn.setActionCommand("selectFile");
            selectFileBtn.addActionListener(this);
            selectFileActionPanel.add(selectFileBtn);
            {
                JPanel selectFilesInfoPanel = new JPanel(new FlowLayout());

                selectFilesInfoPanel.add(new JLabel("已选择文件数:"));
                selectFileTitleView = new JLabel("0");
                selectFilesInfoPanel.add(selectFileTitleView);

                selectFileActionPanel.add(selectFilesInfoPanel);
            }

            JButton unselectFileBtn = new JButton("清空已选文件");
            unselectFileBtn.setActionCommand("clearFile");
            unselectFileBtn.addActionListener(this);
            selectFileActionPanel.add(unselectFileBtn);

            this.add(selectFileActionPanel, BorderLayout.NORTH);
        }
        {
            JPanel fileListPanel = new JPanel(new BorderLayout());

            {

                this.fileListView = new JList<Object>();
                this.fileListView.setAutoscrolls(false);
                this.fileListView.setFocusable(false);
                this.fileListView.setRequestFocusEnabled(false);
                this.fileListView.setDragEnabled(false);
                JScrollPane listViewScrollPane = new JScrollPane(fileListView);
                listViewScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                listViewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                fileListPanel.add(listViewScrollPane, BorderLayout.CENTER);
            }
            this.add(fileListPanel, BorderLayout.CENTER);

        }
        {
            JPanel actionBar = new JPanel();
            actionBar.setLayout(new BorderLayout());
            this.progressBar = new JProgressBar();

            this.progressBar.setStringPainted(true);
            actionBar.add(progressBar);
            beginBtn = new JButton();
            beginBtn.setActionCommand("begin");

            actionBar.add(beginBtn, BorderLayout.EAST);

            this.add(actionBar, BorderLayout.SOUTH);
        }
        paintNoFileSelected();
    }

    private void paintNoFileSelected(){
        this.progressBar.setString("请先选择文件");
        this.beginBtn.setText("开始处理");
        selectFileTitleView.setText("0");
        this.fileListView.setListData(new Object[0]);
        beginBtn.removeActionListener(this);
        this.beginBtn.setEnabled(false);
        this.progressBar.setMaximum(0);
        this.progressBar.setValue(0);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "selectFile":
                this.handleSelectFileBtnClick();
                break;
            case "clearFile":
                this.handleClearSelectFilesBtnClick();
                break;
            case "begin":
                this.handleBeginBtnClick();

            default:
                break;
        }
    }

    private void handleBeginBtnClick() {
        if (this.selectedFiles.size()== 0) {
            paintNoFileSelected();
        } else {
            new TaskWorker(this).execute();
        }
    }

    private int showFileChooser() {
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileHidingEnabled(true);
        }
        return chooser.showOpenDialog(this);
    }

    private void handleClearSelectFilesBtnClick(){
        this.selectedFiles.clear();
        paintNoFileSelected();
    }

    private void handleSelectFileBtnClick() {
        int result = showFileChooser();
        if(result==JFileChooser.APPROVE_OPTION){
            List<File> fileCollector=new LinkedList<>();
            loadAllSelectFiles(chooser.getSelectedFiles(),fileCollector);
            if(fileCollector.size()>0){
                selectedFiles=new ArrayList<>(fileCollector.size());
                int i=0;
                for(File f:fileCollector){
                    selectedFiles.add(new FileItemView(i++,f));
                }

                updateListView(selectedFiles);
                taskStartView();
            }
        }

    }

    private void loadAllSelectFiles(File[] fs,List<File> fileCollector) {
        if (fs == null) return;
        for (File file : fs) {
            if (file.isFile()) {
                if(!fileCollector.contains(file)) {
                    fileCollector.add(file);
                }
            } else if (file.isDirectory()) {
                loadAllSelectFiles(file.listFiles(),fileCollector);
            }
        }
    }


    private void updateListView(final List<FileItemView> listData) {

        this.fileListView.setModel(new AbstractListModel<Object>() {
            @Override
            public int getSize() {
                return listData.size();
            }
            @Override
            public Object getElementAt(int index) {
                return listData.get(index);
            }
        });
    }
    private void taskStartView(){
            this.beginBtn.setEnabled(true);
            this.beginBtn.setText("开始处理");
            this.beginBtn.addActionListener(this);
            int total = selectedFiles.size();
            selectFileTitleView.setText(String.valueOf(total));
            this.progressBar.setMaximum(total);
            this.progressBar.setValue(0);
            this.progressBar.setString("0/" + total);

    }

    @Override
    public void begin(TaskProcessInfo info) {
        this.beginBtn.setEnabled(false);
        this.beginBtn.setText("处理中..");
    }

    @Override
    public void process(List<TaskProcessInfo> infos) {
        for (TaskProcessInfo info : infos) {
                this.progressBar.setString(info.current+1 + "/"
                        + info.total);
                this.progressBar.setValue(info.current+1);
                FileItemView fv=selectedFiles.get(info.current);
                fv.setDonePercent(100);
                updateListView(selectedFiles);
        }
    }

    @Override
    public void done(TaskProcessInfo info) {
        this.beginBtn.removeActionListener(this);
        JOptionPane.showMessageDialog(this,"处理完毕！");
        paintNoFileSelected();
    }


    @Override
    public boolean processError(Exception e, TaskProcessInfo info) {
        JOptionPane.showMessageDialog(this,e.getMessage());
        return true;
    }

    @Override
    public List<FileItemView> getTaskFiles() {

        return selectedFiles;
    }
}