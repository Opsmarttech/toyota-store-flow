package com.opsmarttech.toyota.storeflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opsmarttech.toyota.storeflow.mapper.Result1Mapper;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;
import com.opsmarttech.toyota.storeflow.service.IResult1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Result1ServiceImpl extends ServiceImpl<Result1Mapper, Result1> implements IResult1Service {

    @Autowired
    private Result1Mapper resultMapper;

    @Override
    public List<Result1> queryStoreFlow(int nodeId) throws Exception {
        return resultMapper.queryStoreFlow(nodeId);
    }

    @Override
    public List<Result1> querySwatchInfo(int dateBegin, int dateEnd, int midId, int nodeId) throws Exception {
        return resultMapper.querySwatchInfo(dateBegin, dateEnd, midId, nodeId);
    }

    @Override
    public List<Result1> queryExchbInfoV1(int dateBegin, int dateEnd, int dimId, int nodeId) {
        return resultMapper.queryExchbInfoV1(dateBegin, dateEnd, dimId, nodeId);
    }

}
