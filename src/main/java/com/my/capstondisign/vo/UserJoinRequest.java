package com.my.capstondisign.vo;

import lombok.Data;


import java.util.List;

@Data
public class UserJoinRequest {
    private int user_id;
    private String id;
    private String passwd;
    private String name;
    private String nickname;
    private String email;
    private String emailVerificationCode;
    private List<String> category;
    private String region;
}
