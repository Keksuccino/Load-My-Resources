package de.keksuccino.loadmyresources.pack;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LMRFolderResourcePack extends FolderResourcePack {

    public LMRFolderResourcePack() {
        super(PackHandler.resourcesDirectory);
    }

    @Override
    public String getPackName() {
        return PackHandler.PACK_NAME;
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return (T)readMetadata(metadataSerializer, Minecraft.getMinecraft().getResourceManager().getResource(PackHandler.DUMMY_PACK_META).getInputStream(), metadataSectionName);
    }

    private static <T extends IMetadataSection> T readMetadata(MetadataSerializer metadataSerializer, InputStream p_110596_1_, String sectionName) {
        JsonObject jsonobject = null;
        BufferedReader bufferedreader = null;

        try {
            bufferedreader = new BufferedReader(new InputStreamReader(p_110596_1_, StandardCharsets.UTF_8));
            jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
        } catch (RuntimeException runtimeexception) {
            throw new JsonParseException(runtimeexception);
        } finally {
            IOUtils.closeQuietly((Reader)bufferedreader);
        }

        return (T)metadataSerializer.parseMetadataSection(sectionName, jsonobject);
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public Set<String> getResourceDomains() {
        Set<String> s = Sets.newHashSet();
        File[] files = this.resourcePackFile.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for(File f : files) {
                String path = getRelativePath(this.resourcePackFile, f);
                if (path.equals(path.toLowerCase(Locale.ROOT))) {
                    s.add(path.substring(0, path.length() - 1));
                }
            }
        }
        return s;
    }

    protected static String getRelativePath(File f1, File f2) {
        return f1.toURI().relativize(f2.toURI()).getPath();
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        return this.getInputStreamByName(getPathFromLocation(location));
    }

    //To change the assets dir from packRoot/assets to packRoot/
    @Override
    public boolean resourceExists(ResourceLocation location) {
        return this.hasResourceName(getPathFromLocation(location));
    }

    private static String getPathFromLocation(ResourceLocation location) {
        return String.format("%s/%s", location.getResourceDomain(), location.getResourcePath());
    }

}
