package com.example.newexceltocsv.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvValidations {
    public String sanitizeCellValue(String cellValue) {
        cellValue = cellValue.replaceAll("[\\p{Cntrl}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
        boolean cellValueFlag = containsHiddenCharacters(cellValue);
        return cellValue;
    }

    public String[] sanitizeHeader(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].replaceAll("[^\\p{Print}]", "");
        }
        return headers;
    }

    private boolean containsHiddenCharacters(String value) {
        return value.matches(".*[\\p{Cntrl}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}].*");
    }

    public String sanitizeSheetName(String sheetName) {
        return sheetName.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}
