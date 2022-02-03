package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import java.nio.charset.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.utils.*;
import org.javacord.api.entity.message.embed.*;
import java.awt.*;
import de.aggromc.confighubhost.webserver.utils.*;
import com.sun.net.httpserver.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class LoginForm extends AdvancedWebHandler
{
    @Override
    public ArrayList<String> getAvailableMethods() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("POST");
        return list;
    }
    
    @Override
    public boolean needsVerification() {
        return false;
    }
    
    @Override
    public void handleRequest(final HttpExchange ex) throws Exception {
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
        if (!ans.contains("\r\n") || !ans.contains("username=") || !ans.contains("pw=") || !ans.contains("h-captcha-response=")) {
            final String response = "Malformed POST Data";
            this.send(400, response, ex);
            return;
        }
        final String[] array = ans.split("\r\n");
        final String uname = array[0].replace("username=", "");
        final String pw = array[1].replace("pw=", "");
        String key;
        if (ans.contains("redir=")) {
            key = array[4].replace("h-captcha-response=", "");
        }
        else {
            key = array[3].replace("h-captcha-response=", "");
        }
        if (!this.hcaptchaRight(key, ex)) {
            this.send(403, "You did not complete the Captcha!", ex);
            return;
        }
        if (ConfigHost.getUserDB().userNameExists(uname)) {
            if (ConfigHost.getUserDB().credentialsRight(uname, pw)) {
                final Headers respHeaders = ex.getResponseHeaders();
                final List<String> values = new ArrayList<String>();
                if (ConfigHost.getUserDB().isBanned(ConfigHost.getUserDB().getUserIdByName(uname))) {
                    final String s;
                    ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.LOGIN, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(ConfigHost.getUserDB().getUserIdByName(s)), "https://confighub.host/admin?usermanage=" + ConfigHost.getUserDB().getUserIdByName(s), "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Banned User Tried to Login").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("User **" + s + "** tried to login but is banned.")));
                    this.send(403, "Your account is banned", ex);
                    return;
                }
                final String uid = ConfigHost.getLoginManager().create(ConfigHost.getUserDB().getUserIdByName(uname));
                values.add("USERID=" + uid + ";");
                respHeaders.put("Set-Cookie", values);
                if (!ConfigHost.getUserDB().hasNoIP(uid)) {
                    ConfigHost.getUserDB().setIP(uid, this.getIp(ex));
                    ConfigHost.getUserDB().setCountry(uid, this.getCountry(ex));
                }
                String response2 = FileUtils.getFileContents("hidden/redirect");
                if (ans.contains("redir=")) {
                    final String red = array[2].replace("redir=", "");
                    response2 = response2.replace("%url", "https://confighub.host" + red);
                }
                else {
                    response2 = response2.replace("%url", "https://confighub.host/panel");
                }
                final String s2;
                ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.LOGIN, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(ConfigHost.getUserDB().getUserIdByName(s2)), "https://confighub.host/admin?usermanage=" + ConfigHost.getUserDB().getUserIdByName(s2), "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Successful Login").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("User **" + s2 + "** logged in successfully.")));
                this.send(response2, ex);
            }
            else {
                final String s3;
                ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.LOGIN, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setAuthor(ConfigHost.getUserDB().getName(ConfigHost.getUserDB().getUserIdByName(s3)), "https://confighub.host/admin?usermanage=" + ConfigHost.getUserDB().getUserIdByName(s3), "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Login-Attempt failed").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("Device failed to login because of a wrong password.")));
                String response3 = FileUtils.getFileContents("hidden/redirect");
                response3 = response3.replace("%url", "https://confighub.host/login_error?e=wrongpw");
                this.send(response3, ex);
            }
        }
        else {
            final String s4;
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.LOGIN, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setAuthor(ConfigHost.getUserDB().getName(ConfigHost.getUserDB().getUserIdByName(s4)), "https://confighub.host/admin?usermanage=" + ConfigHost.getUserDB().getUserIdByName(s4), "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Login-Attempt failed").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("Device failed to login because of a non-existant account.")));
            String response3 = FileUtils.getFileContents("hidden/redirect");
            response3 = response3.replace("%url", "https://confighub.host/login_error?e=acc");
            this.send(response3, ex);
        }
    }
    
    private boolean hcaptchaRight(final String key, final HttpExchange ex) {
        final String urlParameters = "sitekey=4896c204-9e7f-4a4d-970e-de655c69e221&secret=0x24416D4C657A42f8106760261a37821693565d8a&remoteip=" + this.getIp(ex) + "&response=" + key;
        try {
            System.out.println(urlParameters);
            final URL url = new URL("https://hcaptcha.com/siteverify");
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + urlParameters.getBytes().length);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            final DataInputStream in = new DataInputStream(connection.getInputStream());
            final int bufferSize = 1024;
            final char[] buffer = new char[1024];
            final StringBuilder out = new StringBuilder();
            final Reader inr = new InputStreamReader(in, StandardCharsets.UTF_8);
            int charsRead;
            while ((charsRead = inr.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, charsRead);
            }
            System.out.println(out.toString());
            return out.toString().contains("\"success\":true");
        }
        catch (Exception e) {
            return false;
        }
    }
}
