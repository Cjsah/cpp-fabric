package net.cpp.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
//TODO
public class GolemHerderEntity extends AGolemEntity {

	public GolemHerderEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient && world.getTime() % 1200 == 0) {
//			Map<Class<? extends MobEntity>, List<ItemStack>> map = new HashMap<>();
//			Map<Item, List<ItemStack>> map2 = new HashMap<>();
//			List<ItemStack> tmpList = new LinkedList<>();
//
//			map.put(HorseEntity.class, tmpList);
//			map.put(DonkeyEntity.class, tmpList);
//			map2.put(GOLDEN_APPLE, tmpList);
//			map2.put(GOLDEN_CARROT, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(SheepEntity.class, tmpList);
//			map.put(CowEntity.class, tmpList);
//			map.put(MooshroomEntity.class, tmpList);
//			map2.put(WHEAT, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(PigEntity.class, tmpList);
//			map2.put(CARROT, tmpList);
//			map2.put(POTATO, tmpList);
//			map2.put(BEETROOT, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(ChickenEntity.class, tmpList);
//			map2.put(BEETROOT_SEEDS, tmpList);
//			map2.put(MELON_SEEDS, tmpList);
//			map2.put(PUMPKIN_SEEDS, tmpList);
//			map2.put(WHEAT_SEEDS, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(WolfEntity.class, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(CatEntity.class, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(OcelotEntity.class, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(RabbitEntity.class, tmpList);
//
//			tmpList = new LinkedList<>();
//			map.put(LlamaEntity.class, tmpList);
//			List<ItemStack> defList = new LinkedList<>();
//			for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()), entity -> true)) {
//				Item item = itemEntity.getStack().getItem();
//				map2.getOrDefault(item, defList).add(itemEntity.getStack());
//			}
			List<AnimalEntity> animals = world.getEntitiesByClass(AnimalEntity.class, new Box(getPos(), getPos()).expand(16), animal -> animal.getBreedingAge() == 0);
			int s = animals.size();
			System.out.println(s);
			Block block = world.getBlockState(getBlockPos().up()).getBlock();
			for (AnimalEntity animal : animals) {
//				int c = 0;
//				for1: for (ItemStack stack: map.get(animal.getClass())) {
//					while (!stack.isEmpty()) {
//						stack.decrement(1);
//						c++;
//						if (c >=2 )
//							break for1;
//					}
//				}
//				if (c>=2) {
//					
//				}
				animal.setLoveTicks(600);
				if (animal instanceof SheepEntity)
					((SheepEntity) animal).sheared(SoundCategory.MASTER);
				if (block == Blocks.RED_WOOL && s >= 24 || block == Blocks.PINK_WOOL && s >= 48 || block == Blocks.MAGENTA_WOOL && s >= 96)
					animal.damage(DamageSource.mob(this), 100);
			}
			for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()).expand(16), itemEntity -> itemEntity.getStack().isOf(Items.EGG))) {
				int c = itemEntity.getStack().getCount();
				itemEntity.setStack(ItemStack.EMPTY);
				Vec3d pos = itemEntity.getPos();
				while (c-- > 0) {
					EggEntity eggEntity = new EggEntity(world, pos.x, pos.y, pos.z);
					world.spawnEntity(eggEntity);
				}
			}
		}
	}
}
