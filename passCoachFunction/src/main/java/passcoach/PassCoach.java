package passcoach;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import passcoach.checks.BruteForceCheck;
import passcoach.checks.ComplexityCheck;
import passcoach.checks.DBChecks;

import java.util.concurrent.atomic.AtomicReference;

public class PassCoach
{

    private final DBChecks dbCheck;
    private final ComplexityCheck complexityCheck = new ComplexityCheck();
    private final BruteForceCheck bruteForcecheck = new BruteForceCheck();
    private final LambdaLogger logger;

    public PassCoach(LambdaLogger logger)
    {
        this.logger = logger;
        dbCheck = new DBChecks(logger);
    }

    public String evaluatePass(final String pass)
    {
        long kezdo = System.currentTimeMillis();
        logger.log("Start Eval");
        PassInput passInput = new PassInput(pass);
        //1 check against in db
        logger.log("DB check");
        AtomicReference<Boolean> dictionary = new AtomicReference<>();
        Thread dictionaryThread = new Thread(() -> dictionary.set(dbCheck.checkDictionary(pass)), "dictionaryThread");
        dictionaryThread.start();
        AtomicReference<Boolean> leak = new AtomicReference<>();
        Thread leakThread = new Thread(() -> leak.set(dbCheck.checkLeaked(pass)), "leakThread");
        leakThread.start();
        //2 check against common complexity
        logger.log("Complexity check");
        AtomicReference<String> complexityResult = new AtomicReference<>();
        Thread complexityThread = new Thread(() -> complexityResult.set(complexityCheck.complexityAsString(passInput)), "complexityThread");
        complexityThread.start();
        //3 estimate brute force
        logger.log("Brute force estimate");
        AtomicReference<Double> bruteforce = new AtomicReference<>((double) 0);
        Thread bruteforceThread = new Thread(() -> bruteforce.set(bruteForcecheck.estimateBruteForce(passInput)), "bruteforceThread");
        bruteforceThread.start();
        // get back to main exec thread
        try
        {
            dictionaryThread.join();
            leakThread.join();
            complexityThread.join();
            bruteforceThread.join();

        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        //4 check against common transformation
        //5 formulate response chatGPT
        logger.log("Start GPT");
        GPTIntegration gptIntegration = new GPTIntegration(logger);
        boolean hasUpper = passInput.getUpper() > 0;
        String explodedResult = gptIntegration.explodeResult(pass, complexityResult.get(), hasUpper, leak.get(),
                dictionary.get(), bruteforce.get());
        logger.log("Persist request/response");
        dbCheck.save(pass, explodedResult);
        logger.log("Returning result");
        long vege = System.currentTimeMillis();
        logger.log("Time in PassCoach: " + (vege - kezdo));

        return explodedResult;
    }
}
