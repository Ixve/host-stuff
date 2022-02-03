package de.aggromc.confighubhost.ddosprotection.checks;

public class UnderAttack
{
    public static int requests;
    public static boolean notified;
    public static boolean fwRuleOn;
    public static int cps;
    public static int cpm;
    public static int cph;
    public static int rss;
    
    static {
        UnderAttack.requests = 0;
        UnderAttack.notified = false;
        UnderAttack.fwRuleOn = false;
        UnderAttack.cps = 0;
        UnderAttack.cpm = 0;
        UnderAttack.cph = 0;
        UnderAttack.rss = 0;
    }
}
