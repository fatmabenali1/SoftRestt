package com.ecommerce.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
public class DtoUtilisateur {
    private String username;
    private String password;
    private String email;
    private  Role role ;
    @Id
    private String idU;

}
