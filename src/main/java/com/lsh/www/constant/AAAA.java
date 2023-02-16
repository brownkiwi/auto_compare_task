package com.lsh.www.constant;

import com.lsh.www.entity.ChayifenxiData;
import com.lsh.www.entity.DmodelData;
import com.lsh.www.entity.ZhongjianbiaoData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AAAA {
    public static boolean each_compare_flag = false;

    //读到了第几个表
    public static int read_seq;
    public static List<String> tables = new ArrayList<>(Arrays.asList("one|2", "two|1", "three|1"));
    public static List<DmodelData> dmodel_list = new ArrayList<>();
    public static List<ChayifenxiData> chayifenxi_list = new ArrayList<>();
    public static List<ZhongjianbiaoData> zhongjianbiao_list = new ArrayList<>();
    public static List<Integer> chayifenxi_Line_number = new ArrayList<>();
    public static List<Integer> zhongjianbiao_Line_number = new ArrayList<>();
    public static List<String> field_en = new ArrayList<>();  //中间表的
    public static List<String> field_zh = new ArrayList<>();  //中间表的
    public static List<String> changyifenxi_field_zh = new ArrayList<>();

}
