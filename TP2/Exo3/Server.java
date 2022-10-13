import java.nio.channels.Channel;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Server {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            Task task = bagOfTask.nextTask();
            while(task!= null){
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize(task));

                task = bagOfTask.nextTask(); 
            }
        }catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
