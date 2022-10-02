import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Server {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        try {
            BagOfTaskImp bagOfTask = new BagOfTaskImp();

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
    
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
    
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message message = SerializationUtils.deserialize(delivery.getBody());
                switch(message.getCode()) {
                    case Message.CODE_ASK_TASK:
                      Task nextTask = bagOfTask.nextTask();
                      Message sendTask = new Message(Message.CODE_RECEIVE_TASK,nextTask);
                      break;
                    case y:
                      // code block
                      break;
                    default:
                      // code block
                  }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
