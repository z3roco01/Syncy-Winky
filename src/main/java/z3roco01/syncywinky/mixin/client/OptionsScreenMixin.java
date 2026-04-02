package z3roco01.syncywinky.mixin.client;

import net.minecraft.client.Options;
//?if <=1.19.2{
/*import net.minecraft.client.gui.screens.OptionsScreen;*/
//?}
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z3roco01.syncywinky.ResourcePackUtil;

//? if <=1.19.2 {
/*@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow
    @Final
    private Options options;

    protected OptionsScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "updatePackList", at = @At("HEAD"), cancellable = true)
    private void updatePackList(PackRepository packRepository, CallbackInfo ci) {
        ResourcePackUtil.updateResourcePacks(packRepository, this.options, this.minecraft);
        ci.cancel();
    }
}
*///?} else {
@Mixin(Screen.class) // garbage mixin to avoid error on newer versions
public class OptionsScreenMixin {
}
//?}
