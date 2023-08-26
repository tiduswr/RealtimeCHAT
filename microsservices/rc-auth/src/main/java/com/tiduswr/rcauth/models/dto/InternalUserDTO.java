package com.tiduswr.rcauth.models.dto;

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

        public static InternalUserDTO from(UserDTO u) {
                return InternalUserDTO.builder()
                        .redirectUrl(u.getRedirectUrl())
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .email(u.getEmail())
                        .id(u.getId())
                        .build();
        }
}
