import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.logging.Logger;


public class MqttService {
    public static final String HOST = "tcp://pw38151089.zicp.vip:12546";
    public static final String AIR_CONDITIONER_REQUEST_TOPIC = "requestAirConditionerControl";
    public static final String AIR_CONDITIONER_RESPONSE_TOPIC = "AirConditionerResponse";
    private static final String clientId = "raspberryAirController";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "zyc";    //非必须
    private String passWord = "123456";  //非必须
    public static boolean status=true;

    private CommandResolver commandResolver = new CommandResolver();
    private static Timer timer=new Timer();

    private Logger logger = Logger.getLogger("MqttClientLogger");

    public String getCurrentCommand() {
        return currentCommand;
    }

    public MqttClient getClient() {
        return client;
    }

    public void setCurrentCommand(String currentCommand) {
        this.currentCommand = currentCommand;
    }

    private String currentCommand;

    public static Timer getTimer() {
        return timer;
    }

    public void initMqttClient(){
        try{
            client=new MqttClient(HOST,clientId,new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            options.setAutomaticReconnect(true);
            MqttMessage message = new MqttMessage("PayLoad".getBytes());
            message.setQos(1);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    throwable.printStackTrace();
                    //设置重连
                    logger.info("connection lost");
                    connectToMqttServer();
                }
                //s  =  topic
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(new String(mqttMessage.getPayload()));
                    setCurrentCommand(new String(mqttMessage.getPayload()));
                    commandResolver.resolve(getCurrentCommand());
                    logger.info("thread has started");

                    response(getCurrentCommand());
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    logger.info("delivery Complete---------" + iMqttDeliveryToken.isComplete());
                }
            });
            logger.info("client initialize complete, start to connect to server");
            connectToMqttServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void response(String condition) throws MqttException {

        String head="AirConditioner_";
        String tail1="_success";
        String tail2="_failed";

        String res=head+ condition.split("_")[0]+(this.status?tail1:tail2);


        client.publish(AIR_CONDITIONER_RESPONSE_TOPIC,res.getBytes(),1,false);
        logger.info(res);
    }


    private void connectToMqttServer(){
        try{
            client.connect(options);

            logger.info("connect success,start subscribe");
            client.subscribe(AIR_CONDITIONER_REQUEST_TOPIC,1);
            logger.info("subscribe success,start listen");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
