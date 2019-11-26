package com.opsmarttech.toyota.storeflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opsmarttech.toyota.storeflow.model.entity.Result5;

import java.util.List;

public interface IResult5Service extends IService<Result5> {

    public List<Result5> queryActive(int date1, int date2, int dimId, int nodeId);

}
