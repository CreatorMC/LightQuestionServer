package com.zyh.lightquestionserver.entity;

import lombok.Data;

@Data
public class Result {
    private Boolean result;

    /**
     * 返回布尔型结果
     * @return Result
     */
    public static Result returnSuccessResult() {
        Result result1 = new Result();
        result1.setResult(true);
        return result1;
    }
}
