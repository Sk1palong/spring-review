package org.example.springreview;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponseDto {

    private String msg;

    private Integer statusCode;

}
