package com.example.newexceltocsv.utils;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@Slf4j
public class UploadUtils {
    public static Supplier<Stream<Row>> getRowStreamSupplier(Iterable<Row> rows) {
        return () -> getStream(rows);
    }

    public static <T> Stream<T> getStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static Supplier<Stream<Integer>> cellIteratorSupplier(int end) {
        return () -> numberStream(end);
    }

    public static Stream<Integer> numberStream(int end) {
        return IntStream.range(0, end).boxed();
    }

    public void saveCSVFile(String filePath, List<String> headers, List<Map<String, String>> data) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();

            if (!parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.info("Directory created: " + parentDir.getAbsolutePath());
                } else {
                    throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }
            try (FileWriter fileWriter = new FileWriter(file); CSVWriter csvWriter = new CSVWriter(fileWriter)) {
                Set<String> uniqueHeaders = new LinkedHashSet<>(headers);
                String[] headerArray = uniqueHeaders.toArray(new String[0]);

                boolean headerWritten = false;

                for (Map<String, String> row : data) {
                    String[] rowData = new String[headerArray.length];
                    for (int i = 0; i < headerArray.length; i++) {
                        rowData[i] = row.get(headerArray[i]);
                    }
                    if (!headerWritten) {
                        csvWriter.writeNext(headerArray);
                        headerWritten = true;
                    }
                    csvWriter.writeNext(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving CSV file: " + e.getMessage());
        }
    }
}
