package passcoach.checks;

import passcoach.PassInput;

public class BruteForceCheck
{

    private static final int lower = 26, upper = 26, digits = 10, special = 32;
    private static final double RTX3080 = 9669000;
    private static final int hour = 3600;

    public double estimateBruteForce(PassInput passInput)
    {
        int posibilities = 0;
        if (passInput.getUpper() > 0) posibilities = posibilities + upper;
        if (passInput.getLower() > 0) posibilities = posibilities + lower;
        if (passInput.getDigit() > 0) posibilities = posibilities + digits;
        if (passInput.getSpecial() > 0) posibilities = posibilities + special;
        double sSpace = Math.pow(posibilities, passInput.getLength());

        return sSpace / RTX3080 / hour /2;
    }
}