package passcoach;

import junit.framework.TestCase;


public class GPTIntegrationTest extends TestCase
{
    private final GPTIntegration gptIntegration = new GPTIntegration();
    public void testGenerateQuery()
    {
        String generatedQuery = gptIntegration.generateQuery("retek1024","weak", false, true, true, 128);
        assertTrue(generatedQuery.contains("retek1024"));
        assertTrue(generatedQuery.contains("weak"));
        assertTrue(generatedQuery.contains(GPTIntegration.DOES_NOT_CONTAIN_UPPERCASE_LETTERS));
        assertTrue(generatedQuery.contains(GPTIntegration.WAS_PART_OF_A_KNOWN_LEAK));
        assertTrue(generatedQuery.contains(GPTIntegration.CAN_BE_FOUND_IN_THE_ENGLISH_VOCABULARY));
        assertTrue(generatedQuery.contains("128"));
    }

    public void testExplodeResult(){
      //todo mock external service
        String gptResult = gptIntegration.explodeResult("bela1024", "weak", false, false, true, 5000.2);
        assertTrue(gptResult.contains("bela1024"));
        assertTrue(gptResult.contains("weak"));
        assertTrue(gptResult.contains("common"));

    }
}