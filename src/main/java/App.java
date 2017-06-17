import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static Gson gson;

    public static void main(String[] args) throws Exception {
        gson = new Gson();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("incap");
        factory.setPassword("ORDZnBMCLH4BRYAAbdi1i3jTVonWozDE");
        factory.setHost("broker.elektro.dtu.dk");
        factory.setPort(5000);
        factory.setVirtualHost("evbe");
        factory.setAutomaticRecoveryEnabled(true);
        factory.useSslProtocol();

        Connection connection = factory.newConnection();
        Channel dataChannel = connection.createChannel();

        dataChannel.exchangeDeclare("evbe.json", "topic", true);

        String dataQueue = dataChannel.queueDeclare().getQueue();

        dataChannel.queueBind(dataQueue, "evbe.json", "#");

        Consumer dataConsumer = new Listener(dataChannel);
        dataChannel.basicConsume(dataQueue, true, dataConsumer); // comment this line to receive less noise in output while testing commands
        

        while (true);
//        dataChannel.close();
//        statusChannel.close();
//        controlChannel.close();
//        connection.close();
    }

}
