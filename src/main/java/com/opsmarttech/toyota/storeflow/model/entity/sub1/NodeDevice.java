package com.opsmarttech.toyota.storeflow.model.entity.sub1;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName("node_device")
public class NodeDevice {

    @TableField("nodeID")
    private int nodeId;

    @TableField("serial")
    private String serial;

    @TableField("beginTime")
    private String beginTime;

    @TableField("endTime")
    private String endTime;

    @TableField("status")
    private int status;

    @TableField("position")
    private String position;

    @TableField("bindTime")
    private String bindTime;

}
