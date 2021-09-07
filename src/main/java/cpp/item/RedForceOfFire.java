package cpp.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RedForceOfFire extends Item {
	/**
	 * 公用精准镐，用于获取方块的原样掉落物
	 */
	public static final ItemStack SILK_TOUCH_PICKAXE = new ItemStack(Items.NETHERITE_PICKAXE);

	public RedForceOfFire(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Text getName(ItemStack stack) {
		return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			BlockPos pos = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY).getBlockPos();
			BlockState blockState = world.getBlockState(pos);
			ItemStack product = blockState.getBlock().asItem().getDefaultStack();
			if (product != (product = smelt(product, world.getServer(), world))) {
				ItemEntity spawnItem = new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, product);
				spawnItem.setToDefaultPickupDelay();
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.spawnEntity(spawnItem);
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), .5f, 1));
				((ServerPlayerEntity) user).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FLAME, false, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, .2f, .2f, .2f, .02f, 3));
				return TypedActionResult.success(stack);
			}
		}
		return TypedActionResult.pass(stack);
	}

	public static ItemStack smelt(ItemStack stack, MinecraftServer server,@Nullable World world) {
		ItemStack product = stack;
		SmeltingRecipe recipe = server.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(stack), world == null ? server.getOverworld() : world).orElse(null);
		if (recipe != null) {
			product = recipe.getOutput().copy();
		}
		return product;
	}

	static {
		SILK_TOUCH_PICKAXE.addEnchantment(Enchantments.SILK_TOUCH, 1);
	}
}
