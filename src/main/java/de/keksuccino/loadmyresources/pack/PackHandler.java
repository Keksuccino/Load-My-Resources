package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class PackHandler {

    public static File resourcesDirectory = new File("resources/");

    public static void init() {

        if (LoadMyResources.config != null) {
            resourcesDirectory = new File(LoadMyResources.config.getOrDefault("resource_path", "resources/"));
        } else {
            System.err.println("[LOAD MY RESOURCES] ERROR: Config not loaded! Can't get resource path! Path set to default 'resources/'!");
        }

        System.out.println("[LOAD MY RESOURCES] PackHandler initialized!");

    }

    public static void prepareResourcesFolder() {

        if (!resourcesDirectory.isDirectory()) {
            resourcesDirectory.mkdirs();
        }

    }

    public static void addPackToDefaults(IResourcePack pack) {
        try {
            Field f = ObfuscationReflectionHelper.findField(Minecraft.class, "field_110449_ao");
            List<IResourcePack> defaultPacks = (List<IResourcePack>) f.get(Minecraft.getMinecraft());
            defaultPacks.add(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


