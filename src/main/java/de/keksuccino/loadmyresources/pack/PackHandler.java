package de.keksuccino.loadmyresources.pack;

import de.keksuccino.loadmyresources.LoadMyResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class PackHandler {

    public static final String PACK_NAME = "loadmyresources.hiddenpack";
//    public static final int PACK_FORMAT = 6;
//    public static final String PACK_DESCRIPTION = "LMR Resources";
    public static final ResourceLocation DUMMY_PACK_META = new ResourceLocation("loadmyresources", "dummy.pack.mcmeta");

    public static File resourcesDirectory = new File("resources/");

    public static void init() {

        if (LoadMyResources.config != null) {
            resourcesDirectory = new File(LoadMyResources.config.getOrDefault("resource_path", "resources/"));
        } else {
            LoadMyResources.LOGGER.error("[LOAD MY RESOURCES] ERROR: Config not loaded! Can't get resource path! Path set to default 'resources/'!");
        }

        LoadMyResources.LOGGER.info("[LOAD MY RESOURCES] PackHandler initialized!");

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


