package com.teamapt.offlinesales.testing.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
@Data
@ToString
public class ResponseModel<T> {
    private T data;
    private String message;
    private List<String> errors;
    private int status;

    public ResponseModel(T data) {
        this.data = data;
        this.status = HttpStatus.OK.value();
        this.message = "Request successful";
    }

    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(this.getStatus()));
    }
}