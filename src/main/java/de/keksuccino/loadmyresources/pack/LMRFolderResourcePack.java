package de.keksuccino.loadmyresources.pack;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LMRFolderResourcePack extends FolderResourcePack {

    public LMRFolderResourcePack() {
        super(PackHandler.resourcesDirectory);
    }

    @Override
    public String getPackName() {
        return "Load My Resources";
    }

    //To not need to create a pack.mcmeta file
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        if (metadataSectionName.equals("language")) {
            return (T) new LanguageMetadataSection(new ArrayList<Language>());
        } else {
            return (T) new PackMetadataSection(new TextComponentString(""), 6);
        }
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
