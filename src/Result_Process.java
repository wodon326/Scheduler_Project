public class Result_Process {
    private int pid;
    private int turnaround_time;
    private int waiting_time;
    private int response_time;
    private int running_time;
    private int priority;
    private boolean isProcess;
    public Result_Process(int pid,int running_time,boolean isProcess){
        this.pid = pid;
        this.running_time = running_time;
        this.isProcess = isProcess;
    }
    public Result_Process(int pid,int turnaround_time,int waiting_time,int response_time,int priority){
        this.pid = pid;
        this.turnaround_time = turnaround_time;
        this.waiting_time = waiting_time;
        this.response_time = response_time;
        this.priority = priority;
    }
    public int getPid(){
        return pid;
    }
    public int getTurnaround_time(){
        return turnaround_time;
    }
    public int getWaiting_time(){
        return waiting_time;
    }
    public int getResponse_time(){
        return response_time;
    }
    public int getRunning_time(){
        return running_time;
    }
    public void plusRunning_time(int plus){
        running_time += plus;
    }
    public boolean getisProcess(){
        return isProcess;
    }
    public int getPriority(){
        return priority;
    }
}
