package com.xhh.aicode.easyexcel.service.impl;

import com.alibaba.excel.util.ListUtils;
import com.xhh.aicode.easyexcel.entity.DemoData;
import com.xhh.aicode.easyexcel.service.EasyExcelService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class EasyExcelServiceImplTest {

    @Resource
    private EasyExcelService easyExcelService;

    @Test
    void export() {
        // https://ai-code-1372346116.cos.ap-guangzhou.myqcloud.com/export/excel/2025-10-25/demo..csv
        String result = easyExcelService.export("demo", "sheet1", data(), DemoData.class);
        Assertions.assertNotNull(result);
    }

    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}