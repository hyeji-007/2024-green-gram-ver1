package com.green.greengramver1.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserInsReq {
    private String uid;
    private String upw;
    private String pic;
    private String nickName;
}
