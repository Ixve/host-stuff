package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;
import java.nio.charset.*;
import java.io.*;
import org.apache.commons.lang3.*;
import java.util.stream.*;
import de.aggromc.confighubhost.utils.*;
import org.javacord.api.entity.message.embed.*;
import java.sql.*;
import java.util.*;

public class Api extends AdvancedWebHandler
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
        final String query = ex.getRequestURI().getPath();
        final String uID = this.getuID(ex);
        if (query.equalsIgnoreCase("/api/resetSecret")) {
            ConfigHost.getUserDB().regenerateToken(uID);
            final String response = "Secret reset.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/domainShort")) {
            ConfigHost.getUserDB().setUrlType(uID, "short");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/domainLong")) {
            ConfigHost.getUserDB().setUrlType(uID, "long");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/wipeImages")) {
            final String response = "Images wiped";
            try {
                final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from images WHERE userID=?");
                pst.setString(1, uID);
                final ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ConfigHost.getImageDB().deleteImage(rs.getString("imageID"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/downloadConfig")) {
            final String response = FileUtils.readRaw("ConfigHub.HOST.sxcu").replace("%secret", ConfigHost.getUserDB().getUploadToken(uID));
            ex.getResponseHeaders().set("Content-Type", "application/octet-stream");
            ex.getResponseHeaders().set("Content-Disposition", "attachment; filename=ConfigHub.HOST.sxcu");
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/invisEnable")) {
            ConfigHost.getUserDB().setInvis(uID, "true");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/invisDisable")) {
            ConfigHost.getUserDB().setInvis(uID, "false");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/embedEnable")) {
            ConfigHost.getUserDB().setEmbed(uID, "true");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/embedDisable")) {
            ConfigHost.getUserDB().setEmbed(uID, "false");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/rembedEnable")) {
            ConfigHost.getUserDB().setRandomEmbed(uID, "true");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/rembedDisable")) {
            ConfigHost.getUserDB().setRandomEmbed(uID, "false");
            final String response = "Value changed.";
            this.send(response, ex);
        }
        else if (query.equalsIgnoreCase("/api/setHex")) {
            final String quere = ex.getRequestURI().getQuery();
            if (quere == null || quere.isEmpty() || quere.equalsIgnoreCase("hex=") || !quere.startsWith("hex=")) {
                final String response2 = "Bad request";
                this.send(401, response2, ex);
                return;
            }
            final String col = quere.substring(4);
            if (col.length() != 6) {
                final String response3 = "Bad color code";
                this.send(401, response3, ex);
                return;
            }
            try {
                ConfigHost.getUserDB().setHex(uID, "#" + col);
                final String response3 = "Hex set.";
                this.send(response3, ex);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        else if (query.equalsIgnoreCase("/api/changeSettings")) {
            if (ex.getRequestMethod().equalsIgnoreCase("POST")) {
                final InputStreamReader isr = new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8);
                final BufferedReader br = new BufferedReader(isr);
                final StringBuilder buf = new StringBuilder(512);
                int b;
                while ((b = br.read()) != -1) {
                    buf.append((char)b);
                }
                br.close();
                isr.close();
                final String ans = buf.toString();
                if (!ans.contains("\r\n") || !ans.contains("edesc=") || !ans.contains("etitle=") || !ans.contains("ecolor=")) {
                    final String response4 = "Malformed POST Data";
                    this.send(400, response4, ex);
                    return;
                }
                final String[] array = ans.split("\r\n");
                String hexc = "";
                String et = "";
                String ed = "";
                String subdomain = "";
                String domain = "";
                boolean urltype = false;
                boolean invisurl = false;
                boolean rembed = false;
                boolean embeds = false;
                for (final String asd : array) {
                    if (asd.startsWith("ecolor=")) {
                        hexc = asd.substring(7);
                    }
                    else if (asd.startsWith("etitle=")) {
                        et = asd.substring(7);
                    }
                    else if (asd.startsWith("edesc=")) {
                        ed = asd.substring(6);
                    }
                    else if (asd.startsWith("subdomain=")) {
                        subdomain = asd.substring(10);
                    }
                    else if (asd.startsWith("domain=")) {
                        domain = asd.substring(7);
                    }
                    else if (asd.startsWith("urltype=")) {
                        urltype = true;
                    }
                    else if (asd.startsWith("invisurl=")) {
                        invisurl = true;
                    }
                    else if (asd.startsWith("rembed=")) {
                        rembed = true;
                    }
                    else if (asd.startsWith("embeds=")) {
                        embeds = true;
                    }
                }
                if (urltype) {
                    ConfigHost.getUserDB().setUrlType(uID, "Long");
                }
                else {
                    ConfigHost.getUserDB().setUrlType(uID, "Short");
                }
                if (invisurl) {
                    ConfigHost.getUserDB().setInvis(uID, "true");
                }
                else {
                    ConfigHost.getUserDB().setInvis(uID, "false");
                }
                if (rembed) {
                    ConfigHost.getUserDB().setRandomEmbed(uID, "true");
                }
                else {
                    ConfigHost.getUserDB().setRandomEmbed(uID, "false");
                }
                if (embeds) {
                    ConfigHost.getUserDB().setEmbed(uID, "true");
                }
                else {
                    ConfigHost.getUserDB().setEmbed(uID, "false");
                }
                if (!et.isEmpty()) {
                    et = StringEscapeUtils.escapeHtml4(et);
                    ConfigHost.getUserDB().setEmbedTitle(uID, et);
                }
                if (!ed.isEmpty()) {
                    ed = StringEscapeUtils.escapeHtml4(ed);
                    ConfigHost.getUserDB().setEmbedDesc(uID, ed);
                }
                if (!subdomain.isEmpty()) {
                    if (subdomain == null || subdomain.isEmpty()) {
                        if (ConfigHost.domainList.contains(domain)) {
                            ConfigHost.getUserDB().setDomain(uID, domain);
                        }
                    }
                    else if (ConfigHost.domainList.contains(domain)) {
                        subdomain = StringEscapeUtils.escapeHtml4(subdomain);
                        final Set<Character> filter = new HashSet<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_', '-', '.'));
                        subdomain = subdomain.chars().filter(i -> filter.contains((char)i)).mapToObj(i -> "" + (char)i).collect((Collector<? super Object, ?, String>)Collectors.joining());
                        ConfigHost.getUserDB().setDomain(uID, subdomain + "." + domain);
                    }
                }
                else if (!domain.isEmpty() && ConfigHost.domainList.contains(domain)) {
                    ConfigHost.getUserDB().setDomain(uID, domain);
                }
                if (!hexc.isEmpty()) {
                    ConfigHost.getUserDB().setHex(uID, hexc);
                }
                final String finalDomain;
                domain = (finalDomain = ConfigHost.getUserDB().getDomain(uID));
                final boolean finalInvisurl = invisurl;
                final String uid;
                ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.SETTINGS, new EmbedBuilder().setTitle("Settings Changed").setDescription("User " + uid + " changed his Settings").setAuthor(ConfigHost.getUserDB().getName(uid), "https://confighub.host/admin?usermanage=" + uid, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").addField("Domain", finalDomain).addField("Invis", finalInvisurl + "")));
                this.send(FileUtils.getFileContents("hidden/redirect").replace("%url", "https://confighub.host/panel?tab=settings"), ex);
            }
            else {
                final String response = "Method not allowed";
                this.send(403, response, ex);
            }
        }
        else {
            final String response = "Bad request";
            this.send(401, response, ex);
        }
    }
}
