package com.tiduswr.rcimageworker.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tiduswr.rcimageworker.exceptions.UserNotFoundException;
import com.tiduswr.rcimageworker.models.dto.InternalUserDTO;

@Component
@FeignClient(contextId = "user-service", name = "${feignclient.user.service.name}", url="${feignclient.user.service.url}", path = "/internal/user")
public interface UserService {

    @GetMapping("/find/by/username/{username}")
    public InternalUserDTO findUserByUsername(@PathVariable String username) throws UserNotFoundException;

}
