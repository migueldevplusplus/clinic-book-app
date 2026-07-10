package com.clinicbook.application.dtos;

public record RegisterUserCommand(String fullName, String email, String nationalId, String rawPassword) {

}
