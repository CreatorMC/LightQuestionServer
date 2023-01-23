package com.zyh.lightquestionserver.entity;

import com.zyh.lightquestionserver.annotation.EncryptField;
import lombok.Data;

@Data
public class UserEmailRegister {
    @EncryptField
    private String email;
    @EncryptField
    private String password;
}
