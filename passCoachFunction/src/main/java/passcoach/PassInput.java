package passcoach;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PassInput {

    private static final Set<Character> SPECIAL_SET = new HashSet<>(Arrays.asList(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', ' ', '_'));

    private int length;
    private int lower;
    private int digit;
    private int upper;
    private int special;

    public PassInput(String pass) {
        length = pass.length();

        for (char i : pass.toCharArray()) {
            if (Character.isLowerCase(i))
                lower++;
            if (Character.isUpperCase(i))
                upper++;
            if (Character.isDigit(i))
                digit++;
            if (SPECIAL_SET.contains(i))
                special++;
        }
    }

    public int getLength() {
        return length;
    }

    public int getLower() {
        return lower;
    }

    public int getDigit() {
        return digit;
    }

    public int getUpper() {
        return upper;
    }

    public int getSpecial() {
        return special;
    }
}
