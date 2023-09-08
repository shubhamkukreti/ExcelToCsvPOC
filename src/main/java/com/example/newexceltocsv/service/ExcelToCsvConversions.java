package com.example.newexceltocsv.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ExcelToCsvConversions {
    List<Map<String, Object>> read(MultipartFile file);
}
