package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;

public class LoginError extends AdvancedWebHandler
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
        final String query = ex.getRequestURI().getQuery();
        if (query == null || query.isEmpty() || !query.startsWith("e=")) {
            this.send(404, FileUtils.getFileContents("hidden/404"), ex);
        }
        else {
            final String substring;
            final String req = substring = query.substring(2);
            switch (substring) {
                case "wrongpw": {
                    String r1 = FileUtils.getFileContents("hidden/register_errors");
                    r1 = r1.replace("%error", "Wrong Password!");
                    this.send(r1, ex);
                    break;
                }
                case "acc": {
                    String r2 = FileUtils.getFileContents("hidden/register_errors");
                    r2 = r2.replace("%error", "Account doesnt exist!");
                    this.send(r2, ex);
                    break;
                }
                default: {
                    final String r3 = FileUtils.getFileContents("hidden/404");
                    this.send(r3, ex);
                    break;
                }
            }
        }
    }
}
