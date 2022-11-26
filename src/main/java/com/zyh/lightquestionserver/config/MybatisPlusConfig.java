package com.zyh.lightquestionserver.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MybatisPlusConfig {
    static List<String> tableList(){
        List<String> tables = new ArrayList<>();
        //表名
        tables.add("table");
        return tables;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            String newTable = null;
            for (String table : tableList()) {
                newTable = RequestDataHelper.getRequestData(table);
                if (table.equals(tableName) && newTable!=null){
                    tableName = newTable;
                    break;
                }
            }
            return tableName;
        });
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }
}
