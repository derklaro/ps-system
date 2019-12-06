package de.derklaro.privateservers.commands;

import de.derklaro.privateservers.PrivateServers;
import de.derklaro.privateservers.mobs.config.Mob;
import de.derklaro.privateservers.utility.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public final class CommandPrivateServers implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            PrivateServers.getInstance().getMobSelector().getSpawnedMobs().forEach((k, v) ->
                    commandSender.sendMessage(" => " + v.mob.getMobPosition())
            );
            return true;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("available")) {
            Collection<EntityType> entityTypes = CollectionUtils.filterAll(EntityType.values(), e -> e.getEntityClass() != null);
            for (EntityType entityType : entityTypes) {
                if (entityType.isSpawnable())
                    commandSender.sendMessage(entityType.getEntityClass().getSimpleName());
            }
            return true;
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("delete")) {
            if (PrivateServers.getInstance().getMobSelector().deleteMob(strings[1])) {
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandMobDeleted());
            } else
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandMobNotExists());

            return true;
        }

        if (strings.length >= 4 && strings[0].equalsIgnoreCase("create")) {
            if (PrivateServers.getInstance().getMobSelector().findMobByName(strings[3]) != null) {
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandMobAlreadyExists());
                return true;
            }

            EntityType entityType = CollectionUtils.filter(EntityType.values(), e -> e.getEntityClass() != null
                    && e.getEntityClass().getSimpleName().equalsIgnoreCase(strings[1]));
            if (entityType == null || !entityType.isSpawnable()) {
                commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandMobMobTypeInvalid());
                return false;
            }

            StringBuilder displayName = new StringBuilder();
            for (int i = 3; i < strings.length; i++)
                displayName.append(strings[i]).append(" ");

            Mob mob = new Mob(
                    UUID.randomUUID(),
                    entityType.getEntityClass().getSimpleName(),
                    strings[2],
                    ChatColor.translateAlternateColorCodes('&', displayName.substring(0, displayName.length() - 1)),
                    PrivateServers.getInstance().getMobSelector().toPosition(((Player) commandSender).getLocation())
            );
            PrivateServers.getInstance().getMobDatabase().insert(mob);
            PrivateServers.getInstance().getMobSelector().handleCreate(mob);
            return true;
        }

        commandSender.sendMessage(PrivateServers.getInstance().getMessages().getCommandMobHelp());

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2 && strings[0].equalsIgnoreCase("create"))
            return new LinkedList<>(available());

        if (strings.length == 3 && strings[0].equalsIgnoreCase("create"))
            return Arrays.asList("PrivateServerMob", "Mob", "derklaro", commandSender.getName());

        if (strings.length == 4 && strings[0].equalsIgnoreCase("create"))
            return Arrays.asList("&9&lPrivateServers", "&6&lStart your private server");

        if (strings.length == 2 && strings[0].equalsIgnoreCase("delete"))
            return new LinkedList<>(names());

        return new LinkedList<>(Arrays.asList("create", "delete", "list", "available"));
    }

    private Collection<String> names() {
        Collection<String> out = new LinkedList<>();
        PrivateServers.getInstance().getMobSelector().getSpawnedMobs().forEach((k, v) -> out.add(v.mob.getName()));
        return out;
    }

    private Collection<String> available() {
        Collection<String> out = new LinkedList<>();
        Collection<EntityType> collection = CollectionUtils.filterAll(EntityType.values(), e -> e.getEntityClass() != null && e.isSpawnable());
        collection.forEach(e -> out.add(e.getEntityClass().getSimpleName()));
        return out;
    }
}
