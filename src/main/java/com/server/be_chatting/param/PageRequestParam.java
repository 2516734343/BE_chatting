package com.server.be_chatting.param;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PageRequestParam {
    private Integer pageNo;
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo != null && pageNo > 0 ? pageNo : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

    public Integer getStart() { // 兼容mysql limit  #{start},#{limit}
        return (getPageNo() - 1) * getPageSize();
    }

    public Integer getLimit() { // 兼容mysql limit  #{start},#{limit}
        return getPageSize();
    }
}
