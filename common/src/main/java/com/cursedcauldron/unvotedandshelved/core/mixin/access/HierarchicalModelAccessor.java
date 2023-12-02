package com.cursedcauldron.unvotedandshelved.core.mixin.access;

import com.mojang.math.Vector3f;
import net.minecraft.client.model.HierarchicalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HierarchicalModel.class)
public interface HierarchicalModelAccessor {
    @Accessor("ANIMATION_VECTOR_CACHE")
    static Vector3f animationVectorCache() {
        throw new UnsupportedOperationException();
    }
}
