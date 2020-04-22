package li.changlin.search.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import li.changlin.search.listener.SearchMqListener;

public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Cookie", "VIDEOSESSION="+SearchMqListener.sessionId);
    }
}
