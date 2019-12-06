package de.derklaro.privateservers.configuration.language.defaults;

import de.derklaro.privateservers.configuration.language.defaults.messages.Message;

public final class English extends Message {

    public English() {
        super(
                "§6§lP§6rivate§6§lS§6ervers §7§l●",
                "%prefix% §7Your server is already started",
                "%prefix% §7The server group configured was not found, please contact an administrator",
                "%prefix% §7The mob was deleted successfully",
                "%prefix% §7The mob doesn't exists",
                "%prefix% §7The mob already exists",
                "%prefix% §7The given mob type is invalid",
                " %prefix% /privateservers create <MOB-TYPE> <NAME> <DISPLAY-NAME> \n %prefix% /privateservers delete <NAME> \n %prefix% /privateservers list \n %prefix% /privateservers available",
                "%prefix% §7There is no private server started",
                "%prefix% §7Your private server is online :)",
                "%prefix% §7Your private server is offline :(",
                "%prefix% §7Your server is now stopping...",
                "%prefix% §7You don't have the permission to start a server with this template :(",
                "%prefix% §7Starting your server, this could take a moment",
                "%prefix% §7An unknown error occurred while starting up the process",
                " %prefix% /plugin install <name> \n %prefix% /plugin uninstall <name> \n %prefix% /plugin available \n %prefix% /plugin list \n %prefix% /plugin gui",
                "%prefix% §7The plugin was installed successfully",
                "%prefix% §7The plugin was uninstalled successfully",
                "%prefix% §7The plugin isn't available",
                "%prefix% §7Deleting your server, please wait several seconds...",
                "%prefix% §7You don't have a custom template",
                "%prefix% §7Your custom template was removed, but the server still exists",
                "%prefix% §7You cancelled the delete"
        );
    }
}
