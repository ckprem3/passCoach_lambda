package passcoach.checks;

import passcoach.PassInput;

public class ComplexityCheck
{
    public String complexityAsString(PassInput passInput)
    {

        int combo = 0;
        if (passInput.getDigit() > 0) combo++;
        if (passInput.getLower() > 0) combo++;
        if (passInput.getUpper() > 0) combo++;
        if (passInput.getSpecial() > 0) combo++;
        // Strength of password
        String result;
        if (passInput.getDigit() > 1 && passInput.getLower() > 3 && passInput.getUpper() > 1
                && passInput.getSpecial() > 0 && (passInput.getLength() >= 12))
            result = "Strong";
        else if (combo > 2 && (passInput.getLength() >= 8))
            result = "Moderate";
        else
            result = "Weak";

        return result;
    }
}