package com.zyh.lightquestionserver.entity;

import com.zyh.lightquestionserver.annotation.EncryptField;
import lombok.Data;

@Data
public class UserVCode {
    private String code;
    private String uuid;
    @EncryptField
    private String phone;
}
