package com.nr.bookstore.util;

import com.nr.bookstore.model.TestJson;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonUtilTest extends TestCase {

    private final TestJson DUMMY_OBJECT = new TestJson();
    private final String DUMMY_STRING = "{\"field1\":\"field1\",\"field2\":{\"innerField1\":[1,2,3]}}";

    @Test
    public void testFromJsonString() {
        TestJson json = JsonUtil.fromJsonString(DUMMY_STRING, TestJson.class);
        assertNotNull(json);
        assertEquals(DUMMY_OBJECT.getField1(), json.getField1());
        assertNotNull(json.getField2());
        for (int i = 0; i < DUMMY_OBJECT.getField2().getInnerField1().size(); i++) {
            assertEquals(DUMMY_OBJECT.getField2().getInnerField1().get(i), json.getField2().getInnerField1().get(i));
        }
    }

    @Test
    public void testFromJsonStringNull() {
        TestJson json = JsonUtil.fromJsonString(null, TestJson.class);
        assertNull(json);
    }


    @Test
    public void testToJsonString() {
        String jsonString = JsonUtil.toJsonString(DUMMY_OBJECT);
        assertNotNull(jsonString);
        assertEquals(DUMMY_STRING, jsonString);
    }

    @Test
    public void testToJsonStringNull() {
        String jsonString = JsonUtil.toJsonString(null);
        assertNull(jsonString);
    }
}