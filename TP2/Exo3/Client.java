import java.rmi.RemoteException;

public class Client extends Thread {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String arg[]) {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("127.0.0.1");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel() 
      channel.exchangeDeclare(EXCHANGE_NAME, "topic");

      for (int index = 0; index < 200; index++) {
        Message askTask = new Message(Message.CODE_ASK_TASK,null);
        channel.basicPublish(EXCHANGE_NAME, "", null, SerializationUtils.serialize(askTask));
        System.out.println(" [x] Sent '" + message + "'");
        Task task =;

        if (task != null) {
          task.execute();

          try {
            stub.sendResult(task);
          } catch (RemoteException e) {
            e.printStackTrace();
          }

        } else {
          sleep(1000);
        }
      }
    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}