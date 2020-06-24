package com.rygf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPasswordDTO {
    public String oldPassword;
    public String newPassword;
}
