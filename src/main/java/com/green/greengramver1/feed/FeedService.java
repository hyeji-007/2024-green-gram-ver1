package com.green.greengramver1.feed;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.common.model.ResultResponse;
import com.green.greengramver1.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final MyFileUtils myFileUtils; //DI받는 조건: 빈등록(spring container가 객체화되게 함)

    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p) {
        int result = mapper.insFeed(p);

        //파일 저장
        //middlePath: feed/${feedId}
        long feedId = p.getFeedId();
        String middlePath = String.format("feed/%d", feedId);

        //폴더 만들기
        myFileUtils.makeFolders(middlePath);

        //파일 저장
        FeedPicDto feedPicDto = new FeedPicDto();
        feedPicDto.setFeedId(feedId);
        //feedPicDto에 feedId값 넣어주세요.

        List<String> picsStr = new ArrayList<String>();

        // 사진 이름 다르게 바뀌도록 랜덤 이름 출력
        for (MultipartFile pic : pics) {
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, savedPicName);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            feedPicDto.setPic(savedPicName); //feedPicDto 객체에 사진 파일명을 저장
            mapper.insFeedPic(feedPicDto); //mapper를 통해 데이터베이스에 사진 정보를 저장
            picsStr.add(savedPicName); //API 응답용 리스트인 picsStr에 사진 파일명을 추가
        }

        FeedPostRes res = new FeedPostRes();
        res.setFeedId(p.getFeedId());
        res.setPics(picsStr);

        return res;
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p) {
        List<FeedGetRes> list = mapper.selFeedList(p);
        //사진 매핑
        // n + 1 이슈 = 성능 저하 있어서 좋지 않음
        // feed list 가져오는 1회 + 튜플 6개 = 7번 조회 -> 성능 저하
        // but 가져올 select 가 많으면 나누는게 좋음 , 적으면 select 한번에 끝내는게 좋음
        for (FeedGetRes res : list) {
            //DB에서 각 피드에 맞는 사진 정보를 가져온다.
            List<String> picList = mapper.selFeedPicList(res.getFeedId());
            res.setPics(picList);
        }
        return list;
    }


}
