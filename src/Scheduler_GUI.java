import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Scheduler_GUI extends JFrame {
    private JTable Input_table;
    private JTable Output_table;
    private JTextField Execution_time_textField;
    private JTextField Response_time_textField;
    private JTextField Turnaround_time_textField;
    private JTextField Throughput_textField;
    private JTextField Waiting_time_textField;
    private JTextField CPU_utilization_ratio_textField;
    private JTextField Time_Quantum_textField;
    private JTextField Context_Switch_textField;
    private JLabel File_Path_JLabel;
    private JComboBox comboBox;
    private String filePath;
    private DrawGantt DrawGantt;
    private Container container;
    public Scheduler_GUI(Scheduler scheduler) {
        container = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 800);
        container.setLayout(null);

        String[] inputColumn = {"Process", "Arrive time", "Burst time","Priority"};
        DefaultTableModel inputModel = new DefaultTableModel(inputColumn, 0);
        Input_table = new JTable(inputModel);
        Input_table.getTableHeader().setReorderingAllowed(false);
        Input_table.getTableHeader().setResizingAllowed(false);
        Input_table.setEnabled(false);
        JScrollPane inputTableScrollPane = new JScrollPane(Input_table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputTableScrollPane.setBounds(12, 98, 760, 160);
        container.add(inputTableScrollPane);

        JLabel input = new JLabel("Input");
        input.setBounds(12, 63, 70, 25);
        container.add(input);
        JLabel output = new JLabel("Output");
        output.setBounds(12, 268, 70, 25);
        container.add(output);

        String[] OutputColumn = {"Process", "Turnaround time", "Waiting time","Response time"};
        DefaultTableModel OutputModel = new DefaultTableModel(OutputColumn, 0);
        Output_table = new JTable(OutputModel);
        Output_table.getTableHeader().setReorderingAllowed(false);
        Output_table.getTableHeader().setResizingAllowed(false);
        Output_table.setEnabled(false);
        JScrollPane outputTableScrollPane = new JScrollPane(Output_table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outputTableScrollPane.setBounds(12, 303, 760, 160);
        container.add(outputTableScrollPane);

        JLabel lblNewLabel = new JLabel("Execution time");
        lblNewLabel.setBounds(12, 473, 86, 15);
        container.add(lblNewLabel);

        Execution_time_textField = new JTextField();
        Execution_time_textField.setBounds(110, 470, 116, 21);
        container.add(Execution_time_textField);
        Execution_time_textField.setColumns(10);

        Response_time_textField = new JTextField();
        Response_time_textField.setBounds(110, 510, 116, 21);
        container.add(Response_time_textField);
        Response_time_textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Response time");
        lblNewLabel_1.setBounds(12, 513, 86, 15);
        container.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Turnaround time");
        lblNewLabel_2.setBounds(272, 473, 114, 15);
        container.add(lblNewLabel_2);

        Turnaround_time_textField = new JTextField();
        Turnaround_time_textField.setBounds(385, 470, 116, 21);
        container.add(Turnaround_time_textField);
        Turnaround_time_textField.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Throughput");
        lblNewLabel_3.setBounds(272, 510, 97, 15);
        container.add(lblNewLabel_3);

        Throughput_textField = new JTextField();
        Throughput_textField.setBounds(385, 510, 116, 21);
        container.add(Throughput_textField);
        Throughput_textField.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Waiting time");
        lblNewLabel_4.setBounds(530, 473, 104, 15);
        container.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("CPU utilization ratio");
        lblNewLabel_5.setBounds(530, 510, 114, 15);
        container.add(lblNewLabel_5);

        Waiting_time_textField = new JTextField();
        Waiting_time_textField.setBounds(656, 473, 116, 21);
        container.add(Waiting_time_textField);
        Waiting_time_textField.setColumns(10);

        CPU_utilization_ratio_textField = new JTextField();
        CPU_utilization_ratio_textField.setBounds(656, 510, 116, 21);
        container.add(CPU_utilization_ratio_textField);
        CPU_utilization_ratio_textField.setColumns(10);

        JLabel ContextSwitch_Label = new JLabel("Context Switch");
        ContextSwitch_Label.setBounds(12, 548, 86, 15);
        container.add(ContextSwitch_Label);

        Context_Switch_textField = new JTextField();
        Context_Switch_textField.setBounds(110, 548, 116, 21);
        container.add(Context_Switch_textField);
        Context_Switch_textField.setColumns(10);

        DrawGantt = new DrawGantt();
        DrawGantt.setBounds(5,600,780,100);
        container.add(DrawGantt);

        String Scheduling[] = {"FCFS","SJF","SRTF","NonPreemptivePriority","PreemptivePriority","RR","HRRN","Priority_Based_MultiLevelQueue"};
        comboBox = new JComboBox(Scheduling);
        comboBox.setBounds(510, 10, 262, 41);
        container.add(comboBox);

        JLabel label = new JLabel("Time_Quantum : ");
        label.setBounds(270, 710, 114, 15);
        container.add(label);

        Time_Quantum_textField = new JTextField();
        Time_Quantum_textField.setBounds(385, 710, 80, 21);
        container.add(Time_Quantum_textField);
        Time_Quantum_textField.setColumns(10);

        File_Path_JLabel = new JLabel("");
        File_Path_JLabel.setBounds(270, 730, 500, 15);
        container.add(File_Path_JLabel);



        JButton File_choose_Button = new JButton("File choose");
        File_choose_Button.setBounds(23, 710, 125, 23);
        File_choose_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("텍스트 문서(*.txt)", "txt");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File("./"));
                fileChooser.setDialogTitle("파일 열기");
                fileChooser.setAcceptAllFileFilterUsed(false);
                int result = fileChooser.showOpenDialog((Component) e.getSource());
                filePath = fileChooser.getSelectedFile().getPath();
                File_Path_JLabel.setText(filePath);
            }
        });
        container.add(File_choose_Button);

        JButton Run_Button = new JButton("Run");
        Run_Button.setBounds(160, 710, 97, 23);
        Run_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputModel.setNumRows(0);
                OutputModel.setNumRows(0);
                String combo_String = comboBox.getSelectedItem().toString();
                Scanner sc = null;
                int process_num=0;
                try {
                    sc = new Scanner(new File(filePath));
                    while (sc.hasNext()) {
                        String input_data = sc.nextLine();
                        String[] data = input_data.split(" ");
                        int [] Process_data = new int[4];
                        for(int i = 0;i<4;++i) {
                            Process_data[i] = Integer.parseInt(data[i+1]);
                        }
                        process_num++;
                        inputModel.addRow(new Integer[]{Process_data[0], Process_data[1], Process_data[2],Process_data[3]});
                    }
                    sc = new Scanner(new File(filePath));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                int CPU_using_time=0;
                int Execution_time=0;
                int Turnaround_time=0;
                int Waiting_time=0;
                int Response_time=0;
                int Context_Switch=0;
                HashMap<Integer, ArrayList<Result_Process>> result;
                ArrayList<Result_Process> Gantt_Chart = null;
                ArrayList<Result_Process> Output = null;
                switch (combo_String){
                    case "FCFS":
                        result = scheduler.FCFS(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "SJF":
                        result = scheduler.SJF(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "SRTF":
                        result = scheduler.SRTF(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "NonPreemptivePriority":
                        result = scheduler.NonPreemptivePriority(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "PreemptivePriority":
                        result = scheduler.PreemptivePriority(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "RR":
                        String Time_Quantum = Time_Quantum_textField.getText();
                        if(Time_Quantum.equals("")) return;
                        result = scheduler.RR(sc,Integer.parseInt(Time_Quantum));
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "HRRN":
                        result = scheduler.HRRN(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                    case "Priority_Based_MultiLevelQueue":
                        result = scheduler.Priority_Based_MultiLevelQueue(sc);
                        Gantt_Chart = result.get(1);
                        Output = result.get(0);
                        break;
                }
                for(Result_Process process : Output) {
                    OutputModel.addRow(new Integer[]{process.getPid(),process.getTurnaround_time(),process.getWaiting_time(),process.getResponse_time()});
                    Turnaround_time+=process.getTurnaround_time();
                    Waiting_time+=process.getWaiting_time();
                    Response_time+=process.getResponse_time();
                }
                boolean add_context_switch = false;
                for(Result_Process process : Gantt_Chart) {
                    Execution_time+=process.getRunning_time();
                    if(process.getisProcess()){
                        if(add_context_switch) Context_Switch++;
                        else add_context_switch = true;
                        CPU_using_time+=process.getRunning_time();
                    }
                    else{
                        add_context_switch = false;
                    }
                }

                container.remove(DrawGantt);
                DrawGantt = new DrawGantt();
                DrawGantt.setBounds(5,580,780,100);
                container.add(DrawGantt);
                DrawGantt.setGantt_Chart(Gantt_Chart);
                DrawGantt.setExecution_time(Execution_time);
                DrawGantt.repaint();
                repaint();

                Context_Switch_textField.setText(Integer.toString(Context_Switch));
                Execution_time_textField.setText(Integer.toString(Execution_time));
                Response_time_textField.setText(Double.toString(Math.round((double)Response_time/(double)process_num*100)/100.0));
                Turnaround_time_textField.setText(Double.toString(Math.round((double)Turnaround_time/(double)process_num*100)/100.0));
                Throughput_textField.setText(Double.toString(Math.round((double)process_num/(double)Execution_time*100)/100.0)); //얘는 다시 찾아보고 수정
                Waiting_time_textField.setText(Double.toString(Math.round((double)Waiting_time/(double)process_num*100)/100.0));
                CPU_utilization_ratio_textField.setText(Double.toString(Math.round((double)CPU_using_time/(double)Execution_time*100)));
            }
        });
        container.add(Run_Button);

    }
}
class DrawGantt extends JPanel{
    private ArrayList<Result_Process> Gantt_Chart=null;
    private int startdraw = 5;
    private int setwidth = 50;
    private int Total_Execution_time = 50;
    private int Execution_time=0;
    public void paint(Graphics g){
        super.paint(g);
        if(Gantt_Chart==null) return;
        setwidth = 400 / Total_Execution_time; //setwidth = 760 / Total_Execution_time;
        g.drawString(Integer.toString(Execution_time),0,90);
        for(Result_Process process : Gantt_Chart) {
            if(process.getisProcess()){
                g.drawRect(startdraw,0,process.getRunning_time()*setwidth,80);
                g.setColor(Color.RED);
                g.drawString("P"+process.getPid(),startdraw+process.getRunning_time()*setwidth/2-5,40);
                g.setColor(Color.BLACK);
                Execution_time+=process.getRunning_time();
                g.drawString(Integer.toString(Execution_time),startdraw+process.getRunning_time()*setwidth-5,90);
            }
            else{
                Execution_time+=process.getRunning_time();
                g.drawString(Integer.toString(Execution_time),startdraw+process.getRunning_time()*setwidth-5,90);
            }
            startdraw += process.getRunning_time()*setwidth;
        }
    }
    public void setExecution_time(int Execution_time){
        this.Total_Execution_time = Execution_time;
    }
    public void setGantt_Chart(ArrayList<Result_Process> Gantt_Chart){
        this.Gantt_Chart = Gantt_Chart;
    }
}