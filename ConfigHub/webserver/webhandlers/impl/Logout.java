package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;

public class Logout extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("GET");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return true;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String cookie = this.getCookie(ex);
        if (ConfigHost.getLoginManager().isValid(cookie)) {
            ConfigHost.getLoginManager().delete(cookie);
        }
        this.send(FileUtils.getFileContents("index"), ex);
    }
}
