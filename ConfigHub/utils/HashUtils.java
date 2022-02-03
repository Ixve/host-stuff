package de.aggromc.confighubhost.utils;

import java.util.function.*;

public class HashUtils
{
    private final int logRounds;
    
    public HashUtils(final int logRounds) {
        this.logRounds = logRounds;
    }
    
    public String hash(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(this.logRounds));
    }
    
    public boolean verifyHash(final String password, final String hash) {
        return BCrypt.checkpw(password, hash);
    }
    
    public boolean verifyAndUpdateHash(final String password, final String hash, final Function<String, Boolean> updateFunc) {
        if (!BCrypt.checkpw(password, hash)) {
            return false;
        }
        final int rounds = this.getRounds(hash);
        if (rounds != this.logRounds) {
            final String newHash = this.hash(password);
            return updateFunc.apply(newHash);
        }
        return true;
    }
    
    private int getRounds(final String salt) {
        if (salt.charAt(0) != '$' || salt.charAt(1) != '2') {
            throw new IllegalArgumentException("Invalid salt version");
        }
        int off;
        if (salt.charAt(2) == '$') {
            off = 3;
        }
        else {
            final char minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$') {
                throw new IllegalArgumentException("Invalid salt revision");
            }
            off = 4;
        }
        if (salt.charAt(off + 2) > '$') {
            throw new IllegalArgumentException("Missing salt rounds");
        }
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}
