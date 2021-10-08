package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.File;

@Mod("loadmyresources")
public class LoadMyResources {

    public static final String VERSION = "1.0.0";
    
    public static final File HOME_DIR = new File("config/loadmyresources/");

    public static Config config;

    public LoadMyResources() {

        if (FMLEnvironment.dist == Dist.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInitClient);

//            MinecraftForge.EVENT_BUS.register(new Test());

        } else {
            System.out.println("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

    }

    //Forge Only - Mixin needed in Fabric: Add 'LoadMyRepositorySource' instance to 'providers' field in TAIL of 'ResourcePackManager' constructor
    private void onInitClient(ParticleFactoryRegisterEvent e) {

        Minecraft.getInstance().getResourcePackRepository().addPackFinder(new LMRRepositorySource());
        Minecraft.getInstance().getResourcePackRepository().reload();
        System.out.println("[LOAD MY RESOURCES] Pack registered!");

    }

    public static void updateConfig() {

        try {

            config = new Config(HOME_DIR.getPath() + "/config.cfg");

            //---------------------

            config.registerValue("resource_path", "resources/", "general", "The path to load resources from.");

            //---------------------

            config.syncConfig();

            config.clearUnusedValues();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
