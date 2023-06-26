package passcoach;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class PassCheckTest {


    private PassCheck checker;

    @Test
    public void successfulResponse() {

        String result = checker.evaluatePass("asdasd");

        assertNotNull(result);
        assertTrue(result.contains("this pass is junk1"));
//            assertTrue(result.contains("\"location\""));
    }

    @Test
    public void complexityAsString_pos_test() {
        String result =  checker.evaluatePass("bullshit");
        assertNotNull(result);
        assertTrue(result.contains("this pass is junk1"));
    }


}