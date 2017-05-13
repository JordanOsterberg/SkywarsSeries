package tech.shadowsystems.skywars.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import tech.shadowsystems.skywars.Skywars;

/**
 * Copyright © 2016 Jordan Osterberg and Shadow Technical Systems LLC. All rights reserved. Please email jordan.osterberg@shadowsystems.tech for usage rights and other information.
 */
public class EntitySpawn implements Listener {

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (Skywars.getInstance().isSingleServerMode()) {
            if (event.getEntityType() != EntityType.PLAYER && event.getEntityType() != EntityType.DROPPED_ITEM && event.getEntityType() != EntityType.PRIMED_TNT) {
                event.setCancelled(true);
            }
        }
    }

}
