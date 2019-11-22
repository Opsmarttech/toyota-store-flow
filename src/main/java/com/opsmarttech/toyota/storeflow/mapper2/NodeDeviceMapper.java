package com.opsmarttech.toyota.storeflow.mapper2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opsmarttech.toyota.storeflow.model.entity.sub1.NodeDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDeviceMapper extends BaseMapper<NodeDevice> {

    public List<NodeDevice> queryNodeInfo(@Param("deviceSerial") String deviceSerial);

}
