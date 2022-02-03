package de.aggromc.confighubhost.auth;

import java.time.format.*;
import java.time.*;
import de.aggromc.confighubhost.*;
import java.time.temporal.*;
import java.util.logging.*;
import java.util.concurrent.atomic.*;
import java.sql.*;
import java.io.*;

public class ImageDB
{
    public void addImage(final String userID, final String id, final String loc, final String type, final String invisUrl) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("INSERT INTO images(userID, imageID, imageLocation, imageType, uploadedAt, invisLink) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setString(1, userID);
            pst.setString(2, id);
            pst.setString(3, loc);
            pst.setString(4, type);
            pst.setString(5, dtf.format(now));
            pst.setString(6, invisUrl);
            pst.execute();
            ConfigHost.getLogger().log(Level.INFO, "Image '" + id + "' was created.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getImageType(final String id) {
        final AtomicReference<String> type = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from images WHERE imageID=?");
            pst.setString(1, id);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                type.set(rs.getString("imageType"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return type.get();
    }
    
    public boolean imageExists(final String id) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM images WHERE imageID=?");
            pst.setString(1, id);
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
    
    public String getImageLoc(final String id) {
        final AtomicReference<String> type = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM images WHERE imageID=?");
            pst.setString(1, id);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                type.set(rs.getString("imageLocation"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return type.get();
    }
    
    public String getImageOwner(final String id) {
        final AtomicReference<String> type = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM images WHERE imageID=?");
            pst.setString(1, id);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                type.set(rs.getString("userID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return type.get();
    }
    
    public void deleteImage(final String id) {
        final File img = new File(this.getImageLoc(id));
        img.delete();
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("DELETE FROM images WHERE imageID=?");
            pst.setString(1, id);
            pst.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getUploads(final String uID) {
        final AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT COUNT(imageID) FROM images WHERE userID=?");
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
    
    public boolean invisExists(final String invis) {
        final AtomicReference<Boolean> exists = new AtomicReference<Boolean>(false);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM images WHERE invisLink=?");
            pst.setString(1, invis);
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
    
    public String getForInvis(final String substring) {
        final AtomicReference<String> type = new AtomicReference<String>(null);
        try {
            final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * FROM images WHERE invisLink=?");
            pst.setString(1, substring);
            final ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                type.set(rs.getString("imageID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return type.get();
    }
}
