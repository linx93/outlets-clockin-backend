package com.outletcn.app.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

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

    public PageInfo<T> buildPageInfo(Page<T> page) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page.getCurrent());
        pageInfo.setSize(page.getSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setRecords(page.getRecords());
        return pageInfo;
    }
}
