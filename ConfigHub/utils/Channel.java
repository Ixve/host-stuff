package de.aggromc.confighubhost.utils;

public enum Channel
{
    INVITES("807972616421113857"), 
    UPLOADS("807972641512226846"), 
    ALERTS("807972745777250306"), 
    LOGIN("807972663465082911"), 
    REGISTER("807972688953737239"), 
    SETTINGS("807972722503057438"), 
    CONSOLE("807972796813672458"), 
    ANNOUNCEMENTS("808350902359818280");
    
    private final String cID;
    
    private Channel(final String cID) {
        this.cID = cID;
    }
    
    public String getId() {
        return this.cID;
    }
}
