package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;
import de.aggromc.confighubhost.*;

public class Register extends AdvancedWebHandler
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
        if (this.isAuthenticated(ex)) {
            final String resp = FileUtils.getFileContents("hidden/redirect").replace("%url", "https://confighub.host/panel");
            this.send(resp, ex);
        }
        else {
            final String query = ex.getRequestURI().getQuery();
            if (query != null && !query.isEmpty() && !query.equalsIgnoreCase("invite=") && query.startsWith("invite=") && query.length() == 47 && query.startsWith("invite=")) {
                final String inv = query.substring(7);
                if (ConfigHost.getInvites().inviteExists(inv)) {
                    final String response = FileUtils.getFileContents("register").replace("%inv", inv);
                    this.send(response, ex);
                    return;
                }
            }
            final String response2 = FileUtils.getFileContents("register").replace("value=\"%inv\"", "");
            this.send(response2, ex);
        }
    }
}
