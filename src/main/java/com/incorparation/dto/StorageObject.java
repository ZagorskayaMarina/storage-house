package com.incorparation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.incorparation.model.constant.Status;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StorageObject implements Serializable {
    List<StorageDTO> storages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class StorageDTO implements Serializable {
        @NotBlank(message = "Name is required parameter")
        private String name;
        @NotBlank(message = "Domain is required parameter")
        private String domain;
        @NotBlank(message = "Owner is required parameter")
        private String owner;
        @Email(message = "Email is required parameter")
        private String email;
        @NotEmpty(message = "Country is required parameter")
        private String country;
        private String province;
        private String city;
        @NotBlank(message = "Address is required parameter")
        private String address;
        @NotNull(message = "ZIP is required parameter")
        private String zip;
        @NotNull(message = "Currency is required parameter")
        private String currency;
        @NotNull(message = "Status is required parameter")
        private Status status;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class StorageLoginDTO implements Serializable {
        @Email(message = "Email is required parameter")
        private String email;
    }
}
