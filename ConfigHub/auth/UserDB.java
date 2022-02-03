package de.aggromc.confighubhost.auth;

import de.aggromc.confighubhost.*;
import java.util.concurrent.atomic.*;
import java.sql.*;
import java.util.*;
import java.time.format.*;
import java.time.*;
import java.time.temporal.*;
import java.util.logging.*;
import de.aggromc.confighubhost.utils.*;

public class UserDB
{
    private final MySQL mySQL;
    
    public UserDB() {
        this.mySQL = ConfigHost.getMySQL();
    }
    
    public boolean uploadTokenExists(final String upKey) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from users WHERE uploadToken=?");
            pst.setString(1, upKey);
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
    
    public boolean hasRandomEmbed(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(rs.getString("randomembed").equalsIgnoreCase("true"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public boolean hasEmbed(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(rs.getString("embeds").equalsIgnoreCase("true"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public boolean userNameExists(final String uname) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE name=?");
            pst.setString(1, uname);
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
    
    private String randomEmbed() {
        final String zeros = "000000";
        final Random rnd = new Random();
        String s = Integer.toString(rnd.nextInt(16777216), 16);
        s = zeros.substring(s.length()) + s;
        return s;
    }
    
    public boolean credentialsRight(final String uname, final String pw) {
        final AtomicReference<Boolean> right = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE name=?");
            pst.setString(1, uname);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                right.set(rs.getString("password").equals(pw));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return right.get();
    }
    
    public String getUserIdByName(final String name) {
        final AtomicReference<String> id = new AtomicReference<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE name=?");
            pst.setString(1, name);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id.set(rs.getString("userID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id.get();
    }
    
    public String getUserIdByToken(final String token) {
        final AtomicReference<String> id = new AtomicReference<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE uploadToken=?");
            pst.setString(1, token);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id.set(rs.getString("userID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id.get();
    }
    
    public int getUsers() {
        final AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users");
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                count.set(count.get() + 1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return count.get();
    }
    
    public String getInviter(final String uID) {
        final AtomicReference<String> id = new AtomicReference<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id.set(rs.getString("inviter"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id.get();
    }
    
    public String getInviterName(final String uID) {
        return this.getName(this.getInviter(uID));
    }
    
    public int getInvitedUsersCount(final String uID) {
        final AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT COUNT(*) FROM users WHERE inviter=?");
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
    
    public String getEmbedColor(final String uID) {
        if (this.hasRandomEmbed(uID)) {
            return "#" + this.randomEmbed();
        }
        return this.rawEmbedColor(uID);
    }
    
    private String rawEmbedColor(final String uID) {
        final AtomicReference<String> id = new AtomicReference<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id.set(rs.getString("embedcolor"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return id.get();
    }
    
    public String getRegisteredAt(final String uID) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("registeredAt"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public String getEmbedTitle(final String uID) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("embedtitle"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public String getEmbedDesc(final String uID) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("embedmessage"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public String getUrlType(final String uID) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("urltype"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public String getInvisUrl(final String uID) {
        final AtomicReference<String> reason = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reason.set(rs.getString("invisurl"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reason.get();
    }
    
    public boolean usesInvisUrl(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(rs.getString("invisurl").equalsIgnoreCase("true"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public void setUrlType(final String uID, final String mode) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET urltype=? WHERE userID=?");
            pst.setString(1, mode);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setInvis(final String uID, final String mode) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET invisurl=? WHERE userID=?");
            pst.setString(1, mode);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setHex(final String uID, final String toHexString) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET embedcolor=? WHERE userID=?");
            pst.setString(1, toHexString);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setEmbed(final String uID, final String mode) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET embeds=? WHERE userID=?");
            pst.setString(1, mode);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setRandomEmbed(final String uID, final String mode) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET randomembed=? WHERE userID=?");
            pst.setString(1, mode);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasLongUrl(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(rs.getString("urltype").equalsIgnoreCase("long"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public ArrayList<String> getUsersList() {
        final ArrayList<String> ret = new ArrayList<String>();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users");
            final ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("userID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public boolean isLinked(final String getuID) {
        return false;
    }
    
    public void setDiscord(final String accessToken, final String refreshToken) {
    }
    
    public void setEmbedTitle(final String uID, final String et) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET embedtitle=? WHERE userID=?");
            pst.setString(1, et);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setEmbedDesc(final String uID, final String ed) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET embedmessage=? WHERE userID=?");
            pst.setString(1, ed);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean ipExists(final String ip) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE ip=?");
            pst.setString(1, ip);
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
    
    public boolean hasNoIP(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                exists.set(rs.getString("ip").isEmpty());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exists.get();
    }
    
    public void setIP(final String uid, final String ip) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET ip=? WHERE userID=?");
            pst.setString(1, ip);
            pst.setString(2, uid);
            pst.execute();
            System.out.println("exe");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setCountry(final String uid, final String ip) {
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("UPDATE users SET country=? WHERE userID=?");
            pst.setString(1, ip);
            pst.setString(2, uid);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getNameByIP(final String ip) {
        String name = null;
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM users WHERE ip=?");
            pst.setString(1, ip);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
    
    public void createUser(final String name, final String password, final String uploadToken, final String userid, final Rank rank, final String invited_by, final String ip, final String country) {
        if (!this.userExists(userid)) {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            final LocalDateTime now = LocalDateTime.now();
            try {
                final PreparedStatement pst = this.mySQL.connection.prepareStatement("INSERT INTO users(userID, name, password, uploadToken, `rank`, domain, banned, banreason, inviter, urltype, invisurl, embeds, embedtitle, randomembed, embedmessage, registeredAt, embedcolor, ip, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pst.setString(1, userid);
                pst.setString(2, name);
                pst.setString(3, password);
                pst.setString(4, uploadToken);
                pst.setString(5, rank.name());
                pst.setString(6, "confighub.host");
                pst.setString(7, "0");
                pst.setString(8, "null");
                pst.setString(9, invited_by);
                pst.setString(10, "short");
                pst.setString(11, "true");
                pst.setString(12, "true");
                pst.setString(13, "%imagename.%imagetype");
                pst.setString(14, "true");
                pst.setString(15, "Uploader: %username");
                pst.setString(16, dtf.format(now));
                pst.setString(17, "#99AAB5");
                pst.setString(18, ip);
                pst.setString(19, country);
                pst.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ConfigHost.getLogger().log(Level.FINE, "User '" + name + "' was created.");
    }
    
    public void regenerateToken(final String uID) {
        ConfigHost.getLogger().log(Level.INFO, "Token for " + uID + " was regenerated.");
        RandomString r;
        String rnd;
        for (r = new RandomString(20), rnd = r.nextString(); this.uploadTokenExists(rnd); rnd = r.nextString()) {}
        final String finalRnd = rnd;
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("UPDATE users SET uploadToken=? WHERE userID=?");
            pst.setString(1, finalRnd);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteUser(final String uID) {
        ConfigHost.getLogger().log(Level.FINE, "User '" + uID + "' was deleted.");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("DELETE from users WHERE userID=?");
            pst.setString(1, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean userExists(final String uID) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
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
    
    public boolean isBanned(final String uID) {
        final AtomicReference<Boolean> banned = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                banned.set(rs.getBoolean("banned"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return banned.get();
    }
    
    public String getName(final String uID) {
        final AtomicReference<String> name = new AtomicReference<String>("Error");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                name.set(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return name.get();
    }
    
    public String getDomain(final String uID) {
        final AtomicReference<String> domain = new AtomicReference<String>("");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                domain.set(rs.getString("domain"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return domain.get();
    }
    
    public void setDomain(final String uID, final String domain) {
        ConfigHost.getLogger().log(Level.INFO, "User '" + uID + "' changed Domain to " + domain + ".");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("UPDATE users SET domain=? WHERE userID=?");
            pst.setString(1, domain);
            pst.setString(2, uID);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getUploadToken(final String uID) {
        final AtomicReference<String> uploadToken = new AtomicReference<String>("");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                uploadToken.set(rs.getString("uploadToken"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return uploadToken.get();
    }
    
    public Rank getRank(final String uID) {
        final AtomicReference<String> rank = new AtomicReference<String>("");
        try {
            final PreparedStatement pst = this.mySQL.connection.prepareStatement("SELECT * FROM users WHERE userID=?");
            pst.setString(1, uID);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                rank.set(rs.getString("rank"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Rank.valueOf(rank.get());
    }
    
    public enum Rank
    {
        USER, 
        BETA, 
        MOD, 
        ADMIN;
    }
}
