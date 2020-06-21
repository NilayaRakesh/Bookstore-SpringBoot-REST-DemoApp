package com.nr.bookstore.manager;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.nr.bookstore.constant.HystrixPropertyName;
import com.nr.bookstore.model.external.MediaCoverage;
import com.nr.bookstore.rest.GetRequest;
import com.nr.bookstore.rest.RestApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MediaCoverageManager {

    private RestApiManager restApiManager;
    private String mediaCoverageHost;

    private static final String MEDIA_POST_PATH = "posts";
    public static final String TIMEOUT_ALL_MEDIA_COVERAGES_IN_MS = "3000";

    @Autowired
    public MediaCoverageManager(RestApiManager restApiManager, @Value("${host.mediaCoverage}") String mediaCoverageHost) {
        this.restApiManager = restApiManager;
        this.mediaCoverageHost = mediaCoverageHost;
    }


    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = HystrixPropertyName.TIMEOUT_IN_MS, value = TIMEOUT_ALL_MEDIA_COVERAGES_IN_MS)
    })
    public List<MediaCoverage> getAllMediaCoverages() {
        GetRequest.GetRequestBuilder builder = (GetRequest.GetRequestBuilder) new GetRequest.GetRequestBuilder(mediaCoverageHost)
                .appendUrlSegment(MEDIA_POST_PATH);

        MediaCoverage[] mediaCoverages = restApiManager.get(builder.build(), MediaCoverage[].class);
        return Arrays.asList(mediaCoverages);
    }
}
