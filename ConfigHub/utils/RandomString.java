package de.aggromc.confighubhost.utils;

import java.security.*;
import java.util.*;

public class RandomString
{
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower;
    public static final String digits = "0123456789";
    public static final String alphanum;
    private final Random random;
    private final char[] symbols;
    private final char[] buf;
    
    public RandomString(final int length, final Random random, final String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }
    
    public RandomString(final int length, final Random random) {
        this(length, random, RandomString.alphanum);
    }
    
    public RandomString(final int length) {
        this(length, new SecureRandom());
    }
    
    public String nextString() {
        for (int idx = 0; idx < this.buf.length; ++idx) {
            this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
        }
        return new String(this.buf);
    }
    
    static {
        lower = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase(Locale.ROOT);
        alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + RandomString.lower + "0123456789";
    }
}
