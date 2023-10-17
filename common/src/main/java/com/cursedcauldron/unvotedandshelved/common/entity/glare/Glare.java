package com.cursedcauldron.unvotedandshelved.common.entity.glare;

import com.cursedcauldron.unvotedandshelved.client.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USMemoryModules;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Glare extends AgeableMob implements FlyingAnimal {
    // ENTITY DATA
    private static final EntityDataAccessor<Boolean> DATA_IS_GRUMPY = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_FLOWERING = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_TRACKING_DARKNESS = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_GRUMPINESS_TICKS = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HELD_GLOWBERRIES = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.INT);

    // TEMPTATIONS
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.GLOW_BERRIES);

    public Glare(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 5, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    // ========== DATA CONTROL =========================================================================================

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_GRUMPY, false);
        this.entityData.define(DATA_IS_FLOWERING, false);
        this.entityData.define(DATA_IS_TRACKING_DARKNESS, false);
        this.entityData.define(DATA_GRUMPINESS_TICKS, 0);
        this.entityData.define(DATA_HELD_GLOWBERRIES, 0);
    }

    public void setGrumpy(boolean grumpy) {
        this.entityData.set(DATA_IS_GRUMPY, grumpy);
    }

    public boolean isGrumpy() {
        return this.entityData.get(DATA_IS_GRUMPY);
    }

    public void setFlowering(boolean flowering) {
        this.entityData.set(DATA_IS_FLOWERING, flowering);
    }

    public boolean isFlowering() {
        return this.entityData.get(DATA_IS_FLOWERING);
    }

    public void setTrackingDarkness(boolean trackingDarkness) {
        this.entityData.set(DATA_IS_TRACKING_DARKNESS, trackingDarkness);
    }

    public boolean isTrackingDarkness() {
        return this.entityData.get(DATA_IS_TRACKING_DARKNESS);
    }

    public void setGrumpinessTicks(int grumpyTicks) {
        this.entityData.set(DATA_GRUMPINESS_TICKS, grumpyTicks);
    }

    public int getGrumpinessTicks() {
        return this.entityData.get(DATA_GRUMPINESS_TICKS);
    }

    public void setHeldGlowberries(int glowberries) {
        this.entityData.set(DATA_HELD_GLOWBERRIES, glowberries);
        this.brain.setMemory(USMemoryModules.GLOWBERRIES_GIVEN.get(), glowberries);
    }

    public int getHeldGlowberries() {
        return this.entityData.get(DATA_HELD_GLOWBERRIES);
    }

    @Override
    public boolean isFlying() {
        return this.level.getBlockState(this.blockPosition()).isAir();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsGrumpy", this.isGrumpy());
        tag.putBoolean("IsFlowering", this.isFlowering());
        tag.putBoolean("IsTrackingDarkness", this.isTrackingDarkness());

        tag.putInt("GrumpyTicks", this.getGrumpinessTicks());

        if (this.brain.getMemory(USMemoryModules.TRACKING_DARKNESS_TICKS.get()).isPresent()) {
            tag.putInt("TrackingDarknessTicks", this.brain.getMemory(USMemoryModules.TRACKING_DARKNESS_TICKS.get()).get());
        }

        if (this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
            tag.putInt("HeldGlowberries", this.brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGrumpy(tag.getBoolean("IsGrumpy"));
        this.setFlowering(tag.getBoolean("IsFlowering"));
        this.setGrumpinessTicks(tag.getInt("GrumpyTicks"));
        this.setTrackingDarkness(tag.getBoolean("IsTrackingDarkness"));
        this.setHeldGlowberries(tag.getInt("HeldGlowberries"));
    }

    // ========== BRAIN AI =============================================================================================

    @Override
    protected Brain.Provider<Glare> brainProvider() {
        return GlareBrain.provider();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("unchecked")
    public Brain<Glare> getBrain() {
        return (Brain<Glare>)super.getBrain();
    }

    // ========== BEHAVIOR =============================================================================================

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("glareBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().popPush("glareActivityUpdate");
        GlareBrain.updateActivity(this);
        this.level.getProfiler().pop();

        if (!this.isNoAi()) {
            Optional<Integer> trackingDarknessTicks = this.getBrain().getMemory(USMemoryModules.TRACKING_DARKNESS_TICKS.get());
            this.setTrackingDarkness(trackingDarknessTicks.isPresent() && trackingDarknessTicks.get() > 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.getGameTime() % 20L == 0L) {
            this.updateGrumpy(this.level);
        }

        int berries = this.getHeldGlowberries();
        if (berries > 0) {
            this.setPersistenceRequired();
            this.setHeldGlowberries(berries);
            this.level.addParticle(USParticles.GLOWBERRY_DUST.get(), this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6), 0.0, 0.0, 0.0);
        }

        if (this.isLeashed()) {
            this.setPersistenceRequired();
            if (!this.getBlockStateOn().isAir()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResult result = super.mobInteract(player, hand);
        boolean hasBerries = stack.is(Items.GLOW_BERRIES);

        if (result.consumesAction()) {
            return result;
        } else if (!this.level.isClientSide) {
            Brain<?> brain = this.getBrain();
            if (brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).isPresent()) {
                int berries = brain.getMemory(USMemoryModules.GLOWBERRIES_GIVEN.get()).get();
                if (berries < 5) {
                    if (hasBerries) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                            brain.setMemory(USMemoryModules.GIVE_GLOWBERRIES.get(), this);
                        }

                        this.setHeldGlowberries(berries + 1);
                        this.playSound(USSoundEvents.GLARE_GIVE_GLOWBERRIES.get(), 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        } else {
            return hasBerries ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    private void updateGrumpy(Level level) {
        if (!level.isClientSide) {
            int blockBrightness = level.getBrightness(LightLayer.BLOCK, this.blockPosition());
            int skyBrightness = level.getBrightness(LightLayer.SKY, this.blockPosition());
            int skyLight = level.getSkyDarken();
            boolean inBoat = this.getVehicle() instanceof Boat;
            this.setGrumpy((blockBrightness == 0 && (skyLight > 0 ? skyBrightness >= 0 : skyBrightness == 0)) || blockBrightness == 0 && level.isThundering() || inBoat);
        }
    }

    public void setLightBlock(BlockPos pos) {
        BlockState state = USBlocks.GLOWBERRY_DUST.get().defaultBlockState();
        if (this.level.getBlockState(pos).isAir()) {
            this.level.setBlockAndUpdate(pos, state);
            this.playSound(USSoundEvents.GLOWBERRY_DUST_PLACE.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return super.getStandingEyeHeight(pose, entityDimensions);
    }


    // ========== SPAWNING =============================================================================================

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getBrain().hasMemoryValue(USMemoryModules.GIVE_GLOWBERRIES.get());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag tag) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMobGroupData(false);
        }

        this.setFlowering(this.random.nextInt(100) == 1);
        return spawnGroupData;
    }

    @Nullable @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    // ========== PATH FINDING =========================================================================================

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluid) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    // ========== LEASH BEHAVIOR =======================================================================================

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    @Override
    public Iterable<BlockPos> iteratePathfindingStartNodeCandidatePositions() {
        AABB boundingBox = this.getBoundingBox();
        int minX = Mth.floor(boundingBox.minX - 0.5D);
        int minY = Mth.floor(boundingBox.minY - 0.5D);
        int minZ = Mth.floor(boundingBox.minZ - 0.5D);
        int maxX = Mth.floor(boundingBox.maxX - 0.5D);
        int maxY = Mth.floor(boundingBox.maxY - 0.5D);
        int maxZ = Mth.floor(boundingBox.maxZ - 0.5D);
        return BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ);
    }

    // ========== SOUNDS ===============================================================================================

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isGrumpy() ? USSoundEvents.GLARE_GRUMPY_IDLE.get() : USSoundEvents.GLARE_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return USSoundEvents.GLARE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return USSoundEvents.GLARE_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.MOSS_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.5F, 1.0F);
    }
}