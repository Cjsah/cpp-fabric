package cpp.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import cpp.Craftingpp;
import cpp.api.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

public class CppGameHud extends DrawableHelper {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier HUD_ICON = new Identifier(Craftingpp.MOD_ID3, "textures/gui/effect_hud.png");
    private static final int margin = 5;
    private static final int interval = 1;
    private static final int icon_size = 12;
    private static final int text_left = margin + icon_size + 3;

    @SuppressWarnings("ConstantConditions")
    public void render(MatrixStack matrix) {
        if (client.options.debugEnabled) return;
        ServerPlayerEntity player = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());
        if (player != null) {
            NbtCompound nbt = new NbtCompound();
            player.writeCustomDataToNbt(nbt);
            int weight = nbt.getInt("weight");
            NbtList vaccines = nbt.getList("Vaccines", 10);
            int width = 51;
            int height = margin + (vaccines.size() + 1) * icon_size + vaccines.size() * interval + margin;
            int firstY = client.getWindow().getScaledHeight() - margin - icon_size;
            fill(matrix, 0, client.getWindow().getScaledHeight() - height, width, client.getWindow().getScaledHeight(), client.options.getTextBackgroundColor(0.3F));
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            this.draw(matrix, client.getWindow().getScaledHeight() - margin - icon_size, 5, new LiteralText(String.valueOf(weight)).formatted(
                    weight > -50 && weight < 50 ? Formatting.GREEN : weight > -100 && weight < 100 ? Formatting.YELLOW : Formatting.RED));
            for(int i = 1; i <= vaccines.size(); ++i) {
                NbtCompound vaccine = (NbtCompound) vaccines.get(i - 1);
                this.draw(matrix, firstY - i * (icon_size + interval), vaccine.getByte("Id"), Text.of(Utils.ticksToTime(vaccine.getInt("Duration"))));
            }
            RenderSystem.disableBlend();
        }
    }

    private void draw(MatrixStack matrix, int y, int iconIndex, StringVisitable content) {
        RenderSystem.setShaderTexture(0, HUD_ICON);
        DrawableHelper.drawTexture(matrix, margin, y, 0.0F, iconIndex * icon_size, icon_size, icon_size, icon_size, icon_size * 6);
        client.textRenderer.draw(matrix, Language.getInstance().reorder(content), text_left, y + 2, 0xFFFFFF);
    }
}
