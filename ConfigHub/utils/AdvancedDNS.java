package de.aggromc.confighubhost.utils;

import eu.roboflax.cloudflare.objects.spectrum.*;
import com.google.gson.annotations.*;

public class AdvancedDNS
{
    @SerializedName("type")
    @Expose
    public DNSType type;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
    
    AdvancedDNS(final DNSType type, final String name, final String id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }
}
