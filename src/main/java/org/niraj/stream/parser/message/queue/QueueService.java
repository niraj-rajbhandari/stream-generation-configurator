package org.niraj.stream.parser.message.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.niraj.stream.parser.configuration.ConfigReader;
import org.niraj.stream.parser.pojo.GraphProperty;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class QueueService {
    private static final String EXCHANGE_TYPE = "direct";
    private static final String GRAPH_EXCHANGE_PROPERTY = "graph-exchange";
    private static final String QUEUE_CONNECTION_HOST_PROPERTY = "connection-host";
    private static final String QUEUE_USERNAME_PROPERTY = "rabbitmq-username";
    private static final String QUEUE_PASSWORD_PROPERTY = "rabbitmq-password";
    private static final String QUEUE_VIRTUAL_HOST_PROPERTY = "rabbitmq-vhost";
    private static final String PROCESSED_ITEM_SIZE_PROPERTY = "processed-item-size";
    private static final String SAMPLE_SIZE_PROPERTY = "sample-size";
    private static final String MESSAGE_QUEUE_PROPERTY = "message-queue";

    private ConfigReader configReader;
    private Connection connection;
    private Channel channel;

    private static QueueService instance;

    public static QueueService getInstance() throws IOException, TimeoutException {
        if (instance == null) {
            instance = new QueueService();
        }
        return instance;
    }

    private QueueService() throws IOException, TimeoutException {
        configReader = ConfigReader.getInstance();
        _setConnection();
        _setChannel();
    }

    public void publishGraphToQueue(GraphProperty prop) {
        ObjectMapper mapper = new ObjectMapper();
        String message;
        try {
            message = mapper.writeValueAsString(prop);
        } catch (JsonProcessingException e) {
            message = prop.toString();
        }
        publishToQueue(message);
    }

    public void publishToQueue(String message) {
        String exchange = configReader.getProperty(GRAPH_EXCHANGE_PROPERTY);
        try {
            channel.basicPublish(exchange, configReader.getProperty(MESSAGE_QUEUE_PROPERTY), null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeQueue() throws IOException, TimeoutException {
        instance.channel.close();
        instance.connection.close();
        instance = null;
    }

    private void _setConnection() throws TimeoutException, IOException {
        if (connection == null) {
            System.out.println("Rabbitmq connection is set");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(configReader.getProperty(QUEUE_USERNAME_PROPERTY));
            factory.setPassword(configReader.getProperty(QUEUE_PASSWORD_PROPERTY));
            factory.setHost(configReader.getProperty(QUEUE_CONNECTION_HOST_PROPERTY));
            factory.setVirtualHost(configReader.getProperty(QUEUE_VIRTUAL_HOST_PROPERTY));
            int oneHourHeartBeat = 600 * 61;
            factory.setRequestedHeartbeat(oneHourHeartBeat);
            connection = factory.newConnection();
        }
    }

    private void _setChannel() throws IOException {
        Float processedItemConfig = Float.parseFloat(configReader.getProperty(PROCESSED_ITEM_SIZE_PROPERTY));
        int sampleSize = Integer.parseInt(configReader.getProperty(SAMPLE_SIZE_PROPERTY));
        Double processWindowSize = Math.ceil(sampleSize * processedItemConfig);
        int channelLimit = processWindowSize.intValue();
        String exchange = configReader.getProperty(GRAPH_EXCHANGE_PROPERTY);
        String queue = configReader.getProperty(MESSAGE_QUEUE_PROPERTY);
        channel = connection.createChannel();
        channel.exchangeDeclare(exchange, EXCHANGE_TYPE);
        channel.basicQos(channelLimit, true);
        channel.queueDeclare(queue, false, false, false, null);

        channel.queueBind(queue, exchange, queue);

    }
}
