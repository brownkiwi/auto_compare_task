package com.lsh.www.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
//@JSONType(orders = {"string","date","doubleDate"})
public class DmodelData {
    @ExcelProperty(value = "序号",index = 0)
    private Integer serial_num;
    @ExcelProperty(value = "数据表英文",index = 3)
    private String table_en;
    @ExcelProperty(value = "数据表中文",index = 2)
    private String table_zh;
    @ExcelProperty(value = "字段中文名",index = 5)
    private String field_zh;
    @ExcelProperty(value = "字段英文名",index = 4)
    private String field_en;
    @ExcelProperty(value = "是否为空",index = 11)
    private String null_flag;
    @ExcelProperty(value = "字段类型长度",index = 7)
    private String type_length;
    @ExcelProperty(value = "业务取值说明",index = 8)
    private String Business_value_description;
    @ExcelProperty(value = "字段简要说明",index = 9)
    private String field_value_description;
    @ExcelProperty(value = "是否为主键",index = 10)
    private String primary_key_flag;


    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private Integer num;

}
