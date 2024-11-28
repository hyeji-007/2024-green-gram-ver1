package com.green.greengramver1.user;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.user.model.UserSignInReq;
import com.green.greengramver1.user.model.UserSignInRes;
import com.green.greengramver1.user.model.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;


    public int postSignUp(MultipartFile pic, UserSignUpReq p) {
        //프로필 이미지 파일 처리
        //String savedPicName = myFileUtils.makeRandomFileName(pic.getOriginalFilename());
        // 위와 같은 결과가 나왔으면 좋겠다.
        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null); //pic이 null이 아닐 경우 메소드 호출

        String hashedPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        log.info("hashedPassword: {}", hashedPassword);
        p.setUpw(hashedPassword); //교체 >> 원래 비밀번호 날아감
        p.setPic(savedPicName);

        int result = mapper.insUser(p); //insert 처리

        if(pic == null) {
            return result; //파일 관련 작업을 생략하고 사용자 정보만 DB에 저장, result 값을 반환하여 작업 완료
        }

        // 저장 위치 만든다.
        // middlePath = user/${userId}
        // filePath = user/${userId}/${savedPicName}
        long userId = p.getUserId(); // userId를 insert 후에 얻을 수 있다.
        String middlePath = String.format("user/%d", userId);
        myFileUtils.makeFolders(middlePath);
        log.info("middlePath: {}", middlePath);
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            myFileUtils.transferTo(pic, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public UserSignInRes postSignIn(UserSignInReq p) {
        UserSignInRes res = mapper.selUserForSignIn(p);
        if( res == null ) { //아이디 없음
            res = new UserSignInRes();
            res.setMessage("아이디를 확인해 주세요.");
            return res;
        } else if( !BCrypt.checkpw(p.getUpw(), res.getUpw()) ) { //비밀번호가 다를시
            res = new UserSignInRes();
            res.setMessage("비밀번호를 확인해 주세요.");
            return res;
        }
        res.setMessage("로그인 성공");
        return res;
    }
}