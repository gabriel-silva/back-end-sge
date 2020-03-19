package com.backend.sge.exception;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RestError {

    private Date timestamp;
    private String message;
    private String details;

    public RestError(Date timestamp, String message) {
        super();
        this.timestamp = timestamp;
        this.message = message;
    }

    public RestError(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}