package pl.alek.alekclient.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.alek.alekclient.AlekClient;

/** Zoom: kolko myszy przy wcisnietym zoomie zmienia przyblizenie (zamiast hotbara). */
@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void alekclient$zoomScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (AlekClient.zoomActive && vertical != 0.0 && AlekClient.config != null) {
            double factor = AlekClient.config.zoomFactor * (vertical > 0 ? 1.15 : 1.0 / 1.15);
            AlekClient.config.zoomFactor = Math.max(1.1, Math.min(50.0, factor));
            ci.cancel();
        }
    }
}
