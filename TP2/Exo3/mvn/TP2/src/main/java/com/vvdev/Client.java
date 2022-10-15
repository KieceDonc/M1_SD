package com.vvdev;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Client extends Thread {

  private static final String TASK_QUEUE_NAME = "task_queue";
  private static final String RESULT_QUEUE_NAME = "result_queue";

  // mvn exec:java -Dexec.mainClass=com.vvdev.Client
  public static void main(String arg[]) {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      Connection connection = factory.newConnection();
      Channel channelTopic = connection.createChannel();
      Channel channelQueue = connection.createChannel();

      channelTopic.exchangeDeclare(RESULT_QUEUE_NAME, "topic");

      channelQueue.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
      channelQueue.basicQos(1);

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        Task task = Task.deserialize(delivery.getBody());

        task.execute();

        channelQueue.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        channelTopic.basicPublish(RESULT_QUEUE_NAME, "", null, Task.serializable(task));

        System.out.println(" [" + task.getNumber() + "] task finished ");
      };
      channelQueue.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
      });
    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}