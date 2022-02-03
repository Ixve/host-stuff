package de.aggromc.confighubhost.auth;

import de.aggromc.confighubhost.*;
import java.util.logging.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import de.aggromc.confighubhost.utils.*;

public class UserIDManager
{
    private final HashMap<UserID, Date> hasch;
    public Runnable autoDelete;
    
    public UserIDManager() {
        this.hasch = new HashMap<UserID, Date>();
        final Iterator<Map.Entry<UserID, Date>> iterator;
        Map.Entry<UserID, Date> e;
        Instant now;
        boolean isWithinPrior24Hours;
        this.autoDelete = (() -> {
            ConfigHost.getLogger().log(Level.FINE, "UserCookies were deleted.");
            if (!this.hasch.isEmpty()) {
                this.hasch.entrySet().iterator();
                while (iterator.hasNext()) {
                    e = iterator.next();
                    now = Instant.now();
                    isWithinPrior24Hours = (!e.getValue().toInstant().isBefore(now.minus(24L, (TemporalUnit)ChronoUnit.HOURS)) && e.getValue().toInstant().isBefore(now));
                    if (isWithinPrior24Hours) {
                        this.hasch.remove(e.getKey());
                    }
                }
            }
        });
    }
    
    public boolean isValid(final String ud) {
        for (final Map.Entry<UserID, Date> e : this.hasch.entrySet()) {
            if (e.getKey().getId().equalsIgnoreCase(ud)) {
                return true;
            }
        }
        return false;
    }
    
    public String create(final String uID) {
        ConfigHost.getLogger().log(Level.INFO, "UserCookie for " + uID + " was created.");
        final RandomString cok = new RandomString(60);
        final String uid = cok.nextString();
        this.hasch.put(new UserID(uid, uID), new Date(System.currentTimeMillis()));
        return uid;
    }
    
    public String getUserIdForCookie(final String kok) {
        for (final Map.Entry<UserID, Date> e : this.hasch.entrySet()) {
            if (e.getKey().getId().equalsIgnoreCase(kok)) {
                return e.getKey().getOwnerID();
            }
        }
        return null;
    }
    
    public void delete(final String ud) {
        ConfigHost.getLogger().log(Level.INFO, "UserCookie '" + ud + "' was deleted.");
        for (final Map.Entry<UserID, Date> e : this.hasch.entrySet()) {
            if (e.getKey().getId().equalsIgnoreCase(ud)) {
                this.hasch.remove(e.getKey());
            }
        }
    }
}
