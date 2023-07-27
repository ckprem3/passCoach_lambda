package passcoach;

import org.junit.Test;
import passcoach.checks.MockLambdaLogger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PassCoachTest
{

    private PassCoach checker = new PassCoach(new MockLambdaLogger());

    @Test
    public void successfulResponse() {
// todo mock external service

        String result = checker.evaluatePass("asdasd");

        assertNotNull(result);
        assertTrue(result.contains("asdasd "));
    }


}