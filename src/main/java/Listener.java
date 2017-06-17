import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.UUID;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;

public class Listener extends DefaultConsumer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Listener(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
		JSONParser parser = new JSONParser(); //dublicate detection
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(message);
			JSONArray  measurements = (JSONArray) jsonObject.get("measurements");
			JSONObject location = (JSONObject) jsonObject.get("location");
			/*
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat minute = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			System.out.println(date.format(new Date((Long) jsonObject.get("timestamp"))));
			System.out.println(minute.format(new Date((Long) jsonObject.get("timestamp"))));
			
			/*
			System.out.println((Long) jsonObject.get("timestamp"));
			System.out.println((String) jsonObject.get("id"));
			System.out.println((String) jsonObject.get("name"));
			System.out.println((String) location.toString());
			System.out.println(location.get("latitude").toString());
			System.out.println(location.get("longitude").toString());
			/*
			System.out.println(((JSONObject) measurements.get(0)).get("value"));
			for (int i = 0; i < measurements.size();i++) {
				System.out.print(((JSONObject) measurements.get(i)).get("id"));
				System.out.print(": ");
				System.out.println(((JSONObject) measurements.get(i)).get("value"));
		    }*/
			//JSONObject mes1 = (JSONObject) parser.parse(measurements.toJSONString());
			//System.out.println(measurements.toArray());
			//System.out.println(Arrays.toString(measurements.toArray()));
			//System.out.println(mes1.get("id"));
			//((JSONObject) measurements.get(i)).get("value").toString()
			try {
				Database.sendTomySQL(new DEIF((Long) jsonObject.get("timestamp"), measurements, location, jsonObject.get("id").toString(), jsonObject.get("name").toString()));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        //log.info(envelope.getRoutingKey() + ": " + message);
    }
}
