package com.example.newexceltocsv.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class NewExcelToCsvDTO {
//    private String Show;
//    private String TimeStamp;
//    private String Duration;
//    private String Category;

    @CsvBindByName(column = "start_timestamp")
    private String timeStamp;

    @CsvBindByName(column = "show")
    private String show;

    @CsvBindByName(column = "duration")
    private String duration;

//    @CsvBindByName(column = "infoImage")
//    private String infoImage;
//
//    @CsvBindByName(column = "discoverImage")
//    private String discoverImage;

    @CsvBindByName(column = "category")
    private String categories;
}
