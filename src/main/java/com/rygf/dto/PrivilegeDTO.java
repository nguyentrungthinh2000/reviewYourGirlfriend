package com.rygf.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrivilegeDTO {
    
    private Long id;
    
    @NotBlank
    private String name;
}