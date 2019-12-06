package de.derklaro.privateservers.database.utils.databse;

import com.google.gson.reflect.TypeToken;
import de.derklaro.privateservers.database.utils.DatabaseExt;
import de.derklaro.privateservers.mobs.config.Mob;
import de.derklaro.privateservers.utility.configuration.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MobDatabase extends DatabaseExt<Mob, UUID> {

    private Map<UUID, Mob> mobMap = new HashMap<>();

    @Override
    public void load() {
        if (!Files.exists(Paths.get("plugins/privateservers/database/mobdatabase.json"))) {
            new Configuration()
                    .addValue("mobs", mobMap)
                    .write(Paths.get("plugins/privateservers/database/mobdatabase.json"));
        }

        this.mobMap = Configuration.parse(Paths.get("plugins/privateservers/database/mobdatabase.json"))
                .getValue("mobs", new TypeToken<Map<UUID, Mob>>() {
                });
    }

    @Override
    public void insert(Mob mob) {
        this.mobMap.put(mob.getUniqueID(), mob);
        this.save();
    }

    @Override
    public void forget(UUID key) {
        this.mobMap.remove(key);
        this.save();
    }

    @Override
    public Map<UUID, Mob> getAll() {
        return new HashMap<>(this.mobMap);
    }

    @Override
    public void save() {
        new Configuration()
                .addValue("mobs", mobMap)
                .write(Paths.get("plugins/privateservers/database/mobdatabase.json"));
    }
}
