package pl.alek.alekclient.mixin;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pl.alek.alekclient.AlekClient;

/** Freelook: kamera uzywa wlasnych katow zamiast katow gracza. */
@Mixin(Camera.class)
public abstract class CameraMixin {

    @ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float alekclient$yaw(float yaw) {
        return AlekClient.freelookActive ? AlekClient.freelookYaw : yaw;
    }

    @ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float alekclient$pitch(float pitch) {
        return AlekClient.freelookActive ? AlekClient.freelookPitch : pitch;
    }
}
