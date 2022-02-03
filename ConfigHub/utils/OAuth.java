package de.aggromc.confighubhost.utils;

import io.mokulu.discord.oauth.*;
import de.aggromc.confighubhost.*;
import io.mokulu.discord.oauth.model.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import java.net.*;
import java.io.*;

public class OAuth
{
    public static final String BASE_URI = "https://discord.com/api";
    
    public static boolean codeValid(final String substring) {
        try {
            final DiscordOAuth oauthHandler = new DiscordOAuth("795304705038745620", "e1QwWd3sluM2NRap0doEQdEVYAa8ISEh", "https://confighub.host/oauth", new String[] { "guilds.join", "identify", "guilds" });
            final TokensResponse tokens = oauthHandler.getTokens(substring);
            final String accessToken = tokens.getAccessToken();
            final String refreshToken = tokens.getRefreshToken();
            final DiscordAPI api = new DiscordAPI(accessToken);
            final User user = api.fetchUser();
            final String serverid = "751761552516055101";
            final String userid = user.getId();
            try2("/guilds/" + serverid + "/members/" + userid, accessToken);
            ConfigHost.getUserDB().setDiscord(accessToken, refreshToken);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void joinServer(final String serverid, final String userid, final String accessToken) throws Exception {
        final String requestUri = "/guilds/" + serverid + "/members/" + userid;
        System.out.println(requestUri);
        handleGet(requestUri, accessToken);
    }
    
    private static String handleGet(final String path, final String accessToken) throws Exception {
        final Connection request = Jsoup.connect("https://discord.com/api" + path).method(Connection.Method.PUT).ignoreContentType(true).ignoreHttpErrors(true).method(Connection.Method.PUT);
        setHeaders(request, accessToken);
        System.out.println(request.get().text());
        System.out.println(request.get().body().text());
        final Document got = request.get();
        System.out.println(got.body().text());
        return "asd";
    }
    
    private static void setHeaders(final Connection request, final String accessToken) {
        request.header("Authorization", "Bot Nzk1MzA0NzA1MDM4NzQ1NjIw.X_HbOQ.xqnnbWKU4tBDuDAqvidwdbj5e84");
        request.header("User-Agent", "AggroBot; confighub.host");
        final String json = "{\"access_token\":\"" + accessToken + "\"}";
        request.header("Content-Type", "application/json");
        request.data(json);
    }
    
    private static void try2(final String path, final String accessToken) throws Exception {
        final URL url = new URL("https://discord.com/api" + path);
        final HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.setRequestProperty("Authorization", "Bot Nzk1MzA0NzA1MDM4NzQ1NjIw.X_HbOQ.xqnnbWKU4tBDuDAqvidwdbj5e84");
        httpCon.setRequestProperty("User-Agent", "AggroBot; confighub.host");
        httpCon.setRequestProperty("Content-Type", "application/json");
        final OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("{\"access_token\":\"" + accessToken + "\",\"roles\":\"[751764005827248261]\"}");
        out.close();
        final BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        final StringBuffer content = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        System.out.println(content);
        in.close();
        httpCon.disconnect();
    }
}
