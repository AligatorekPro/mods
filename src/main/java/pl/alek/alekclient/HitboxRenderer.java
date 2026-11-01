package pl.alek.alekclient;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Rysuje hitboxy w kolorach zaleznych od typu bytu.
 * Linie sa rysowane z testem glebokosci, wiec NIE widac ich przez sciany.
 */
public class HitboxRenderer {

    public static void render(WorldRenderContext ctx) {
        AlekConfig cfg = AlekClient.config;
        if (cfg == null || !cfg.hitboxesEnabled) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || ctx.consumers() == null || ctx.matrixStack() == null) {
            return;
        }

        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        float tickDelta = ctx.tickCounter().getTickDelta(false);
        VertexConsumer lines = ctx.consumers().getBuffer(RenderLayer.getLines());

        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        for (Entity e : mc.world.getEntities()) {
            if (e == mc.cameraEntity && mc.options.getPerspective().isFirstPerson()) {
                continue;
            }
            if (e.squaredDistanceTo(cam) > 64.0 * 64.0) {
                continue;
            }
            float[] c = colorFor(e, cfg);
            Vec3d lerped = e.getLerpedPos(tickDelta);
            Box box = e.getBoundingBox().offset(lerped.x - e.getX(), lerped.y - e.getY(), lerped.z - e.getZ());
            WorldRenderer.drawBox(matrices, lines, box, c[0], c[1], c[2], 1.0f);
        }

        matrices.pop();

        if (ctx.consumers() instanceof VertexConsumerProvider.Immediate immediate) {
            immediate.draw(RenderLayer.getLines());
        }
    }

    private static float[] colorFor(Entity e, AlekConfig cfg) {
        if (e instanceof PlayerEntity) {
            return cfg.playerRgb;
        }
        if (e instanceof Monster) {
            return cfg.mobRgb;
        }
        if (e instanceof AnimalEntity || e instanceof PassiveEntity
                || e instanceof WaterCreatureEntity || e instanceof AmbientEntity) {
            return cfg.animalRgb;
        }
        if (e instanceof ItemEntity) {
            return cfg.itemRgb;
        }
        return cfg.otherRgb;
    }
}
