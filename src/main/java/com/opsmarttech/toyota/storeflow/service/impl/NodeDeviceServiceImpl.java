package com.opsmarttech.toyota.storeflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opsmarttech.toyota.storeflow.mapper2.NodeDeviceMapper;
import com.opsmarttech.toyota.storeflow.model.entity.sub1.NodeDevice;
import com.opsmarttech.toyota.storeflow.service.INodeDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeDeviceServiceImpl extends ServiceImpl<NodeDeviceMapper, NodeDevice> implements INodeDeviceService {

    @Autowired
    NodeDeviceMapper nodeDeviceMapper;

    @Override
    public List<NodeDevice> queryNodeInfo(String deviceSerial) {
        return nodeDeviceMapper.queryNodeInfo(deviceSerial);
    }

}
