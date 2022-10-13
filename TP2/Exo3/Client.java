import java.nio.channels.Channel;
import java.sql.Connection;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Client extends Thread {

  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String arg[]) {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("127.0.0.1");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
  
      channel.basicQos(1);
  
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          Task task = SerializationUtils.deserialize(delivery.getBody());
          
          task.execute();

          System.out.println(" [x] Done");
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

      };
      channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}