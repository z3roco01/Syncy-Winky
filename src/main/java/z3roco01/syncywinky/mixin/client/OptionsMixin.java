package z3roco01.syncywinky.mixin.client;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
//? if >=1.19.2 {
import net.minecraft.client.OptionInstance;
//?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
    // backport old settings so they wont get overwritten
    // all added after 1.18
    //? if <=1.18.2 {
    /*@Unique
    private static double darknessEffectScale;
    @Unique
    private static boolean directionalAudio;
    *///?}
    //? if <=1.19.2 {
    /*@Unique
    private static boolean narratorHotkey;
    *///?}
    //? if <=1.21.1 {
    @Unique
    private static String musicFrequency;
    @Unique
    private static String musicToast;
    //?}

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

        //? if <=1.18.2 {
        /*directionalAudio = false;
        darknessEffectScale = 1.0;*/
        //?}
        //? if <=1.19.2 {
        /*narratorHotkey = true;
        *///?}
        //? if <=1.21.1 {
        musicFrequency = "DEFAULT";
        musicToast = "never";
        //?}
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

    // Dont datafix, preserves non existant options from newer versions
    @Inject(method = "dataFix", at = @At("HEAD"), cancellable = true)
    private void dataFix(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
    }

    // make backported options loaded/stored
    @Inject(method = "processOptions", at = @At("TAIL"))
    private void processOptions(Options.FieldAccess fieldAccess, CallbackInfo ci) {
        //? if <=1.18.2 {
        /*directionalAudio = fieldAccess.process("directionalAudio", directionalAudio);
        darknessEffectScale = fieldAccess.process("darknessEffectScale", darknessEffectScale);
        *///?}
        //? if <=1.19.2 {
        /*narratorHotkey = fieldAccess.process("narratorHotkey", narratorHotkey);
        *///?}
        //? if <=1.21.1 {
        musicFrequency = fieldAccess.process("musicFrequency", musicFrequency);
        musicToast = fieldAccess.process("musicToast", musicToast);
        //?}
    }
}
