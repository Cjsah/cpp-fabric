package net.cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedForceOfFire extends Item {
	/**
	 * 公用精准镐，用于获取方块的原样掉落物
	 */
	public static final ItemStack SILK_TOUCH_PICKAXE = new ItemStack(Items.NETHERITE_PICKAXE);

	public RedForceOfFire(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient) {
			boolean success = false;
			BlockPos pos = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY).getBlockPos();
			BlockState blockState = world.getBlockState(pos);
			List<ItemStack> list = blockState.getDroppedStacks(new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, SILK_TOUCH_PICKAXE));
			for (ItemStack itemStack : list) {
				SmeltingRecipe recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(itemStack), world).orElse(null);
				if (recipe != null) {
					ItemStack output = recipe.getOutput().copy();
					output.setCount(itemStack.getCount());
					ItemEntity spawnItem = new ItemEntity(world, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, output);
					spawnItem.setToDefaultPickupDelay();
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					world.spawnEntity(spawnItem);
					user.incrementStat(Stats.USED.getOrCreateStat(this));
					success = true;
				}
			}
			if (success) {
				((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 1, 1));
				((ServerPlayerEntity) user).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FLAME, false, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, .2f, .2f, .2f, .02f, 3));
				return TypedActionResult.success(item);
			}
		}
		return TypedActionResult.pass(item);
	}

	static {
		SILK_TOUCH_PICKAXE.addEnchantment(Enchantments.SILK_TOUCH, 1);
	}
}
