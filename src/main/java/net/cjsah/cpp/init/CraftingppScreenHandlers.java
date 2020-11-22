package net.cjsah.cpp.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.cjsah.cpp.inventory.CraftingMachineInventory;
import net.cjsah.cpp.inventory.CraftingMachineScreenHandler;

public class CraftingppScreenHandlers {
    public static final ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_SCREEN_SCREEN_HANDLER;

    static {
        CRAFTING_SCREEN_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier("cpp","cpp.container"), (i, pinv, buf) -> new CraftingMachineScreenHandler(i, pinv, new CraftingMachineInventory(pinv.player.getStackInHand(buf.readEnumConstant(Hand.class)))));
    }

}
