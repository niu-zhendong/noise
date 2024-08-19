package com.cetc.noise.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "noise")
public class LocalUser {
    private String localusername;
    private String localpassword;
}
