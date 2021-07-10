package com.example.movieReco.message;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public class MessengerService {

    private final LineMessagingClient lineMessagingClient;
    @Value("${line.user.id}")
    private String userId;

    @Autowired
    public MessengerService(LineMessagingClient lineMessagingClient){
        this.lineMessagingClient = lineMessagingClient;
    }

    public void publishTextMessage(String message) {
        try {
            System.out.println("userId: "+ userId);
            TextMessage textMessage = new TextMessage(message);
            BotApiResponse botApiResponse = publishMessage(new PushMessage(userId, textMessage));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private BotApiResponse publishMessage(PushMessage pushMessage) throws Exception{
        return lineMessagingClient.pushMessage(pushMessage).get();
    }

}
