package z3roco01.syncywinky;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// centralise some logic that is used across versions
public class ResourcePackUtil {
    public static List<String> appliedResourcePacks = Lists.newArrayList();
    // Save resourcepacks per instance
    private static final String resourcePacksFilePath = "./resourcepacks.txt";

    // this method exists in different places in different versions, so centralise it
    public static void updateResourcePacks(PackRepository packRepository, Options options, Minecraft minecraft) {
        List<String> oldPacks = ImmutableList.copyOf(options.resourcePacks);
        appliedResourcePacks.clear();

        for(Pack entry : packRepository.getSelectedPacks()) {
            if (!entry.isFixedPosition()) {
                appliedResourcePacks.add(entry.getId());
                if (!entry.getCompatibility().isCompatible()) {
                    options.incompatibleResourcePacks.add(entry.getId());
                }
            }
        }

        options.save();
        try {
            saveResourcePacks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> newPacks = ImmutableList.copyOf(appliedResourcePacks);
        if (!newPacks.equals(oldPacks)) {
            minecraft.reloadResourcePacks();
        }
    }

    // saves loaded packs to instance specific file
    private static void saveResourcePacks() throws IOException {
        FileWriter packsWriter = new FileWriter(resourcePacksFilePath);

        for(String pack : appliedResourcePacks) {
            packsWriter.write(pack);
            packsWriter.write(",");
        }

        packsWriter.flush();
        packsWriter.close();
    }

    // read in all local resource packs, split by comma
    public static void loadResourcePacks() throws IOException {
        File packsFile = new File(resourcePacksFilePath);
        if(!packsFile.exists() || !packsFile.canRead())
            return;

        ArrayList<String> packsList = Lists.newArrayList();
        String packsFileContents = new String(Files.readAllBytes(packsFile.toPath()), StandardCharsets.UTF_8);

        String[] splitPacks = packsFileContents.split(",");
        Collections.addAll(packsList, splitPacks);

        appliedResourcePacks = packsList;
    }
}
