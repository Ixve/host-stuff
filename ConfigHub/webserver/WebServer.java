package de.aggromc.confighubhost.webserver;

import com.sun.net.httpserver.*;
import de.aggromc.confighubhost.*;
import java.util.logging.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class WebServer
{
    private HttpServer server;
    private int port;
    
    public WebServer(final int port) {
        this.port = port;
    }
    
    public boolean run() {
        try {
            ConfigHost.getLogger().log(Level.FINE, "Starting WebServer...");
            this.server = HttpServer.create(new InetSocketAddress(this.port), 2500);
            final ExecutorService exec = Executors.newCachedThreadPool();
            this.server.setExecutor(exec);
            this.server.start();
            ConfigHost.getLogger().log(Level.FINE, "WebServer has started!");
            return true;
        }
        catch (IOException ex) {
            ConfigHost.getLogger().log(Level.FINE, "Start failed!");
            System.exit(-1);
            return false;
        }
    }
    
    public void stop() {
        this.server.stop(0);
        ConfigHost.getLogger().log(Level.FINE, "WebServer was stopped!");
    }
    
    public HttpServer getServer() {
        return this.server;
    }
}
