package passcoach.checks;

import junit.framework.TestCase;
import org.junit.Test;
import passcoach.PassInput;

public class BruteForceCheckTest extends TestCase
{

    @Test
    public void testEstimateBruteForce()
    {
        BruteForceCheck bruteForceCheck= new BruteForceCheck();
        double result = bruteForceCheck.estimateBruteForce(new PassInput("retek12"));
        assertEquals(1.0, result,0.5);
    }

}