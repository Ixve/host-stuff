package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;
import java.util.*;

public class Panel extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("GET");
        list.add("POST");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return true;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
        final String query = ex.getRequestURI().getQuery();
        if (query == null || query.isEmpty()) {
            this.display("index", ex);
        }
        else if (query.startsWith("tab=")) {
            final String substring;
            final String q = substring = query.substring(4);
            switch (substring) {
                case "rules": {
                    this.display("rules", ex);
                    break;
                }
                case "settings": {
                    this.display("settings", ex);
                    break;
                }
                case "discord": {
                    this.display("discord", ex);
                    break;
                }
                case "contact": {
                    this.display("contact", ex);
                    break;
                }
                case "domains": {
                    this.display("domains", ex);
                    break;
                }
                default: {
                    this.display("index", ex);
                    break;
                }
            }
        }
        else {
            this.display("index", ex);
        }
    }
    
    public void display(final String page, final HttpExchange ex) {
        final String uID = ConfigHost.getLoginManager().getUserIdForCookie(this.getCookie(ex));
        String response = FileUtils.getFileContents("hidden/panel/" + page);
        if (page.equalsIgnoreCase("index")) {
            response = response.replace("%user", ConfigHost.getUserDB().getName(uID)).replace("%uploadtotal", ConfigHost.getImageDB().getUploads(uID) + "").replace("%registration", ConfigHost.getUserDB().getRegisteredAt(uID)).replace("%secret", ConfigHost.getUserDB().getUploadToken(uID)).replace("%uscount", ConfigHost.getUserDB().getInvitedUsersCount(uID) + "").replace("%invites", ConfigHost.getInvites().getInvites(uID)).replace("%id", uID);
        }
        else if (page.equalsIgnoreCase("settings")) {
            final StringBuilder dList = new StringBuilder();
            for (final String dm : ConfigHost.domainList) {
                dList.append("<option value=\"").append(dm).append("\">").append(dm).append("</option>\n");
            }
            if (uID.equalsIgnoreCase("307832")) {
                dList.append("<option value=\"").append("images.luax.xyz").append("\">").append("images.luax.xyz").append("</option>\n");
            }
            if (uID.equalsIgnoreCase("923122")) {
                dList.append("<option value=\"").append("is-really-hot.tk").append("\">").append("is-really-hot.tk").append("</option>\n");
            }
            response = response.replace("%inviteduser", ConfigHost.getUserDB().getInviterName(uID)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%currentdomain", ConfigHost.getUserDB().getDomain(uID)).replace("%count", ConfigHost.getInvites().getCount(uID) + "").replace("%urltype", ConfigHost.getUserDB().getUrlType(uID)).replace("%invisurl", ConfigHost.getUserDB().getInvisUrl(uID)).replace("%ut", ConfigHost.getUserDB().getUrlType(uID).toLowerCase().replace("long", "checked").replace("short", "")).replace("%invu", ConfigHost.getUserDB().getInvisUrl(uID).toLowerCase().replace("true", "checked").replace("false", "")).replace("%emb", ConfigHost.getUserDB().hasEmbed(uID) ? "checked" : "").replace("%remb", ConfigHost.getUserDB().hasRandomEmbed(uID) ? "checked" : "").replace("%hex", ConfigHost.getUserDB().getEmbedColor(uID)).replace("%etext", ConfigHost.getUserDB().getEmbedTitle(uID)).replace("%edesc", ConfigHost.getUserDB().getEmbedDesc(uID)).replace("%domains", dList.toString());
        }
        else if (page.equalsIgnoreCase("domains")) {
            final StringBuilder dList = new StringBuilder();
            for (final String dm : ConfigHost.domainList) {
                dList.append(dm).append("<br>");
            }
            response = response.replace("%domains", dList.substring(0, dList.length() - 4));
        }
        ConfigHost.getUserDB().setIP(uID, this.getIp(ex));
        ConfigHost.getUserDB().setCountry(uID, this.getCountry(ex));
        this.send(response, ex);
    }
}
