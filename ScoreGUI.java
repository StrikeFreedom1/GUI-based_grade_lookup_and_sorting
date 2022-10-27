import com.sun.xml.internal.bind.v2.TODO;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.xml.ws.soap.Addressing;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Author:ZengHao
 * CreateTime:2022-10-09-18:44
 * Description:Simple introduction of the code
 */
public class ScoreGUI extends Thread {
    JFrame jf = new JFrame("成绩查询");
    //声明各个组件
    JTextArea textArea;
    JLabel averageLabel, maxLabel, minLabel, rankLabel, inforLabel;
    JTextField averageField, maxField, minField;
    JPanel averagePanel, maxPanel, minPanel, textPanel, rankPanel, inforPanel;

    File studentFile = new File("Transcript.txt");
    File sortFile = new File("ScoreSort.txt");

    //初始化组件的基本设置
    private void initComponent() {
        //设置字体样式
        Font fnt = new Font("Microsoft YaHei", Font.PLAIN, 14);
        averageLabel = new JLabel("平均值");
        maxLabel = new JLabel("最大值");
        minLabel = new JLabel("最小值");
        rankLabel = new JLabel("成绩排名");
        inforLabel = new JLabel("基本信息");
        averageLabel.setFont(fnt);
        maxLabel.setFont(fnt);
        minLabel.setFont(fnt);
        rankLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        inforLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        //设置文本栏
        averageField = new JTextField(10);
        maxField = new JTextField(10);
        minField = new JTextField(10);
        //设置文本框样式
        textArea = new JTextArea(16, 30);
        textArea.setFont(fnt);
        textArea.setBackground(new Color(245, 255, 255));
        textArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(128, 128, 128), new Color(211, 211, 211)));
        textArea.setCaretColor(Color.CYAN);
        //给文本框添加滚动条
        JScrollPane jScrollPane = new JScrollPane(textArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //声明Panel并将组件添加到Panel上
        averagePanel = new JPanel();
        maxPanel = new JPanel();
        minPanel = new JPanel();
        textPanel = new JPanel();
        rankPanel = new JPanel();
        inforPanel = new JPanel();
        averagePanel.add(averageLabel);
        averagePanel.add(averageField);
        maxPanel.add(maxLabel);
        maxPanel.add(maxField);
        minPanel.add(minLabel);
        minPanel.add(minField);
        textPanel.add(jScrollPane);
        //将rankLabel,inforLabel放到对应的Panel上并居中
        rankPanel.add(rankLabel, JLabel.CENTER);
        inforPanel.add(inforLabel, JLabel.CENTER);
    }

    public void init() {
        //初始化组件
        initComponent();
        //将上述的Panel添加到Jframe上
        JPanel contain01 = new JPanel();
        JPanel contain02 = new JPanel();
        contain01.setLayout(new BorderLayout());
        contain02.setLayout(new BorderLayout());
        contain01.add(averagePanel, BorderLayout.NORTH);
        contain01.add(maxPanel, BorderLayout.CENTER);
        contain01.add(minPanel, BorderLayout.SOUTH);
        contain02.add(rankPanel, BorderLayout.NORTH);
        contain02.add(textPanel, BorderLayout.CENTER);
        contain02.add(inforPanel, BorderLayout.SOUTH);
        jf.setLayout(new BorderLayout());
        jf.add(contain02, BorderLayout.NORTH);
        jf.add(contain01, BorderLayout.CENTER);

        //实现对成绩的排序，并将数据输出到文件ScoreSort.txt
        if(studentFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(studentFile));
                String str = null;
                ArrayList<String> list = new ArrayList<>();
                while ((str=br.readLine())!=null){
                    list.add(str);
                }
                br.close();//关闭流
                int length = list.size();
                //将数据存入字符串数组
                String studentInfor[][] = new String[length][3];
                String []temp = new String[3];
                int i = 0;
                for (String sLine : list) {
                    temp = sLine.split("\t");
                    studentInfor[i][0] = temp[0];
                    studentInfor[i][1] = temp[1];
                    studentInfor[i][2] = temp[2];
                    i++;
                }
                //计算成绩数据
                int meanScore = 0;
                int maxScore,minScore,sum = 0;
                for(int num=0;num<length;num++){
                    sum += Integer.parseInt(studentInfor[num][2]);//将字符串转换为数字
                }
                meanScore = sum/length;
                //开始对成绩进行冒泡排序
                for(int num1=0;num1<length-1;num1++){
                    for (int num2=0;num2<length-1-num1;num2++){
                        if (Integer.parseInt(studentInfor[num2][2])<Integer.parseInt(studentInfor[num2+1][2])){
                            String s[] = new String[3];
                            s = studentInfor[num2];
                            studentInfor[num2] = studentInfor[num2+1];
                            studentInfor[num2+1] = s;
                        }
                    }
                }
                //将排序的结果写入到文件中
                BufferedWriter bw = new BufferedWriter(new FileWriter(sortFile));//每次添加数据后要重新排序，使用覆盖的形式
                for(int n=0;n<length;n++){
                    bw.write(studentInfor[n][0]+"\t"+studentInfor[n][1]+"\t"+studentInfor[n][2]);
                    bw.newLine();
                }
                bw.close();
                //获取排序后成绩的最大值和最小值
                maxScore = Integer.parseInt(studentInfor[0][2]);
                minScore = Integer.parseInt(studentInfor[length-1][2]);
                //将计算的结果显示到文本框
                averageField.setText(Integer.toString(meanScore));
                maxField.setText(Integer.toString(maxScore));
                minField.setText(Integer.toString(minScore));
                //将排序后的数据展示在文本框
                for(int k=0;k<length;k++){
                    textArea.append(studentInfor[k][0]+"\t"+studentInfor[k][1]+"\t"+studentInfor[k][2]+"\n");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(jf,"排序失败，文件不存在！！！","警告",JOptionPane.WARNING_MESSAGE);
            System.out.println("排序失败文件不存在");
        }





        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置只关闭当前的窗口
        jf.setVisible(true);
        jf.pack();
    }


    @Override
    public void run() {
        super.run();
        new ScoreGUI().init();
    }

    public static void main(String[] args) {
        new ScoreGUI().start();
    }
}
