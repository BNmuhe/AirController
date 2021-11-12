import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
public class CommandRunner implements Runnable{
    private String command = "irsend SEND_ONCE mini_con ";
    private Logger logger = Logger.getLogger("CommandRunnerLogger");

    public CommandRunner(String command){
        this.command=this.command+command;
    }

    public void run(){
        try {
            logger.info(command);
            Process process =  Runtime.getRuntime().exec(command);
            int status = process.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if(status!=0){
                logger.warning("Failed to call shell's command and the return status's is: " + status);
            }else {
                logger.info("success running:"+command);
                String line=input.readLine();
                while(line!=null){
                    System.out.println(line);
                    line=input.readLine();
                }
            }
            logger.info("thread create ok");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
