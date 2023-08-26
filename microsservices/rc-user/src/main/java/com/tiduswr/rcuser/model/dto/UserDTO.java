package com.tiduswr.rcuser.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tiduswr.rcuser.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data @Builder
public class UserDTO {

        @JsonProperty(access = Access.READ_ONLY)
        private Long id;

        private String userName;

        @JsonProperty(access = Access.WRITE_ONLY)
        private String password;

        private String formalName;

        private String email;

        private String redirectUrl;

        public static UserDTO from(User u) {
                return UserDTO.builder()
                        .email(u.getEmail())
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .id(u.getId())
                        .build();
        }

        public static UserDTO from(InternalUserDTO u) {
                return UserDTO.builder()
                        .redirectUrl(u.getRedirectUrl())
                        .email(u.getEmail())
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .id(u.getId())
                        .build();
        }

}
