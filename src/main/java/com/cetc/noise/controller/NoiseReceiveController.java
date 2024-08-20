package com.cetc.noise.controller;

import com.cetc.noise.config.TokenUtil;
import com.cetc.noise.model.LocalUser;
import com.cetc.noise.model.Noise;
import com.cetc.noise.model.RemoteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class NoiseReceiveController {

    private final Logger logger = LoggerFactory.getLogger(NoiseReceiveController.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private LocalUser localuser;

    @PostMapping("/authenticate")
    public Map<String, Object> authenticate(@RequestBody RemoteUser remoteUser){
        Map<String, Object> res = new HashMap<>();
        String username = remoteUser.getUsername();
        String password = remoteUser.getPassword();
        if(username != null && password != null){
            if (username.equals(localuser.getLocalusername()) && password.equals(localuser.getLocalpassword())) {
                Date now = new Date();
                Date expiration = new Date(now.getTime() + (24*60*60*1000));
                res.put("status",200);
                res.put("token",TokenUtil.createToken(username,password,expiration));
                res.put("expiration",expiration.getTime());
            }else {
                res.put("status",500);
                res.put("message","用户名密码错误！");
                res.put("timestamp",new Date().getTime());
            }
        }else{
            res.put("status",500);
            res.put("timestamp",new Date().getTime());
        }
        return res;
    }

    @PostMapping("/noiseReceiver")
    public Map<String, Object> NoiseReceiver(@RequestBody Noise noise){
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp",new Date().getTime());
        try {
            SendResult<String, Object> sendResult = kafkaTemplate.send("noise","data",noise).get(3, TimeUnit.SECONDS);
            res.put("status",200);
            logger.info("数据转发成功：{}", sendResult);
        }
        catch (ExecutionException | TimeoutException | InterruptedException e) {
            res.put("status",500);
            logger.warn("数据转发失败：{}", e.getMessage());
        }
        return res;
    }
}
