package com.example.newexceltocsv.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewConverterRequestDTO {
    private MultipartFile file;
    private String name;
}
