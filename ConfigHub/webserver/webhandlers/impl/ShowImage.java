package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;
import de.aggromc.confighubhost.auth.*;

public class ShowImage extends AdvancedWebHandler
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
        if (query == null || query.isEmpty() || !query.startsWith("key=") || query.equalsIgnoreCase("key=") || query.length() != 14 || !ConfigHost.getImageDB().imageExists(query.substring(4))) {
            final String response = FileUtils.getFileContents("hidden/404");
            this.send(404, response, ex);
        }
        final String img = query.substring(4);
        if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("url")) {
            final String resp = FileUtils.getFileContents("hidden/empty").replace("content", "<meta property=\"og:url\" content=\"" + ConfigHost.getImageDB().getImageLoc(img) + "\" />");
            ex.getResponseHeaders().set("Location", ConfigHost.getImageDB().getImageLoc(img));
            this.sendNC(301, resp, ex);
        }
        else if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("mp4")) {
            String response2 = FileUtils.getFileContents("hidden/showvid");
            ex.getResponseHeaders().set("Content-Type", "text/html");
            final String uID = ConfigHost.getImageDB().getImageOwner(img);
            System.out.println("https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
            response2 = response2.replaceAll("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
            response2 = response2.replace("%uploader", ConfigHost.getUserDB().getName(uID));
            response2 = response2.replace("%title", ConfigHost.getUserDB().getEmbedTitle(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID))).replace("%hex", ConfigHost.getUserDB().getEmbedColor(ConfigHost.getImageDB().getImageOwner(img)));
            response2 = response2.replace("%edesc", ConfigHost.getUserDB().getEmbedDesc(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID)));
            final UserDB.Rank rank = ConfigHost.getUserDB().getRank(ConfigHost.getImageDB().getImageOwner(img));
            if (rank == UserDB.Rank.BETA) {
                response2 = response2.replace("%badge", "<i id=\"kek\" class=\"fas fa-flask\"></i>");
            }
            else if (rank == UserDB.Rank.ADMIN || rank == UserDB.Rank.MOD) {
                response2 = response2.replace("%badge", "<i id=\"kek\" class=\"far fa-check-circle\"></i>");
            }
            else {
                response2 = response2.replace("%badge", "");
            }
            this.send(response2, ex);
        }
        else {
            final String uID2 = ConfigHost.getImageDB().getImageOwner(img);
            String response3 = FileUtils.getFileContents("hidden/showpic");
            ex.getResponseHeaders().set("Content-Type", "text/html");
            response3 = response3.replace("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
            response3 = response3.replace("%uploader", ConfigHost.getUserDB().getName(uID2));
            response3 = response3.replace("%title", img + "." + ConfigHost.getImageDB().getImageType(img)).replace("%hex", ConfigHost.getUserDB().getEmbedColor(uID2));
            response3 = response3.replace("%title", ConfigHost.getUserDB().getEmbedTitle(uID2).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID2)).replace("%domain", ConfigHost.getUserDB().getDomain(uID2))).replace("%hex", ConfigHost.getUserDB().getEmbedColor(uID2));
            response3 = response3.replace("%edesc", ConfigHost.getUserDB().getEmbedDesc(uID2).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID2)).replace("%domain", ConfigHost.getUserDB().getDomain(uID2)));
            final UserDB.Rank rank = ConfigHost.getUserDB().getRank(ConfigHost.getImageDB().getImageOwner(img));
            if (rank == UserDB.Rank.BETA) {
                response3 = response3.replace("%badge", "<i id=\"kek\" class=\"fas fa-flask\"></i>");
            }
            else if (rank == UserDB.Rank.ADMIN || rank == UserDB.Rank.MOD) {
                response3 = response3.replace("%badge", "<i id=\"kek\" class=\"far fa-check-circle\"></i>");
            }
            else {
                response3 = response3.replace("%badge", "");
            }
            this.send(response3, ex);
        }
    }
}
