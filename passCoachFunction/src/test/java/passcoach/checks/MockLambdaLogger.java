package passcoach.checks;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MockLambdaLogger implements LambdaLogger
{
    @Override
    public void log(String message)
    {
        String pattern = "hh:mm:ss:SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        System.out.println("T:" + Thread.currentThread().getName() + " Time: " +
                simpleDateFormat.format(new Date()) +" "+ message);
    }

    @Override
    public void log(byte[] message)
    {

    }
}
