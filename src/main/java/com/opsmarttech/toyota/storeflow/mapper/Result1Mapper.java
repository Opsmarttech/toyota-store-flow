package com.opsmarttech.toyota.storeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("result1Mapper")
public interface Result1Mapper extends BaseMapper<Result1> {

    public List<Result1> queryStoreFlow(@Param("nodeId")int nodeId);

    public List<Result1> querySwatchInfo(@Param("date1")int date1, @Param("date2")int date2, @Param("dimId")int dimId, @Param("nodeId")int nodeId);

    public List<Result1> queryExchbInfoV1(@Param("date1")int date1, @Param("date2")int date2, @Param("dimId")int dimId, @Param("nodeId")int nodeId);

}
