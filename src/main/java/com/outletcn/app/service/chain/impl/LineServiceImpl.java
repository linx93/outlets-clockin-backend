package com.outletcn.app.service.chain.impl;

import com.outletcn.app.model.dto.chain.PutOnRequest;
import com.outletcn.app.service.chain.LineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tanwei
 * @version v1.0
 * @desc
 * @datetime 2022/5/16 3:00 PM
 */
@Slf4j
@AllArgsConstructor
@Service("lineService")
public class LineServiceImpl implements LineService {

    @Override
    public void putOnDestination(PutOnRequest putOnRequest) {

    }
}
