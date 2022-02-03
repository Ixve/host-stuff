package de.aggromc.confighubhost.auth;

public class UserID
{
    private final String id;
    private final String ownerID;
    
    public UserID(final String id, final String ownerID) {
        this.id = id;
        this.ownerID = ownerID;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getOwnerID() {
        return this.ownerID;
    }
}
