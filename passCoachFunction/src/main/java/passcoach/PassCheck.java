package passcoach;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PassCheck
{
    private String pass;

    public String evaluatePass(final String pass)
    {
        this.pass = pass;

        //todo
        //1 check against leaks in db
        //2 check against common complexity
        String complexityResult = complexity(pass);
        //3 estimate brute force
        //4 formulate response chatGPT
        System.out.println(complexityResult);
        return "this pass is junk"; //todo
    }

    private String complexity(final String input)
    {
        int n = input.length();
        int lower = 0, digit = 0, upper = 0, special = 0;
        Set<Character> set = new HashSet<>(Arrays.asList(
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', ' ', '_'));

        for (char i : input.toCharArray())
        {
            if (Character.isLowerCase(i))
                lower++;
            if (Character.isUpperCase(i))
                upper++;
            if (Character.isDigit(i))
                digit++;
            if (set.contains(i))
                special++;
        }
        int combo = 0;
        if (digit > 0) combo++;
        if (lower > 0) combo++;
        if (upper > 0) combo++;
        if (special > 0) combo++;
        // Strength of password
        String result;
        if (digit > 1 && lower > 3 && upper > 1 && special > 0 && (n >= 12))
            result = "Strong";
        else if (combo > 2 && (n >= 8))
            result = " Moderate";
        else
            result = " Weak";

        return result;
    }
}
