package com.green.greengramver1.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "로그인") //namd은 키 값을 바꾼다.
public class UserSignInReq { //Req 프론트로부터 받는 데이터
    @Schema(title = "아이디", example = "bbong", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(title = "비밀번호", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;

}
