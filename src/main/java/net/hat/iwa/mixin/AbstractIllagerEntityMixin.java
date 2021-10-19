package net.hat.iwa.mixin;

import net.hat.iwa.IWAExtraStuff;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(IllagerEntity.class)
public abstract class AbstractIllagerEntityMixin extends RaiderEntity {
	protected AbstractIllagerEntityMixin(EntityType<? extends RaiderEntity> type, World worldIn) {
		super(type, worldIn);
	}

	// this is a temp way to get illagers to spawn with armor until pr = merged
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		if (spawnReason == SpawnReason.EVENT && this.getRaid() != null) {
			this.giveArmorOnRaids();
		} else {
			this.giveArmorNaturally(difficulty);
		}
		super.initEquipment(difficulty);

		return entityData;
	}

	public void giveArmorOnRaids() {
		float f = this.world.getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
		int illagerWaves = Objects.requireNonNull(this.getRaid()).getGroupsSpawned();
		int armorChance = Math.min(illagerWaves, 4);
		float waveChances = IWAExtraStuff.getWaveArmorChances(illagerWaves);
		if (this.random.nextFloat() < waveChances) {
			if (this.random.nextFloat() < 0.045F) {
				++armorChance;
			}

			boolean flag = true;

			for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
				if (equipmentslottype.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack itemstack = this.getEquippedStack(equipmentslottype);
					if (!flag && this.random.nextFloat() < f) {
						break;
					}

					flag = false;
					if (itemstack.isEmpty()) {
						Item item = getEquipmentForSlot(equipmentslottype, armorChance);
						if (item != null) {
							this.equipStack(equipmentslottype, new ItemStack(item));
						}
					}
				}
			}
		}
	}

	protected void giveArmorNaturally(LocalDifficulty difficulty) {
		if (this.random.nextFloat() < 0.15F * difficulty.getClampedLocalDifficulty()) {
			int i = this.random.nextInt(2);
			float f = this.world.getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
			if (this.random.nextFloat() < 0.095F) {
				++i;
			}

			if (this.random.nextFloat() < 0.095F) {
				++i;
			}

			if (this.random.nextFloat() < 0.095F) {
				++i;
			}

			boolean flag = true;

			for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
				if (equipmentslottype.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack itemstack = this.getEquippedStack(equipmentslottype);
					if (!flag && this.random.nextFloat() < f) {
						break;
					}

					flag = false;
					if (itemstack.isEmpty()) {
						Item item = getEquipmentForSlot(equipmentslottype, i);
						if (item != null) {
							this.equipStack(equipmentslottype, new ItemStack(item));
						}
					}
				}
			}
		}
	}

}
