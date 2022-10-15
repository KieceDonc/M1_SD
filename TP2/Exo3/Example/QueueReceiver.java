import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class QueueReceiver {

    private final static String QUEUE_NAME = "hello";

    // Consommateur en attente de messages
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.31.18.45");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    // Pour consommer les messages déjà publiés
    /*public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            int nbMessages = (int) channel.messageCount(QUEUE_NAME);
            System.out.println(nbMessages + " messages in queue " + QUEUE_NAME);

            for (int i = 0; i < nbMessages; i++) {
                GetResponse message = channel.basicGet(QUEUE_NAME, true);
                if (message != null) {
                    String content = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + content + "'");
                }
            }
        }
    }*/
}
