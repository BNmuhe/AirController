import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

public class Timer {
    private boolean exit=false;
    private Integer onHour=null;
    private Integer onMinute=null;
    private Integer offHour=null;
    private Integer offMinute=null;
    private LocalTime currTime=null;
    private Integer currHour=null;
    private Integer currMinute=null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Logger logger = Logger.getLogger("TimerLogger");

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    private void setCurrTime(){
        currTime =LocalTime.now();
        System.out.println(currTime.format(formatter));
        String[] times=currTime.format(formatter).split(":");
        currHour=Integer.parseInt(times[0]);
        currMinute=Integer.parseInt(times[1]);
    }

    public void setOnHour(Integer onHour) {
        this.onHour = onHour;
    }
    public void setOnMinute(Integer onMinute) {
        this.onMinute = onMinute;
    }
    public void setOffHour(Integer offHour) {
        this.offHour = offHour;
    }
    public void setOffMinute(Integer offMinute) {
        this.offMinute = offMinute;
    }

    private void timingOn(String command){
        logger.info("start timing");
        new Thread(() -> {
            while(exit!=true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setCurrTime();
                if((currHour==onHour&&currMinute>=onMinute)||(currHour>onHour)){
                    logger.info("command start running");
                    new CommandRunner(command).run();
                    break;
                }
            }
        }).start();
    }
    private void timingOff(){
        logger.info("start timing");
        new Thread(() -> {
            while(exit!=true){
                try {

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setCurrTime();
                if((currHour==offHour&&currMinute>=offMinute)||(currHour>offHour)){
                    logger.info("command start running");
                    new CommandRunner("off").run();
                    break;
                }
            }
        }).start();
    }



    public void run(String command) {
        timingOff();
        timingOn(command);

    }
}
