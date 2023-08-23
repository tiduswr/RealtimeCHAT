package com.tiduswr.rcmessage.config;

import java.util.List;

public class WebsocketSubscribableEndpoits {

    public static List<String> privateEndpoint(String userName){
        final String privateEndpoint = "/topic/private." + userName;
        final String publicEndpoint = "/topic/chatroom.public";
        final String errorsEndpoint = "/topic/errors." + userName;

        return List.of(privateEndpoint, publicEndpoint, errorsEndpoint);
    }

}
