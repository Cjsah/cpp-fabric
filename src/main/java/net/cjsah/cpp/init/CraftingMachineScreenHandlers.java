package net.cjsah.cpp.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;

public final class CraftingMachineScreenHandlers {
//    public static final ScreenHandlerType<net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler> CRAFTING_MACHINE_SCREEN_HANDLER;

    static {
//        CRAFTING_MACHINE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler.TITLE, (i, pinv, buf) -> new net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler(i, pinv, new CraftingMachineInventory(buf.readInt(), buf.readInt(), pinv.player.getStackInHand(buf.readEnumConstant(Hand.class)))));
    }

}
