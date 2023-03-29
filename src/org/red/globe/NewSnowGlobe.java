package org.red.globe;

import org.bukkit.Location;

public class NewSnowGlobe {
    private final String name;

    public NewSnowGlobe(String name, Location start, Location end) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
