package passcoach;

import passcoach.checks.BruteForcecheck;
import passcoach.checks.ComplexityCheck;
import passcoach.checks.DBChecks;

public class PassCoach
{

    private final DBChecks dbCheck = new DBChecks();
    private final ComplexityCheck complexityCheck = new ComplexityCheck();
    private final BruteForcecheck bruteForcecheck = new BruteForcecheck();

    public String evaluatePass(final String pass)
    {
        PassInput passInput = new PassInput(pass);
        //1 check against in db
        boolean dictionary = dbCheck.checkDictionary(pass);
        boolean leak = dbCheck.checkLeaked(pass);
        //2 check against common complexity
        String complexityResult = complexityCheck.complexityAsString(passInput);
        System.out.println(complexityResult);
        //3 estimate brute force
        double bruteforce = bruteForcecheck.estimateBruteForce(passInput);
        //4 check against common transformation
        //5 formulate response chatGPT
        GPTIntegration gptIntegration = new GPTIntegration();
        boolean hasUpper = passInput.getUpper() > 0;

        return gptIntegration.explodeResult(pass, complexityResult, hasUpper, leak, dictionary, bruteforce);
    }
}
