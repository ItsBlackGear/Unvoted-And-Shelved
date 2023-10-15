package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem;

import com.cursedcauldron.unvotedandshelved.common.entity.USPoses;
import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USDataSerializers;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CopperGolem extends AbstractGolem {
    // ENTITY DATA
    public static final EntityDataAccessor<WeatherState> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, USDataSerializers.WEATHER_STATE);
    public static final EntityDataAccessor<Boolean> DATA_IS_WAXED = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);

    // ENTITY EVENTS
    public static final byte SPAWN_SPARK_PARTICLES = 14;

    // ANIMATIONS
    public final AnimationState walkingAnimation = new AnimationState();
    public final AnimationState headSpinAnimation = new AnimationState();
    public final AnimationState interactingAnimation = new AnimationState();
    public final AnimationState interactingAboveAnimation = new AnimationState();
    public final AnimationState interactingBelowAnimation = new AnimationState();

    public CopperGolem(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    // ========== DATA CONTROL =========================================================================================

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WEATHER_STATE, WeatherState.UNAFFECTED);
        this.entityData.define(DATA_IS_WAXED, false);
    }

    public void setWeatherState(WeatherState state) {
        this.entityData.set(DATA_WEATHER_STATE, state);
        this.applySpeedModifier(state);
    }

    public WeatherState getWeatherState() {
        return this.entityData.get(DATA_WEATHER_STATE);
    }

    public void setWaxed(boolean waxed) {
        this.entityData.set(DATA_IS_WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.entityData.get(DATA_IS_WAXED);
    }

    public boolean isOxidized() {
        return this.getWeatherState() == WeatherState.OXIDIZED;
    }

    public boolean isMoving() {
        return this.onGround && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && !this.isInWaterRainOrBubble();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("State", this.getWeatherState().ordinal());
        tag.putBoolean("Waxed", this.isWaxed());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setWeatherState(WeatherState.values()[tag.getInt("State")]);
        this.setWaxed(tag.getBoolean("Waxed"));
    }

    // ========== BRAIN AI =============================================================================================

    @Override
    protected Brain.Provider<CopperGolem> brainProvider() {
        return CopperGolemBrain.provider();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CopperGolemBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    @Override @SuppressWarnings("unchecked")
    public Brain<CopperGolem> getBrain() {
        return (Brain<CopperGolem>)super.getBrain();
    }

    // ========== ANIMATION ============================================================================================

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_POSE.equals(data)) {
            if (this.inPose(USPoses.HEAD_SPIN.get())) {
                this.headSpinAnimation.startIfStopped(this.tickCount);
            } else {
                this.headSpinAnimation.stop();
            }

            if (this.inPose(USPoses.INTERACT.get())) {
                this.interactingAnimation.startIfStopped(this.tickCount);
            } else {
                this.interactingAnimation.stop();
            }

            if (this.inPose(USPoses.INTERACT_ABOVE.get())) {
                this.interactingAboveAnimation.startIfStopped(this.tickCount);
            } else {
                this.interactingAboveAnimation.stop();
            }

            if (this.inPose(USPoses.INTERACT_BELOW.get())) {
                this.interactingBelowAnimation.startIfStopped(this.tickCount);
            } else {
                this.interactingBelowAnimation.stop();
            }

            if (this.isOxidized()) {
                this.headSpinAnimation.stop();
                this.interactingAnimation.stop();
                this.interactingAboveAnimation.stop();
                this.interactingBelowAnimation.stop();
            }
        }

        super.onSyncedDataUpdated(data);
    }

    private boolean inPose(Pose pose) {
        return this.getPose() == pose;
    }

    // ========== PARTICLES ============================================================================================

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SPAWN_SPARK_PARTICLES) {
            spawnParticlesAtRod(this.level, this);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public static void spawnParticlesAtRod(Level level, LivingEntity entity) {
        if (level.random.nextInt(200) <= level.getGameTime() % 200L && level.canSeeSky(entity.blockPosition())) {
            spawnSparklingParticles(level, entity.position().add(0, 1, 0), UniformInt.of(1, 2));
        }
    }

    private static void spawnSparklingParticles(Level level, Vec3 pos, IntProvider amount) {
        int i = amount.sample(level.random);

        for (int j = 0; j < i; j++) {
            double x = pos.x + Mth.nextDouble(level.random, -1.0, 1.0) * 0.125;
            double y = pos.y + Mth.nextDouble(level.random, 0.75, 1.25) * 0.5;
            double z = pos.z + Mth.nextDouble(level.random, -1.0, 1.0) * 0.125;
            double speed = Mth.nextDouble(level.random, -1.0, 1.0);
            level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, speed, 0);
        }
    }

    // ========== SOUNDS ===============================================================================================

    @Nullable @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return USSoundEvents.COPPER_GOLEM_HIT.get();
    }

    @Nullable @Override
    protected SoundEvent getDeathSound() {
        return USSoundEvents.COPPER_GOLEM_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(USSoundEvents.COPPER_GOLEM_WALK.get(), 0.5F, 1.0F);
    }

    public SoundEvent getHeadSpinSound() {
        return switch (this.getWeatherState()) {
            case UNAFFECTED -> USSoundEvents.HEAD_SPIN.get();
            case EXPOSED -> USSoundEvents.HEAD_SPIN_SLOWER.get();
            case WEATHERED -> USSoundEvents.HEAD_SPIN_SLOWEST.get();
            case OXIDIZED -> null;
        };
    }

    // ========== BEHAVIOR =============================================================================================

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("copperGolemBrain");
        this.getBrain().tick((ServerLevel)this.level, this);
        this.level.getProfiler().popPush("copperGolemActivityUpdate");
        CopperGolemBrain.updateActivity(this);
        this.level.getProfiler().pop();
        super.customServerAiStep();

        if (this.level.isThundering() && this.level instanceof ServerLevel) {
            this.level.broadcastEntityEvent(this, SPAWN_SPARK_PARTICLES);
        }
    }

    private void applySpeedModifier(WeatherState state) {
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(0.5D + state.ordinal() * -0.125D);
        }
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            if (!this.isOxidized() && this.isMoving()) {
                this.walkingAnimation.startIfStopped(this.tickCount);
            } else {
                this.walkingAnimation.stop();
            }
        }

        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.isOxidized()) {
                this.convertTo(USEntities.OXIDIZED_COPPER_GOLEM.get(), true);
            }

            if (!this.isWaxed() && !this.isOxidized()) {
                if (this.random.nextFloat() < 3.4290552E-5F) {
                    this.setWeatherState(WeatherState.values()[this.getWeatherState().ordinal() + 1]);
                }
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Check if the player is using honeycomb and if the copper golem is not waxed
        if (stack.is(Items.HONEYCOMB) && !this.isWaxed()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1); // Reduce the honeycomb amount
            }

            // Waxes the copper golem and spawn particles.
            this.setWaxed(true);
            this.level.levelEvent(player, 3003, this.blockPosition(), 0);
            this.gameEvent(GameEvent.ENTITY_INTERACT, this);

            return InteractionResult.SUCCESS;
        }

        // Check if the player is using an Axe
        if (stack.getItem() instanceof AxeItem) {
            if (this.isWaxed()) {
                // Remove the wax from the copper golem and spawn particles
                this.setWaxed(false);
                this.playSound(SoundEvents.AXE_WAX_OFF, 1.0F, 1.0F);
                this.level.levelEvent(player, 3004, this.blockPosition(), 0);
                this.gameEvent(GameEvent.ENTITY_INTERACT, this);

                // Reduce the durability of the axe
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            } else {
                if (this.getWeatherState() != WeatherState.UNAFFECTED) {
                    // Shift the copper golem state backwards and spawn particles
                    this.setWeatherState(WeatherState.values()[this.getWeatherState().ordinal() - 1]);
                    this.playSound(SoundEvents.AXE_SCRAPE, 1.0F, 1.0F);
                    this.level.levelEvent(player, 3005, this.blockPosition(), 0);
                    this.gameEvent(GameEvent.ENTITY_INTERACT, this);

                    // Reduce the durability of the axe
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                } else {
                    // If the copper golem cannot be scrapped, skip the interaction
                    return InteractionResult.PASS;
                }
            }

            return InteractionResult.SUCCESS;
        }

        // If no specific conditions are met, proceed as normal.
        return super.mobInteract(player, hand);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (this.getWeatherState() != WeatherState.UNAFFECTED && !this.isWaxed()) {
            this.setWeatherState(WeatherState.values()[this.getWeatherState().ordinal() - 1]);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.75F;
    }
}