package com.tiduswr.rcauth.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tiduswr.rcauth.models.User;
import com.tiduswr.rcauth.models.validation.anotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data @Builder @ToString
public class UserDTO {

        @JsonProperty(access = Access.READ_ONLY)
        private Long id;

        @NotNull(message = "{user_name.notnull}")
        @Size(min = 3, max = 30, message = "{user_name.size}")
        private String userName;

        @NotNull(message = "{password.notnull}")
        @ValidPassword
        @JsonProperty(access = Access.WRITE_ONLY)
        private String password;

        @NotNull(message = "{formal_name.notnull}")
        @Size(min = 3, max = 100, message = "{formal_name.size}")
        private String formalName;

        public static UserDTO from(User u) {
                return UserDTO.builder()
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .id(u.getId())
                        .build();
        }

        public static UserDTO from(InternalUserDTO u) {
                return UserDTO.builder()
                        .formalName(u.getFormalName())
                        .password(u.getPassword())
                        .userName(u.getUserName())
                        .id(u.getId())
                        .build();
        }
}
