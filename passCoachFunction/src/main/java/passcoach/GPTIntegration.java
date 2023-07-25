package passcoach;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class GPTIntegration
{
    public static final String URI = "https://api.openai.com/v1/chat/completions";
    public static final String DOES_NOT_CONTAIN_UPPERCASE_LETTERS = "does not contain uppercase letters";
    public static final String WAS_PART_OF_A_KNOWN_LEAK = "was part of a known leak";
    public static final String CAN_BE_FOUND_IN_THE_ENGLISH_VOCABULARY = "can be found in the english vocabulary,";
    public static final String PART_OF_A_KNOWN_SECURITY_BREACH = "part of a known security breach";
    public static final String A_WORD_IN_THE_ENGLISH_DICTIONARY = "a word in the english dictionary";
    public static final String IS_IT = "is it ";
    public static final String IT_IS = "it is ";
    public static final String QUESTION = "?";
    public static final String IT_S_LENGTH_IS = "it's length is ";
    private final String msgs = "[{\"role\": \"system\", \"content\": \"cyber security assistant.\"},{\"role\": \"user\"," +
            " \"content\": \"What vulnerabilities do you see in the password '%1$s'? " +
            "Consider that it's complexity is %2$s, %3$s, %4$s " +
            "and expected duration of a brute force attack is %5$.2f hours and your suggestions\"}]";
    private final String fallbackMessage = "The password %1$s, is considered %2$s, "
            + "consider using a longer passphrase instead. <br> %3$s %4$s <br> "
            + "The estimated time to bruteforce is: %5$.2f hours <br>"
            + "Try to use special characters that can not be found on the keyboard like: ë,®,Ñ.. <br>"
            + "Always consider that password length is more important then character types";

    private final LambdaLogger logger;

    public GPTIntegration(LambdaLogger logger)
    {
        this.logger = logger;
    }
    /* curl https://api.openai.com/v1/chat/completions \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $OPENAI_API_KEY" \
     -d '{
     "model": "gpt-3.5-turbo",
     "max_tokens": 200,
     "messages": [
     {"role": "system", "content": "cyber security assistant."}, {"..."}]
           }'
*/

    public String generateQuery(String pass, String complexityResult, boolean hasUpper, Boolean leak, Boolean dictionary, double bruteforce)
    {
        String p2, p3 = "", p4 = "";
        p2 = complexityResult;
        if (!hasUpper)
            p2 = complexityResult + ", " + DOES_NOT_CONTAIN_UPPERCASE_LETTERS;
        if (leak == null)
            p3 = IS_IT + PART_OF_A_KNOWN_SECURITY_BREACH + QUESTION;
        else if (leak) p3 = WAS_PART_OF_A_KNOWN_LEAK;
        if (dictionary == null)
            p4 = IS_IT + A_WORD_IN_THE_ENGLISH_DICTIONARY + QUESTION;
        else if (dictionary) p4 = CAN_BE_FOUND_IN_THE_ENGLISH_VOCABULARY;
        return String.format(msgs, pass, p2, p3, p4, bruteforce);
    }


    public String explodeResult(String pass, String complexityResult, boolean hasUpper, Boolean leak, Boolean dictionary, double bruteforce)
    {

        String resp = createFallBackMessage(pass, complexityResult, hasUpper, leak, dictionary, bruteforce);
        try
        {
            String apiKey = System.getenv("API_KEY");
            if (StringUtils.isEmpty(apiKey))
                throw new RuntimeException("No api key defined in the environment");
            String auth = "Bearer " + apiKey;
            logger.log("Generating GPT query");
            String generatedQuery = generateQuery(pass, complexityResult, hasUpper, leak, dictionary, bruteforce);
            JsonElement jsonElement = JsonParser.parseString(generatedQuery);
            logger.log("GPT query: " + generatedQuery);

            HttpURLConnection con = (HttpURLConnection) new URL(URI).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", auth);

            JsonObject data = new JsonObject();
            data.addProperty("model", "gpt-3.5-turbo");

            data.addProperty("max_tokens", 256);
            data.add("messages", jsonElement);

            con.setDoOutput(true);
            logger.log("Sending query");
            con.getOutputStream().write(data.toString().getBytes());

            logger.log("Awaiting response");
            String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                    .reduce((a, b) -> a + b).get();

            resp = parsereponse(output);

        } catch (ProtocolException e)
        {
            throw new RuntimeException("Communication with GPT not possible, wrong Protocol", e);
        } catch (IOException e)
        {
            //todo clean up
            System.out.println(e.getMessage());
            return resp;
        }
        return resp;
    }

    public String createFallBackMessage(String pass, String complexityResult, boolean hasUpper, Boolean leak, Boolean dictionary, double bruteforce)
    {
        String p2, p3 = "", p4 = "";
        p2 = complexityResult;
        if (!hasUpper)
            p2 = complexityResult + ", " + IT_S_LENGTH_IS + pass.length() + " characters" + DOES_NOT_CONTAIN_UPPERCASE_LETTERS;
        if (leak != null && leak) p3 = IT_IS + WAS_PART_OF_A_KNOWN_LEAK;
        if (dictionary != null && dictionary) p4 = IT_IS + CAN_BE_FOUND_IN_THE_ENGLISH_VOCABULARY;
        return String.format(fallbackMessage, pass, p2, p3, p4, bruteforce);
    }

    private String parsereponse(String output)
    {
        logger.log("Parsing response:");
        logger.log(output);
        JsonElement jsonResp = JsonParser.parseString(output);
        String resp = jsonResp.getAsJsonObject().get("choices").getAsJsonArray().get(0).
                getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();
        resp = resp.replace("\n", "<br>").replace("\t", "<&emsp>");
        int lastSentence = resp.lastIndexOf(".");
        if (resp.length() - lastSentence < 50)
            resp = resp.substring(0, lastSentence );
        logger.log("Response: " + resp);
        return resp;
    }
}
