
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CommandResolver {

    private Logger logger = Logger.getLogger("CommandResolverLogger");

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public void resolve(String command){
        if(command.indexOf("time")!=-1){
            logger.info("resolve to timer");
            resolveToTimer(command);
        }else{
            logger.info("resolve to command runner");
            Thread commandRunner =new Thread(new CommandRunner(resolveExecutableCommand(command)));
            commandRunner.start();
        }
    }

    private void resolveToTimer(String command){
        Timer timer = MqttService.getTimer();
        String[] commands = command.split("_");
        if(commands[1].equals("off")){
            timer.setExit(true);
        }else{
            timer.setExit(false);
            for (int i = 2; i <= 5; i++) {
                if(!isInteger(commands[i])){
                    return;
                }
            }

            timer.setOnHour(Integer.parseInt(commands[2]));
            timer.setOnMinute(Integer.parseInt(commands[3]));
            timer.setOffHour(Integer.parseInt(commands[4]));
            timer.setOffMinute(Integer.parseInt(commands[5]));
            timer.run(commands[6]+commands[7]+commands[8]);
        }
    }

    private String resolveExecutableCommand (String command){

        String executableCommand=null;
        String[] commands=command.split("_");
        if("off".equals(commands[0])){
            executableCommand=commands[0];
        }else {
            try{
                executableCommand=commands[1]+commands[2]+commands[3];
            }catch (Exception e){
                e.printStackTrace();

            }

        }
        return executableCommand;
    }
}
