package net.cpp.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static net.minecraft.item.Items.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class GolemHerderEntity extends AGolemEntity {

	public GolemHerderEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient && world.getTime() % 600 == 0) {
			Map<Class<? extends MobEntity>, List<ItemStack>> map = new HashMap<>();
			Map<Item, List<ItemStack>> map2 = new HashMap<>();
			List<ItemStack> tmpList = new LinkedList<>();

			map.put(HorseEntity.class, tmpList);
			map.put(DonkeyEntity.class, tmpList);
			map2.put(GOLDEN_APPLE, tmpList);
			map2.put(GOLDEN_CARROT, tmpList);

			tmpList = new LinkedList<>();
			map.put(SheepEntity.class, tmpList);
			map.put(CowEntity.class, tmpList);
			map.put(MooshroomEntity.class, tmpList);
			map2.put(WHEAT, tmpList);

			tmpList = new LinkedList<>();
			map.put(PigEntity.class, tmpList);
			map2.put(CARROT, tmpList);
			map2.put(POTATO, tmpList);
			map2.put(BEETROOT, tmpList);

			tmpList = new LinkedList<>();
			map.put(ChickenEntity.class, tmpList);
			map2.put(BEETROOT_SEEDS, tmpList);
			map2.put(MELON_SEEDS, tmpList);
			map2.put(PUMPKIN_SEEDS, tmpList);
			map2.put(WHEAT_SEEDS, tmpList);

			tmpList = new LinkedList<>();
			map.put(WolfEntity.class, tmpList);

			tmpList = new LinkedList<>();
			map.put(CatEntity.class, tmpList);

			tmpList = new LinkedList<>();
			map.put(OcelotEntity.class, tmpList);

			tmpList = new LinkedList<>();
			map.put(RabbitEntity.class, tmpList);

			tmpList = new LinkedList<>();
			map.put(LlamaEntity.class, tmpList);
			// TODO
			List<ItemStack> defList = new LinkedList<>();
			for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()), entity -> true)) {
				Item item = itemEntity.getStack().getItem();
				map2.getOrDefault(item, defList).add(itemEntity.getStack());
			}
			for (AnimalEntity animal : world.getEntitiesByClass(AnimalEntity.class, new Box(getPos(), getPos()), entity -> true)) {
				int c = 0;
				for1: for (ItemStack stack: map.get(animal.getClass())) {
					while (!stack.isEmpty()) {
						stack.decrement(1);
						c++;
						if (c >=2 )
							break for1;
					}
				}
				if (c>=2) {
					
				}
			}
		}

	}

}
