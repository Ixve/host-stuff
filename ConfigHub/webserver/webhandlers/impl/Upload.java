package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import de.aggromc.confighubhost.upload.*;
import de.aggromc.confighubhost.auth.*;

public class Upload extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("POST");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return false;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String query = ex.getRequestURI().getQuery();
        if (query == null || !ex.getRequestURI().getQuery().startsWith("secret=")) {
            this.send(401, "Missing: Secret", ex);
        }
        else {
            final String upKey = query.substring(7);
            final UserDB ud = ConfigHost.getUserDB();
            if (!ud.uploadTokenExists(upKey)) {
                final String response = "Wrong UploadToken";
                this.send(400, response, ex);
                return;
            }
            final String uID = ud.getUserIdByToken(upKey);
            if (ud.userExists(uID) && !ud.isBanned(uID)) {
                if (!UploadChecks.uploadAllowed(ex)) {
                    final String response2 = "You are sending Too Many Requests";
                    this.send(429, response2, ex);
                    return;
                }
                final String first;
                final String cHeader = first = ex.getRequestHeaders().getFirst("Content-Type");
                String imageType = null;
                switch (first) {
                    case "image/png": {
                        imageType = "png";
                        break;
                    }
                    case "image/gif": {
                        imageType = "gif";
                        break;
                    }
                    case "image/jpeg": {
                        imageType = "jpg";
                        break;
                    }
                    case "video/mp4": {
                        imageType = "mp4";
                        break;
                    }
                    default: {
                        final String response3 = "Type not supported (PNG,GIF,JPEG,MP4)";
                        this.send(415, response3, ex);
                        return;
                    }
                }
                try {
                    if (ConfigHost.getUserDB().hasNoIP(uID)) {
                        ConfigHost.getUserDB().setIP(uID, this.getIp(ex));
                        ConfigHost.getUserDB().setCountry(uID, this.getCountry(ex));
                    }
                    final String response4 = UploadManager.uploadNew(uID, ex.getRequestBody(), this.getIp(ex), this.getCountry(ex), imageType);
                    if (response4.equalsIgnoreCase("noallow")) {
                        this.sendNC(413, "File too large", ex);
                        return;
                    }
                    ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    this.sendNC(200, response4, ex);
                }
                catch (Exception e) {
                    final String response5 = "Malformed Request";
                    this.send(400, response5, ex);
                }
            }
            else {
                final String response2 = "Not Permitted";
                this.send(403, response2, ex);
            }
        }
    }
}
