package com.server.be_chatting.vo;

import java.util.List;

import lombok.Data;


@Data
public class RestListData<T> {
    private Integer total; //总数据量
    private List<T> list; //返回的列表

    public static <T> RestListData<T> create(Number total, List<T> list) {
        RestListData<T> data = new RestListData<>();
        data.setTotal(total != null ? total.intValue() : null);
        data.setList(list);
        return data;
    }

    public Integer getTotal() {
        return total;
    }

    public List<T> getList() {
        return list;
    }
}
