package com.green.greengramver1.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

//@Setter와 생성자가 있으면 Setter을 먼저 사용한다.
//immutable : 1. setter x
//            2. 멤버 필드 private final 선언

@Getter
@ToString
public class Paging {
    //final static: 상수(값을 한번만 초기화)
    //private: 외부에서 직접 접근하지 못하도록 제한.
    //final: 값을 한 번만 초기화하며 이후 변경 불가능.
    //static: 모든 객체에서 공유되며 클래스 레벨에서 관리.
    private final static int DEFAULT_PAGE_SIZE = 20;

    @Schema(example = "1", description = "Selected Page")
    private int page;
    @Schema(example = "30", description = "item count per page")
    private int size;
    @JsonIgnore
    private int startIdx;

    public Paging(Integer page, Integer size) {
        this.page = (page == null || page <= 0) ? 1: page;
        this.size = (size == null || size <= 0) ?  DEFAULT_PAGE_SIZE : size;
        this.startIdx = ( this.page - 1 ) * this.size;
    }
}
