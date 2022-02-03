package de.aggromc.confighubhost.auth;

import java.time.format.*;
import java.time.*;
import de.aggromc.confighubhost.*;
import java.time.temporal.*;
import java.util.logging.*;
import de.aggromc.confighubhost.utils.*;
import org.javacord.api.entity.message.embed.*;
import java.awt.*;
import java.util.concurrent.atomic.*;
import java.sql.*;
import java.util.*;

public class Invites
{
    public String createInvite(final String ownerID) {
        RandomString rnd;
        String str;
        for (rnd = new RandomString(40), str = rnd.nextString(); this.inviteExists(str); str = rnd.nextString()) {}
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("INSERT INTO invites(inviteID, ownerID, createdAT) VALUES (?,?,?)");
            pst.setString(1, str);
            pst.setString(2, ownerID);
            pst.setString(3, dtf.format(now));
            pst.execute();
            ConfigHost.getLogger().log(Level.INFO, "Invite '" + str + "' created.");
            final String finalStr = str;
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.INVITES, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(ownerID), "https://confighub.host/admin?usermanage=" + ownerID, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Invite created").setDescription("Invite `" + finalStr + "` was created.\nOwner: " + ConfigHost.getUserDB().getName(ownerID) + " (" + ownerID + ")")));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    public boolean inviteExists(final String invCode) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from invites WHERE inviteID=?");
            pst.setString(1, invCode);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(true);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public void deleteInvite(final String invCode) {
        try {
            ConfigHost.getLogger().log(Level.FINE, "Invite '" + invCode + "' was deleted.");
            final String ownerID = this.getOwner(invCode);
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("DELETE from invites WHERE inviteID=?");
            pst.setString(1, invCode);
            pst.execute();
            final String s;
            ConfigHost.getDiscord().discordSenders.execute(() -> ConfigHost.getDiscord().send(Channel.INVITES, new EmbedBuilder().setColor(Color.RED).setAuthor(ConfigHost.getUserDB().getName(s), "https://confighub.host/admin?usermanage=" + s, "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setFooter("Made by TeamAggro#9242", "https://raw.githubusercontent.com/NLDev/Discord-Trivia-Cheat/master/.src/icon.png").setTitle("Invite deleted").setDescription("Invite `" + invCode + "` was deleted.\nOwner: " + ConfigHost.getUserDB().getName(s) + " (" + s + ")")));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getOwner(final String invCode) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from invites WHERE inviteID=?");
            pst.setString(1, invCode);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("ownerID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public int getCount(final String uID) {
        final AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT COUNT(*) FROM invites WHERE ownerID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count.set(rs.getInt(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return count.get();
    }
    
    private ArrayList<String> getInvitees(final String uID) {
        final ArrayList<String> invs = new ArrayList<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from invites WHERE ownerID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                invs.add(rs.getString("inviteID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return invs;
    }
    
    public String getInvites(final String uID) {
        final ArrayList<String> invs = this.getInvitees(uID);
        if (invs.isEmpty()) {
            return "<span class=\"text-light\">You don´t have Invites!</span><br>";
        }
        final StringBuilder fin = new StringBuilder();
        for (final String invcode : this.getInvitees(uID)) {
            fin.append("<span class=\"text-light\">").append(invcode).append("</span><br>");
        }
        return fin.toString();
    }
    
    public void createInviteWave() {
        for (final String uID : ConfigHost.getUserDB().getUsersList()) {
            this.createInvite(uID);
        }
    }
}
