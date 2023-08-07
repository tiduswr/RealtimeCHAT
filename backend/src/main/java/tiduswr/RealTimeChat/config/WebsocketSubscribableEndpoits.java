package tiduswr.RealTimeChat.config;

import java.util.List;

public class WebsocketSubscribableEndpoits {

    public static List<String> privateEndpoint(String userName){
        final String privateEndpoint = "/user/" + userName + "/private";
        final String publicEndpoint = "/chatroom/public";
        final String errorsEndpoint = "/user/" + userName + "/errors";

        return List.of(privateEndpoint, publicEndpoint, errorsEndpoint);
    }

}
