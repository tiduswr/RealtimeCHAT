package com.tiduswr.rcmessage.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tiduswr.rcmessage.exceptions.UserNotFoundException;
import com.tiduswr.rcmessage.model.User;

@Component
@FeignClient(contextId = "user-service", name = "${feignclient.user.service.name}", url="${feignclient.user.service.url}", path = "/internal/user")
public interface UserService {

    @GetMapping("/find/by/username/{username}")
    public User findUserByUsername(@PathVariable String username) throws UserNotFoundException;

}
