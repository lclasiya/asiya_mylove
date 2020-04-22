package li.changlin.search.config;

import feign.codec.Decoder;
import li.changlin.common.utils.WxMappingJackson2HttpMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConverterConfig {
    @Bean
    public Decoder feignDecoder(){
        WxMappingJackson2HttpMessageConverter wxConverter = new WxMappingJackson2HttpMessageConverter();
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(wxConverter);
        return new SpringDecoder(objectFactory);
    }

}
