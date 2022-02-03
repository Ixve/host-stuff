package de.aggromc.confighubhost.webserver.utils;

import java.io.*;

public class FileUtils
{
    public static boolean imageExists(final String pathToImage) {
        System.out.println("html/img/" + pathToImage);
        final File f = new File("html/img/" + pathToImage);
        return f.exists();
    }
    
    public static String getFileContents(final String file) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader("html/" + file));
            final StringBuilder sb = new StringBuilder();
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                sb.append(l);
                sb.append(System.lineSeparator());
            }
            br.close();
            return sb.toString();
        }
        catch (Exception e) {
            return getFileContents("index");
        }
    }
    
    public static String getFiles(final String file) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(file));
            final StringBuilder sb = new StringBuilder();
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                sb.append(l);
                sb.append(System.lineSeparator());
            }
            br.close();
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static String getContentType(final String typ) {
        switch (typ) {
            case "png": {
                return "image/png";
            }
            case "gif": {
                return "image/gif";
            }
            case "jpg": {
                return "image/jpeg";
            }
            case "mp4": {
                return "video/mp4";
            }
            default: {
                return "text/html";
            }
        }
    }
    
    public static String readRaw(final String s) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader("raw/" + s));
            final StringBuilder sb = new StringBuilder();
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                sb.append(l);
                sb.append(System.lineSeparator());
            }
            br.close();
            return sb.toString();
        }
        catch (Exception e) {
            return getFileContents("index");
        }
    }
}
