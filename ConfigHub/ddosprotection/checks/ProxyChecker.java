package de.aggromc.confighubhost.ddosprotection.checks;

import java.net.*;
import java.util.*;

public class ProxyChecker
{
    public static ArrayList<String> proxies;
    public static ArrayList<String> not_proxies;
    
    public static boolean isProxy(final String ip) {
        if (ProxyChecker.proxies.contains(ip)) {
            return true;
        }
        try {
            final URL url = new URL("http://check.getipintel.net/check.php?ip=" + ip + "&contact=teamaggro@aggromc.de&flags=m");
            final Scanner sc = new Scanner(url.openStream());
            final StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            final String result = sb.toString();
            final double i = Double.parseDouble(result);
            if (i > 0.6) {
                ProxyChecker.proxies.add(ip);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    static {
        ProxyChecker.proxies = new ArrayList<String>();
        ProxyChecker.not_proxies = new ArrayList<String>();
    }
}
