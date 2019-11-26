package com.opsmarttech.toyota.storeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;
import com.opsmarttech.toyota.storeflow.model.entity.Result5;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("result5Mapper")
public interface Result5Mapper extends BaseMapper<Result5> {

    public List<Result5> queryActive(@Param("date1") int date1, @Param("date2") int date2, @Param("dimId") int dimId, @Param("nodeId") int nodeId);

}
