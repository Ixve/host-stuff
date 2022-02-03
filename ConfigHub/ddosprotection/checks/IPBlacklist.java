package de.aggromc.confighubhost.ddosprotection.checks;

import java.nio.file.attribute.*;
import java.time.*;
import java.time.temporal.*;
import java.util.concurrent.*;
import java.nio.file.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
import java.io.*;

public class IPBlacklist
{
    public static ArrayList<String> blocked;
    
    public static void init() {
        System.out.println("Start fetching blocklists...");
        try {
            final Path file = new File("blacklisted.txt").toPath();
            final BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class, new LinkOption[0]);
            final Duration d = Duration.between(attr.creationTime().toInstant(), Instant.now());
            final long secondsElapsed = d.getSeconds();
            final long limit = TimeUnit.HOURS.toSeconds(6L);
            if (secondsElapsed <= limit) {
                System.out.println("[BLOCKLIST] Proxy file is valid, importing...");
                final File f = new File("blacklisted.txt");
                final BufferedReader br = new BufferedReader(new FileReader(f));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    IPBlacklist.blocked.add(line);
                }
                System.out.println("[BLOCKLIST] Loaded " + IPBlacklist.blocked.size() + " Blacklisted IPS.");
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long time;
        final String[] array;
        int length;
        int i = 0;
        String s;
        final String[] array2;
        int length2;
        int j = 0;
        String s2;
        final String[] array3;
        int length3;
        int k = 0;
        String s3;
        final String[] array4;
        int length4;
        int l = 0;
        String s4;
        final String[] array5;
        int length5;
        int n = 0;
        String s5;
        final String[] array6;
        int length6;
        int n2 = 0;
        String s6;
        final String[] array7;
        int length7;
        int n3 = 0;
        String s7;
        final String[] array8;
        int length8;
        int n4 = 0;
        String s8;
        final String[] array9;
        int length9;
        int n5 = 0;
        String s9;
        final String[] array10;
        int length10;
        int n6 = 0;
        String s10;
        final String[] array11;
        int length11;
        int n7 = 0;
        String s11;
        final String[] array12;
        int length12;
        int n8 = 0;
        String s12;
        final String[] array13;
        int length13;
        int n9 = 0;
        String s13;
        final String[] array14;
        int length14;
        int n10 = 0;
        String s14;
        final String[] array15;
        int length15;
        int n11 = 0;
        String s15;
        final String[] array16;
        int length16;
        int n12 = 0;
        String s16;
        final String[] array17;
        int length17;
        int n13 = 0;
        String s17;
        final String[] array18;
        int length18;
        int n14 = 0;
        String s18;
        final String[] array19;
        int length19;
        int n15 = 0;
        String s19;
        final String[] array20;
        int length20;
        int n16 = 0;
        String s20;
        final String[] array21;
        int length21;
        int n17 = 0;
        String s21;
        File f2;
        final BufferedReader bufferedReader;
        BufferedReader br2;
        String line2;
        File f3;
        final PrintWriter printWriter;
        PrintWriter pw;
        final Iterator<String> iterator;
        String s22;
        new Thread(() -> {
            try {
                time = System.currentTimeMillis();
                System.out.println("[BLOCKLIST] Fetching from list 1 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/8.txt");
                for (length = array.length; i < length; ++i) {
                    s = array[i];
                    if (!IPBlacklist.blocked.contains(s)) {
                        IPBlacklist.blocked.add(s);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 2 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/7.txt");
                for (length2 = array2.length; j < length2; ++j) {
                    s2 = array2[j];
                    if (!IPBlacklist.blocked.contains(s2)) {
                        IPBlacklist.blocked.add(s2);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 3 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/6.txt");
                for (length3 = array3.length; k < length3; ++k) {
                    s3 = array3[k];
                    if (!IPBlacklist.blocked.contains(s3)) {
                        IPBlacklist.blocked.add(s3);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 4 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/5.txt");
                for (length4 = array4.length; l < length4; ++l) {
                    s4 = array4[l];
                    if (!IPBlacklist.blocked.contains(s4)) {
                        IPBlacklist.blocked.add(s4);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 5 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/4.txt");
                for (length5 = array5.length; n < length5; ++n) {
                    s5 = array5[n];
                    if (!IPBlacklist.blocked.contains(s5)) {
                        IPBlacklist.blocked.add(s5);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 6 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://raw.githubusercontent.com/stamparm/ipsum/master/levels/3.txt");
                for (length6 = array6.length; n2 < length6; ++n2) {
                    s6 = array6[n2];
                    if (!IPBlacklist.blocked.contains(s6)) {
                        IPBlacklist.blocked.add(s6);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 7 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                try {
                    getForUrl("https://lists.blocklist.de/lists/strongips.txt");
                    for (length7 = array7.length; n3 < length7; ++n3) {
                        s7 = array7[n3];
                        if (!IPBlacklist.blocked.contains(s7)) {
                            IPBlacklist.blocked.add(s7);
                        }
                    }
                }
                catch (Exception e5) {
                    System.out.println("list 7 down :/");
                }
                try {
                    System.out.println("[BLOCKLIST] Fetching from list 8 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                    getForUrl("https://lists.blocklist.de/lists/all.txt");
                    for (length8 = array8.length; n4 < length8; ++n4) {
                        s8 = array8[n4];
                        if (!IPBlacklist.blocked.contains(s8)) {
                            IPBlacklist.blocked.add(s8);
                        }
                    }
                }
                catch (Exception e6) {
                    System.out.println("list 8 down :/");
                }
                System.out.println("[BLOCKLIST] Fetching from list 9 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("http://www.ciarmy.com/list/ci-badguys.txt");
                for (length9 = array9.length; n5 < length9; ++n5) {
                    s9 = array9[n5];
                    if (!IPBlacklist.blocked.contains(s9)) {
                        IPBlacklist.blocked.add(s9);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 10 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("http://reputation.alienvault.io/reputation.generic");
                for (length10 = array10.length; n6 < length10; ++n6) {
                    s10 = array10[n6];
                    if (!IPBlacklist.blocked.contains(s10)) {
                        IPBlacklist.blocked.add(s10);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 11 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://binarydefense.com/banlist.txt");
                for (length11 = array11.length; n7 < length11; ++n7) {
                    s11 = array11[n7];
                    if (!IPBlacklist.blocked.contains(s11) && !s11.startsWith("#") && !s11.startsWith("\n") && !s11.isEmpty()) {
                        IPBlacklist.blocked.add(s11);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 12 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("http://charles.the-haleys.org/ssh_dico_attack_hdeny_format.php/hostsdeny.txt");
                for (length12 = array12.length; n8 < length12; ++n8) {
                    s12 = array12[n8];
                    if (!IPBlacklist.blocked.contains(s12) && !s12.startsWith("#")) {
                        IPBlacklist.blocked.add(s12.split(": ")[1]);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 13 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("http://api.proxyscrape.com?request=getproxies&proxytype=http&timeout=10000&country=all&ssl=all&anonymity=all");
                for (length13 = array13.length; n9 < length13; ++n9) {
                    s13 = array13[n9];
                    if (!IPBlacklist.blocked.contains(s13)) {
                        IPBlacklist.blocked.add(s13);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 14 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://api.proxyscrape.com?request=getproxies&proxytype=socks4&timeout=10000&country=all");
                for (length14 = array14.length; n10 < length14; ++n10) {
                    s14 = array14[n10];
                    if (!IPBlacklist.blocked.contains(s14)) {
                        IPBlacklist.blocked.add(s14);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 15 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://api.proxyscrape.com?request=getproxies&proxytype=socks5&timeout=10000&country=all");
                for (length15 = array15.length; n11 < length15; ++n11) {
                    s15 = array15[n11];
                    if (!IPBlacklist.blocked.contains(s15)) {
                        IPBlacklist.blocked.add(s15);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 16 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://www.proxy-list.download/api/v1/get?type=https");
                for (length16 = array16.length; n12 < length16; ++n12) {
                    s16 = array16[n12];
                    if (!IPBlacklist.blocked.contains(s16)) {
                        IPBlacklist.blocked.add(s16);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 17 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://www.proxy-list.download/api/v1/get?type=http");
                for (length17 = array17.length; n13 < length17; ++n13) {
                    s17 = array17[n13];
                    if (!IPBlacklist.blocked.contains(s17)) {
                        IPBlacklist.blocked.add(s17);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 18 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://www.proxy-list.download/api/v1/get?type=socks4");
                for (length18 = array18.length; n14 < length18; ++n14) {
                    s18 = array18[n14];
                    if (!IPBlacklist.blocked.contains(s18)) {
                        IPBlacklist.blocked.add(s18);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 19 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://www.proxy-list.download/api/v1/get?type=socks5");
                for (length19 = array19.length; n15 < length19; ++n15) {
                    s19 = array19[n15];
                    if (!IPBlacklist.blocked.contains(s19)) {
                        IPBlacklist.blocked.add(s19);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 20 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("https://proxy-daily.com/");
                for (length20 = array20.length; n16 < length20; ++n16) {
                    s20 = array20[n16];
                    if (!IPBlacklist.blocked.contains(s20) && (s20.startsWith("1") || s20.startsWith("2") || s20.startsWith("3") || s20.startsWith("4") || s20.startsWith("5") || s20.startsWith("6") || s20.startsWith("7") || s20.startsWith("8") || (s20.startsWith("9") && s20.contains(":")))) {
                        IPBlacklist.blocked.add(s20.split(":")[0]);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching from list 21 (" + (System.currentTimeMillis() - time) + "ms, " + IPBlacklist.blocked.size() + ") ...");
                getForUrl("http://spys.me/proxy.txt");
                for (length21 = array21.length; n17 < length21; ++n17) {
                    s21 = array21[n17];
                    if (!IPBlacklist.blocked.contains(s21) && (s21.startsWith("1") || s21.startsWith("2") || s21.startsWith("3") || s21.startsWith("4") || s21.startsWith("5") || s21.startsWith("6") || s21.startsWith("7") || s21.startsWith("8") || (s21.startsWith("9") && s21.contains(":")))) {
                        IPBlacklist.blocked.add(s21.split(":")[0]);
                    }
                }
                System.out.println("[BLOCKLIST] Fetching took " + (System.currentTimeMillis() - time) + "ms");
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                System.out.println("[BLOCKLIST] Fetching from file...");
                f2 = new File("blacklist.txt");
                new BufferedReader(new FileReader(f2));
                for (br2 = bufferedReader, line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    if (!line2.startsWith("#")) {
                        IPBlacklist.blocked.add(line2);
                    }
                }
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
            System.out.println("[BLOCKLIST] Loaded " + IPBlacklist.blocked.size() + " Blacklisted IPS.");
            try {
                System.out.println("[BLOCKLIST] Writing...");
                f3 = new File("blacklisted.txt");
                if (f3.exists()) {
                    f3.delete();
                }
                f3.createNewFile();
                new PrintWriter(new FileWriter(f3));
                pw = printWriter;
                IPBlacklist.blocked.iterator();
                while (iterator.hasNext()) {
                    s22 = iterator.next();
                    pw.write(s22 + "\n");
                }
                pw.flush();
                pw.close();
            }
            catch (Exception e4) {
                e4.printStackTrace();
            }
        }).start();
    }
    
    public static boolean isListed(final String ip) {
        return IPBlacklist.blocked.contains(ip);
    }
    
    public static String[] getForUrl(final String link) throws Exception {
        final URL url = new URL(link);
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.setDoOutput(true);
        connection.setRequestProperty("accept", "application/json");
        final InputStream responseStream = connection.getInputStream();
        final int bufferSize = 32768;
        final char[] buffer = new char[32768];
        final StringBuilder out = new StringBuilder();
        final Reader in = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        int charsRead;
        while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString().split("\n");
    }
    
    static {
        IPBlacklist.blocked = new ArrayList<String>();
    }
}
