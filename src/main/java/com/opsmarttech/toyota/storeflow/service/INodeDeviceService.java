package com.opsmarttech.toyota.storeflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opsmarttech.toyota.storeflow.model.entity.Result1;
import com.opsmarttech.toyota.storeflow.model.entity.sub1.NodeDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface INodeDeviceService extends IService<NodeDevice> {

    public List<NodeDevice> queryNodeInfo(String deviceSerial);

}
