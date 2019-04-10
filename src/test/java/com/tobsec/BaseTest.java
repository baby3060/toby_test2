package com.tobsec;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class BaseTest {

    @Test
    public void formatTest() {
        String str = String.format("Hello 실행 시간 : %d", 10);
        assertThat(str, is("Hello 실행 시간 : 10"));
    }
}