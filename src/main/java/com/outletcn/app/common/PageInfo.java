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
        ArrayList<R> records = new ArrayList<>(16);
        page.getRecords().forEach(item -> records.add(f.apply(item)));
        return PageInfo.buildPageInfo(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }

    private static <T> PageInfo<T> buildPageInfo(long current, long size, long total, List<T> records) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(current);
        pageInfo.setSize(size);
        pageInfo.setTotal(total);
        pageInfo.setRecords(records);
        return pageInfo;
    }
}
