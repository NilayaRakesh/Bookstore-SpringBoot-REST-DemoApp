package com.nr.bookstore.service;

import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.dto.MediaCoverageDto;

import java.util.List;

public interface MediaCoverageService {

    List<MediaCoverageDto> fetchMediaCoverageForIsbn(String isbn) throws NotFoundException, InternalException, ExternalException;
}
