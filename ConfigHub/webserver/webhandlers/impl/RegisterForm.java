package de.aggromc.confighubhost.webserver.webhandlers.impl;

import de.aggromc.confighubhost.webserver.webhandlers.*;
import com.sun.net.httpserver.*;
import java.nio.charset.*;
import java.io.*;
import java.util.stream.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;
import org.javacord.api.entity.message.embed.*;
import java.awt.*;
import de.aggromc.confighubhost.utils.*;
import de.aggromc.confighubhost.auth.*;
import java.util.*;

public class RegisterForm extends AdvancedWebHandler
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
        if (!ans.contains("\r\n") || !ans.contains("username=") || !ans.contains("pw1=") || !ans.contains("pw2=") || !ans.contains("invite=")) {
            final String response = "Malformed POST Data";
            this.send(400, response, ex);
        }
        final String[] array = ans.split("\r\n");
        String username = array[0].replace("username=", "");
        final String pw1 = array[1].replace("pw1=", "");
        final String pw2 = array[2].replace("pw2=", "");
        final String invite = array[3].replace("invite=", "");
        String respFile = "";
        boolean lock = false;
        final Set<Character> filter = new HashSet<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        username = username.chars().filter(i -> filter.contains((char)i)).mapToObj(i -> "" + (char)i).collect((Collector<? super Object, ?, String>)Collectors.joining());
        if (username.length() < 3 || username.length() > 20) {
            respFile = "unamelength";
            lock = true;
        }
        if (!pw1.equals(pw2) && !lock) {
            respFile = "pwident";
            lock = true;
        }
        if ((pw1.length() < 6 || pw1.length() > 32) && !lock) {
            respFile = "pwlength";
            lock = true;
        }
        if (!ConfigHost.getInvites().inviteExists(invite) && !lock) {
            respFile = "invite";
            lock = true;
        }
        if (lock) {
            String response2 = FileUtils.getFileContents("hidden/redirect");
            response2 = response2.replace("%url", "https://confighub.host/register_error?e=" + respFile);
            final String finalRespFile = respFile;
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.REGISTER, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Registration failed").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("Device failed to register: " + finalRespFile)));
            this.send(response2, ex);
            return;
        }
        RandomString rn;
        String uploadToken;
        for (rn = new RandomString(20), uploadToken = rn.nextString(); ConfigHost.getUserDB().uploadTokenExists(uploadToken); uploadToken = rn.nextString()) {}
        String userID;
        for (userID = NumberString.getRandomNumberString(); ConfigHost.getUserDB().userExists(userID); userID = NumberString.getRandomNumberString()) {}
        if (ConfigHost.getUserDB().userNameExists(username)) {
            this.send("This Username is already in Use!", ex);
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.REGISTER, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Registration failed").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).setDescription("Device failed to register: Username already in Use")));
            return;
        }
        if (ConfigHost.getUserDB().ipExists(this.getIp(ex))) {
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.ALERTS, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Multiaccount").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).addField("Username", ConfigHost.getUserDB().getNameByIP(this.getIp(ex))).setDescription("The User tried to create another Account.")));
            this.send(FileUtils.getFileContents("hidden/redirect").replace("%url", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"), ex);
            ConfigHost.getInvites().deleteInvite(invite);
            return;
        }
        ConfigHost.getUserDB().createUser(username, pw1, uploadToken, userID, UserDB.Rank.USER, ConfigHost.getInvites().getOwner(invite), this.getIp(ex), this.getCountry(ex));
        ConfigHost.getInvites().deleteInvite(invite);
        final String finalUsername = username;
        ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.REGISTER, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Register successful").addField("IP", this.getIp(ex), true).addField("Country", this.getCountry(ex), true).addField("Username", finalUsername).setDescription("The User successfully registered.")));
        String response3 = FileUtils.getFileContents("hidden/register_success");
        response3 = response3.replace("%text", "Your Account was successfully created! Username: " + username);
        this.send(response3, ex);
    }
}
