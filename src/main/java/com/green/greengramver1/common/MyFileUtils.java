package com.green.greengramver1.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component // 빈등록: spring container에게 객체 관리를 해달라고 하는 것 >> 필요한 것: 생성자 호출(생성자: class와 이름이 똑같고 return 타입을 적지 않는다.)
// 그리고 그 주소값을 가지고 있다가 필요할 때 달라고 하면 준다.
// 기본적으로 싱글톤(객체를 딱 하나만 만든다), shallow copy(항상 같은 값을 준다)
public class MyFileUtils {
    private final String uploadPath;

    /*
    @Value("${file.directory}")은
    yaml 파일에 있는 file.directory 속성에 저장된 값을 생성자 호출할 때 값을 넣어준다.
     */

    public MyFileUtils(@Value("${file.directory}") String uploadPath) { //빈등록하면 뭘 넣어야하는지 spring에게 알려줘야 함, @AllArgsConstructor 사용 불가
        log.info("MyFileUtils - 생성자: {}", uploadPath);
        this.uploadPath = uploadPath;
    }

    // path = "ddd/aaa"
    // D:\khj\2024-02\download\greengram_ver1/ddd/aaa >> uploadPath에 저장
    // 디렉토리 생성
    public String makeFolders(String path) { //path: service에서 만든 middel path
        File file = new File(uploadPath, path); //생성자 호출

        // static 아님 >> 객체화하고 주소값.(file.) 으로 호출했기 때문에
        // 리턴타입은 boolean >> if()안에서 호출했기 때문에
        // 파라미터는 없음 >> 호출 때 인자를 보내지 않았기 때문에
        // 메소드명은 >> exists였다.

        if(!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath(); // AbsolutePath: 절대 경로

    }

    //파일명에서 확장자 추출
    public String getExt(String fileName) {
        int lastIdx = fileName.lastIndexOf(".");
        return fileName.substring(lastIdx);
    }

    //랜덤파일명 생성 , 상속받은 메소드 재정의 >> 오버라이딩
    // 똑같은 메소드명 여러개 만듦 >> 오버로딩(파라미터가 다르면 ok, 호출할 때 구분할 수 있어야 한다.)
    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }

    //랜덤파일명 + 확장자 생성
    public String makeRandomFileName(String originalFileName) {
        return makeRandomFileName() + getExt(originalFileName);
    }

    //업로드된 파일의 이름에서 확장자를 포함한 새로운 랜덤 파일명을 생성
    public String makeRandomFileName(MultipartFile file) {
        return makeRandomFileName(file.getOriginalFilename()); //확장자명 얻기 위해, getOriginalFilename(): return 메소드
    }

    //파일을 원하는 경로에 저장
    public void transferTo(MultipartFile mf, String path) throws IOException {
        File file = new File(uploadPath, path);
        mf.transferTo(file);
    }
}

//class Test {
//    public static void main(String[] args) {
//        MyFileUtils myFileUtils = new MyFileUtils("C:/temp");
//        String randomFileName = myFileUtils.makeRandomFileName("119255016.1.jpg");
//        System.out.println(randomFileName);
//    }
//}
