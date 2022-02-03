package de.aggromc.confighubhost.webserver.webhandlers;

import de.aggromc.confighubhost.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.utils.*;
import java.util.logging.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import java.util.*;
import de.aggromc.confighubhost.webserver.utils.*;
import com.sun.istack.internal.*;
import java.nio.charset.*;
import java.io.*;

public abstract class AdvancedWebHandler implements HttpHandler
{
    public abstract ArrayList<String> getAvailableMethods();
    
    public abstract boolean needsVerification();
    
    public String getIp(final HttpExchange ex) {
        return ex.getRequestHeaders().getFirst("X-CLIENT-IP");
    }
    
    public String getCountry(final HttpExchange ex) {
        return "US";
    }
    
    public String getCookie(final HttpExchange ex) {
        final Headers reqHeaders = ex.getRequestHeaders();
        final List<String> cookies = reqHeaders.get((Object)"Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            for (final String cookie : cookies) {
                final String[] split;
                final String[] cockses = split = cookie.split("; ");
                final int length = split.length;
                int i = 0;
                while (i < length) {
                    final String kokies = split[i];
                    if (kokies.startsWith("USERID=")) {
                        final String kok = kokies.substring(7);
                        if (!ConfigHost.getLoginManager().isValid(kok)) {
                            return null;
                        }
                        return kok;
                    }
                    else {
                        ++i;
                    }
                }
            }
        }
        return null;
    }
    
    public String getuID(final HttpExchange ex) {
        return ConfigHost.getLoginManager().getUserIdForCookie(this.getCookie(ex));
    }
    
    @Override
    public void handle(final HttpExchange ex) {
        String ip = null;
        final long startTime = System.currentTimeMillis();
        String country = null;
        ++UnderAttack.rss;
        ++UnderAttack.cps;
        ++UnderAttack.cpm;
        ++UnderAttack.cph;
        ++UnderAttack.requests;
        if (ConfigHost.reverse) {
            final String resp = "Were currently under attack, you will be located to the one who´s Attacking us so we can take him down.";
            ex.getResponseHeaders().set("Location", ConfigHost.reverseIP);
            this.sendNC(302, resp, ex);
            return;
        }
        if (UnderAttack.requests > 100 && !UnderAttack.notified) {
            ConfigHost.getDiscord().sendPlain(Channel.ALERTS, "@everyone WE CURRENTLY HAVE VERY MUCH REQUESTS! CHECK FOR DDOS!\nCP5S: " + UnderAttack.requests + "\nCPS: " + UnderAttack.cps + "\nBLACKLISTED: " + Arrays.toString(RequestLimiter.limit.toArray()));
            if (!UnderAttack.fwRuleOn) {
                try {
                    CFUtils.toggleEmergencyOn(UnderAttack.requests);
                    UnderAttack.fwRuleOn = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            this.getAvailableMethods().add("HEAD");
            if (!this.getAvailableMethods().contains(ex.getRequestMethod().toUpperCase())) {
                final String response = "Your request was denied. Reason: Method not allowed!";
                final String meth = ex.getRequestMethod().toUpperCase();
                if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) {
                    ex.sendResponseHeaders(405, 0L);
                    ex.close();
                }
                else {
                    this.send(405, response, ex);
                }
                final long time = System.currentTimeMillis() - startTime;
                ConfigHost.getLogger().log(Level.WARNING, "Computer from '" + ex.getRemoteAddress().getAddress().getCanonicalHostName() + "' (" + time + "ms) tried to request with Method '" + meth + "' (BLOCKED)");
                return;
            }
            ip = ex.getRequestHeaders().getFirst("X-CLIENT-IP");
            country = "US";
            if (ip == null || country == null || ip.equalsIgnoreCase("null") || ip.isEmpty()) {
                final long time2 = System.currentTimeMillis() - startTime;
                ConfigHost.getLogger().log(Level.WARNING, "Computer from '" + ex.getRemoteAddress().getAddress().getCanonicalHostName() + "' (" + time2 + "ms) tried to request us outside of Cloudflare´s Protection. (BLOCKED)");
                final String response2 = "Your request was denied. Reason: A bad Request was made to the Origin Server!";
                if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) {
                    ex.sendResponseHeaders(401, 0L);
                    ex.close();
                    return;
                }
                this.send(401, response2, ex);
                return;
            }
            else if (IPBlacklist.isListed(ip)) {
                final long time2 = System.currentTimeMillis() - startTime;
                ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ", " + time2 + "ms) is blacklisted. (BLOCKED)");
                final String response2 = "Your request was denied. Reason: Your ip is blacklisted!";
                if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) {
                    ex.sendResponseHeaders(404, 0L);
                    ex.close();
                    return;
                }
                this.send(404, response2, ex);
                return;
            }
            else if (RequestLimiter.limit.contains(ip)) {
                final String response = "Your request was denied. Reason: You are sending too many Requests. Please wait 30 Seconds before requesting us again!";
                if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) {
                    ex.sendResponseHeaders(429, 0L);
                    ex.close();
                    return;
                }
                this.send(429, response, ex);
                return;
            }
            else {
                if (RequestLimiter.reqs.containsKey(ip)) {
                    int req = RequestLimiter.reqs.get(ip);
                    if (req > 25) {
                        final long time3 = System.currentTimeMillis() - startTime;
                        RequestLimiter.limit.add(ip);
                        final String finalIp = ip;
                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                RequestLimiter.limit.remove(finalIp);
                                t.cancel();
                            }
                        }, 30000L);
                        ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ", " + time3 + "ms) sent too many Requests in the last 5 Seconds. (BLOCKED)");
                        final String response3 = "Your request was denied. Reason: You are sending too many Requests.  Please wait 30 Seconds before requesting us again!";
                        if (ex.getRequestMethod().toUpperCase().equalsIgnoreCase("HEAD")) {
                            ex.sendResponseHeaders(429, 0L);
                            ex.close();
                            return;
                        }
                        this.send(429, response3, ex);
                        return;
                    }
                    else {
                        ++req;
                        RequestLimiter.reqs.put(ip, req);
                    }
                }
                else {
                    RequestLimiter.reqs.put(ip, 1);
                }
                String q = ex.getRequestURI().getQuery();
                if (q == null || q.isEmpty()) {
                    q = "(No Query)";
                }
                if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) {
                    ex.sendResponseHeaders(200, 0L);
                    ex.close();
                    final long time3 = System.currentTimeMillis() - startTime;
                    if (time3 > 100L) {
                        ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time3 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q + "\nWARNING: Request took longer than 100ms");
                    }
                    else {
                        ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time3 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q);
                    }
                    return;
                }
            }
        }
        catch (Exception e) {
            ConfigHost.getLogger().log(Level.FINER, "Error while parsing Request from " + ip + ": " + e.getMessage());
            ex.close();
            e.printStackTrace();
        }
        try {
            if (this.needsVerification()) {
                final Headers reqHeaders = ex.getRequestHeaders();
                final List<String> cookies = reqHeaders.get((Object)"Cookie");
                if (cookies != null && !cookies.isEmpty()) {
                    for (final String cookie : cookies) {
                        final String[] split;
                        final String[] cockses = split = cookie.split("; ");
                        for (final String kokies : split) {
                            if (kokies.startsWith("USERID=")) {
                                final String kok = kokies.substring(7);
                                if (!ConfigHost.getLoginManager().isValid(kok)) {
                                    final String response4 = FileUtils.getFileContents("hidden/redirect").replace("%url", "https://confighub.host/login?redir=" + ex.getRequestURI().getPath());
                                    final long time4 = System.currentTimeMillis() - startTime;
                                    ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ", " + time4 + "ms) requested " + ex.getRequestURI().getPath() + " but is not authorized. (BLOCKED)");
                                    this.send(401, response4, ex);
                                }
                                else {
                                    this.handleRequest(ex);
                                    final long time5 = System.currentTimeMillis() - startTime;
                                    String q2 = ex.getRequestURI().getQuery();
                                    if (q2 == null || q2.isEmpty()) {
                                        q2 = "(No Query)";
                                    }
                                    if (time5 > 100L) {
                                        ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time5 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q2 + "\nWARNING: Request took longer than 100ms");
                                    }
                                    else {
                                        ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time5 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q2);
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
                final String response2 = FileUtils.getFileContents("hidden/redirect").replace("%url", "https://confighub.host/login?redir=" + ex.getRequestURI().getPath());
                final long time6 = System.currentTimeMillis() - startTime;
                ConfigHost.getLogger().log(Level.WARNING, "Computer from " + ip + " (Country: " + country + ", " + time6 + "ms) " + ex.getRequestURI().getPath() + " but is not authorized. (BLOCKED)");
                this.send(401, response2, ex);
            }
            else {
                this.handleRequest(ex);
                final long time2 = System.currentTimeMillis() - startTime;
                String q3 = ex.getRequestURI().getQuery();
                if (q3 == null || q3.isEmpty()) {
                    q3 = "(No Query)";
                }
                if (time2 > 100L) {
                    ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time2 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q3 + "\nWARNING: Request took longer than 100ms");
                }
                else {
                    ConfigHost.getLogger().log(Level.FINER, "Computer from " + ip + " (Country: " + country + ", " + time2 + "ms) requested: " + ex.getRequestURI().getPath() + "; Method: " + ex.getRequestMethod() + "; Query: " + q3);
                }
            }
        }
        catch (Exception e) {
            ConfigHost.getLogger().log(Level.FINER, "Error while handling Request from " + ip + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public abstract void handleRequest(@NotNull final HttpExchange p0) throws Exception;
    
    public boolean isAuthenticated(@NotNull final HttpExchange ex) {
        final Headers reqHeaders = ex.getRequestHeaders();
        final List<String> cookies = reqHeaders.get((Object)"Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            for (final String cookie : cookies) {
                final String[] split;
                final String[] cockses = split = cookie.split("; ");
                for (final String kokies : split) {
                    if (kokies.startsWith("USERID=")) {
                        final String kok = kokies.substring(7);
                        return ConfigHost.getLoginManager().isValid(kok);
                    }
                }
            }
        }
        return false;
    }
    
    public void send(final String contents, @NotNull final HttpExchange ex) {
        ex.getResponseHeaders().set("Content-Type", "text/html");
        if (ConfigHost.notfallmodus && !ex.getResponseHeaders().containsKey("Cache-Control")) {
            ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=600; immutable;");
        }
        this.sendNC(200, contents, ex);
    }
    
    public void sendNC(final int code, final String contents, @NotNull final HttpExchange ex) {
        this.sendNC(code, contents.getBytes(StandardCharsets.UTF_8), ex);
    }
    
    public void sendNC(final int code, final byte[] bs, @NotNull final HttpExchange ex) {
        try {
            ex.sendResponseHeaders(code, bs.length);
            final OutputStream os = ex.getResponseBody();
            os.write(bs);
            os.flush();
            os.close();
            ex.getRequestBody().close();
            ex.getResponseBody().close();
            ex.close();
        }
        catch (IOException sex) {
            sex.printStackTrace();
        }
    }
    
    public void send(final int code, final String contents, @NotNull final HttpExchange ex) {
        ex.getResponseHeaders().set("Content-Type", "text/html");
        this.sendNC(code, contents, ex);
    }
}
