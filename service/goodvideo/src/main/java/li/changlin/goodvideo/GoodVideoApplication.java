package li.changlin.goodvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class GoodVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodVideoApplication.class, args);
    }
}
