package ru.gb.chat.client;

public class Wait_Count {
    private int time_Step;
    private int time_Diff;
    private int current_Diff;
    private  Callback onCall;
    private Thread counter;
    private Object objMon;
    private boolean active;
    public Wait_Count(int wait_time, int step_time, Callback OnCall){
        time_Step = step_time;
        time_Diff = wait_time;
        onCall = OnCall;
        current_Diff = 0;
        active = false;
        counter = new Thread();
    }
    public void startW(){
        current_Diff = 0;
        active = true;
        counter = new Thread(()->{
            try {
                while (active) {
                    synchronized (counter) {
                        current_Diff += time_Step;
                        counter.wait(time_Step);
                        if (current_Diff == time_Diff)
                            onCall.callback();
                    }
                }
           } catch(InterruptedException e){
                e.printStackTrace();
           }
        });
        counter.setDaemon(true);
        counter.start();
    }
    public void update(){
        current_Diff = 0;
        synchronized (counter){
            counter.notify();
        }
    }
    public void stopW(){
        active = false;
        update();
    }
}
