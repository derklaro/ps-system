package de.derklaro.privateservers.configuration;

import com.google.common.base.Preconditions;
import com.google.gson.reflect.TypeToken;
import de.derklaro.privateservers.configuration.language.defaults.English;
import de.derklaro.privateservers.configuration.language.defaults.German;
import de.derklaro.privateservers.configuration.language.defaults.messages.Message;
import de.derklaro.privateservers.mobs.MobConfig;
import de.derklaro.privateservers.utility.configuration.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class MessageConfig {

    private Message loaded;

    public MessageConfig(MobConfig mobConfig) {
        if (!Files.exists(Paths.get("plugins/privateservers/language/" + mobConfig.getLanguage() + ".json"))) {
            Preconditions.checkArgument(new File("plugins/privateservers/language").mkdirs());
            Message message = getByPrefix(mobConfig.getLanguage());
            new Configuration()
                    .addValue("messages", message)
                    .write("plugins/privateservers/language/" + mobConfig.getLanguage() + ".json");
        }

        this.loaded = Configuration.parse("plugins/privateservers/language/" + mobConfig.getLanguage() + ".json")
                .getValue("messages", new TypeToken<Message>() {
                });
    }

    public static Message getByPrefix(String in) {
        switch (in.toLowerCase()) {
            case "german":
                return new German();
            case "english":
            default:
                return new English();
        }
    }

    public Message getLoaded() {
        return this.loaded;
    }
}
