package com.example.newexceltocsv.serviceImpl;

import com.example.newexceltocsv.dto.NewExcelToCsvDTO;
import com.example.newexceltocsv.service.ExcelToCsvConversions;
import com.example.newexceltocsv.utils.CsvValidations;
import com.example.newexceltocsv.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelToCsvConversionsImpl implements ExcelToCsvConversions {

    //    @Autowired
    private UploadUtils uploadUtil;
//    private CsvValidations csvValidations;

    @Override
    public List<Map<String, Object>> read(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        List<String> listOfHeaders = new ArrayList<>();
        listOfHeaders.add("Show");
        listOfHeaders.add("TimeStamp");
        listOfHeaders.add("Duration");
        listOfHeaders.add("Category");


        try {
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve(fileName).toFile();
            file.transferTo(tempFile);
            Workbook workbook = WorkbookFactory.create(tempFile);
            Sheet sheet = workbook.getSheetAt(0);
//            Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
//            Row headerRow = rowStreamSupplier.get().findFirst().get();
//            List<String> headerCells = uploadUtil.getStream(headerRow)
//                    .map(e -> getCellValue(e))
//                    .map(String::valueOf)
//                    .collect(Collectors.toList());

//            int colCount = headerCells.size();
            DataFormatter dataFormat = new DataFormatter();

            List<List<String>> originalExelDataRowList = new ArrayList<>();

            List<String[]> totalRows = new ArrayList<>();

            for (Row row : sheet) {
                List<String> rowDataList = new ArrayList<>();
                for (Cell cell : row) {
                    String cellValue = dataFormat.formatCellValue(cell);
                    rowDataList.add(cellValue);
                }
                List<String> rowContent = rowDataList;
//                new String[rowDataList.size()];
//                rowContent = rowContent;
                originalExelDataRowList.add(rowContent);
            }

            List<String> headerKeys = new ArrayList<>();
            originalExelDataRowList.get(0).forEach(it -> headerKeys.add(it));
            List<NewExcelToCsvDTO> listNewExcelToCsvDTO = new ArrayList<>();

            IntStream.range(1, originalExelDataRowList.size())
                    .forEach(index -> {
                        List<String> originalRowData = originalExelDataRowList.get(index);

                        IntStream.range(1, originalRowData.size()).forEach(orgRow -> {

                            IntStream.range(0, headerKeys.size())
                                    .forEach(keyIndex -> {
                                        String headerKey = headerKeys.get(keyIndex);
                                        if (keyIndex == orgRow) {
                                            NewExcelToCsvDTO newExcelToCsvDTO = new NewExcelToCsvDTO();
                                            newExcelToCsvDTO.setShow(originalRowData.get(orgRow));
                                            newExcelToCsvDTO.setTimeStamp(headerKey + " " + originalRowData.get(0));
                                            listNewExcelToCsvDTO.add(newExcelToCsvDTO);
                                        }
                                    });
                        });
                    });


//            Set<NewExcelToCsvDTO> finaldata = new HashSet<>();

            // Add CsvColumnRequest objects to the finaldata set
            System.out.println(listNewExcelToCsvDTO.toString());
            // Convert the set to a list of HashMaps
            List<Map<String, String>> listOfMaps = new ArrayList<>();
            for (NewExcelToCsvDTO request : listNewExcelToCsvDTO) {
                Map<String, String> requestMap = new HashMap<>();
                requestMap.put("Show", request.getShow());
                requestMap.put("TimeStamp", request.getTimeStamp());
                requestMap.put("Duration", request.getDuration());
                requestMap.put("Category", request.getCategories());
                listOfMaps.add(requestMap);
            }


            String sheetName = sheet.getSheetName();
            String csvFileName = new CsvValidations().sanitizeSheetName(sheetName) + ".csv";
            String csvFilePath = "/home/cyno/Desktop" + File.separator + csvFileName;

            // Only save the data if there's valid data to save
            if (!listOfMaps.isEmpty()) {
                new UploadUtils().saveCSVFile(csvFilePath, listOfHeaders, listOfMaps);
            }

            return null;
        } catch (Exception e) {
//            log.error("FileBasedIngestionServiceImpl : read failed for file: {}", fileName);
//            throw new IngestionFileReadFailedException(fileName, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    Object getCellValue(Cell cell) {
        try {
            String value = "";
            if (cell.getCellType() == CellType.STRING)
                value = cell.getStringCellValue().trim();
            else if (cell.getCellType() == CellType.NUMERIC)
                value = String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue());
            else {
                value = String.valueOf(cell.getNumericCellValue()).trim();
            }
            if (value == null || value.trim().length() == 0) {
                value = "UNKNOWN";
            }
            return value;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
