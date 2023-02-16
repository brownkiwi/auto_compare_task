package com.lsh.www.service;

import com.alibaba.fastjson.JSON;
import com.lsh.www.constant.AAAA;
import com.lsh.www.entity.ChayifenxiData;
import com.lsh.www.entity.DmodelData;
import com.lsh.www.entity.ZhongjianbiaoData;

import java.util.List;
import java.util.Objects;

public class Check_impl {


    public void check(List<ZhongjianbiaoData> zhongjianbiao_list, List<ChayifenxiData> chayifenxi_list) throws Exception {

        for (ZhongjianbiaoData s : zhongjianbiao_list
        ) {
            System.out.println("zhongjianbiao=======" + JSON.toJSONString(s));
        }
        for (ChayifenxiData s : chayifenxi_list
        ) {
            System.out.println("chayifenxi========" + JSON.toJSONString(s));
        }
        //拿差异分析的属性比中间表的属性，没有【差异分析有，中间表没有】就删除
        chayifenxi_to_zhongjianbiao(zhongjianbiao_list, chayifenxi_list);
        //拿中间表的属性比差异分析的属性，有就比长度类型，PK,是否为空，没有【中间表有，差异分析没有】就插入
        zhongjianbiao_to_chayifenxi(zhongjianbiao_list, chayifenxi_list);

    }


    public void zhongjianbiao_to_chayifenxi(List<ZhongjianbiaoData> zhongjianbiao_list, List<ChayifenxiData> chayifenxi_list) throws Exception {
        boolean exist_flag1 = false; //为false,说明是新增属性
        for (int i = 0; i < zhongjianbiao_list.size(); i++) {
            for (int j = 0; j < chayifenxi_list.size(); j++) {
                //比较属性英文是否一样
                if (zhongjianbiao_list.get(i).getField_en().equals(chayifenxi_list.get(j).getField_en())) {
                    //一样的话还要比较行号，行号不一样，说明要插入，置为false
                    if (!AAAA.chayifenxi_Line_number.contains(zhongjianbiao_list.get(i).getNum())) {
                        exist_flag1 = false;
                    } else {
                        exist_flag1 = true;
                        //todo:比较pk,为空，长度类型,如果不同，就改掉并标上颜色
                        if (zhongjianbiao_list.get(i).getPrimary_key_flag() != null && !"".equals(zhongjianbiao_list.get(i).getPrimary_key_flag())) {
                            if (!zhongjianbiao_list.get(i).getPrimary_key_flag().equals(chayifenxi_list.get(j).getPrimary_key_flag())) {
                                WriteExcel.write_Primary_key_flag(zhongjianbiao_list.get(i));
                            }
                        }
                        if (zhongjianbiao_list.get(i).getNull_flag() != null && !"".equals(zhongjianbiao_list.get(i).getNull_flag())) {
                            if (!zhongjianbiao_list.get(i).getNull_flag().equals(chayifenxi_list.get(j).getNull_flag())) {
                                WriteExcel.write_Null_flag(zhongjianbiao_list.get(i));
                            }
                        }
                        if (zhongjianbiao_list.get(i).getType_length() != null && !"".equals(zhongjianbiao_list.get(i).getType_length())) {
                            if (!zhongjianbiao_list.get(i).getType_length().equals(chayifenxi_list.get(j).getType_length())) {
                                WriteExcel.write_Type_length(zhongjianbiao_list.get(i));
                            }
                        }

                    }

                }
            }
            if (!exist_flag1) {
                //todo:插入空行，填入带颜色的数据
                WriteExcel.insert_row1(zhongjianbiao_list.get(i));
            }
        }

    }

    public void chayifenxi_to_zhongjianbiao(List<ZhongjianbiaoData> zhongjianbiao_list, List<ChayifenxiData> chayifenxi_list) throws Exception {
        boolean exist_flag2 = false;
        for (int i = 0; i < chayifenxi_list.size(); i++) {
            for (int j = 0; j < zhongjianbiao_list.size(); j++) {
                //if (!chayifenxi_list.get(i).getField_en().equals(zhongjianbiao_list.get(j).getField_en()))
                if (AAAA.field_en.contains(chayifenxi_list.get(i).getField_en()) || AAAA.field_zh.contains(chayifenxi_list.get(i).getField_zh())) {
                    exist_flag2 = true;
                }
                //中间表没有差异分析的行号，删
                if (!AAAA.zhongjianbiao_Line_number.contains(chayifenxi_list.get(i).getNum())) {
                    exist_flag2 = false;
                }
            }
            if (exist_flag2 == false) {
                //todo:删除
                WriteExcel.delete_row(chayifenxi_list.get(i).getNum());
            }
        }
    }
}
