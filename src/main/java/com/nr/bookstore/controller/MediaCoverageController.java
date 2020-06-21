package com.nr.bookstore.controller;

import com.nr.bookstore.builder.ResponseEntityBuilder;
import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.MediaCoverageDto;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.service.MediaCoverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MediaCoverageController {

    private MediaCoverageService mediaCoverageService;

    @Autowired
    public MediaCoverageController(MediaCoverageService mediaCoverageService) {
        this.mediaCoverageService = mediaCoverageService;
    }

    @GetMapping("/mediaCoverage/{isbn}")
    public ResponseEntity<RestResponse<List<MediaCoverageDto>>> getMediaCoverage(@PathVariable String isbn)
            throws NotFoundException, InternalException, ExternalException {

        List<MediaCoverageDto> mediaCoverageDtos = mediaCoverageService.fetchMediaCoverageForIsbn(isbn);
        return new ResponseEntityBuilder<List<MediaCoverageDto>>(HttpStatus.OK)
                .withData(mediaCoverageDtos)
                .build();
    }
}
