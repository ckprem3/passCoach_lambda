package passcoach;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import junit.framework.TestCase;
import org.junit.Test;
import passcoach.checks.MockLambdaLogger;


public class GPTIntegrationTest extends TestCase
{
    LambdaLogger ll = new MockLambdaLogger();
    private final GPTIntegration gptIntegration = new GPTIntegration(ll);

    public void testGenerateQuery()
    {
        String generatedQuery = gptIntegration.generateQuery("retek1024", "weak", false, true, true, 128);
        assertTrue(generatedQuery.contains("retek1024"));
        assertTrue(generatedQuery.contains("weak"));
        assertTrue(generatedQuery.contains(GPTIntegration.DOES_NOT_CONTAIN_UPPERCASE_LETTERS));
        assertTrue(generatedQuery.contains(GPTIntegration.WAS_PART_OF_A_KNOWN_LEAK));
        assertTrue(generatedQuery.contains(GPTIntegration.CAN_BE_FOUND_IN_THE_ENGLISH_VOCABULARY));
        assertTrue(generatedQuery.contains("128"));
    }
    public void testGenerateQueryNoDB()
    {
        String generatedQuery = gptIntegration.generateQuery("retek1024", "weak", false, null, null, 128);
        assertTrue(generatedQuery.contains("retek1024"));
        assertTrue(generatedQuery.contains("weak"));
        assertTrue(generatedQuery.contains(GPTIntegration.DOES_NOT_CONTAIN_UPPERCASE_LETTERS));
        assertTrue(generatedQuery.contains(GPTIntegration.A_WORD_IN_THE_ENGLISH_DICTIONARY));
        assertTrue(generatedQuery.contains(GPTIntegration.PART_OF_A_KNOWN_SECURITY_BREACH));
        assertTrue(generatedQuery.contains("128"));
    }



    @Test (expected = RuntimeException.class)
    public void myTestMethod()
    {
        try
        {
            //Run exception throwing operation here
        }
        catch(RuntimeException re)
        {
            String message = "Employee ID is null";
            assertEquals(message, re.getMessage());
            throw re;
        }
        fail("Employee Id Null exception did not throw!");
    }
    @Test(expected = RuntimeException.class)
    public void testExplodeResult()
    {
//        todo mock external  until that test exception

        try
        {
            String gptResult = gptIntegration.explodeResult("bela1024", "weak", false, false, true, 50.2);
            assertTrue(gptResult.contains("bela1024"));
            assertTrue(gptResult.contains("weak"));
            assertTrue(gptResult.contains("common"));

        }
        catch(RuntimeException re)
        {
            String message = "No api key defined in the environment";
            assertEquals(message, re.getMessage());
        }

    }


    public void testCreateFallBackMessage() {
        String generatedQuery = gptIntegration.createFallBackMessage("retek1024", "weak", false, null, null, 128);
        assertTrue(generatedQuery.contains("retek1024"));
        assertTrue(generatedQuery.contains("weak"));
        assertTrue(generatedQuery.contains(GPTIntegration.DOES_NOT_CONTAIN_UPPERCASE_LETTERS));
        assertTrue(generatedQuery.contains("128"));
    }
}