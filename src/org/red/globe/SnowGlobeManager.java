package org.red.globe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SnowGlobeManager {
    private static SnowGlobeManager instance;

    public static SnowGlobeManager getInstance() {
        if (instance == null)
            instance = new SnowGlobeManager();

        return instance;
    }

    private final Map<String, SnowGlobe> snowGlobeMap = new HashMap<>();

    public void register(SnowGlobe snowGlobe) {
        this.snowGlobeMap.put(snowGlobe.getName(), snowGlobe);
    }

    public boolean contains(String key) {
        return this.snowGlobeMap.containsKey(key);
    }

    public SnowGlobe get(String key) {
        return this.snowGlobeMap.get(key);
    }

    public Set<String> keys() {
        return this.snowGlobeMap.keySet();
    }
}
