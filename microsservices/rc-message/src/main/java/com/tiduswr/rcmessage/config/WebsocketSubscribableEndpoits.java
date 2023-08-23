package com.tiduswr.rcmessage.config;

import java.util.List;

public class WebsocketSubscribableEndpoits {

    public static List<String> privateEndpoint(String userName){
        final String privateEndpoint = "/user/" + userName + "/queue.private";
        final String publicEndpoint = "/topic/chatroom.public";
        final String errorsEndpoint = "/user/" + userName + "/queue.errors";

        return List.of(privateEndpoint, publicEndpoint, errorsEndpoint);
    }

}
