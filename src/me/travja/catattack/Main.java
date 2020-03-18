package me.travja.catattack;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        registerEntity("Ocelot", 98, EntityOcelot.class, CustomCat.class);
    }

    public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        try {

            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void spawn(CreatureSpawnEvent event) {
        Entity ent = event.getEntity();
        if(ent.getType() != EntityType.OCELOT)
            return;

        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            double closeDist = 20d;
            Player nearest = null;
            for (Entity near : ent.getNearbyEntities(5, 5, 5)) {
                if((nearest == null || closeDist > ent.getLocation().distance(near.getLocation())) && near instanceof Player) {
                    nearest = (Player) near;
                    closeDist = ent.getLocation().distance(near.getLocation());
                }
            }

            if(nearest != null) {
                Ocelot cat = (Ocelot) ent;
                cat.setTamed(true);
                cat.setOwner(nearest);
            }
        }

    }
}
