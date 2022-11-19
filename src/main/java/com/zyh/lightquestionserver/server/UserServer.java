package com.zyh.lightquestionserver.server;

import com.zyh.lightquestionserver.entity.User;

import java.util.Map;

public interface UserServer {
    public User loginServer(User user);
    public Object loginCodeServer(Map<String,String> map);
}
