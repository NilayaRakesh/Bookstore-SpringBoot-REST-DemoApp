package com.nr.bookstore.manager;

import com.nr.bookstore.model.external.MediaCoverage;
import com.nr.bookstore.rest.RestApiManager;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MediaCoverageManagerTest extends TestCase {

    public static final Integer UID1 = 1;
    public static final Integer UID2 = 2;
    public static final Integer ID1 = 1;
    public static final Integer ID2 = 2;
    public static final String TITLE1 = "Title one";
    public static final String TITLE2 = "Title two";
    public static final String BODY1 = "body 1";
    public static final String BODY2 = "body 2";

    @Mock
    private RestApiManager restApiManager;

    @InjectMocks
    private MediaCoverageManager cut;


    @Test
    public void testGetAllMediaCoveragesOk() {
        try {
            ReflectionTestUtils.setField(cut, "mediaCoverageHost", "http://dummy.com");
            MediaCoverage coverage1 = new MediaCoverage(UID1, ID1, TITLE1, BODY1);
            MediaCoverage coverage2 = new MediaCoverage(UID2, ID2, TITLE2, BODY2);
            MediaCoverage[] dummyCoverages = new MediaCoverage[]{coverage1, coverage2};
            doReturn(dummyCoverages).when(restApiManager).get(any(), any());

            List<MediaCoverage> response = cut.getAllMediaCoverages();
            assertNotNull(response);
            assertEquals(2, response.size());
            assertEquals(coverage1, response.get(0));
            assertEquals(coverage2, response.get(1));

        } catch (Exception e) {
            fail();
        }
    }
}