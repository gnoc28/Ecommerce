package com.example.ecommerce.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = Map.of(
                "cloud_name", "dtjwxiaxn",
                "api_key", "354617484762437",
                "api_secret", "SCImTzBtRpTAry2QzBEU54epBrY"
        );

        return new Cloudinary(config);
    }

}
