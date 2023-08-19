package com.tiduswr.rcuser.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data @Builder @ToString
public class InternalUserDTO {

        private Long id;
        private String userName;
        private String password;
        private String formalName;

        public static InternalUserDTO from(UserDTO u) {
                return InternalUserDTO.builder()
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .id(u.getId())
                        .build();
        }
}
