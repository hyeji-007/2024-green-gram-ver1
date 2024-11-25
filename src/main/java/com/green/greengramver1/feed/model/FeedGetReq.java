package com.green.greengramver1.feed.model;

import com.green.greengramver1.common.model.Paging;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeedGetReq extends Paging {
    public FeedGetReq(Integer page, Integer size) {
        super(page, size);
        log.info("size: {}", getSize());
    }
}
