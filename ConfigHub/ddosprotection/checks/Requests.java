package de.aggromc.confighubhost.ddosprotection.checks;

import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import java.util.logging.*;

public class Requests
{
    public static boolean valid(final HttpExchange ex) {
        final String ip = ex.getRequestHeaders().getFirst("CF-Connecting-IP");
        final String country = ex.getRequestHeaders().getFirst("CF-Ipcountry");
        if (ip == null || country == null) {
            ConfigHost.getLogger().log(Level.WARNING, "Computer from '" + ex.getRemoteAddress().getAddress().getCanonicalHostName() + "' tried to request us outside of Cloudflare´s Protection. (BLOCKED)");
            return false;
        }
        if (RequestLimiter.reqs.containsKey(ip)) {
            int req = RequestLimiter.reqs.get(ip);
            if (req > 25) {
                ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ") sent too many Requests in the last 5 Seconds. (BLOCKED)");
                return false;
            }
            ++req;
            RequestLimiter.reqs.put(ip, req);
        }
        else {
            RequestLimiter.reqs.put(ip, 1);
        }
        String q = ex.getRequestURI().getQuery();
        if (q == null || q.isEmpty()) {
            q = "(No Query)";
        }
        ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ") requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q);
        return true;
    }
}
