package com.example.newexceltocsv.controller;


import com.example.newexceltocsv.dto.NewConverterRequestDTO;
import com.example.newexceltocsv.service.ExcelToCsvConversions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel-to-csv")
@RequiredArgsConstructor
public class ExcelToCsvConverter {

    private final ExcelToCsvConversions convertExcelToCSV;

    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertExcelToJson(NewConverterRequestDTO csvColumnRequest) {
        try {
            List<Map<String, Object>> result = convertExcelToCSV.read(csvColumnRequest.getFile());
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting Excel to CSV: " + e.getMessage());
        }
    }

//    @PostMapping("/upload")
//    public void uploadExcelFile(@RequestParam("file") MultipartFile file) {
//        try {
//            excelReader.readExcelFile(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
