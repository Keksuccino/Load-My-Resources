package de.keksuccino.loadmyresources;

import de.keksuccino.loadmyresources.events.RegisterRepositorySourceEvent;
import de.keksuccino.loadmyresources.pack.LMRRepositorySource;
import de.keksuccino.loadmyresources.pack.PackHandler;
import de.keksuccino.loadmyresources.utils.config.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("loadmyresources")
public class LoadMyResources {

    public static final String VERSION = "1.0.1";
    
    public static final File HOME_DIR = new File("config/loadmyresources/");

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    public LoadMyResources() {

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (FMLEnvironment.dist == Dist.CLIENT) {

            if (!HOME_DIR.exists()) {
                HOME_DIR.mkdirs();
            }

            updateConfig();

            PackHandler.init();

            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterResourcePacks);

        } else {
            LOGGER.warn("## WARNING ## 'Load My Resources' is a client mod and has no effect when loaded on a server!");
        }

    }

    private void onRegisterResourcePacks(RegisterRepositorySourceEvent e) {
        e.addRepositorySource(new LMRRepositorySource());
        LOGGER.info("[LOAD MY RESOURCES] Pack registered!");
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
