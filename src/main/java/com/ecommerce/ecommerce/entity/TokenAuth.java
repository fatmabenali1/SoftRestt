package com.ecommerce.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class TokenAuth {
    private String token;
    private String username;
    private String email;
    private Role role ;
    @Id
    private String idU;
}
