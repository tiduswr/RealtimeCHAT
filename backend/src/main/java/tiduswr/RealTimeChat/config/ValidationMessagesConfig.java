package tiduswr.RealTimeChat.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@SuppressWarnings("unused")
public class ValidationMessagesConfig {

    @Bean
    public MessageSource messageSource(){
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }

}
