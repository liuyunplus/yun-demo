package com.github.liuyun;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class CanalClient {

    public static final Integer BATCH_SIZE = 200;

    public static void main(String[] args) {
        //创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111), "example", "", "");
        try {
            //打开连接
            connector.connect();
            //订阅数据库表，全部表
            connector.subscribe(".*\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            while (true) {
                //批量获取数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                //获取批次ID
                long batchId = message.getId();
                //获取批次大小
                int size = message.getEntries().size();
                if (batchId != -1 && size != 0) {
                    //处理数据
                    printEntry(message.getEntries());
                } else {
                    //没数据则休眠2秒
                    Thread.sleep(2000);
                }
                connector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            //若是开启或关闭事务的实体类型，则跳过
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("RowChange Parse Happen Error, entry=" + entry.toString(), e);
            }
            EventType eventType = rowChange.getEventType();
            //打印Header信息
            System.out.println(String.format("binlog[%s:%s], schema=%s, table=%s, eventType=%s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
            //判断是否是DDL语句
            if (rowChange.getIsDdl()) {
                System.out.println("============ DDL Operate, SQL Is Below ============");
                System.out.println(rowChange.getSql());
            }
            for (RowData rowData : rowChange.getRowDatasList()) {
                switch (eventType) {
                    case DELETE:
                        printColumn(rowData.getBeforeColumnsList());
                        break;
                    case INSERT:
                        printColumn(rowData.getAfterColumnsList());
                        break;
                    case UPDATE:
                        System.out.println("============ before ============");
                        //变更前的数据
                        printColumn(rowData.getBeforeColumnsList());
                        System.out.println("============ after ============");
                        //变更后的数据
                        printColumn(rowData.getAfterColumnsList());
                        break;
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        List<String> list = new ArrayList();
        for (Column column : columns) {
            list.add(String.format("%s:%s:%s", column.getName(), column.getValue(), column.getUpdated()));
        }
        System.out.println(String.join(", ", list));
    }

}
