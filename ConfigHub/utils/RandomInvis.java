package de.aggromc.confighubhost.utils;

import java.util.*;

public class RandomInvis
{
    private final Random random;
    private final String[] symbols;
    private final String[] buf;
    
    public RandomInvis(final int length, final Random random, final String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.split(",");
        this.buf = new String[length];
    }
    
    public String nextString() {
        for (int idx = 0; idx < this.buf.length; ++idx) {
            this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
        }
        return Arrays.toString(this.buf).replaceAll(",", "").replaceAll(" ", "").replace("[", "").replaceAll("]", "");
    }
}
