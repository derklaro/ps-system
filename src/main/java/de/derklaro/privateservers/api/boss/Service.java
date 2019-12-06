package de.derklaro.privateservers.api.boss;

import de.derklaro.privateservers.utility.api.AbstractBossService;

public final class Service extends AbstractBossService {

    protected boolean lobby;

    public Service(boolean lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean isLobbyInstance() {
        return this.lobby;
    }
}
