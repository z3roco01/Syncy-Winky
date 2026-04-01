package z3roco01.syncywinky.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z3roco01.syncywinky.SyncyWinkyClient;

import java.io.File;

@Mixin(Options.class)
public class OptionsMixin {
    @Shadow
    @Final
    private File optionsFile;

    @Unique
    private static File globalOptionsFile;

    @Inject(method = "<init>", at = @At("HEAD"))
    private static void init(Minecraft minecraft, File workingDirectory, CallbackInfo ci) {
        globalOptionsFile = new File(System.getProperty("user.home") + "/.config/syncy-winky/options.txt");
        // just need to create global config directory if it does not exist
        if(!globalOptionsFile.getParentFile().exists()) {
            // create all parent directories
            if(!globalOptionsFile.getParentFile().mkdirs()) {
                throw new RuntimeException("Could not create configuration directory \"" + globalOptionsFile.getParentFile().toString() + "\" for Syncy Winky !!!");
            }
            SyncyWinkyClient.LOGGER.info("Created directory for config with path: " + globalOptionsFile.getParentFile().toString());
        }
    }

    @Redirect(method = "load", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;optionsFile:Ljava/io/File;", opcode = Opcodes.GETFIELD))
    private File loadGetOptionsFile(Options options) {
        return globalOptionsFile;
    }

    @Redirect(method = "save", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;optionsFile:Ljava/io/File;", opcode = Opcodes.GETFIELD))
    private File saveGetOptionsFile(Options instance) {
        return globalOptionsFile;
    }

    // not used in Options class grrrrr but just in #case
    @Inject(method = "getFile", at = @At("HEAD"), cancellable = true)
    private void getFile(CallbackInfoReturnable<File> cir) {
        cir.setReturnValue(globalOptionsFile);
        cir.cancel();
    }
}
