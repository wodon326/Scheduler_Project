import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Scheduler {
    private int Now_time;
    public HashMap<Integer, ArrayList<Result_Process>> FCFS(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet);
        Now_time = 0;
        while(!processTreeSet.isEmpty()){
            Process select = processTreeSet.pollFirst();
            if(Now_time<select.getArrive_time()){
                Gantt_Chart.add(new Result_Process(-1,select.getArrive_time()-Now_time,false));
                Now_time = select.getArrive_time();
            }
            Now_time += select.getBurst_time();
            Gantt_Chart.add(new Result_Process(select.getPid(),select.getBurst_time(),true));
            Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getPriority()));
        }

        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }

    public HashMap<Integer, ArrayList<Result_Process>> SJF(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        CompByRemainTime CompByRemainTime = new CompByRemainTime();
        Comparator<Process> compRTthenAT = CompByRemainTime.thenComparing(new CompByArriveTimeThenInputIndex());
        TreeSet<Process> processTreeSet_RemainTime = new TreeSet<>(compRTthenAT);
        while(!processTreeSet_ArriveTime.isEmpty()||!processTreeSet_RemainTime.isEmpty()){
            while(!processTreeSet_ArriveTime.isEmpty()){
                Process select = processTreeSet_ArriveTime.pollFirst();
                if(Now_time>=select.getArrive_time()){
                    processTreeSet_RemainTime.add(select);
                }
                else{
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(!processTreeSet_RemainTime.isEmpty()){
                Process select = processTreeSet_RemainTime.pollFirst();
                Now_time += select.getBurst_time();
                Gantt_Chart.add(new Result_Process(select.getPid(),select.getBurst_time(),true));
                Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getPriority()));
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        Gantt_Chart.add(new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        processTreeSet_RemainTime.add(select);
                    }
                }
            }
        }
        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public HashMap<Integer, ArrayList<Result_Process>> SRTF(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());

        CompByRemainTime CompByRemainTime = new CompByRemainTime();
        Comparator<Process> compRTthenAT = CompByRemainTime.thenComparing(new CompByArriveTimeThenInputIndex());
        TreeSet<Process> processTreeSet_RemainTime = new TreeSet<>(compRTthenAT);
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        while(!processTreeSet_ArriveTime.isEmpty()||!processTreeSet_RemainTime.isEmpty()){
            while(!processTreeSet_ArriveTime.isEmpty()){
                Process select = processTreeSet_ArriveTime.pollFirst();
                if(Now_time>=select.getArrive_time()){
                    processTreeSet_RemainTime.add(select);
                }
                else{
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }

            if(!processTreeSet_RemainTime.isEmpty()){
                Process select = processTreeSet_RemainTime.pollFirst();
                select.minusRemain_time(1);
                if(select.getResponse_time()==-1)
                    select.setResponse_time(Now_time-select.getArrive_time());
                add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(), 1, true));
                Now_time++;
                if(select.getRemain_time()==0){
                    Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getResponse_time(),select.getPriority()));
                }
                else{
                    processTreeSet_RemainTime.add(select);
                }
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        add_Gantt_Chart(Gantt_Chart,new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        processTreeSet_RemainTime.add(select);
                    }
                }
            }
        }

        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public HashMap<Integer, ArrayList<Result_Process>> RR(Scanner sc,int Time_Quantum){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        int Now_Time_Quantum = 0;
        Deque<Process> queue = new LinkedList<>();
        boolean is_push = false;
        Process push_process = null;
        while(!processTreeSet_ArriveTime.isEmpty()||!queue.isEmpty()||is_push) {
            while (!processTreeSet_ArriveTime.isEmpty()) {
                Process select = processTreeSet_ArriveTime.pollFirst();
                if (Now_time >= select.getArrive_time()) {
                    queue.add(select);
                } else {
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(is_push){
                queue.addLast(push_process);
                is_push = false;
            }
            if(!queue.isEmpty()){
                Process select = queue.poll();
                select.minusRemain_time(1);
                if(select.getResponse_time()==-1)
                    select.setResponse_time(Now_time-select.getArrive_time());
                add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(), 1, true));
                Now_time++;
                Now_Time_Quantum++;
                if(select.getRemain_time()==0){
                    Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getResponse_time(),select.getPriority()));
                    Now_Time_Quantum = 0;
                }
                else{
                    if(Now_Time_Quantum==Time_Quantum){
                        push_process = select;
                        is_push = true;
                        Now_Time_Quantum = 0;
                    }
                    else{
                        queue.addFirst(select);
                    }
                }
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        add_Gantt_Chart(Gantt_Chart,new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        queue.add(select);
                    }
                }
            }
        }


        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public HashMap<Integer, ArrayList<Result_Process>> NonPreemptivePriority(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        CompByPriority CompByPriority = new CompByPriority();
        Comparator<Process> compPthenAT = CompByPriority.thenComparing(new CompByArriveTimeThenInputIndex());
        TreeSet<Process> processTreeSet_Priority = new TreeSet<>(compPthenAT);
        while(!processTreeSet_ArriveTime.isEmpty()||!processTreeSet_Priority.isEmpty()){
            while(!processTreeSet_ArriveTime.isEmpty()){
                Process select = processTreeSet_ArriveTime.pollFirst();
                if(Now_time>=select.getArrive_time()){
                    processTreeSet_Priority.add(select);
                }
                else{
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(!processTreeSet_Priority.isEmpty()){
                Process select = processTreeSet_Priority.pollFirst();
                Now_time += select.getBurst_time();
                Gantt_Chart.add(new Result_Process(select.getPid(),select.getBurst_time(),true));
                Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getPriority()));
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        Gantt_Chart.add(new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        processTreeSet_Priority.add(select);
                    }
                }
            }
        }
        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public HashMap<Integer, ArrayList<Result_Process>> PreemptivePriority(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        CompByPriority CompByPriority = new CompByPriority();
        Comparator<Process> compPthenAT = CompByPriority.thenComparing(new CompByArriveTimeThenInputIndex());
        TreeSet<Process> processTreeSet_Priority = new TreeSet<>(compPthenAT);
        while(!processTreeSet_ArriveTime.isEmpty()||!processTreeSet_Priority.isEmpty()){
            while(!processTreeSet_ArriveTime.isEmpty()){
                Process select = processTreeSet_ArriveTime.pollFirst();
                if(Now_time>=select.getArrive_time()){
                    processTreeSet_Priority.add(select);
                }
                else{
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(!processTreeSet_Priority.isEmpty()){
                Process select = processTreeSet_Priority.pollFirst();
                select.minusRemain_time(1);
                if(select.getResponse_time()==-1)
                    select.setResponse_time(Now_time-select.getArrive_time());
                add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(), 1, true));
                Now_time++;
                if(select.getRemain_time()==0){
                    Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getResponse_time(),select.getPriority()));
                }
                else{
                    processTreeSet_Priority.add(select);
                }
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        add_Gantt_Chart(Gantt_Chart,new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        processTreeSet_Priority.add(select);
                    }
                }
            }
        }


        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public HashMap<Integer, ArrayList<Result_Process>> HRRN(Scanner sc){
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByArriveTimeThenInputIndex());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        CompByHRRN CompByHRRN = new CompByHRRN();
        Comparator<Process> compHRRNthenP = CompByHRRN.thenComparing(new CompByPriority());
        Comparator<Process> compHRRNthenPthenAT = compHRRNthenP.thenComparing(new CompByArriveTimeThenInputIndex());
        Queue<Process> resortQueue = new LinkedList<>();
        TreeSet<Process> processTreeSet_HRRN = new TreeSet<>(compHRRNthenPthenAT);
        while(!processTreeSet_ArriveTime.isEmpty()||!processTreeSet_HRRN.isEmpty()){
            while(!processTreeSet_ArriveTime.isEmpty()){
                Process select = processTreeSet_ArriveTime.pollFirst();
                if(Now_time>=select.getArrive_time()){
                    processTreeSet_HRRN.add(select);
                }
                else{
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(!processTreeSet_HRRN.isEmpty()){
                Process select = processTreeSet_HRRN.pollFirst();
                Now_time += select.getBurst_time();
                add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(),select.getBurst_time(),true));
                Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getPriority()));
                while(!processTreeSet_HRRN.isEmpty()) {
                    resortQueue.add(processTreeSet_HRRN.pollFirst());
                }
                while(!resortQueue.isEmpty()) {
                    processTreeSet_HRRN.add(resortQueue.poll());
                }
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        add_Gantt_Chart(Gantt_Chart,new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        processTreeSet_HRRN.add(select);
                    }
                }
            }
        }
        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }

    public HashMap<Integer, ArrayList<Result_Process>> Priority_Based_MultiLevelQueue(Scanner sc){
        int Time_Quantum = 15;
        ArrayList<Result_Process> Gantt_Chart = new ArrayList<>();
        ArrayList<Result_Process> Output = new ArrayList<>();
        TreeSet<Process> processTreeSet_ArriveTime = new TreeSet<>(new CompByPriority_Based_RR());
        add_input_data(sc,processTreeSet_ArriveTime);
        Now_time = 0;
        int Now_Time_Quantum = 0;
        Deque<Process> queue = new LinkedList<>();
        CompByPriority CompByPriority = new CompByPriority();
        Comparator<Process> CompByPrioritythenAT = CompByPriority.thenComparing(new CompByArriveTimeThenInputIndex());
        TreeSet<Process> processTreeSet_Priority = new TreeSet<>(CompByPrioritythenAT);

        boolean is_push = false;
        boolean is_almost_done = false;
        Process push_process = null;
        while(!processTreeSet_ArriveTime.isEmpty()||!queue.isEmpty()||is_push) {
            while (!processTreeSet_ArriveTime.isEmpty()) {
                Process select = processTreeSet_ArriveTime.pollFirst();
                if (Now_time >= select.getArrive_time()) {
                    if(0<=select.getPriority()&&select.getPriority()<=2){
                        processTreeSet_Priority.add(select);
                    }
                    else{
                        queue.add(select);
                    }
                } else {
                    processTreeSet_ArriveTime.add(select);
                    break;
                }
            }
            if(is_push){
                if(is_almost_done){
                    queue.addFirst(push_process);
                    is_almost_done = false;
                }
                else{
                    queue.addLast(push_process);
                }
                is_push = false;
            }
            if(!queue.isEmpty()||!processTreeSet_Priority.isEmpty()){
                if(!processTreeSet_Priority.isEmpty()){
                    Process select = processTreeSet_Priority.pollFirst();
                    select.minusRemain_time(1);
                    if(select.getResponse_time()==-1)
                        select.setResponse_time(Now_time-select.getArrive_time());
                    add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(), 1, true));
                    Now_time++;
                    if(select.getRemain_time()==0){
                        Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getResponse_time(),select.getPriority()));
                    }
                    else{
                        processTreeSet_Priority.add(select);
                    }
                    continue;
                }
                Process select = queue.poll();
                select.minusRemain_time(1);
                if(select.getResponse_time()==-1)
                    select.setResponse_time(Now_time-select.getArrive_time());
                add_Gantt_Chart(Gantt_Chart,new Result_Process(select.getPid(), 1, true));
                Now_time++;
                Now_Time_Quantum++;
                if(select.getRemain_time()==0){
                    Output.add(new Result_Process(select.getPid(),Now_time-select.getArrive_time(),Now_time-select.getArrive_time()-select.getBurst_time(),select.getResponse_time(),select.getPriority()));
                    Now_Time_Quantum = 0;
                }
                else{
                    if(Now_Time_Quantum==Time_Quantum-select.getPriority()/3){
                        push_process = select;
                        is_push = true;
                        Now_Time_Quantum = 0;
                        if(Time_Quantum-select.getPriority()/3>=select.getRemain_time())
                            is_almost_done = true;
                    }
                    else{
                        queue.addFirst(select);
                    }
                }
            }
            else{
                if(!processTreeSet_ArriveTime.isEmpty()) {
                    Process select = processTreeSet_ArriveTime.pollFirst();
                    if (Now_time <= select.getArrive_time()) {
                        add_Gantt_Chart(Gantt_Chart,new Result_Process(-1, select.getArrive_time() - Now_time, false));
                        Now_time = select.getArrive_time();
                        queue.add(select);
                    }
                }
            }
        }


        HashMap<Integer,ArrayList<Result_Process>> result = new HashMap<>();
        result.put(0,Output);
        result.put(1,Gantt_Chart);
        return result;
    }
    public static void add_Gantt_Chart(ArrayList<Result_Process> Gantt_Chart,Result_Process process){
        if(Gantt_Chart.size()!=0&&Gantt_Chart.get(Gantt_Chart.size()-1).getPid() == process.getPid()){
            Gantt_Chart.get(Gantt_Chart.size()-1).plusRunning_time(process.getRunning_time());
        }
        else{
            Gantt_Chart.add(process);
        }
    }
    public static void add_input_data(Scanner sc,TreeSet<Process> tree){
        int input_index=0;
        while (sc.hasNext()) {
            String input_data = sc.nextLine();
            String[] data = input_data.split(" ");
            int [] Process_data = new int[4];
            for(int i = 0;i<4;++i) {
                Process_data[i] = Integer.parseInt(data[i+1]);
            }
            Process process = new Process(Process_data[0],Process_data[1],Process_data[2],Process_data[3],input_index++);
            tree.add(process);
        }
    }
    public static void main(String[] args) {

        Scheduler_GUI frame = new Scheduler_GUI(new Scheduler());
        frame.setVisible(true);

    }
    class CompByArriveTimeThenInputIndex implements Comparator<Process> {
        public int compare(Process P1, Process P2) {
            int A1 = P1.getArrive_time();
            int A2 = P2.getArrive_time();
            if(A1 < A2) return -1;
            else if(A1 > A2) return 1;
            else {
                Integer I1 = P1.getInput_index();
                Integer I2 = P2.getInput_index();
                return I1.compareTo(I2);
            }
        }
    }
    class CompByRemainTime implements Comparator<Process> {
        public int compare(Process P1, Process P2) {
            Integer I1 = P1.getRemain_time();
            Integer I2 = P2.getRemain_time();
            return I1.compareTo(I2);
        }
    }
    class CompByPriority implements Comparator<Process> {
        public int compare(Process P1, Process P2) {
            int priority1 = P1.getPriority();
            int priority2 = P2.getPriority();
            if(priority1 < priority2) return -1;
            else if(priority1 > priority2) return 1;
            else {
                Integer I1 = P1.getRemain_time();
                Integer I2 = P2.getRemain_time();
                return I1.compareTo(I2);
            }
        }
    }

    class CompByHRRN implements Comparator<Process>{
        public int compare(Process P1, Process P2) {
            Double W1 = (double)(Now_time - P1.getArrive_time()+P1.getBurst_time())/(double) P1.getBurst_time();
            Double W2 = (double)(Now_time - P2.getArrive_time()+P2.getBurst_time())/(double) P2.getBurst_time();
            return W2.compareTo(W1);
        }
    }

    class CompByPriority_Based_RR implements Comparator<Process>{
        public int compare(Process P1, Process P2) {
            int A1 = P1.getArrive_time();
            int A2 = P2.getArrive_time();
            if(A1 < A2) return -1;
            else if(A1 > A2) return 1;
            else {
                int priority1 = P1.getPriority();
                int priority2 = P2.getPriority();
                if(priority1 < priority2) return -1;
                else if(priority1 > priority2) return 1;
                else {
                    int R1 = P1.getRemain_time();
                    int R2 = P2.getRemain_time();
                    if(R1 < R2) return -1;
                    else if(R1 > R2) return 1;
                    else {
                        Integer I1 = P1.getInput_index();
                        Integer I2 = P2.getInput_index();
                        return I1.compareTo(I2);
                    }
                }
            }
        }
    }
}

