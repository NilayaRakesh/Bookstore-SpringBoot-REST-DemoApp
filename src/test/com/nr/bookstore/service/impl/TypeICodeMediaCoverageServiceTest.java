package com.nr.bookstore.service.impl;

import com.nr.bookstore.exception.ExternalException;
import com.nr.bookstore.manager.MediaCoverageManager;
import com.nr.bookstore.model.dto.BookDto;
import com.nr.bookstore.model.dto.MediaCoverageDto;
import com.nr.bookstore.model.external.MediaCoverage;
import com.nr.bookstore.service.BookService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TypeICodeMediaCoverageServiceTest extends TestCase {

    public static final Integer UID1 = 1;
    public static final Integer UID2 = 2;
    public static final Integer UID3 = 3;
    public static final Integer ID1 = 1;
    public static final Integer ID2 = 2;
    public static final Integer ID3 = 3;
    public static final String TITLE1 = "Rowling's Harry Potter Media";
    public static final String TITLE2 = "Title two";
    public static final String TITLE3 = "Title Three";
    public static final String BODY1 = "body 1";
    public static final String BODY2 = "Harry potter is a good story";
    public static final String BODY3 = "body 3";


    @Mock
    private BookService bookService;

    @Mock
    private MediaCoverageManager mediaCoverageManager;

    @InjectMocks
    private TypeICodeMediaCoverageService cut;

    @Test
    public void testFetchMediaCoverageForIsbn() {
        try {
            MediaCoverage coverage1 = new MediaCoverage(UID1, ID1, TITLE1, BODY1);
            MediaCoverage coverage2 = new MediaCoverage(UID2, ID2, TITLE2, BODY2);
            MediaCoverage coverage3 = new MediaCoverage(UID3, ID3, TITLE3, BODY3);
            doReturn(Arrays.asList(coverage1, coverage2, coverage3)).when(mediaCoverageManager).getAllMediaCoverages();

            BookDto bookDto = new BookDto();
            bookDto.setTitle("Harry Potter");
            doReturn(bookDto).when(bookService).getBookByIsbn(anyString());

            List<MediaCoverageDto> response = cut.fetchMediaCoverageForIsbn("Dummy");
            assertNotNull(response);
            assertEquals(2, response.size());
            List<String> titles = response.stream().map(MediaCoverageDto::getTitle).collect(Collectors.toList());
            assertTrue(titles.contains(TITLE1));
            assertTrue(titles.contains(TITLE2));
            assertFalse(titles.contains(TITLE3));
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testFetchMediaCoverageForIsbnExternalError() {
        try {
            doThrow(RestClientException.class).when(mediaCoverageManager).getAllMediaCoverages();

            List<MediaCoverageDto> response = cut.fetchMediaCoverageForIsbn("Dummy");
            fail();
        } catch (ExternalException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}