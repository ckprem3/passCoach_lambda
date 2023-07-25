package passcoach;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import passcoach.checks.BruteForceCheck;
import passcoach.checks.ComplexityCheck;
import passcoach.checks.DBChecks;

public class PassCoach
{

    private final DBChecks dbCheck;
    private final ComplexityCheck complexityCheck = new ComplexityCheck();
    private final BruteForceCheck bruteForcecheck = new BruteForceCheck();
    private final LambdaLogger logger;

    public PassCoach(LambdaLogger logger) {
        this.logger=logger;
        dbCheck = new DBChecks(logger);
    }

    public String evaluatePass(final String pass)
    {
        logger.log("Start Eval");
        PassInput passInput = new PassInput(pass);
        //1 check against in db
        logger.log("DB check");
        Boolean dictionary = dbCheck.checkDictionary(pass);
        Boolean leak = dbCheck.checkLeaked(pass);
        //2 check against common complexity
        logger.log("Complexity check");
        String complexityResult = complexityCheck.complexityAsString(passInput);
        logger.log("Complexity= "+ complexityResult);
        //3 estimate brute force
        logger.log("Brute force estimate");
        double bruteforce = bruteForcecheck.estimateBruteForce(passInput);
        //4 check against common transformation
        //5 formulate response chatGPT
        logger.log("Start GPT");
        GPTIntegration gptIntegration = new GPTIntegration(logger);
        boolean hasUpper = passInput.getUpper() > 0;
        String explodedResult = gptIntegration.explodeResult(pass, complexityResult, hasUpper, leak, dictionary, bruteforce);
        logger.log("Persist request/response");
        dbCheck.save(pass, explodedResult);
        logger.log("Returning result");
        return explodedResult;
    }
}
