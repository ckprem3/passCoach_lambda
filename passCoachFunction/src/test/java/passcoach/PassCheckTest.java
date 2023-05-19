package passcoach;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class PassCheckTest
{


        @Test
        public void successfulResponse() {
            PassCheck checker = new PassCheck();
            String result = checker.evaluatePass("asdasd");

            assertNotNull(result);
            assertTrue(result.contains("\"message\""));
            assertTrue(result.contains("\"location\""));
        }


}
