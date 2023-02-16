package com.lsh.www.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.lsh.www.constant.AAAA;
import com.lsh.www.entity.ChayifenxiData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.FocusEvent;
import java.util.List;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Getter
@Setter
@Slf4j
public class ChayifenxiDataListener implements ReadListener<ChayifenxiData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5000;
    /**
     * 缓存的数据
     */
    private List<ChayifenxiData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */

    public ChayifenxiDataListener() {
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ChayifenxiData data, AnalysisContext context) {
        Integer row = context.readRowHolder().getRowIndex();

        if (AAAA.tables.get(AAAA.read_seq).split("\\|")[0].equals(data.getTable_en())) {
            data.setNum(row + 1);
            AAAA.chayifenxi_list.add(data);
        }

    }


    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        //saveData();
        log.info("差异分析表所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        log.info("存储数据库成功！");
    }

}
