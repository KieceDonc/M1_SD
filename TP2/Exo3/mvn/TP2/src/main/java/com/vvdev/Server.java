package com.vvdev;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.MessageProperties;

public class Server {

    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String RESULT_QUEUE_NAME = "result_queue";

    // mvn exec:java -Dexec.mainClass=com.vvdev.Server
    public static void main(String[] args) {
        try {
            BagOfTaskImp bagOfTask = new BagOfTaskImp();
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channelTopic = connection.createChannel();
            Channel channelQueue = connection.createChannel();

            // ------------------------------------------------------------------------------------

            channelTopic.exchangeDeclare(RESULT_QUEUE_NAME, "topic");
            String queueName = channelTopic.queueDeclare().getQueue();
            channelTopic.queueBind(queueName, RESULT_QUEUE_NAME, "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Task task = Task.deserialize(delivery.getBody());
                System.out.println(" [" + task.getNumber() + "] isPrimeNumber ? : " + task.isPrimeNumber());
            };
            channelTopic.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

            // ------------------------------------------------------------------------------------

            channelQueue.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            Task task = bagOfTask.nextTask();
            while (task != null) {
                channelQueue.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                        Task.serializable(task));

                task = bagOfTask.nextTask();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
