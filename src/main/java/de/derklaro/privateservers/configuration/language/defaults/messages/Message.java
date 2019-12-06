package de.derklaro.privateservers.configuration.language.defaults.messages;

import org.bukkit.ChatColor;

public class Message {

    private String prefix;

    private String alreadyStarted;

    private String serverGroupNotFound;

    private String commandMobDeleted;

    private String commandMobNotExists;

    private String commandMobAlreadyExists;

    private String commandMobMobTypeInvalid;

    private String commandMobHelp;

    private String noPrivateServerStarted;

    private String statusPrivateServerOnline;

    private String statusPrivateServerOffline;

    private String stopSuccessful;

    private String startNoPermission;

    private String startStarting;

    private String startUnknownError;

    private String commandPluginHelp;

    private String commandPluginInstalled;

    private String commandPluginUninstalled;

    private String commandPluginPluginNotAvailable;

    private String resetPleaseWait;

    private String resetNoCustomTemplate;

    private String resetTemplateRemoved;

    private String resetCancelled;

    public Message(String prefix, String alreadyStarted, String serverGroupNotFound, String commandMobDeleted, String commandMobNotExists, String commandMobAlreadyExists, String commandMobMobTypeInvalid, String commandMobHelp, String noPrivateServerStarted, String statusPrivateServerOnline, String statusPrivateServerOffline, String stopSuccessful, String startNoPermission, String startStarting, String startUnknownError, String commandPluginHelp, String commandPluginInstalled, String commandPluginUninstalled, String commandPluginPluginNotAvailable, String resetPleaseWait, String resetNoCustomTemplate, String resetTemplateRemoved, String resetCancelled) {
        this.prefix = prefix;
        this.alreadyStarted = alreadyStarted;
        this.serverGroupNotFound = serverGroupNotFound;
        this.commandMobDeleted = commandMobDeleted;
        this.commandMobNotExists = commandMobNotExists;
        this.commandMobAlreadyExists = commandMobAlreadyExists;
        this.commandMobMobTypeInvalid = commandMobMobTypeInvalid;
        this.commandMobHelp = commandMobHelp;
        this.noPrivateServerStarted = noPrivateServerStarted;
        this.statusPrivateServerOnline = statusPrivateServerOnline;
        this.statusPrivateServerOffline = statusPrivateServerOffline;
        this.stopSuccessful = stopSuccessful;
        this.startNoPermission = startNoPermission;
        this.startStarting = startStarting;
        this.startUnknownError = startUnknownError;
        this.commandPluginHelp = commandPluginHelp;
        this.commandPluginInstalled = commandPluginInstalled;
        this.commandPluginUninstalled = commandPluginUninstalled;
        this.commandPluginPluginNotAvailable = commandPluginPluginNotAvailable;
        this.resetPleaseWait = resetPleaseWait;
        this.resetNoCustomTemplate = resetNoCustomTemplate;
        this.resetTemplateRemoved = resetTemplateRemoved;
        this.resetCancelled = resetCancelled;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public String getAlreadyStarted() {
        return ChatColor.translateAlternateColorCodes('&', alreadyStarted).replace("%prefix%", getPrefix());
    }

    public String getServerGroupNotFound() {
        return ChatColor.translateAlternateColorCodes('&', serverGroupNotFound).replace("%prefix%", getPrefix());
    }

    public String getCommandMobDeleted() {
        return ChatColor.translateAlternateColorCodes('&', commandMobDeleted).replace("%prefix%", getPrefix());
    }

    public String getCommandMobNotExists() {
        return ChatColor.translateAlternateColorCodes('&', commandMobNotExists).replace("%prefix%", getPrefix());
    }

    public String getCommandMobAlreadyExists() {
        return ChatColor.translateAlternateColorCodes('&', commandMobAlreadyExists).replace("%prefix%", getPrefix());
    }

    public String getCommandMobMobTypeInvalid() {
        return ChatColor.translateAlternateColorCodes('&', commandMobMobTypeInvalid).replace("%prefix%", getPrefix());
    }

    public String getCommandMobHelp() {
        return ChatColor.translateAlternateColorCodes('&', commandMobHelp).replace("%prefix%", getPrefix());
    }

    public String getNoPrivateServerStarted() {
        return ChatColor.translateAlternateColorCodes('&', noPrivateServerStarted).replace("%prefix%", getPrefix());
    }

    public String getStatusPrivateServerOnline() {
        return ChatColor.translateAlternateColorCodes('&', statusPrivateServerOnline).replace("%prefix%", getPrefix());
    }

    public String getStatusPrivateServerOffline() {
        return ChatColor.translateAlternateColorCodes('&', statusPrivateServerOffline).replace("%prefix%", getPrefix());
    }

    public String getStopSuccessful() {
        return ChatColor.translateAlternateColorCodes('&', stopSuccessful).replace("%prefix%", getPrefix());
    }

    public String getStartNoPermission() {
        return ChatColor.translateAlternateColorCodes('&', startNoPermission).replace("%prefix%", getPrefix());
    }

    public String getStartStarting() {
        return ChatColor.translateAlternateColorCodes('&', startStarting).replace("%prefix%", getPrefix());
    }

    public String getStartUnknownError() {
        return ChatColor.translateAlternateColorCodes('&', startUnknownError).replace("%prefix%", getPrefix());
    }

    public String getCommandPluginHelp() {
        return ChatColor.translateAlternateColorCodes('&', commandPluginHelp).replace("%prefix%", getPrefix());
    }

    public String getCommandPluginInstalled() {
        return ChatColor.translateAlternateColorCodes('&', commandPluginInstalled).replace("%prefix%", getPrefix());
    }

    public String getCommandPluginUninstalled() {
        return ChatColor.translateAlternateColorCodes('&', commandPluginUninstalled).replace("%prefix%", getPrefix());
    }

    public String getCommandPluginPluginNotAvailable() {
        return ChatColor.translateAlternateColorCodes('&', commandPluginPluginNotAvailable).replace("%prefix%", getPrefix());
    }

    public String getResetPleaseWait() {
        return ChatColor.translateAlternateColorCodes('&', resetPleaseWait).replace("%prefix%", getPrefix());
    }

    public String getResetNoCustomTemplate() {
        return ChatColor.translateAlternateColorCodes('&', resetNoCustomTemplate).replace("%prefix%", getPrefix());
    }

    public String getResetCancelled() {
        return ChatColor.translateAlternateColorCodes('&', resetCancelled).replace("%prefix%", getPrefix());
    }

    public String getResetTemplateRemoved() {
        return ChatColor.translateAlternateColorCodes('&', resetTemplateRemoved).replace("%prefix%", getPrefix());
    }
}
