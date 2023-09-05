package com.tiduswr.rcmessage.feignclients;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcmessage.exceptions.UserNotFoundException;
import com.tiduswr.rcmessage.model.dto.InternalUserDTO;
import com.tiduswr.rcmessage.model.dto.PublicUserDTO;

@Component
@FeignClient(contextId = "user-service", name = "${feignclient.user.service.name}", url="${feignclient.user.service.url}", path = "/internal/user")
public interface UserService {

    @GetMapping("/find/by/username/{username}")
    public InternalUserDTO findUserByUsername(@PathVariable String username) throws UserNotFoundException;

    @PostMapping("/find/formalname/by/usernames")
    public List<PublicUserDTO> retrieveFormalName(@RequestBody Set<String> usernames);

}
