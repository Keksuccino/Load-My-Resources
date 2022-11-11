package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.pack.LMRFolderResourcePack;
import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "loadmyresources", acceptedMinecraftVersions="[1.12,1.12.2]", dependencies = "required:forge@[14.23.5.2855,]", clientSideOnly = true)
public class LoadMyResources {

    public static final String VERSION = "1.0.2";

    public static final File HOME_DIR = new File(Minecraft.getMinecraft().mcDataDir, "config/loadmyresources/");

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    public LoadMyResources() {

        if (FMLClientHandler.instance().getSide() == Side.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            initPack();

//            MinecraftForge.EVENT_BUS.register(new Test());

        } else {
            LOGGER.info("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

    }

    private static void initPack() {

        PackHandler.addPackToDefaults(new LMRFolderResourcePack());
        LOGGER.info("[LOAD MY RESOURCES] Pack registered!");

    }

    public static void updateConfig() {

        try {

            config = new Config(HOME_DIR.getAbsolutePath() + "/config.cfg");

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
