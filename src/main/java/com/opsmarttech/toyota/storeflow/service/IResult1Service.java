package com.opsmarttech.toyota.storeflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;

import java.util.List;

public interface IResult1Service extends IService<Result1> {

    public List<Result1> queryStoreFlow(int nodeId) throws Exception;

    public List<Result1> querySwatchInfo(int dateBegin, int dateEnd, int midId, int nodeId) throws Exception;

    public List<Result1> queryExchbInfoV1(int date1, int date2, int dimId, int nodeId);

}
