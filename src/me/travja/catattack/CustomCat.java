package me.travja.catattack;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import java.lang.reflect.Field;


public class CustomCat extends EntityOcelot {

    public CustomCat() {
        super(null);
    }

    public CustomCat(World world) {
        super(world);
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        this.targetSelector.a();
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, false, null));
    }
}
