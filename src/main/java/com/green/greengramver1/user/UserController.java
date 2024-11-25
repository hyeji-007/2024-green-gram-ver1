package com.green.greengramver1.user;

import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.user.model.UserSignInReq;
import com.green.greengramver1.user.model.UserSignInRes;
import com.green.greengramver1.user.model.UserSignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Tag(name = "유저", description = "회원가입, 로그인")
public class UserController {
    private final UserService service;

    /*
        파일(MultipartFile) + Data
        파일 업로드시에는 RequestBody를 사용할 수 없음.
        RequestPart애노테이션 사용해야 함 (required=true)가 기본값
     */
    @PostMapping("sign-up")
    @Operation(summary = "회원 가입")
    public ResultResponse<Integer> signUp(@RequestPart UserSignUpReq p //@RequestPart는 파일 받을 때만 사용
            , @RequestPart(required = false) MultipartFile pic) { //회원가입할 때 사진이 없어도 업로드 가능(null값 허용)
        log.info("UserInsReq: {}, file: {}", p, pic != null ? pic.getOriginalFilename() : null); //null일 경우 메소드를 호출하지 않기 위해, null이 아니었을 때만 메소드 호출
        int result = service.postSignUp(pic, p);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입 완료")
                .resultData(result)
                .build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultResponse<UserSignInRes> signIn(@RequestBody UserSignInReq p) { 
        UserSignInRes res = service.postSignIn(p);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(res.getMessage())
                .resultData(res)
                .build();
    }


}