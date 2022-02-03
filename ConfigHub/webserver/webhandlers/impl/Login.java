package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;

public class Login extends AdvancedWebHandler
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
            if (query == null || query.isEmpty() || query.equalsIgnoreCase("redir=") || !query.startsWith("redir=")) {
                this.send(FileUtils.getFileContents("login").replace("%extr", ""), ex);
            }
            else {
                this.send(FileUtils.getFileContents("login").replace("%extr", "<input type=\"hidden\" name=\"redir\" value=\"" + query.substring(6) + "\">"), ex);
            }
        }
    }
}
