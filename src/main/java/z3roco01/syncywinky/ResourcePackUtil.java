package z3roco01.syncywinky;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.List;

// centralise some logic that is used across versions
public class ResourcePackUtil {
    public static List<String> appliedResourcePacks = Lists.newArrayList();

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
        List<String> newPacks = ImmutableList.copyOf(appliedResourcePacks);
        if (!newPacks.equals(oldPacks)) {
            minecraft.reloadResourcePacks();
        }
    }
}
