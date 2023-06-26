package passcoach;

public class PassCheck {

    private PassInput passInput;

    public String evaluatePass(final String pass) {
        passInput = new PassInput(pass);
        //todo
        //1 check against leaks in db
        //2 check against common complexity
        String complexityResult = complexityAsString();
        //3 estimate brute force
        //4 check against common transformation
        //5 formulate response chatGPT
        System.out.println(complexityResult);
        return "this pass is junk1"; //todo
    }

    private String complexityAsString() {
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
