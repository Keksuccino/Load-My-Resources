package de.keksuccino.loadmyresources.pack;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.FileUtil;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LMRPackResources extends PathPackResources {

    private static final Logger LOGGER = LogManager.getLogger("loadmyresources/LMRPackResources");
    private static final boolean ON_WINDOWS = Util.getPlatform() == Util.OS.WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');

    private File file;

    public LMRPackResources() {
        super(PackHandler.PACK_NAME, PackHandler.resourcesDirectory.toPath(), true);
        this.file = PackHandler.resourcesDirectory;
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) throws IOException {
        InputStream in = Minecraft.getInstance().getResourceManager().open(PackHandler.DUMMY_PACK_META);
        Object o;
        try {
            o = getMetadataFromStream(serializer, in);
        } catch (Throwable throwable1) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable throwable) {
                    throwable1.addSuppressed(throwable);
                }
            }
            throw throwable1;
        }

        if (in != null) {
            in.close();
        }
        return (T)o;
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Set<String> getNamespaces(PackType type) {
        Set<String> s = Sets.newHashSet();
        File[] files = this.file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for(File f : files) {
                String path = getRelativePath(this.file, f);
                if (path.equals(path.toLowerCase(Locale.ROOT))) {
                    s.add(path.substring(0, path.length() - 1));
                } else {
                    this.logWarning(path);
                }
            }
        }
        return s;
    }

    private static String getPathFromLocation(PackType p_10227_, ResourceLocation p_10228_) {
        return String.format(Locale.ROOT, "%s/%s/%s", p_10227_.getDirectory(), p_10228_.getNamespace(), p_10228_.getPath());
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Nullable
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        try {
            File f = this.getFile(getPathFromLocation(location));
            if (f != null) {
                return IoSupplier.create(f.toPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private File getFile(String p_10282_) {
        try {
            File file1 = new File(this.file, p_10282_);
            if (file1.isFile() && validatePath(file1, p_10282_)) {
                return file1;
            }
        } catch (IOException ioexception) {
        }
        return null;
    }

    public static boolean validatePath(File p_10274_, String p_10275_) throws IOException {
        String s = p_10274_.getCanonicalPath();
        if (ON_WINDOWS) {
            s = BACKSLASH_MATCHER.replaceFrom(s, '/');
        }
        return s.endsWith(p_10275_);
    }

    private static String getPathFromLocation(ResourceLocation location) {
        return String.format("%s/%s", location.getNamespace(), location.getPath());
    }

    @Override
    public void listResources(PackType packType, String namespace, String resourcePreNamePath, PackResources.ResourceOutput output) {
        this.listFiles(new File(new File(this.file, namespace), resourcePreNamePath), namespace, resourcePreNamePath + "/", output);
    }

    private void listFiles(File file, String string, String string2, PackResources.ResourceOutput output) {
        File[] files = file.listFiles();
        if (files != null) {
            File[] var7 = files;
            int var8 = files.length;
            for(int var9 = 0; var9 < var8; ++var9) {
                File file2 = var7[var9];
                if (file2.isDirectory()) {
                    this.listFiles(file2, string, string2 + file2.getName() + "/", output);
                } else if (!file2.getName().endsWith(".mcmeta")) {
                    try {
                        String string3 = string2 + file2.getName();
                        ResourceLocation resourceLocation = ResourceLocation.tryBuild(string, string3);
                        if (resourceLocation == null) {
                            LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", string, string3);
                        } else {
                            output.accept(resourceLocation, IoSupplier.create(file2.toPath()));
                        }
                    } catch (ResourceLocationException var13) {
                        LOGGER.error(var13.getMessage());
                    }
                }
            }
        }
    }

    protected static String getRelativePath(File p_10218_, File p_10219_) {
        return p_10218_.toURI().relativize(p_10219_.toURI()).getPath();
    }

    protected void logWarning(String p_10231_) {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", p_10231_, this.file);
    }

    //Forge Only - Mixins needed in Fabric version: Override 'updatePackList' in 'PackScreen'
    @Override
    public boolean isHidden() {
        return true;
    }

}
