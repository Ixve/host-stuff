package de.aggromc.confighubhost.webserver.utils;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.io.*;
import de.aggromc.confighubhost.*;
import java.util.logging.*;
import de.aggromc.confighubhost.webserver.webhandlers.impl.*;
import java.util.*;
import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.auth.*;

public class WebManager extends AdvancedWebHandler
{
    public WebManager() {
        this.load();
    }
    
    public void load() {
        final File dir = new File("html/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (!file.isDirectory()) {
                    final String name = file.getName();
                    if (name.equalsIgnoreCase("register") || name.equalsIgnoreCase("login") || name.equalsIgnoreCase("register_success") || name.equalsIgnoreCase("panel")) {
                        ConfigHost.getLogger().log(Level.FINE, "Didnt Register default Webpage '" + file.getName() + "' because its handled otherwise");
                    }
                    else {
                        ConfigHost.getWebServer().getServer().createContext("/" + file.getName(), new PathHandler());
                        ConfigHost.getLogger().log(Level.FINE, "Registering default Webpage '" + file.getName() + "' to normal Path Handler...");
                    }
                }
            }
        }
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Panel...");
        ConfigHost.getWebServer().getServer().createContext("/panel", new Panel());
        ConfigHost.getWebServer().getServer().createContext("/logout", new Logout());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Login...");
        ConfigHost.getWebServer().getServer().createContext("/login", new Login());
        ConfigHost.getWebServer().getServer().createContext("/login_post", new LoginForm());
        ConfigHost.getWebServer().getServer().createContext("/login_error", new LoginError());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Register...");
        ConfigHost.getWebServer().getServer().createContext("/register", new Register());
        ConfigHost.getWebServer().getServer().createContext("/register_post", new RegisterForm());
        ConfigHost.getWebServer().getServer().createContext("/register_error", new RegisterError());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Upload...");
        ConfigHost.getWebServer().getServer().createContext("/upload", new Upload());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module ShowImage...");
        ConfigHost.getWebServer().getServer().createContext("/api", new Api());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Api...");
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Status...");
        ConfigHost.getWebServer().getServer().createContext("/status", new Status());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module ShowImage...");
        ConfigHost.getWebServer().getServer().createContext("/showimage", new ShowImage());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module ShowRaw...");
        ConfigHost.getWebServer().getServer().createContext("/showraw", new ShowRaw());
        ConfigHost.getWebServer().getServer().createContext("/showraw.gif", new ShowRaw());
        ConfigHost.getWebServer().getServer().createContext("/showraw.png", new ShowRaw());
        ConfigHost.getWebServer().getServer().createContext("/showraw.jpg", new ShowRaw());
        ConfigHost.getWebServer().getServer().createContext("/showraw.mp4", new ShowRaw());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module Delete...");
        ConfigHost.getWebServer().getServer().createContext("/delete", new Delete());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module URL...");
        ConfigHost.getWebServer().getServer().createContext("/url", new URL());
        ConfigHost.getLogger().log(Level.FINE, "Registering Module OAuth...");
        ConfigHost.getWebServer().getServer().createContext("/oauth", new OAuth());
        ConfigHost.getLogger().log(Level.FINE, "Registering Default Handler...");
        ConfigHost.getWebServer().getServer().createContext("/", this);
        ConfigHost.getLogger().log(Level.FINE, "Successfully registered all Modules!");
    }
    
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
        if (ex.getRequestURI().getPath().equalsIgnoreCase("/")) {
            final String response = FileUtils.getFileContents("index");
            ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=21600; immutable;");
            this.send(response, ex);
            return;
        }
        final String equri = ex.getRequestURI().getPath();
        if (equri.startsWith("/s")) {
            if (equri.length() == 12) {
                final String img = equri.substring(2);
                if (ConfigHost.getImageDB().imageExists(img)) {
                    if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("mp4")) {
                        String response2 = FileUtils.getFileContents(ConfigHost.getUserDB().hasEmbed(ConfigHost.getImageDB().getImageOwner(img)) ? "hidden/showvid" : "hidden/showvid_noemb");
                        response2 = response2.replaceAll("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
                        response2 = response2.replace("%uploader", ConfigHost.getUserDB().getName(ConfigHost.getImageDB().getImageOwner(img)));
                        response2 = response2.replace("%title", img + "." + ConfigHost.getImageDB().getImageType(img)).replace("%hex", ConfigHost.getUserDB().getEmbedColor(ConfigHost.getImageDB().getImageOwner(img)));
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
                        ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=21600; immutable;");
                        this.send(response2, ex);
                        return;
                    }
                    String response2 = FileUtils.getFileContents(ConfigHost.getUserDB().hasEmbed(ConfigHost.getImageDB().getImageOwner(img)) ? "hidden/showpic" : "hidden/showpic_noemb");
                    final String uID = ConfigHost.getImageDB().getImageOwner(img);
                    response2 = response2.replace("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
                    response2 = response2.replace("%uploader", ConfigHost.getUserDB().getName(uID));
                    response2 = response2.replace("%title", ConfigHost.getUserDB().getEmbedTitle(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID))).replace("%hex", ConfigHost.getUserDB().getEmbedColor(ConfigHost.getImageDB().getImageOwner(img)));
                    response2 = response2.replace("%edesc", ConfigHost.getUserDB().getEmbedDesc(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID)));
                    final UserDB.Rank rank2 = ConfigHost.getUserDB().getRank(ConfigHost.getImageDB().getImageOwner(img));
                    if (rank2 == UserDB.Rank.BETA) {
                        response2 = response2.replace("%badge", "<i id=\"kek\" class=\"fas fa-flask\"></i>");
                    }
                    else if (rank2 == UserDB.Rank.ADMIN || rank2 == UserDB.Rank.MOD) {
                        response2 = response2.replace("%badge", "<i id=\"kek\" class=\"far fa-check-circle\"></i>");
                    }
                    else {
                        response2 = response2.replace("%badge", "");
                    }
                    ex.getResponseHeaders().set("Cache-Control", "public; only-if-cached; max-age=21600; immutable;");
                    this.send(response2, ex);
                    return;
                }
            }
        }
        else if (equri.length() > 10 && ConfigHost.getImageDB().invisExists(equri.substring(1))) {
            final String img = ConfigHost.getImageDB().getForInvis(equri.substring(1));
            if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("url")) {
                final String resp = FileUtils.getFileContents("hidden/empty").replace("content", "<meta property=\"og:url\" content=\"" + ConfigHost.getImageDB().getImageLoc(img) + "\" />");
                ex.getResponseHeaders().set("Location", ConfigHost.getImageDB().getImageLoc(img));
                this.sendNC(301, resp, ex);
                return;
            }
            if (ConfigHost.getImageDB().getImageType(img).equalsIgnoreCase("mp4")) {
                String response2 = FileUtils.getFileContents(ConfigHost.getUserDB().hasEmbed(ConfigHost.getImageDB().getImageOwner(img)) ? "hidden/showvid" : "hidden/showvid_noemb");
                response2 = response2.replaceAll("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
                response2 = response2.replace("%uploader", ConfigHost.getUserDB().getName(ConfigHost.getImageDB().getImageOwner(img)));
                response2 = response2.replace("%title", img + "." + ConfigHost.getImageDB().getImageType(img)).replace("%hex", ConfigHost.getUserDB().getEmbedColor(ConfigHost.getImageDB().getImageOwner(img)));
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
                return;
            }
            String response2 = FileUtils.getFileContents(ConfigHost.getUserDB().hasEmbed(ConfigHost.getImageDB().getImageOwner(img)) ? "hidden/showpic" : "hidden/showpic_noemb");
            final String uID = ConfigHost.getImageDB().getImageOwner(img);
            response2 = response2.replace("%url", "https://confighub.host/showraw." + ConfigHost.getImageDB().getImageType(img) + "?key=" + img);
            response2 = response2.replace("%uploader", ConfigHost.getUserDB().getName(uID));
            response2 = response2.replace("%title", ConfigHost.getUserDB().getEmbedTitle(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID))).replace("%hex", ConfigHost.getUserDB().getEmbedColor(ConfigHost.getImageDB().getImageOwner(img)));
            response2 = response2.replace("%edesc", ConfigHost.getUserDB().getEmbedDesc(uID).replace("%imagename", img).replace("%imagetype", ConfigHost.getImageDB().getImageType(img)).replace("%username", ConfigHost.getUserDB().getName(uID)).replace("%domain", ConfigHost.getUserDB().getDomain(uID)));
            final UserDB.Rank rank2 = ConfigHost.getUserDB().getRank(uID);
            if (rank2 == UserDB.Rank.BETA) {
                response2 = response2.replace("%badge", "<i id=\"kek\" class=\"fas fa-flask\"></i>");
            }
            else if (rank2 == UserDB.Rank.ADMIN || rank2 == UserDB.Rank.MOD) {
                response2 = response2.replace("%badge", "<i id=\"kek\" class=\"far fa-check-circle\"></i>");
            }
            else {
                response2 = response2.replace("%badge", "");
            }
            this.send(response2, ex);
            return;
        }
        final String response3 = FileUtils.getFileContents("hidden/404");
        this.send(404, response3, ex);
    }
}
