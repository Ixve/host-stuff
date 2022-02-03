package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;

public class RegisterError extends AdvancedWebHandler
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
                case "unamelength": {
                    String r1 = FileUtils.getFileContents("hidden/register_errors");
                    r1 = r1.replace("%error", "Your Username has a wrong length ( 3 - 20 )");
                    this.send(r1, ex);
                    break;
                }
                case "pwident": {
                    String r2 = FileUtils.getFileContents("hidden/register_errors");
                    r2 = r2.replace("%error", "Your Password is not identical");
                    this.send(r2, ex);
                    break;
                }
                case "pwlength": {
                    String r3 = FileUtils.getFileContents("hidden/register_errors");
                    r3 = r3.replace("%error", "Your Password has a wrong length ( 6 - 32 )");
                    this.send(r3, ex);
                    break;
                }
                case "invite": {
                    String r4 = FileUtils.getFileContents("hidden/register_errors");
                    r4 = r4.replace("%error", "The Invite Code you provided is wrong!");
                    this.send(r4, ex);
                    break;
                }
                default: {
                    final String r5 = FileUtils.getFileContents("hidden/404");
                    this.send(r5, ex);
                    break;
                }
            }
        }
    }
}
