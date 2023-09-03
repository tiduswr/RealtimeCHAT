package tiduswr.RealTimeChat.model.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PasswordRecoverRequest{
    private String email;
    private String redirectPrefix;
    private String browserName;
    private String operatingSystem;
}
