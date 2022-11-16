package com.github.liuyun;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVReaderHeaderAware;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class OpenCsvUtils {

    public static void main(String[] args) throws Exception {
        check(Lists.newArrayList("deviceId"));
    }

    public static void check(List<String> headers) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get("/home/liuyun/group.csv"));

        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        List<String> matchHeaders = new ArrayList();
        String[] record;
        while ((record = csvReader.readNext()) != null) {
            if (record.length == 1 && StringUtils.isBlank(record[0])) {
                continue;
            }
            for (String header : record) {
                if (headers.contains(header)) {
                    matchHeaders.add(header);
                }
            }
            break;
        }
        headers.removeAll(matchHeaders);
        if (CollectionUtils.isNotEmpty(headers)) {
            String msg = String.format("header [%s] not exist", String.join(", ", headers));
            throw new RuntimeException(msg);
        }
    }

}
