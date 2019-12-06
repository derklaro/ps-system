package de.derklaro.privateservers.mobs.config;

public final class PermissionConfig {

    private String commandOnOwnerJoin;

    private String commandOnOwnerLeave;

    private String opMessage;

    private boolean setOP;

    public PermissionConfig(String commandOnOwnerJoin, String commandOnOwnerLeave, String opMessage, boolean setOP) {
        this.commandOnOwnerJoin = commandOnOwnerJoin;
        this.commandOnOwnerLeave = commandOnOwnerLeave;
        this.opMessage = opMessage;
        this.setOP = setOP;
    }

    public String getCommandOnOwnerJoin() {
        return this.commandOnOwnerJoin;
    }

    public String getCommandOnOwnerLeave() {
        return this.commandOnOwnerLeave;
    }

    public String getOpMessage() {
        return this.opMessage;
    }

    public boolean isSetOP() {
        return this.setOP;
    }
}
