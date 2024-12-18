package edu.fast_track.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name","dwh4u71t3");
        config.put("api_key","876311436391795");
        config.put("api_secret","6nOgfIj6CCayi58tQcv8Xzohjm4");
        return new Cloudinary(config);
    }
}

