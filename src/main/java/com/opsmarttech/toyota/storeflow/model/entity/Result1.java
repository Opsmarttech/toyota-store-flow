package com.opsmarttech.toyota.storeflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("result1")
public class Result1 extends Model<Result1> {

    private int nodeid;
    private int userid;
    private int customid;
    private int areaid;
    private int cityid;
    private int begindate;
    private int enddate;
    private int dimid;
    private int value;
    private int cycleid;

}
