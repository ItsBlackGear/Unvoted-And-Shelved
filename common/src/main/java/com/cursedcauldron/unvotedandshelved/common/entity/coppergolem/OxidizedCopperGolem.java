package com.cursedcauldron.unvotedandshelved.common.entity.coppergolem;

import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class OxidizedCopperGolem extends AbstractGolem {
    private static final Predicate<Entity> RIDEABLE_MINECARTS = entity -> entity instanceof AbstractMinecart cart && cart.getMinecartType() == AbstractMinecart.Type.RIDEABLE;

    public static final byte SPAWN_SPARK_PARTICLES = 14;
    public static final byte SPAWN_DESTROY_PARTICLES = 32;

    public long lastHit;

    public OxidizedCopperGolem(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 0.0F;
    }

    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public boolean isEffectiveAi() {
        return super.isEffectiveAi() && !this.isNoGravity();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox(), RIDEABLE_MINECARTS);
        for (Entity entity : entities) {
            if (this.distanceToSqr(entity) <= 0.2) {
                entity.push(this);
            }
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof AxeItem) {
            this.convertTo(USEntities.COPPER_GOLEM.get(), true);
            this.gameEvent(GameEvent.ENTITY_INTERACT, this);
            this.playSound(SoundEvents.AXE_SCRAPE, 1.0F, 1.0F);
            this.level.levelEvent(player, 3005, this.blockPosition(), 0);

            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.SUCCESS;
        }

        return super.interactAt(player, vec, hand);
    }

    @Nullable @Override
    public <T extends Mob> T convertTo(EntityType<T> entityType, boolean transferInventory) {
        if (this.isRemoved()) {
            return null;
        } else {
            T mob = entityType.create(this.level);
            Objects.requireNonNull(mob);
            mob.copyPosition(this);
            mob.setNoAi(this.isNoAi());
            ((CopperGolem)mob).setWeatherState(WeatheringCopper.WeatherState.WEATHERED);

            if (this.hasCustomName()) {
                mob.setCustomName(this.getCustomName());
                mob.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistenceRequired()) {
                mob.setPersistenceRequired();
            }

            mob.setInvulnerable(this.isInvulnerable());

            this.level.addFreshEntity(mob);
            if (this.isPassenger() && this.getVehicle() != null) {
                this.stopRiding();
                mob.startRiding(this.getVehicle(), true);
            }

            this.discard();
            return mob;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level.isClientSide || this.isRemoved()) {
            return false;
        } else if (source == DamageSource.OUT_OF_WORLD) {
            this.kill();
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.isExplosion()) {
            this.playBrokenSound();
            this.kill();
            return false;
        } else if (source == DamageSource.IN_FIRE) {
            if (this.isOnFire()) {
                this.causeDamage(source, 0.15F);
            } else {
                this.setSecondsOnFire(5);
            }

            return false;
        } else if (source == DamageSource.ON_FIRE && this.getHealth() > 0.5F) {
            this.causeDamage(source, 4.0F);
            return false;
        } else {
            boolean isArrow = source.getDirectEntity() instanceof AbstractArrow;
            boolean canPierce = isArrow && ((AbstractArrow)source.getDirectEntity()).getPierceLevel() > 0;
            boolean isFromPlayer = source.getMsgId().equals("player");
            if (!isFromPlayer && !isArrow) {
                return false;
            } else if (source.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                return false;
            } else if (source.isCreativePlayer()) {
                this.playBrokenSound();
                this.showBreakingParticles();
                this.kill();
                return canPierce;
            } else {
                long gameTime = this.level.getGameTime();
                if (gameTime - this.lastHit > 5L && !isArrow) {
                    this.level.broadcastEntityEvent(this, SPAWN_DESTROY_PARTICLES);
                    this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
                    this.lastHit = gameTime;
                } else {
                    Block.popResource(this.level, this.blockPosition(), new ItemStack(Blocks.OXIDIZED_COPPER));
                    this.playSound(SoundEvents.COPPER_BREAK, 1.0F, 1.0F);
                    this.showBreakingParticles();
                    this.kill();
                }

                return true;
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == SPAWN_DESTROY_PARTICLES) {
            if (this.level.isClientSide) {
                this.playSound(SoundEvents.COPPER_HIT, 0.3F, 1.0F);
                this.lastHit = this.level.getGameTime();
            }
        } else if (id == SPAWN_SPARK_PARTICLES) {
            CopperGolem.spawnParticlesAtRod(this.level, this);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.level.isThundering() && this.level instanceof ServerLevel) {
            this.level.broadcastEntityEvent(this, SPAWN_SPARK_PARTICLES);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d) || d == 0.0) {
            d = 4.0;
        }

        d *= 64.0;
        return distance < d * d;
    }

    private void showBreakingParticles() {
        if (this.level instanceof ServerLevel server) {
            ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OXIDIZED_COPPER.defaultBlockState());
            server.sendParticles(particle, this.getX(), this.getY(0.6F), this.getZ(), 10, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05);
        }
    }

    private void causeDamage(DamageSource source, float amount) {
        float health = this.getHealth();
        health -= amount;

        if (health <= 0.5F) {
            this.playBrokenSound();
            this.kill();
        } else {
            this.setHealth(health);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
        }
    }

    private void playBrokenSound() {
        this.playSound(SoundEvents.COPPER_BREAK, 1.0F, 1.0F);
    }

    @Override
    protected float tickHeadTurn(float yRot, float animStep) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
        return 0.0F;
    }

    @Override
    public void travel(Vec3 travel) {
        if (!this.isNoGravity()) {
            super.travel(travel);
        }
    }

    @Override
    public void setYBodyRot(float yBodyRot) {
        this.yBodyRotO = this.yRotO = yBodyRot;
        this.yHeadRotO = this.yHeadRot = yBodyRot;
    }

    @Override
    public void setYHeadRot(float yHeadRot) {
        this.yBodyRotO = this.yRotO = yHeadRot;
        this.yHeadRotO = this.yHeadRot = yHeadRot;
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return entity instanceof Player player && !this.level.mayInteract(player, this.blockPosition());
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.COPPER_FALL, SoundEvents.COPPER_FALL);
    }

    @Nullable @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COPPER_HIT;
    }

    @Nullable @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COPPER_BREAK;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        this.convertTo(USEntities.COPPER_GOLEM.get(), true);
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Blocks.OXIDIZED_COPPER);
    }
}