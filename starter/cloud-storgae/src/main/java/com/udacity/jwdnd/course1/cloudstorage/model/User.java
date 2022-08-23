package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class User {

    private Integer userid;
    private String username;
    private String salt;
    private String password;
    private String firstname;
    private String lastname;

}
