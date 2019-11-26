package com.opsmarttech.toyota.storeflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opsmarttech.toyota.storeflow.mapper.Result5Mapper;
import com.opsmarttech.toyota.storeflow.model.entity.Result5;
import com.opsmarttech.toyota.storeflow.service.IResult5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Result5ServiceImpl extends ServiceImpl<Result5Mapper, Result5> implements IResult5Service {

    @Autowired
    private Result5Mapper result5Mapper;

    @Override
    public List<Result5> queryActive(int dateBegin, int dateEnd, int dimId, int nodeId) {
        return result5Mapper.queryActive(dateBegin, dateEnd, dimId, nodeId);
    }

}
