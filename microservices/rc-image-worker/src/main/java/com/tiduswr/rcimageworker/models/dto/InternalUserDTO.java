package com.tiduswr.rcimageworker.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data @Builder @ToString
public class InternalUserDTO {

        private Long id;
        private String userName;
        private String password;
        private String formalName;
        private String email;
        private String redirectUrl;
        
}
