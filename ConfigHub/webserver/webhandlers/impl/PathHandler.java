package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;

public class PathHandler extends AdvancedWebHandler
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
        this.send(FileUtils.getFileContents(ex.getRequestURI().getPath()), ex);
    }
}
