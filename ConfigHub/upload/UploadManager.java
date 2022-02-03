package de.aggromc.confighubhost.upload;

import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.webserver.utils.*;
import org.javacord.api.entity.message.embed.*;
import java.awt.*;
import java.security.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import de.aggromc.confighubhost.utils.*;

public class UploadManager
{
    public static String uploadDO(final String uID, final InputStream requestBody, final String ip, final String country, final String imageType) throws Exception {
        final RandomString imgl = new RandomString(25);
        String ye;
        String outPath;
        for (ye = imgl.nextString() + "." + imageType, outPath = "images/" + uID + "/" + ye; ConfigHost.s3.fileExists(outPath); outPath = "images/" + uID + "/" + ye) {
            ye = imgl.nextString() + "." + imageType;
        }
        RandomString imgkey;
        String ikey;
        for (imgkey = new RandomString(10), ikey = imgkey.nextString(); ConfigHost.getImageDB().imageExists(ikey); ikey = imgkey.nextString()) {}
        if (!ConfigHost.s3.uploadFile(uID, outPath, FileUtils.getContentType(imageType), requestBody)) {
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.UPLOADS, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(uID), "https://confighub.host/admin?usermanage=" + uID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Image too big").addField("User", ConfigHost.getUserDB().getName(uID), true).addField("Rank", ConfigHost.getUserDB().getRank(uID).name(), true).addField("IP", ip, true).addField("Country", country, true).setDescription("User tried to upload an Image but its too big")));
            return "noallow";
        }
        requestBody.close();
        String invis = "null";
        if (ConfigHost.getUserDB().usesInvisUrl(uID)) {
            RandomInvis rinvis;
            for (rinvis = new RandomInvis(30, new SecureRandom(), "\u200d,\u200b"), invis = rinvis.nextString(); ConfigHost.getImageDB().invisExists(invis); invis = rinvis.nextString()) {}
            ConfigHost.getImageDB().addImage(uID, ikey, "images/" + uID + "/" + ye, imageType, invis);
        }
        else {
            ConfigHost.getImageDB().addImage(uID, ikey, "images/" + uID + "/" + ye, imageType, "null");
        }
        final String yass = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/s" + ikey;
        final String thumbnailUrl = "https://cdn.confighub.host/images/" + uID + "/" + ye;
        final String delUrl = "https://api.confighub.host/images/delete?key=" + ConfigHost.getUserDB().getUploadToken(uID) + "&img=" + ikey;
        String imageLink;
        String json;
        if (ConfigHost.getUserDB().usesInvisUrl(uID)) {
            imageLink = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + invis;
            json = "{\"imageUrl\":\"https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + invis + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        else if (ConfigHost.getUserDB().hasLongUrl(uID)) {
            imageLink = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/showimage?key=" + ikey;
            json = "{\"imageUrl\":\"https://" + ConfigHost.getUserDB().getDomain(uID) + "/showimage?key=" + ikey + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        else {
            imageLink = yass;
            json = "{\"imageUrl\":\"" + yass + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        final String finalImageLink = imageLink;
        if (!uID.equalsIgnoreCase("133337")) {
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.UPLOADS, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(uID), "https://confighub.host/admin?usermanage=" + uID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Image uploaded").addField("User", ConfigHost.getUserDB().getName(uID), true).addField("Rank", ConfigHost.getUserDB().getRank(uID).name(), true).addField("IP", ip, true).addField("Country", country, true).setImage(thumbnailUrl).addField("Link", finalImageLink, true)));
        }
        return json;
    }
    
    public static String uploadNew(final String uID, final InputStream requestBody, final String ip, final String country, final String imageType) throws Exception {
        final RandomString imgl = new RandomString(25);
        String ye = imgl.nextString() + "." + imageType;
        final File fold = new File("html/img/" + uID);
        if (!fold.exists()) {
            fold.mkdir();
        }
        File outputFile;
        for (outputFile = new File("html/img/" + uID + "/" + ye); outputFile.exists(); outputFile = new File("html/img/" + uID + "/" + ye)) {
            ye = imgl.nextString() + "." + imageType;
        }
        RandomString imgkey;
        String ikey;
        for (imgkey = new RandomString(10), ikey = imgkey.nextString(); ConfigHost.getImageDB().imageExists(ikey); ikey = imgkey.nextString()) {}
        if (!FileUtil.copy(requestBody, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING)) {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.UPLOADS, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(uID), "https://confighub.host/admin?usermanage=" + uID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Image too big").addField("User", ConfigHost.getUserDB().getName(uID), true).addField("Rank", ConfigHost.getUserDB().getRank(uID).name(), true).addField("IP", ip, true).addField("Country", country, true).setDescription("User tried to upload an Image but its too big")));
            return "noallow";
        }
        requestBody.close();
        String invis = "null";
        if (ConfigHost.getUserDB().usesInvisUrl(uID)) {
            RandomInvis rinvis;
            for (rinvis = new RandomInvis(30, new SecureRandom(), "\u200d,\u200b"), invis = rinvis.nextString(); ConfigHost.getImageDB().invisExists(invis); invis = rinvis.nextString()) {}
            ConfigHost.getImageDB().addImage(uID, ikey, "html/img/" + uID + "/" + ye, imageType, invis);
        }
        else {
            ConfigHost.getImageDB().addImage(uID, ikey, "html/img/" + uID + "/" + ye, imageType, "null");
        }
        final String yass = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/s" + ikey;
        final String thumbnailUrl = "https://confighub.host/showraw." + imageType + "?key=" + ikey;
        final String delUrl = "https://confighub.host/delete?key=" + ConfigHost.getUserDB().getUploadToken(uID) + "&img=" + ikey;
        String imageLink;
        String json;
        if (ConfigHost.getUserDB().usesInvisUrl(uID)) {
            imageLink = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + invis;
            json = "{\"imageUrl\":\"https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + invis + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        else if (ConfigHost.getUserDB().hasLongUrl(uID)) {
            imageLink = "https://" + ConfigHost.getUserDB().getDomain(uID) + "/showimage?key=" + ikey;
            json = "{\"imageUrl\":\"https://" + ConfigHost.getUserDB().getDomain(uID) + "/showimage?key=" + ikey + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        else {
            imageLink = yass;
            json = "{\"imageUrl\":\"" + yass + "\",\"thumbnailUrl\":\"" + thumbnailUrl + "\",\"deleteUrl\":\"" + delUrl + "\"}";
        }
        final String finalImageLink = imageLink;
        if (!uID.equalsIgnoreCase("133337")) {
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.UPLOADS, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(uID), "https://confighub.host/admin?usermanage=" + uID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Image uploaded").addField("User", ConfigHost.getUserDB().getName(uID), true).addField("Rank", ConfigHost.getUserDB().getRank(uID).name(), true).addField("IP", ip, true).addField("Country", country, true).setImage(thumbnailUrl).addField("Link", finalImageLink, true)));
        }
        return json;
    }
    
    public static String shorten(final String uID, final String substring, final String ip, final String country) {
        RandomInvis rinvis;
        String invis;
        for (rinvis = new RandomInvis(30, new SecureRandom(), "\u200d,\u200b"), invis = rinvis.nextString(); ConfigHost.getImageDB().invisExists(invis); invis = rinvis.nextString()) {}
        RandomString imgkey;
        String ikey;
        for (imgkey = new RandomString(10), ikey = imgkey.nextString(); ConfigHost.getImageDB().imageExists(ikey); ikey = imgkey.nextString()) {}
        ConfigHost.getImageDB().addImage(uID, ikey, substring, "url", invis);
        final String delUrl = "https://confighub.host/delete?key=" + ConfigHost.getUserDB().getUploadToken(uID) + "&img=" + ikey;
        final String finalInvis = invis;
        ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.UPLOADS, new EmbedBuilder().setColor(Color.RED).setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Url shortened").setAuthor(ConfigHost.getUserDB().getName(uID), "https://confighub.host/admin?usermanage=" + uID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").addField("User", ConfigHost.getUserDB().getName(uID), true).addField("Rank", ConfigHost.getUserDB().getRank(uID).name(), true).addField("IP", ip, true).addField("Country", country, true).addField("Link", "https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + finalInvis, true)));
        return "{\"shortUrl\":\"https://" + ConfigHost.getUserDB().getDomain(uID) + "/" + invis + "\",\"deleteUrl\":\"" + delUrl + "\"}";
    }
}
