package de.aggromc.confighubhost.utils;

import com.sun.management.*;
import java.lang.management.*;
import java.util.concurrent.*;
import org.javacord.api.entity.user.*;
import java.time.*;
import org.javacord.api.*;
import org.javacord.api.entity.server.*;
import org.javacord.api.entity.permission.*;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.embed.*;
import java.awt.*;
import org.javacord.api.event.message.*;
import de.aggromc.confighubhost.*;
import de.aggromc.confighubhost.ddosprotection.checks.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.javacord.api.event.message.reaction.*;
import org.javacord.api.event.server.member.*;
import org.javacord.api.entity.message.*;
import org.javacord.api.event.server.*;
import java.util.function.*;

public class Discord
{
    public final OperatingSystemMXBean osBean;
    public Thread discordThread;
    public ExecutorService discordSenders;
    public DiscordApi api;
    
    public Discord() {
        this.osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        this.discordSenders = Executors.newCachedThreadPool();
    }
    
    private static boolean isAlt(final User m) {
        final Instant is = Instant.now().minusSeconds(1209600L);
        return m.getCreationTimestamp().isAfter(is);
    }
    
    public void start() {
        final String token;
        final Server server;
        final Role domains;
        final Role updates;
        final Role giveaways;
        final ServerTextChannel staff;
        final Role member;
        final Collection<User>[] boostuhs;
        final ServerTextChannel joins;
        (this.discordThread = new Thread(() -> {
            token = "ODA3OTU2NTg1OTA5MTkwNjk2.YB_iNQ.Qgbl0ZxFqPv2cXJp5RecD3zO9p0";
            this.api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();
            server = this.api.getServerById("751761552516055101").get();
            domains = server.getRoleById("798482457606619146").get();
            updates = server.getRoleById("798482460497150005").get();
            giveaways = server.getRoleById("798482463596609556").get();
            staff = server.getChannelById("799528930842378281").get().asServerTextChannel().get();
            member = server.getRoleById("751763866765230141").get();
            boostuhs = (Collection<User>[])new Collection[] { server.getRoleById("786984728191565855").get().getUsers() };
            joins = server.getChannelById("786987822971748372").get().asServerTextChannel().get();
            this.api.addMessageCreateListener(event -> {
                if (!event.isServerMessage()) {
                    return;
                }
                if (event.getMessageContent().contains("discord.gg/") && !event.getMessageAuthor().isServerAdmin()) {
                    final String[] split;
                    final String[] all = split = event.getMessageContent().split(" ");
                    for (final String asd : split) {
                        if (!asd.contains("discord.gg/images")) {
                            event.getMessage().delete().join();
                            event.getChannel().sendMessage("<@" + event.getMessageAuthor().getId() + ">, no Invite Links.").join();
                        }
                    }
                }
            });
            this.api.addServerChangeBoostCountListener(event -> {
                staff.sendMessage("Boost Count Changed.\n" + event.getOldBoostCount() + " -> " + event.getNewBoostCount()).join();
                final Collection<User> newBoostuhs = (Collection<User>)server.getRoleById("786984728191565855").get().getUsers();
                boostuhs[0].removeIf(newBoostuhs::contains);
                if (boostuhs[0].isEmpty()) {
                    staff.sendMessage("I could not figure out who unboosted").join();
                }
                else {
                    final StringBuilder ment = new StringBuilder();
                    for (final User u : boostuhs[0]) {
                        ment.append(" ").append(u.getMentionTag());
                    }
                    staff.sendMessage("Booster-List changes:" + ment.toString()).join();
                }
                boostuhs[0] = (Collection<User>)server.getRoleById("786984728191565855").get().getUsers();
            });
            this.api.addServerChangeBoostLevelListener(event -> {
                final Message message = staff.sendMessage("Boost Level Changed.\n" + event.getOldBoostLevel().getId() + " -> " + event.getNewBoostLevel().getId()).join();
            });
            this.api.addServerMemberJoinListener(event -> {
                if (event.getServer().getIdAsString().equalsIgnoreCase("751761552516055101")) {
                    joins.sendMessage("Welcome, " + event.getUser().getMentionTag() + "! Please Read the <#786990339860201512> and add yourself the Ping-Roles you want in <#798469553704796181>.").join();
                    event.getUser().addRole(member).join();
                }
            });
            this.api.addReactionAddListener(event -> {
                try {
                    if (event.getChannel().getIdAsString().equalsIgnoreCase("798469553704796181")) {
                        if (event.getEmoji().asUnicodeEmoji().orElse("").equalsIgnoreCase("\ud83c\udd95")) {
                            event.getUser().get().addRole(domains).join();
                        }
                        else if (event.getEmoji().getMentionTag().equalsIgnoreCase("<a:Giveaway:801355341802766337>")) {
                            event.getUser().get().addRole(giveaways).join();
                        }
                        else if (event.getEmoji().getMentionTag().equalsIgnoreCase("<:pinguserver:798481683220267029>")) {
                            event.getUser().get().addRole(updates).join();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            this.api.addReactionRemoveListener(event -> {
                try {
                    if (event.getChannel().getIdAsString().equalsIgnoreCase("798469553704796181")) {
                        if (event.getEmoji().asUnicodeEmoji().orElse("").equalsIgnoreCase("\ud83c\udd95")) {
                            event.getServer().get().removeRoleFromUser((User)event.getUser().get(), (Role)event.getServer().get().getRoleById("798482457606619146").get()).join();
                        }
                        else if (event.getEmoji().getMentionTag().equalsIgnoreCase("<a:Giveaway:801355341802766337>")) {
                            event.getServer().get().removeRoleFromUser((User)event.getUser().get(), (Role)event.getServer().get().getRoleById("798482463596609556").get()).join();
                        }
                        else if (event.getEmoji().getMentionTag().equalsIgnoreCase("<:pinguserver:798481683220267029>")) {
                            event.getServer().get().removeRoleFromUser((User)event.getUser().get(), (Role)event.getServer().get().getRoleById("798482460497150005").get()).join();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            this.api.addMessageCreateListener(event -> {
                if (!event.getChannel().getIdAsString().equalsIgnoreCase("807972796813672458") && event.getMessageAuthor().getId() != 710468576196165723L && event.getMessageAuthor().getId() != 765944420674568224L) {
                    if (!event.getChannel().getIdAsString().equalsIgnoreCase("790927769495142460") || !event.getMessage().getContent().equalsIgnoreCase("a.invite")) {
                        return;
                    }
                }
                try {
                    if (event.getMessage().getContent().startsWith("a.")) {
                        final String ct = event.getMessage().getContent().substring(2);
                        if (ct.equalsIgnoreCase("help")) {
                            event.getChannel().sendMessage("```css\n\nBot-Help\n\na.help - Shows this\na.geninvite <UID or Username> - Gens an invite for the given userid or username\na.invitewave - Creates an Invitewave\na.delimage <ImgID> - Deletes an Image\na.lookup <ID/Name> - Looks up an User\na.cps - Shows Server Connection Stats\na.status - Shows Server Status\na.refresh - Refreshs Domain List\na.getlist - Prints all domains\na.add <Domain> - Configures a Domain\na.invite - Invites for Tickets etc.\na.checkpagerules - Checks and adds Pagerules, can only be run by TeamAggro\na.wipe <UID> - Wipes Images for an User\na.regenkey <UID> - Regenerates UploadKey for an User\na.delimg <IMGID> - Deletes an Image\na.ban <UID> - Bans an User. Requires Higher Permissions\n```").join();
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("delimg ")) {
                            final String imgID = ct.substring(7);
                            if (ConfigHost.getImageDB().imageExists(imgID)) {
                                ConfigHost.getImageDB().deleteImage(imgID);
                                event.getChannel().sendMessage("Image successfully deleted. Warning: It can take 8 Hours till it fully disappears.").join();
                            }
                            else {
                                event.getChannel().sendMessage("I could not find that Image. You perhaps forgot to remove the s?").join();
                            }
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("ban ")) {
                            if (event.getMessageAuthor().getId() == 765944420674568224L) {
                                if (!ConfigHost.getUserDB().userExists(ct.substring(4))) {
                                    event.getChannel().sendMessage("Dat user no exist").join();
                                }
                            }
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("regenkey ")) {
                            final String uID = ct.substring(9);
                            if (ConfigHost.getUserDB().userExists(uID)) {
                                ConfigHost.getUserDB().regenerateToken(uID);
                                event.getChannel().sendMessage("Token regenerated.").join();
                            }
                            else {
                                event.getChannel().sendMessage("User not Found.").join();
                            }
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("wipe ")) {
                            if (ConfigHost.getUserDB().userExists(ct.substring(5))) {
                                try {
                                    final PreparedStatement pst = ConfigHost.getMySQL().connection.prepareStatement("SELECT * from images WHERE userID=?");
                                    pst.setString(1, ct.substring(5));
                                    final ResultSet rs = pst.executeQuery();
                                    while (rs.next()) {
                                        ConfigHost.getImageDB().deleteImage(rs.getString("imageID"));
                                    }
                                    event.getChannel().sendMessage("Wiping finished.").join();
                                }
                                catch (SQLException e) {
                                    event.getChannel().sendMessage("Looks like something went wrong.").join();
                                    e.printStackTrace();
                                }
                            }
                            else {
                                event.getChannel().sendMessage("That userID doesnt exist.").join();
                            }
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("geninvite")) {
                            final String u = ct.substring(10);
                            if (ConfigHost.getUserDB().userExists(u)) {
                                ConfigHost.getInvites().createInvite(u);
                                event.getChannel().sendMessage("Alright, " + ConfigHost.getUserDB().getName(u) + " just got an Invite!").join();
                            }
                            else if (ConfigHost.getUserDB().userNameExists(u)) {
                                ConfigHost.getInvites().createInvite(ConfigHost.getUserDB().getUserIdByName(u));
                                event.getChannel().sendMessage("Alright, " + u + " just got an Invite!");
                            }
                            else {
                                event.getChannel().sendMessage("This is not a valid UserID/Username").join();
                            }
                        }
                        else if (ct.equalsIgnoreCase("invitewave")) {
                            ConfigHost.getInvites().createInviteWave();
                            event.getChannel().sendMessage("**INVITEWAVE** done. " + ConfigHost.getUserDB().getUsers() + " Users received an Invite.").join();
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("delimage")) {
                            event.getChannel().sendMessage("Coming soon iam lazy").join();
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("lookup")) {
                            String u = ct.substring(7);
                            if (ConfigHost.getUserDB().userExists(u)) {
                                event.getChannel().sendMessage(new EmbedBuilder().setTitle("User Information").addField("Domain", ConfigHost.getUserDB().getDomain(u)).addField("Name", ConfigHost.getUserDB().getName(u)).addField("Registered", ConfigHost.getUserDB().getRegisteredAt(u)).addField("Invites", ConfigHost.getInvites().getCount(u) + "").addField("Users Invited", ConfigHost.getUserDB().getInvitedUsersCount(u) + "").setColor(Color.RED)).join();
                            }
                            else if (ConfigHost.getUserDB().userNameExists(u)) {
                                u = ConfigHost.getUserDB().getUserIdByName(u);
                                event.getChannel().sendMessage(new EmbedBuilder().setTitle("User Information").addField("Domain", ConfigHost.getUserDB().getDomain(u)).addField("UID", u).addField("Registered", ConfigHost.getUserDB().getRegisteredAt(u)).addField("Invites", ConfigHost.getInvites().getCount(u) + "").addField("Users Invited", ConfigHost.getUserDB().getInvitedUsersCount(u) + "").setColor(Color.RED)).join();
                            }
                            else {
                                event.getChannel().sendMessage("This is not a valid UserID/Username").join();
                            }
                        }
                        else if (ct.equalsIgnoreCase("cps")) {
                            event.getChannel().sendMessage("```css\nRequest-Stats of confighub.host:\n\nCPS: " + UnderAttack.cps + "\nCPM: " + UnderAttack.cpm + "\nCPH: " + UnderAttack.cph + "\nRequests since start: " + UnderAttack.rss + "\n```").join();
                        }
                        else if (ct.equalsIgnoreCase("status")) {
                            final File f = new File("/");
                            event.getChannel().sendMessage("```yml\n\nStatus of confighub.host:\n\nRam Usage: " + this.getRam() + "\nProcess CPU Usage: " + this.osBean.getProcessCpuLoad() * 1000.0 / 10.0 + "%\nSystem CPU Usage: " + this.osBean.getSystemCpuLoad() * 1000.0 / 10.0 + "%\nAvailable Processors: " + this.osBean.getAvailableProcessors() + "\nDisk Usage: " + Math.round((f.getTotalSpace() - f.getUsableSpace()) / 1.0E9) + "GB/" + Math.round(f.getTotalSpace() / 1.0E9) + "GB\n```").join();
                        }
                        else if (ct.equalsIgnoreCase("invite")) {
                            event.getChannel().sendMessage("`https://confighub.gifts/" + ConfigHost.getInvites().createInvite("133337") + "`").join();
                        }
                        else if (ct.equalsIgnoreCase("refresh")) {
                            ConfigHost.domainList = CFUtils.getDomains();
                            event.getChannel().sendMessage("Domainlist refreshed!").join();
                        }
                        else if (ct.equalsIgnoreCase("getlist")) {
                            final StringBuilder dlist = new StringBuilder();
                            for (final String dm : ConfigHost.domainList) {
                                dlist.append(dm).append("\n");
                            }
                            event.getChannel().sendMessage(dlist.toString()).join();
                        }
                        else if (ct.toLowerCase(Locale.ROOT).startsWith("add")) {
                            CFUtils.configureDomain(ct.substring(4));
                        }
                        else if (ct.equalsIgnoreCase("checkpagerules")) {
                            if (event.getMessageAuthor().getId() == 765944420674568224L) {
                                event.getChannel().sendMessage("Checking Page Rules for every Domain, please Wait...").join();
                                this.discordSenders.execute(() -> {
                                    try {
                                        CFUtils.setupPageRules();
                                    }
                                    catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                    event.getChannel().sendMessage("Done.").join();
                                });
                            }
                            else {
                                event.getChannel().sendMessage("Sorry dude").join();
                            }
                        }
                    }
                }
                catch (Exception e3) {
                    final StringBuilder fin = new StringBuilder();
                    for (final StackTraceElement ste : e3.getStackTrace()) {
                        fin.append(ste.toString()).append("\n");
                    }
                    event.getChannel().sendMessage("Because of you twinky little cocksucker, this happened:").join();
                    final List<String> strings = new ArrayList<String>();
                    for (int index = 0; index < fin.length(); index += 1996) {
                        strings.add(fin.substring(index, Math.min(index + 1996, fin.length())));
                    }
                    for (final String ssss : strings) {
                        event.getChannel().sendMessage(ssss).join();
                    }
                }
            });
        })).start();
    }
    
    public void stop() {
        this.api.disconnect();
        this.discordThread.interrupt();
    }
    
    public void send(final Channel c, final EmbedBuilder emb) {
        this.api.getChannelById(c.getId()).get().asServerTextChannel().get().sendMessage(emb.setColor(Color.RED)).join();
    }
    
    public void sendPlain(final Channel alerts, final String s) {
        this.api.getChannelById(alerts.getId()).get().asServerTextChannel().get().sendMessage(s).join();
    }
    
    private String getRam() throws IOException {
        final Runtime runtime = Runtime.getRuntime();
        final BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec("free -g").getInputStream()));
        String memLine = "";
        int index = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (index == 1) {
                memLine = line;
            }
            ++index;
        }
        final List<String> memInfoList = Arrays.asList(memLine.split("\\s+"));
        final int totalSystemMemory = Integer.parseInt(memInfoList.get(1));
        final int totalSystemUsedMemory = Integer.parseInt(memInfoList.get(2));
        return totalSystemUsedMemory + "GB/" + totalSystemMemory + "GB";
    }
}
