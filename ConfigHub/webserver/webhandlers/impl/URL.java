package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import de.aggromc.confighubhost.upload.*;
import de.aggromc.confighubhost.auth.*;

public class URL extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("GET");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return false;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String header = ex.getRequestHeaders().getFirst("secret");
        if (header == null || header.length() != 20) {
            this.send(401, "Missing Header: Secret", ex);
        }
        final String query = ex.getRequestURI().getQuery();
        if (query == null || !ex.getRequestURI().getQuery().startsWith("url=")) {
            this.send(401, "Missing: URL to shorten", ex);
        }
        else {
            final UserDB ud = ConfigHost.getUserDB();
            if (!ud.uploadTokenExists(header)) {
                final String response = "Wrong UploadToken";
                this.send(400, response, ex);
                return;
            }
            final String uID = ud.getUserIdByToken(header);
            if (ud.userExists(uID) && !ud.isBanned(uID)) {
                if (!UploadChecks.uploadAllowed(ex)) {
                    final String response2 = "You are sending Too Many Requests";
                    this.send(429, response2, ex);
                    return;
                }
                try {
                    final String response2 = UploadManager.shorten(uID, query.substring(4), this.getIp(ex), this.getCountry(ex));
                    ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    this.sendNC(200, response2, ex);
                }
                catch (Exception e) {
                    final String response3 = "Malformed Request";
                    this.send(400, response3, ex);
                }
            }
            else {
                final String response2 = "Not Permitted";
                this.send(403, response2, ex);
            }
        }
    }
}
