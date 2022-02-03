package de.aggromc.confighubhost.utils;

import java.util.logging.*;
import java.time.format.*;
import java.time.*;
import java.time.temporal.*;

public class Logger
{
    private java.util.logging.Logger importantLogger;
    private java.util.logging.Logger ipLogger;
    private java.util.logging.Logger debugLogger;
    private java.util.logging.Logger accessLogger;
    private java.util.logging.Logger blockedLogger;
    
    public Logger() {
        try {
            (this.importantLogger = java.util.logging.Logger.getLogger("ConfigHost-1")).setLevel(Level.FINE);
            this.importantLogger.setUseParentHandlers(false);
            final FileHandler fh = new FileHandler("logs/important.log");
            fh.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(final LogRecord lr) {
                    System.out.println(Logger.this.getFormattedTimestamp() + lr.getMessage());
                    return Logger.this.getFormattedTimestamp() + lr.getMessage() + "\r\n";
                }
            });
            this.importantLogger.addHandler(fh);
            (this.ipLogger = java.util.logging.Logger.getLogger("ConfigHost-2")).setLevel(Level.FINEST);
            this.ipLogger.setUseParentHandlers(false);
            final FileHandler fh2 = new FileHandler("logs/ips.log");
            fh2.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(final LogRecord lr) {
                    System.out.println(Logger.this.getFormattedTimestamp() + lr.getMessage());
                    return Logger.this.getFormattedTimestamp() + lr.getMessage() + "\r\n";
                }
            });
            this.ipLogger.addHandler(fh2);
            (this.debugLogger = java.util.logging.Logger.getLogger("ConfigHost-3")).setLevel(Level.INFO);
            this.debugLogger.setUseParentHandlers(false);
            final FileHandler fh3 = new FileHandler("logs/debug.log");
            fh3.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(final LogRecord lr) {
                    System.out.println(Logger.this.getFormattedTimestamp() + lr.getMessage());
                    return Logger.this.getFormattedTimestamp() + lr.getMessage() + "\r\n";
                }
            });
            this.debugLogger.addHandler(fh3);
            (this.accessLogger = java.util.logging.Logger.getLogger("ConfigHost-4")).setLevel(Level.FINER);
            this.accessLogger.setUseParentHandlers(false);
            final FileHandler fh4 = new FileHandler("logs/access.log");
            fh4.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(final LogRecord lr) {
                    System.out.println(Logger.this.getFormattedTimestamp() + lr.getMessage());
                    return Logger.this.getFormattedTimestamp() + lr.getMessage() + "\r\n";
                }
            });
            this.accessLogger.addHandler(fh4);
            (this.blockedLogger = java.util.logging.Logger.getLogger("ConfigHost-5")).setLevel(Level.WARNING);
            this.blockedLogger.setUseParentHandlers(false);
            final FileHandler fh5 = new FileHandler("logs/blocked.log");
            fh5.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(final LogRecord lr) {
                    System.out.println(Logger.this.getFormattedTimestamp() + lr.getMessage());
                    return Logger.this.getFormattedTimestamp() + lr.getMessage() + "\r\n";
                }
            });
            this.blockedLogger.addHandler(fh5);
        }
        catch (Exception e) {
            System.exit(0);
        }
    }
    
    public void log(final Level l, final String message) {
        if (l == Level.FINE) {
            this.importantLogger.log(l, message);
        }
        else if (l == Level.FINEST) {
            this.ipLogger.log(l, message);
        }
        else if (l == Level.INFO) {
            this.debugLogger.log(l, message);
        }
        else if (l == Level.FINER) {
            this.accessLogger.log(l, message);
        }
        else if (l == Level.WARNING) {
            this.blockedLogger.log(l, message);
        }
    }
    
    private String getFormattedTimestamp() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] ";
    }
}
