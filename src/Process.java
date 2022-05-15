public class Process {
    private int pid;
    private int burst_time;
    private int arrive_time;
    private int priority;
    private int response_time;
    private int remain_time;
    private int input_index;
    public Process(int pid,int arrive_time,int burst_time,int priority,int input_index){
        this.pid = pid;
        this.arrive_time = arrive_time;
        this.burst_time = burst_time;
        this.priority = priority;
        this.input_index = input_index;
        response_time = -1;
        remain_time = burst_time;
    }
    public int getPid(){
        return pid;
    }
    public int getBurst_time(){
        return burst_time;
    }
    public int getArrive_time(){
        return arrive_time;
    }
    public int getPriority(){
        return priority;
    }
    public int getResponse_time(){
        return response_time;
    }
    public void setResponse_time(int response_time){
        this.response_time = response_time;
    }
    public int getRemain_time(){
        return remain_time;
    }
    public int getInput_index(){
        return input_index;
    }
    public void minusRemain_time(int minus){
        remain_time-= minus;
    }
}
