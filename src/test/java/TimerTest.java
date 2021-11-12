import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @org.junit.jupiter.api.Test
    void run() throws InterruptedException {
        Timer timer = new Timer();
        timer.setOffHour(13);
        timer.setOffMinute(41);


    }
}