package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.repository.elasticsearch.BookESDocRepository;
import com.nr.bookstore.rest.RestApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Autowired
    private BookESDocRepository bookESDocRepository;
    @Autowired
    private RestApiManager restApiManager;

    @GetMapping("/ping")
    public ResponseEntity<RestResponse<String>> ping() {
        return new ResponseEntityBuilder<String>(HttpStatus.OK).withData("Ping Successful to Bookstore!").build();
    }

}
