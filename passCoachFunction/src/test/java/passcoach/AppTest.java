package passcoach;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppTest
{
//
//    @Mock
//    Context mContext;
//    @Mock
//    LambdaLogger loggerMock;
//
//    @Before
//    public void setUp() throws Exception
//    {
//        when(mContext.getLogger()).thenReturn(loggerMock);
//
//        doAnswer(call -> {
//            System.out.println((String) call.getArgument(0));//print to the console
//            return null;
//        }).when(loggerMock).log(anyString());
//
//    }

    @Test
    @SetEnvironmentVariable(key = "API_KEY", value = "Something")
    public void successfulResponse()
    {
      //todo mock external service

//        App app = new App();
//        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//        input.setBody("{\"pass\":\"asd\",\"\":\"\"}");
//        APIGatewayProxyResponseEvent result = app.handleRequest(input, mContext);
//        assertEquals(200, result.getStatusCode().intValue());
//        assertEquals("application/json", result.getHeaders().get("Content-Type"));
//        String content = result.getBody();
//        assertNotNull(content);
//        assertTrue(content.contains("\"pass\""));
//        assertTrue(content.contains("\"result\""));
    }
}
