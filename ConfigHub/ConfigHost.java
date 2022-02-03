package de.aggromc.confighubhost;

import de.aggromc.confighubhost.webserver.*;
import de.aggromc.confighubhost.auth.*;
import java.util.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import de.aggromc.confighubhost.utils.*;
import java.util.concurrent.*;

public class ConfigHost
{
    private static final ExecutorService service;
    public static boolean notfallmodus;
    public static boolean reverse;
    public static ArrayList<String> domainList;
    public static S3Utils s3;
    public static String reverseIP;
    private static WebServer webServer;
    private static Logger logger;
    private static UserIDManager userIDManager;
    private static Invites invites;
    private static MySQL mySQL;
    private static ImageDB imageDB;
    private static UserDB userDB;
    private static ScheduledExecutorService scheduler;
    private static Discord discord;
    
    public static Discord getDiscord() {
        return ConfigHost.discord;
    }
    
    public static void main(final String[] args) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc             "devmode"
        //     6: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //     9: invokevirtual   java/io/File.exists:()Z
        //    12: istore_1        /* devmode */
        //    13: bipush          100
        //    15: invokestatic    java/util/concurrent/Executors.newScheduledThreadPool:(I)Ljava/util/concurrent/ScheduledExecutorService;
        //    18: putstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //    21: ldc             "aNdRgUkXp2s5u8x/A?D(G+KbPeShVmYq3t6w9y$B&E)H@McQfTjWnZr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?E(G+KbPeShVmYq3t6w9z$C&F)J@McQfTjWnZr4u7x!A%D*G-KaPdRgUkXp2s5v8y/B?E(H+MbQeThVmYq3t6w9z$C&F)J@NcRfUjXnZr4u7x!A%D*G-KaPdSgVkYp3s5v8y/B?E(H+MbQeThWmZq4t7w9z$C&F)J@NcRfUjXn2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-JaNcRfUjXn2r5u8x/A?D(G+KbPeSgVkYp3s6v9y$B&E)H@McQfTjWmZq4t7w!z%C*F-JaNdRgUkXp2r5u8x/A?D(G+KbPeShVmYq3t6v9y$B&E)H@McQfTjWnZr4u7x!z%C*F-JaNdRgUkXp2s5v8y/B?D(G+KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-"
        //    23: invokestatic    de/aggromc/confighubhost/utils/Encryption.setKey:(Ljava/lang/String;)V
        //    26: new             Ljava/lang/Thread;
        //    29: dup            
        //    30: invokedynamic   BootstrapMethod #0, run:()Ljava/lang/Runnable;
        //    35: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //    38: invokevirtual   java/lang/Thread.start:()V
        //    41: new             Lde/aggromc/confighubhost/utils/S3Utils;
        //    44: dup            
        //    45: invokespecial   de/aggromc/confighubhost/utils/S3Utils.<init>:()V
        //    48: putstatic       de/aggromc/confighubhost/ConfigHost.s3:Lde/aggromc/confighubhost/utils/S3Utils;
        //    51: iload_1         /* devmode */
        //    52: ifeq            175
        //    55: new             Ljava/io/BufferedReader;
        //    58: dup            
        //    59: new             Ljava/io/FileReader;
        //    62: dup            
        //    63: ldc             "in.txt"
        //    65: invokespecial   java/io/FileReader.<init>:(Ljava/lang/String;)V
        //    68: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    71: astore_2        /* r */
        //    72: aload_2         /* r */
        //    73: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    76: astore_3        /* line */
        //    77: aload_3         /* line */
        //    78: ifnull          174
        //    81: aload_3         /* line */
        //    82: ldc             "\""
        //    84: ldc             ""
        //    86: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    89: astore_3        /* line */
        //    90: aload_3         /* line */
        //    91: ldc             ","
        //    93: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    96: iconst_0       
        //    97: aaload         
        //    98: astore          asn
        //   100: aload_3         /* line */
        //   101: new             Ljava/lang/StringBuilder;
        //   104: dup            
        //   105: invokespecial   java/lang/StringBuilder.<init>:()V
        //   108: aload           asn
        //   110: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   113: ldc             ","
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   118: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   121: ldc             ""
        //   123: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   126: astore          comment
        //   128: getstatic       java/lang/System.out:Ljava/io/PrintStream;
        //   131: new             Ljava/lang/StringBuilder;
        //   134: dup            
        //   135: invokespecial   java/lang/StringBuilder.<init>:()V
        //   138: aload           asn
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: ldc             " | "
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: aload           comment
        //   150: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   153: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   156: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   159: aload           asn
        //   161: aload           comment
        //   163: invokestatic    de/aggromc/confighubhost/utils/CFUtils.addCF:(Ljava/lang/String;Ljava/lang/String;)V
        //   166: aload_2         /* r */
        //   167: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   170: astore_3        /* line */
        //   171: goto            77
        //   174: return         
        //   175: new             Lde/aggromc/confighubhost/utils/Discord;
        //   178: dup            
        //   179: invokespecial   de/aggromc/confighubhost/utils/Discord.<init>:()V
        //   182: putstatic       de/aggromc/confighubhost/ConfigHost.discord:Lde/aggromc/confighubhost/utils/Discord;
        //   185: getstatic       de/aggromc/confighubhost/ConfigHost.discord:Lde/aggromc/confighubhost/utils/Discord;
        //   188: invokevirtual   de/aggromc/confighubhost/utils/Discord.start:()V
        //   191: new             Lde/aggromc/confighubhost/utils/Logger;
        //   194: dup            
        //   195: invokespecial   de/aggromc/confighubhost/utils/Logger.<init>:()V
        //   198: putstatic       de/aggromc/confighubhost/ConfigHost.logger:Lde/aggromc/confighubhost/utils/Logger;
        //   201: invokestatic    de/aggromc/confighubhost/ddosprotection/checks/IPBlacklist.init:()V
        //   204: new             Lde/aggromc/confighubhost/webserver/WebServer;
        //   207: dup            
        //   208: ldc             58224
        //   210: invokespecial   de/aggromc/confighubhost/webserver/WebServer.<init>:(I)V
        //   213: putstatic       de/aggromc/confighubhost/ConfigHost.webServer:Lde/aggromc/confighubhost/webserver/WebServer;
        //   216: getstatic       de/aggromc/confighubhost/ConfigHost.webServer:Lde/aggromc/confighubhost/webserver/WebServer;
        //   219: invokevirtual   de/aggromc/confighubhost/webserver/WebServer.run:()Z
        //   222: pop            
        //   223: new             Lde/aggromc/confighubhost/webserver/utils/WebManager;
        //   226: dup            
        //   227: invokespecial   de/aggromc/confighubhost/webserver/utils/WebManager.<init>:()V
        //   230: pop            
        //   231: new             Lde/aggromc/confighubhost/auth/MySQL;
        //   234: dup            
        //   235: invokespecial   de/aggromc/confighubhost/auth/MySQL.<init>:()V
        //   238: putstatic       de/aggromc/confighubhost/ConfigHost.mySQL:Lde/aggromc/confighubhost/auth/MySQL;
        //   241: getstatic       de/aggromc/confighubhost/ConfigHost.mySQL:Lde/aggromc/confighubhost/auth/MySQL;
        //   244: invokevirtual   de/aggromc/confighubhost/auth/MySQL.connect:()V
        //   247: new             Lde/aggromc/confighubhost/auth/UserDB;
        //   250: dup            
        //   251: invokespecial   de/aggromc/confighubhost/auth/UserDB.<init>:()V
        //   254: putstatic       de/aggromc/confighubhost/ConfigHost.userDB:Lde/aggromc/confighubhost/auth/UserDB;
        //   257: new             Lde/aggromc/confighubhost/auth/Invites;
        //   260: dup            
        //   261: invokespecial   de/aggromc/confighubhost/auth/Invites.<init>:()V
        //   264: putstatic       de/aggromc/confighubhost/ConfigHost.invites:Lde/aggromc/confighubhost/auth/Invites;
        //   267: new             Lde/aggromc/confighubhost/auth/UserIDManager;
        //   270: dup            
        //   271: invokespecial   de/aggromc/confighubhost/auth/UserIDManager.<init>:()V
        //   274: putstatic       de/aggromc/confighubhost/ConfigHost.userIDManager:Lde/aggromc/confighubhost/auth/UserIDManager;
        //   277: new             Lde/aggromc/confighubhost/auth/ImageDB;
        //   280: dup            
        //   281: invokespecial   de/aggromc/confighubhost/auth/ImageDB.<init>:()V
        //   284: putstatic       de/aggromc/confighubhost/ConfigHost.imageDB:Lde/aggromc/confighubhost/auth/ImageDB;
        //   287: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   290: invokedynamic   BootstrapMethod #1, run:()Ljava/lang/Runnable;
        //   295: lconst_0       
        //   296: lconst_1       
        //   297: getstatic       java/util/concurrent/TimeUnit.HOURS:Ljava/util/concurrent/TimeUnit;
        //   300: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   305: pop            
        //   306: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   309: invokedynamic   BootstrapMethod #2, run:()Ljava/lang/Runnable;
        //   314: lconst_0       
        //   315: ldc2_w          5
        //   318: getstatic       java/util/concurrent/TimeUnit.SECONDS:Ljava/util/concurrent/TimeUnit;
        //   321: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   326: pop            
        //   327: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   330: invokedynamic   BootstrapMethod #3, run:()Ljava/lang/Runnable;
        //   335: lconst_0       
        //   336: lconst_1       
        //   337: getstatic       java/util/concurrent/TimeUnit.SECONDS:Ljava/util/concurrent/TimeUnit;
        //   340: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   345: pop            
        //   346: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   349: invokedynamic   BootstrapMethod #4, run:()Ljava/lang/Runnable;
        //   354: lconst_0       
        //   355: lconst_1       
        //   356: getstatic       java/util/concurrent/TimeUnit.MINUTES:Ljava/util/concurrent/TimeUnit;
        //   359: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   364: pop            
        //   365: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   368: invokedynamic   BootstrapMethod #5, run:()Ljava/lang/Runnable;
        //   373: lconst_0       
        //   374: lconst_1       
        //   375: getstatic       java/util/concurrent/TimeUnit.HOURS:Ljava/util/concurrent/TimeUnit;
        //   378: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   383: pop            
        //   384: getstatic       de/aggromc/confighubhost/ConfigHost.scheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   387: invokedynamic   BootstrapMethod #6, run:()Ljava/lang/Runnable;
        //   392: lconst_0       
        //   393: lconst_1       
        //   394: getstatic       java/util/concurrent/TimeUnit.HOURS:Ljava/util/concurrent/TimeUnit;
        //   397: invokeinterface java/util/concurrent/ScheduledExecutorService.scheduleAtFixedRate:(Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   402: pop            
        //   403: invokestatic    de/aggromc/confighubhost/ddosprotection/checks/RequestLimiter.init:()V
        //   406: new             Ljava/lang/Thread;
        //   409: dup            
        //   410: invokedynamic   BootstrapMethod #7, run:()Ljava/lang/Runnable;
        //   415: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //   418: astore_2        /* t */
        //   419: aload_2         /* t */
        //   420: invokevirtual   java/lang/Thread.start:()V
        //   423: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    StackMapTable: 00 03 FE 00 4D 01 07 00 C5 07 00 C6 FB 00 60 F9 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Unsupported node type: com.strobel.decompiler.ast.Lambda
        //     at com.strobel.decompiler.ast.Error.unsupportedNode(Error.java:32)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:612)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:586)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:598)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:586)
        //     at com.strobel.decompiler.ast.GotoRemoval.transformLeaveStatements(GotoRemoval.java:625)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotosCore(GotoRemoval.java:57)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotos(GotoRemoval.java:53)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:276)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static void readCommandInput() {
        final Scanner in = new Scanner(System.in);
        final String s = in.nextLine();
        final String[] array = s.split(" ");
        if (!s.startsWith("createUser ")) {
            if (s.startsWith("createInvite ")) {
                final String inv = array[1];
                if (ConfigHost.invites.inviteExists(inv)) {
                    System.out.println("Dieser Invite existiert schon!");
                }
                else {
                    ConfigHost.invites.createInvite(inv);
                    System.out.println("Invite wurde erstellt!");
                }
            }
            else if (s.startsWith("deleteUser ")) {
                final String uID = array[1];
                ConfigHost.userDB.deleteUser(uID);
                System.out.println("User gel\u00f6scht!");
            }
            else if (s.startsWith("deleteInvite ")) {
                final String inv = array[1];
                ConfigHost.invites.deleteInvite(inv);
                System.out.println("Invite gel\u00f6scht!");
            }
            else if (s.startsWith("deleteImage ")) {
                final String id = array[1];
                ConfigHost.imageDB.deleteImage(id);
                System.out.println("Bild gel\u00f6scht!");
            }
            else if (s.startsWith("blacklist ")) {
                final String ip = array[1];
                IPBlacklist.blocked.add(ip);
                System.out.println("IP geblacklisted!");
            }
            else if (s.startsWith("remove ")) {
                final String ip = array[1];
                IPBlacklist.blocked.remove(ip);
                System.out.println("IP aus der Blacklist entfernt!");
            }
            else if (s.startsWith("notfallmodus")) {
                if (ConfigHost.notfallmodus) {
                    ConfigHost.notfallmodus = false;
                    System.out.println("Notfallmodus deaktiviert!");
                }
                else {
                    ConfigHost.notfallmodus = true;
                    System.out.println("Notfallmodus aktiviert. Alle Seiten werden nun gecached!");
                }
            }
            else if (s.startsWith("reverse ")) {
                if (ConfigHost.reverse) {
                    ConfigHost.reverse = false;
                }
                else {
                    ConfigHost.reverseIP = "http://" + array[1];
                    ConfigHost.reverse = true;
                }
            }
            else if (s.startsWith("inviteWave")) {
                getInvites().createInviteWave();
                System.out.println("Invite-Wave done!");
            }
            else {
                System.out.println("Das ist kein bekannter Command! ['createUser USERNAME PASSWORT', 'deleteUser USERID', 'deleteInvite INVITEID', 'deleteImage IMAGEID']");
            }
        }
    }
    
    public static UserDB getUserDB() {
        return ConfigHost.userDB;
    }
    
    public static MySQL getMySQL() {
        return ConfigHost.mySQL;
    }
    
    public static Invites getInvites() {
        return ConfigHost.invites;
    }
    
    public static WebServer getWebServer() {
        return ConfigHost.webServer;
    }
    
    public static Logger getLogger() {
        return ConfigHost.logger;
    }
    
    public static UserIDManager getLoginManager() {
        return ConfigHost.userIDManager;
    }
    
    public static ImageDB getImageDB() {
        return ConfigHost.imageDB;
    }
    
    public static ExecutorService getService() {
        return ConfigHost.service;
    }
    
    public static ScheduledExecutorService getScheduler() {
        return ConfigHost.scheduler;
    }
    
    static {
        service = Executors.newCachedThreadPool();
        ConfigHost.reverse = false;
    }
}
