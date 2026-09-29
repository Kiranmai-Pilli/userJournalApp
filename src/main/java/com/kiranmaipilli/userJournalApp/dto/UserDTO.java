package com.kiranmaipilli.userJournalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NonNull
    @NotEmpty
    @Schema(description = "user's username") // for description in schema in swagger ui
    private String userName;
    @NonNull
    @NotEmpty
    private String password;
    private String email;
    private boolean sentimentAnalysis;
    private List<String> roles;
}



