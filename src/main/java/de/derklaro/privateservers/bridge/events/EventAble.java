package de.derklaro.privateservers.bridge.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

abstract class EventAble extends Event {

    EventAble() {
        super(!Bukkit.getServer().isPrimaryThread());
    }

}
