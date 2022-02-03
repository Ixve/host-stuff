package de.aggromc.confighubhost.ddosprotection.checks;

import java.util.*;
import java.util.concurrent.*;

public class RequestLimiter
{
    public static HashMap<String, Integer> reqs;
    public static ArrayList<String> limit;
    public static Runnable purge;
    
    public static void init() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(RequestLimiter.purge, 0L, 5L, TimeUnit.SECONDS);
        final ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses2.scheduleAtFixedRate(UploadChecks.uploads::clear, 0L, 1L, TimeUnit.MINUTES);
    }
    
    static {
        RequestLimiter.reqs = new HashMap<String, Integer>();
        RequestLimiter.limit = new ArrayList<String>();
        RequestLimiter.purge = (() -> RequestLimiter.reqs.clear());
    }
}
