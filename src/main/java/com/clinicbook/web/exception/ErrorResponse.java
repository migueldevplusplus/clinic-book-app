package com.clinicbook.web.exception;

import java.time.LocalDateTime;

public record ErrorResponse(String message, int value, LocalDateTime now) {

}
