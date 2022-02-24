package com.poc.samplecrud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
public class ResponseDto {
    private String status;
    private Optional<?> message;

    public ResponseDto(String status) {
        this.status = status;
    }

    public ResponseDto(String status, Optional<?> message) {
        this.status = status;
        this.message = message;
    }
}
