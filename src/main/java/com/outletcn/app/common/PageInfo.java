package com.outletcn.app.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 分页数据
 *
 * @author linx
 * @since 2022-03-22 13:41
 */
@Data
public class PageInfo<T> {
    private long current;
    private long size;
    private long total;
    private List<T> records;

    public PageInfo<T> buildPage(Page<T> page) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page.getCurrent());
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setRecords(page.getRecords());
        return pageInfo;
    }

    public static <T, R> PageInfo<R> buildPageInfo(Page<T> page, Function<T, R> f) {
        PageInfo<R> pageInfo = new PageInfo<>();
        List<T> records = page.getRecords();
        ArrayList<R> ts = new ArrayList<>(16);
        records.forEach(T -> ts.add(f.apply(T)));
        pageInfo.setCurrent(page.getCurrent());
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setRecords(ts);
        return pageInfo;
    }
}
