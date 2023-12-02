package com.cursedcauldron.unvotedandshelved.core.mixin.client;

import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(KeyframeAnimations.class)
public class KeyframeAnimationsMixin {
    @Shadow
    private static float getElapsedSeconds(AnimationDefinition animationDefinition, long accumulatedTime) {
        return 0;
    }

    @Inject(method = "animate", at = @At("HEAD"), cancellable = true)
    private static void us$animate(HierarchicalModel<?> model, AnimationDefinition animationDefinition, long accumulatedTime, float scale, Vector3f cache, CallbackInfo ci) {
        float elapsedSeconds = getElapsedSeconds(animationDefinition, accumulatedTime);
        for (Map.Entry<String, List<AnimationChannel>> entries : animationDefinition.boneAnimations().entrySet()) {
            Optional<ModelPart> modelPart = model.getAnyDescendantWithName(entries.getKey());
            List<AnimationChannel> animations = entries.getValue();
            modelPart.ifPresent(part -> animations.forEach(animation -> {
                Keyframe[] keyframes = animation.keyframes();
                int startIndex = Math.max(0, Mth.binarySearch(0, keyframes.length, ix -> elapsedSeconds <= keyframes[ix].timestamp()) - 1);
                int finalIndex = Math.min(keyframes.length - 1, startIndex + 1);
                Keyframe startKeyframe = keyframes[startIndex];
                Keyframe finalKeyframe = keyframes[finalIndex];
                float timeSinceStartKeyframe = elapsedSeconds - startKeyframe.timestamp();
                float interpolationFactor = finalIndex != startIndex ? Mth.clamp(timeSinceStartKeyframe / (finalKeyframe.timestamp() - startKeyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
                finalKeyframe.interpolation().apply(cache, interpolationFactor, keyframes, startIndex, finalIndex, scale);
                animation.target().apply(part, cache);
            }));
        }

        ci.cancel();
    }
}