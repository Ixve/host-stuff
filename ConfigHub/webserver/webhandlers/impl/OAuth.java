package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;
import de.aggromc.confighubhost.*;

public class OAuth extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> l = new ArrayList<String>();
        l.add("GET");
        return l;
    }
    
    @Override
    public boolean needsVerification() {
        return true;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String query = ex.getRequestURI().getQuery();
        if (query == null || query.isEmpty() || !query.contains("code=")) {
            this.send(FileUtils.getFileContents("hidden/redirect").replace("%url", "https://discord.com/api/oauth2/authorize?response_type=code&client_id=795304705038745620&redirect_uri=https%3A%2F%2Fconfighub.host%2Foauth&scope=guilds.join%20identify%20guilds"), ex);
        }
        else if (ConfigHost.getUserDB().isLinked(this.getuID(ex))) {
            this.send(403, "You are already linked.", ex);
        }
        else if (de.aggromc.confighubhost.utils.OAuth.codeValid(query.substring(5))) {
            this.send("You may close this window now.", ex);
        }
        else {
            this.send(401, "Malformed Request", ex);
        }
    }
}
