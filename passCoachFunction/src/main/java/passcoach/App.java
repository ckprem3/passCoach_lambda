package passcoach;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String PASS = "pass";
    public static final String RESULT = "result";

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Code starts");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "*");

        //parsing the request body for the received String
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        JsonObject request = JsonParser.parseString(input.getBody()).getAsJsonObject();
        JsonElement jsonElement = request.get(PASS);
        String pass = jsonElement.getAsString();

        //evaluate pass
        logger.log("Pass evaluation");
        PassCoach pc = new PassCoach(logger);
        String output = pc.evaluatePass(pass);

        //creating the response json
        logger.log("Assembling response");
        JsonObject resp = new JsonObject();
        resp.addProperty(PASS,pass);
        resp.addProperty(RESULT, output);
        logger.log("Response: "+resp.toString());

        return response
                .withStatusCode(200)
                .withBody(resp.toString());
    }
}
