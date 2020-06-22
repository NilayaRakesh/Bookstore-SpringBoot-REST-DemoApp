package com.nr.bookstore.controller;

import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.exception.InternalException;
import com.nr.bookstore.exception.NotFoundException;
import com.nr.bookstore.model.api.RestResponse;
import com.nr.bookstore.model.dto.MediaCoverageDto;
import com.nr.bookstore.service.MediaCoverageService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MediaCoverageControllerTest extends TestCase {

    public static final String TITLE1 = "title 1";
    public static final String TITLE2 = "title 2";
    public static final String DUMMY_ISBN = "1001";

    @Mock
    private MediaCoverageService mediaCoverageService;

    @InjectMocks
    private MediaCoverageController cut;

    @Test
    public void testGetMediaCoverageOk() {
        try {
            MediaCoverageDto dummyCoverage1 = new MediaCoverageDto(TITLE1);
            MediaCoverageDto dummyCoverage2 = new MediaCoverageDto(TITLE2);
            List<MediaCoverageDto> dummyResponse = Arrays.asList(dummyCoverage1, dummyCoverage2);
            doReturn(dummyResponse).when(mediaCoverageService).fetchMediaCoverageForIsbn(DUMMY_ISBN);

            ResponseEntity<RestResponse<List<MediaCoverageDto>>> response = cut.getMediaCoverage(DUMMY_ISBN);
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
            assertNull(response.getBody().getError());
            assertEquals(dummyResponse, response.getBody().getData());

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetMediaCoverageNotFound() {
        try {
            doThrow(NotFoundException.class).when(mediaCoverageService).fetchMediaCoverageForIsbn(DUMMY_ISBN);

            ResponseEntity<RestResponse<List<MediaCoverageDto>>> response = cut.getMediaCoverage(DUMMY_ISBN);
            fail();

        } catch (NotFoundException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetMediaCoverageInternalError() {
        try {
            doThrow(InternalException.class).when(mediaCoverageService).fetchMediaCoverageForIsbn(DUMMY_ISBN);

            ResponseEntity<RestResponse<List<MediaCoverageDto>>> response = cut.getMediaCoverage(DUMMY_ISBN);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetMediaCoverageExternalError() {
        try {
            doThrow(InternalException.class).when(mediaCoverageService).fetchMediaCoverageForIsbn(DUMMY_ISBN);

            ResponseEntity<RestResponse<List<MediaCoverageDto>>> response = cut.getMediaCoverage(DUMMY_ISBN);
            fail();

        } catch (InternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}