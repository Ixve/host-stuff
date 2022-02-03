package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.webserver.utils.*;
import de.aggromc.confighubhost.*;
import java.io.*;

public class ShowRaw extends AdvancedWebHandler
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
        if (query == null || query.isEmpty()) {
            this.send(404, FileUtils.getFileContents("hidden/404"), ex);
        }
        else {
            final String img = query.substring(4);
            if (ConfigHost.getImageDB().imageExists(img)) {
                if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("url")) {
                    final String resp = FileUtils.getFileContents("hidden/empty").replace("content", "<meta property=\"og:url\" content=\"" + ConfigHost.getImageDB().getImageLoc(img) + "\" />");
                    ex.getResponseHeaders().set("Location", ConfigHost.getImageDB().getImageLoc(img));
                    ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=21600; immutable;");
                    this.sendNC(301, resp, ex);
                    return;
                }
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try {
                    final InputStream fis = new FileInputStream(ConfigHost.getImageDB().getImageLoc(img));
                    int b;
                    while ((b = fis.read()) != -1) {
                        buffer.write(b);
                    }
                    buffer.flush();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                final byte[] response = buffer.toByteArray();
                ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=21600; immutable;");
                ex.getResponseHeaders().set("Content-Type", FileUtils.getContentType(ConfigHost.getImageDB().getImageType(img)));
                this.sendNC(200, response, ex);
            }
            else {
                this.send(404, FileUtils.getFileContents("hidden/404"), ex);
            }
        }
    }
}
