package de.aggromc.confighubhost.utils;

import eu.roboflax.cloudflare.constants.*;
import eu.roboflax.cloudflare.objects.zone.*;
import de.aggromc.confighubhost.*;
import eu.roboflax.cloudflare.*;
import com.google.gson.*;
import java.util.*;
import eu.roboflax.cloudflare.objects.accessrule.*;
import eu.roboflax.cloudflare.http.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import org.javacord.api.entity.message.embed.*;
import java.util.concurrent.*;

public class CFUtils
{
    private static final String CF_API_KEY = "5cbbdcc2008d85f22e3024176aa57e4476854";
    private static final String CF_EMAIL = "admin@confighub.host";
    
    public static ArrayList<String> getDomains() {
        final ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.addAll(getDomains(1));
        toReturn.addAll(getDomains(2));
        toReturn.addAll(getDomains(3));
        return toReturn;
    }
    
    private static String setupDomain(final String name) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final CloudflareResponse<Zone> r = (CloudflareResponse<Zone>)new CloudflareRequest(Category.CREATE_ZONE, cf).body("{\"name\": \"" + name + "\",\"organization\":{\"id\":\"79eb6bc952751537a2004a10a405b0a0\"}}").asObject((Class)Zone.class);
        if (!r.isSuccessful()) {
            ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "Cloudflare says: \nStatus: " + r.getStatusMessage() + "\nError: " + r.getJson().get("errors").getAsJsonObject().get("message").toString());
            return "cancel";
        }
        ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "Cloudflare says: \nStatus: " + r.getStatusMessage() + "\nSetting up...");
        return ((Zone)r.getObject()).getId();
    }
    
    private static void remDomain(final String zone) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        new CloudflareRequest(Category.DELETE_ZONE, cf).additionalPath("zones/" + zone).asObject((Class)JsonElement.class);
    }
    
    public static void configureDomain(final String name) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final String zone = setupDomain(name);
        if (zone.equals("cancel")) {
            return;
        }
        try {
            delete(zone);
        }
        catch (Exception e) {
            e.printStackTrace();
            ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "An Error Occured in Deleting DNS Records, removing Domain from Account...");
            remDomain(zone);
        }
        try {
            setupPageRule(zone, name, cf);
        }
        catch (Exception e) {
            e.printStackTrace();
            ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "An Error Occured in Setting Up Page Rules, removing Domain from Account...");
            remDomain(zone);
        }
        try {
            setupDns(zone);
        }
        catch (Exception e) {
            e.printStackTrace();
            ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "An Error Occured in Setting Up DNS, removing Domain from Account...");
            remDomain(zone);
        }
        try {
            setupSSL(zone);
        }
        catch (Exception e) {
            e.printStackTrace();
            ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "An Error Occured in Setting Up SSL, removing Domain from Account...");
            remDomain(zone);
        }
        ConfigHost.getDiscord().sendPlain(Channel.CONSOLE, "Domain Setup finished.");
    }
    
    public static void setupSSL(final String zone) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        new CloudflareRequest(Category.CHANGE_ALWAYS_USE_HTTPS_SETTING, cf).additionalPath("zones/" + zone + "/settings/always_use_https").body("{\"value\":\"on\"}").asVoid();
        new CloudflareRequest(Category.CHANGE_SSL_SETTING, cf).additionalPath("zones/" + zone + "/settings/ssl").body("{\"value\":\"flexible\"}").asVoid();
    }
    
    public static void setupDns(final String zone) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        new CloudflareRequest(Category.CREATE_DNS_RECORD, cf).additionalPath("zones/" + zone + "/dns_records").body("{\"type\":\"CNAME\",\"name\":\"@\",\"content\":\"confighub.host\",\"proxied\":false}").asVoid();
        new CloudflareRequest(Category.CREATE_DNS_RECORD, cf).additionalPath("zones/" + zone + "/dns_records").body("{\"type\":\"CNAME\",\"name\":\"*\",\"content\":\"confighub.host\",\"proxied\":false}").asVoid();
    }
    
    public static void getDomainIDS() {
        new Thread(() -> {
            System.out.println("Get 1");
            getDomainID(1);
            return;
        }).start();
        new Thread(() -> {
            System.out.println("Get 2");
            getDomainID(2);
            return;
        }).start();
        new Thread(() -> {
            System.out.println("Get 3");
            getDomainID(3);
        }).start();
    }
    
    public static void delete(final String zoneID) throws Exception {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final CloudflareResponse<List<AdvancedDNS>> res = (CloudflareResponse<List<AdvancedDNS>>)new CloudflareRequest(Category.LIST_DNS_RECORDS, cf).additionalPath("zones/" + zoneID + "/dns_records").queryString("per_page", (Object)"50").asObjectList((Class)AdvancedDNS.class);
        if (res.getObject() == null || ((List)res.getObject()).isEmpty()) {
            return;
        }
        for (final AdvancedDNS dns : (List)res.getObject()) {
            new CloudflareRequest(Category.DELETE_DNS_RECORD, cf).additionalPath("zones/" + zoneID + "/dns_records/" + dns.id).asObject((Class)JsonElement.class);
        }
    }
    
    private static ArrayList<String> getDomains(final int page) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final CloudflareResponse<List<Zone>> zoneList = (CloudflareResponse<List<Zone>>)new CloudflareRequest(Category.LIST_ZONES, cf).queryString("per_page", (Object)"50").queryString("page", (Object)page).asObjectList((Class)Zone.class);
        final ArrayList<String> toReturn = new ArrayList<String>();
        for (final Zone z : (List)zoneList.getObject()) {
            if (!z.getPaused()) {
                toReturn.add(z.getName());
            }
        }
        return toReturn;
    }
    
    private static ArrayList<String> getDomainID(final int page) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final CloudflareResponse<List<Zone>> zoneList = (CloudflareResponse<List<Zone>>)new CloudflareRequest(Category.LIST_ZONES, cf).queryString("per_page", (Object)"50").queryString("page", (Object)page).asObjectList((Class)Zone.class);
        final ArrayList<String> toReturn = new ArrayList<String>();
        for (final Zone z : (List)zoneList.getObject()) {
            System.out.println("Found");
            setupPageRule(z.getName(), z.getId(), cf);
        }
        return toReturn;
    }
    
    private static void setupPageRule(final String domainName, final String domain, final CloudflareAccess cf) {
        final CloudflareResponse<List<AccessRule>> list = (CloudflareResponse<List<AccessRule>>)new CloudflareRequest(Category.LIST_PAGE_RULES, cf).additionalPath("zones/" + domainName + "/pagerules").asObjectList((Class)AccessRule.class);
        if (list.getObject() == null || ((List)list.getObject()).isEmpty()) {
            System.out.println("Setup");
            final String json = "{\"targets\":[{\"target\": \"url\",\"constraint\": {\"operator\": \"matches\",\"value\": \"*" + domain + "/*\"}}],\"actions\":[ {\"id\": \"forwarding_url\",\"value\": {\"url\": \"https://confighub.host/$2\", \"status_code\": 302}}],\"priority\": 1,\"status\": \"active\"}";
            new CloudflareRequest(Category.CREATE_PAGE_RULE, cf).additionalPath("zones/" + domainName + "/pagerules").httpMethod(HttpMethod.POST).body(json).asObject((Class)JsonElement.class);
        }
    }
    
    public static void setupPageRules() {
        getDomainIDS();
    }
    
    public static void toggleEmergencyOn(final int strengh) {
        if (!UnderAttack.fwRuleOn) {
            final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
            System.out.println(new CloudflareRequest(Category.CHANGE_SECURITY_LEVEL_SETTING, cf).additionalPath("zones/fc694873c3a0a8a2eb1581dba1bcac10/settings/security_level").body("{\"value\":\"under_attack\"}").asObject((Class)JsonElement.class).getJson().toString());
            ConfigHost.getDiscord().sendPlain(Channel.ANNOUNCEMENTS, "<@&801532269197393952>");
            ConfigHost.getDiscord().send(Channel.ANNOUNCEMENTS, new EmbedBuilder().setThumbnail("https://media.giphy.com/media/U7awnwmEd3UAOn0ByS/giphy.gif").setTitle("Under Attack").setDescription("A DDoS Attack was detected, measures are being taken against it.").addField("Domain", "confighub.host", true).addField("Action", "Enabled UAM", true).addField("Strength", strengh * 3 + " req/s", true));
            ConfigHost.getScheduler().schedule(() -> {
                try {
                    toggleEmergencyOff();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }, 5L, TimeUnit.MINUTES);
        }
    }
    
    public static void toggleEmergencyOff() {
        UnderAttack.fwRuleOn = false;
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        System.out.println(new CloudflareRequest(Category.CHANGE_SECURITY_LEVEL_SETTING, cf).additionalPath("zones/fc694873c3a0a8a2eb1581dba1bcac10/settings/security_level").body("{\"value\":\"low\"}").asObject((Class)JsonElement.class).getJson().toString());
        ConfigHost.getDiscord().sendPlain(Channel.ANNOUNCEMENTS, "<@&801532269197393952>");
        ConfigHost.getDiscord().send(Channel.ANNOUNCEMENTS, new EmbedBuilder().setThumbnail("https://media.giphy.com/media/U7awnwmEd3UAOn0ByS/giphy.gif").setTitle("No Attacks Anymore").setDescription("The DDoS Attack is over, UAM will be disabled.").addField("Domain", "confighub.host", true).addField("Action", "Disabled UAM", true));
    }
    
    public static void addIPS() {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        new CloudflareRequest(Category.CREATE_ZONE_ACCESS_RULE, cf).additionalPath("zones/fc694873c3a0a8a2eb1581dba1bcac10/firewall/access_rules/rules").body("{\"mode\":\"block\",\"configuration\":{\"target\":\"asn\",\"value\":\"Tor\"},\"notes\":\"Test\"}").asVoid();
    }
    
    public static void addCF(final String asn, final String comment) {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        System.out.println("Adding ASN " + asn + " with Reason " + comment + "!");
        try {
            System.out.println(new CloudflareRequest(Category.CREATE_ZONE_ACCESS_RULE, cf).additionalPath("zones/fc694873c3a0a8a2eb1581dba1bcac10/firewall/access_rules/rules").body("{\"mode\":\"block\",\"configuration\":{\"target\":\"asn\",\"value\":\"" + asn + "\"},\"notes\":\"" + comment + "\"}").asObject((Class)JsonElement.class).getJson().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed " + asn);
        }
    }
    
    public static void changeDNS() throws Exception {
        final CloudflareAccess cf = new CloudflareAccess("5cbbdcc2008d85f22e3024176aa57e4476854", "admin@confighub.host");
        final CloudflareResponse<List<Zone>> zoneList = (CloudflareResponse<List<Zone>>)new CloudflareRequest(Category.LIST_ZONES, cf).queryString("per_page", (Object)"50").queryString("page", (Object)"3").asObjectList((Class)Zone.class);
        for (final Zone z : (List)zoneList.getObject()) {
            System.out.println(z.getName());
            if (!z.getName().equalsIgnoreCase("confighub.host")) {
                delete(z.getId());
                final String zone = z.getId();
                new CloudflareRequest(Category.CREATE_DNS_RECORD, cf).additionalPath("zones/" + zone + "/dns_records").body("{\"type\":\"A\",\"name\":\"@\",\"content\":\"1.1.1.1\",\"proxied\":true}").asVoid();
                new CloudflareRequest(Category.CREATE_DNS_RECORD, cf).additionalPath("zones/" + zone + "/dns_records").body("{\"type\":\"A\",\"name\":\"*\",\"content\":\"1.1.1.1\",\"proxied\":false}").asVoid();
            }
        }
    }
}
