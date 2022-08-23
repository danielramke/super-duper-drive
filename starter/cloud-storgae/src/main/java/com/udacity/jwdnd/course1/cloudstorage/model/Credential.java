package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Credential {

    private Integer credentialid;
    private String url;
    private String username;
    private String keyName;
    private String password;
    private Integer userid;
}
