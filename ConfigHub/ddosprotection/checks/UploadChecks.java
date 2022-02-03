package de.aggromc.confighubhost.ddosprotection.checks;

import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import java.util.logging.*;

public class UploadChecks
{
    public static final HashMap<String, Integer> uploads;
    
    public static boolean uploadAllowed(final HttpExchange ex) {
        final String ip = ex.getRequestHeaders().getFirst("CF-Connecting-IP");
        final String country = ex.getRequestHeaders().getFirst("CF-Ipcountry");
        if (UploadChecks.uploads.containsKey(ip)) {
            int req = UploadChecks.uploads.get(ip);
            if (req > 20) {
                ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ") tried to upload more Images than allowed. (BLOCKED)");
                return false;
            }
            ++req;
            UploadChecks.uploads.put(ip, req);
        }
        else {
            UploadChecks.uploads.put(ip, 1);
        }
        return true;
    }
    
    static {
        uploads = new HashMap<String, Integer>();
    }
}
