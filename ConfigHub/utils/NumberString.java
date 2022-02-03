package de.aggromc.confighubhost.utils;

import java.util.*;

public class NumberString
{
    public static String getRandomNumberString() {
        final Random rnd = new Random();
        final int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
