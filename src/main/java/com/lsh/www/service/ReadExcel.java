package com.lsh.www.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.lsh.www.constant.AAAA;
import com.lsh.www.entity.ChayifenxiData;
import com.lsh.www.entity.DmodelData;
import com.lsh.www.entity.ZhongjianbiaoData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import javax.xml.ws.RequestWrapper;

@Slf4j
public class ReadExcel {

    static String Path1 = "D:\\Ideaworkspace\\auto_compare_task\\dmodel.xlsx";
    static String Path2 = "D:\\Ideaworkspace\\auto_compare_task\\zhongjianbiao.xlsx";
    static String Path3 = "D:\\Ideaworkspace\\auto_compare_task\\chayifenxi.xlsx";


    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DmodelData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DmodelDataListener}
     * <p>
     * 3. 直接读即可
     */

    public static void read_dmodel() {

        String fileName = Path1;
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DmodelData.class, new DmodelDataListener()).sheet().doRead();

    }

    public static void read_zhongjianbiao() {

        String fileName = Path2;
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ZhongjianbiaoData.class, new ZhongjianbiaoDataListener()).sheet().doRead();

    }


    @Test
    public void aaa() throws Exception {
        //S-1:---------读Dmodel，生成中间表Excel
        for (int i = 0; i < AAAA.tables.size(); i++) {
            AAAA.read_seq = i;
            read_dmodel();
            //设置序号
            for (int j = 0; j < AAAA.dmodel_list.size(); j++) {
                AAAA.dmodel_list.get(j).setSerial_num(j+1);
            }
            //复制多表情况
            int single_size = AAAA.dmodel_list.size();
            for (int k = 0; k < Integer.parseInt(AAAA.tables.get(AAAA.read_seq).split("\\|")[1]) - 1; k++) {
                //复制一次
                for (int j = 0; j < single_size; j++) {

                    DmodelData dmodelData = new DmodelData();
                    BeanUtils.copyProperties(dmodelData, AAAA.dmodel_list.get(j));
                    AAAA.dmodel_list.add(dmodelData);
                }
            }

            for (DmodelData s : AAAA.dmodel_list
            ) {
                System.out.println(JSON.toJSONString(s));
            }
            WriteExcel.appendWrite();

            AAAA.dmodel_list = ListUtils.newArrayListWithExpectedSize(100);
        }

        AAAA.read_seq = 0;
        //S-2:---------读中间表，生成中间表list
        //for (int i = 0; i < AAAA.tables.size(); i++) {
        //    AAAA.read_seq = i;
        //
        //    //构造list
        //    read_zhongjianbiao();
        //    read_chayifenxi();
        //    for (int j = 0; j < AAAA.chayifenxi_list.size(); j++) {
        //        AAAA.chayifenxi_Line_number.add(AAAA.chayifenxi_list.get(j).getNum());
        //        AAAA.changyifenxi_field_zh.add(AAAA.chayifenxi_list.get(j).getField_zh());
        //    }
        //    for (int j = 0; j < AAAA.zhongjianbiao_list.size(); j++) {
        //        AAAA.zhongjianbiao_Line_number.add(AAAA.zhongjianbiao_list.get(j).getNum());
        //        AAAA.field_en.add(AAAA.zhongjianbiao_list.get(j).getField_en());
        //        AAAA.field_zh.add(AAAA.zhongjianbiao_list.get(j).getField_zh());
        //    }
        //
        //    //写比较的代码，数据在两个list里，一个差异分析list,一个中间表list
        //    Check_impl check_impl = new Check_impl();
        //    check_impl.check(AAAA.zhongjianbiao_list,AAAA.chayifenxi_list);
        //
        //    //清list数据
        //    AAAA.chayifenxi_list = ListUtils.newArrayListWithExpectedSize(100);
        //    AAAA.zhongjianbiao_list = ListUtils.newArrayListWithExpectedSize(100);
        //    AAAA.chayifenxi_Line_number = ListUtils.newArrayListWithExpectedSize(100);
        //    AAAA.zhongjianbiao_Line_number = ListUtils.newArrayListWithExpectedSize(100);
        //    AAAA.field_en = ListUtils.newArrayListWithExpectedSize(100);
        //    AAAA.field_zh = ListUtils.newArrayListWithExpectedSize(100);
        //
        //}
        //AAAA.read_seq = 0;
    }


    public static void read_chayifenxi() {
        String fileName = Path3;
        EasyExcel.read(fileName, ChayifenxiData.class, new ChayifenxiDataListener()).sheet().doRead();
    }

}
