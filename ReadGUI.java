import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:ZengHao
 * CreateTime:2022-10-09-2:58
 * Description:Simple introduction of the code
 */
public class ReadGUI extends Thread {
    JFrame jf = new JFrame("学生成绩管理");
    //声明各个组件
    JLabel name, id, score, information;
    JTextField nameField, idField, scoreField;
    JButton addButton, clearButton, searchButton;//信息的添加、保存和删除
    JTextArea textArea;//显示添加的信息
    JPanel namePanel, idPanel, scorePanel, textPanel, buttonPanel, inforPanel;
    ScoreGUI scoreGui;
    //创建File对象，文本文件保存为相对地址
    File studentFile = new File("Transcript.txt");


    //初始化各个组件完成基本设置
    private void initComponent() {
        //设置字体样式
        Font fnt = new Font("Microsoft YaHei", Font.PLAIN, 14);
        //添加成绩的标签
        name = new JLabel("姓名：");
        name.setFont(fnt);
        id = new JLabel("学号：");
        id.setFont(fnt);
        score = new JLabel("成绩：");
        score.setFont(fnt);
        information = new JLabel("已添加的信息");
        information.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        //初始化文本栏
        nameField = new JTextField(30);
        idField = new JTextField(30);
        scoreField = new JTextField(30);
        //初始化按钮
        addButton = new JButton("添加成绩");
        addButton.setFont(fnt);
        clearButton = new JButton("删除成绩");
        clearButton.setFont(fnt);
        searchButton = new JButton("查询成绩信息");
        searchButton.setFont(fnt);
        //设置按钮点击文本边框不可见
        addButton.setFocusPainted(false);
        clearButton.setFocusPainted(false);
        searchButton.setFocusPainted(false);
        //设置文本框的样式,和边框的样式
        textArea = new JTextArea(16, 30);
        textArea.setFont(fnt);
        textArea.setBackground(new Color(245, 255, 255));
        textArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(128, 128, 128), new Color(211, 211, 211)));
        textArea.setCaretColor(Color.CYAN);
        //将每一个组件添加到Panel组件上
        namePanel = new JPanel();
        idPanel = new JPanel();
        scorePanel = new JPanel();
        buttonPanel = new JPanel();
        textPanel = new JPanel();
        inforPanel = new JPanel();
        namePanel.add(name);
        namePanel.add(nameField);
        idPanel.add(id);
        idPanel.add(idField);
        scorePanel.add(score);
        scorePanel.add(scoreField);
        inforPanel.add(information, JLabel.CENTER);//设置居中

        JScrollPane scrollPane = new JScrollPane(textArea);//文本框添加滚动条
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//滚动条一直显示
        textPanel.add(scrollPane);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(searchButton);
    }

    //定义方法删除文本框的内容
    public void clearText(){
        //读取文件内容并删除最后的部分数据
        String[] content = textArea.getText().split("\n");
        int length = content.length;

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<length-2;i++){
            sb.append(content[i]+"\n");
        }
        textArea.setText(sb.toString());
    }

    //定义方法实现数据的添加
    public void addGrade(String information){
        //文件不存在创建改文件
        if(!studentFile.exists()){
            try {
                studentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        else if(studentFile.exists()){
//            //文件存在就删除之前的文件，创建新的文件
//            studentFile.delete();
//            try {
//                studentFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            FileWriter fw = new FileWriter(studentFile,true);//文件字符输出流，设置成追加内容而不是覆盖内容
            BufferedWriter bw = new BufferedWriter(fw);//转化成输出流
            bw.write(information);//写入字符串
            bw.newLine();//写入一个换行符
            bw.close();//关闭缓冲输出流
            fw.close();//关闭文件字符输出流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //定义方法实现数据的删除
    public void deleteGrade(){
        //首先要将文本中的数据读取出来，使用集合存储，删除数据后将数据再写入文件中
        if (studentFile.exists()){
            ArrayList<String> list = new ArrayList();
            try {
                FileReader fr = new FileReader(studentFile);
                BufferedReader br = new BufferedReader(fr);
                String str = null;
                //逐行读取文件
                while((str=br.readLine())!=null){
                    list.add(str);
                }
                int length = list.size();
                studentFile.delete();
                studentFile.createNewFile();
                FileWriter fw = new FileWriter(studentFile);//此时实现文件的添加不能以追加的方式
                BufferedWriter bw = new BufferedWriter(fw);
                list.remove(length-1);//删除最后一个元素
                //将数据重新写入文件
                for (String s : list) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("文件不存在！！！");
        }
    }

    public void init() {
        //调用方法初始化组件
        initComponent();
        //设置布局方式
        JPanel contain01 = new JPanel();
        JPanel contain02 = new JPanel();
        contain01.setLayout(new BorderLayout());
        contain02.setLayout(new BorderLayout());
        contain01.add(namePanel, BorderLayout.NORTH);
        contain01.add(idPanel, BorderLayout.CENTER);
        contain01.add(scorePanel, BorderLayout.SOUTH);
        contain02.add(contain01, BorderLayout.NORTH);
        contain02.add(buttonPanel, BorderLayout.CENTER);
        contain02.add(inforPanel, BorderLayout.SOUTH);
        //将每个Panel放置到JFrame上
        jf.setLayout(new BorderLayout());
        jf.add(contain02, BorderLayout.NORTH);
        jf.add(textPanel, BorderLayout.SOUTH);

        //点击成绩添加按钮，将数据显示到文本框同时存入文本文件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取文本信息
                String name = nameField.getText();
                String id = idField.getText();
                String score = scoreField.getText();
                //判断输入格式是否正确，格式错误给出提示,使用equals判断输入的内容
                if(!name.equals("") && !id.equals("") && !score.equals("")){
                    //将成绩添加到文本框
                    String information = new String(name+"\t"+id+"\t"+score+"\n");
                    String information01 = new String(name+"\t"+id+"\t"+score);
                    textArea.append(information);
                    textArea.append("--成绩添加成功\n");
                    //添加成功后清空文本条
                    nameField.setText("");
                    idField.setText("");
                    scoreField.setText("");
                    //TODO 添加到文本文件中
                    addGrade(information01);
                }else{
                    if(name.equals("")){
                        JOptionPane.showMessageDialog(jf,"姓名栏不能为空","警告",JOptionPane.WARNING_MESSAGE);
                    }else if(id.equals("")){
                        JOptionPane.showMessageDialog(jf,"学号栏不能为空","警告",JOptionPane.WARNING_MESSAGE);
                    }else if(score.equals("")){
                        JOptionPane.showMessageDialog(jf,"成绩栏不能为空","警告",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });


        //点击删除按钮，前一个添加的信息
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取文本框中的内容，判断不为空才能进行数据的删除不然给出提示
                String information = textArea.getText();
                if(!information.equals("")){
                    //删除文本框的部分信息
                    clearText();
                    //删除文本文件中的该部分数据
                    deleteGrade();
                }else {
                    JOptionPane.showMessageDialog(jf,"文本框信息为空，不能删除数据","警告",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //点击成绩查询按钮出现成绩成绩查询的GUI
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreGui = new ScoreGUI();
                scoreGui.init();
            }
        });

        //设置窗口可见和自适应
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);

    }

    @Override
    public void run() {
        super.run();
        new ReadGUI().init();
    }

    public static void main(String[] args) {
        new ReadGUI().start();
    }

}
