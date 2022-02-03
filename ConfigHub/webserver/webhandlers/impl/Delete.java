package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;

public class Delete extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("GET");
        list.add("DELETE");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return false;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String query = ex.getRequestURI().getQuery();
        if (query == null || query.isEmpty() || !query.contains("&img=") || !query.contains("key=") || query.length() != 39) {
            this.send(400, "Bad request", ex);
            return;
        }
        final String key = query.substring(4, query.length() - 15);
        final String upKey = query.substring(29);
        if (ConfigHost.getUserDB().uploadTokenExists(key) && ConfigHost.getImageDB().imageExists(upKey)) {
            ConfigHost.getImageDB().deleteImage(upKey);
            this.send("Image successfully deleted.", ex);
            return;
        }
        this.send(400, "Bad request", ex);
    }
}
