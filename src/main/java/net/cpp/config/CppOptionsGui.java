/*
来自gbl/GBfabrictools仓库
 */

package net.cpp.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cpp.Craftingpp;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static net.minecraft.client.gui.widget.AbstractButtonWidget.WIDGETS_LOCATION;

public class CppOptionsGui extends Screen implements Supplier<Screen> {

    private final Screen screen;
//    private final ModConfigurationHandler handler;
//    private final List<String> options;
    private final Logger LOGGER;

    private String screenTitle;

    private static final int LINEHEIGHT = 25;
    private static final int BUTTONHEIGHT = 20;
    private static final int TOP_BAR_SIZE = 40;
    private static final int BOTTOM_BAR_SIZE = 35;

    private boolean isDraggingScrollbar = false;
    private boolean mouseReleased = false;      // used to capture mouse release events when a child slider has the mouse dragging

    private int buttonWidth;
    private int scrollAmount;
    private int maxScroll;

    private static final Text trueText = new TranslatableText("de.guntram.mcmod.fabrictools.true").formatted(Formatting.GREEN);
    private static final Text falseText = new TranslatableText("de.guntram.mcmod.fabrictools.false").formatted(Formatting.RED);

    protected CppOptionsGui(Screen screen, String modName) {
        super(new LiteralText(modName));
        this.screen = screen;
        this.screenTitle = modName + "Configuration";
        this.LOGGER = Craftingpp.logger;

    }

    @Override
    protected void init() {
        buttonWidth = this.width / 2 -50;
        if (buttonWidth > 200)
            buttonWidth = 200;

        this.addButton(new AbstractButtonWidget(this.width / 2 - 100, this.height - 27, 200, BUTTONHEIGHT, new TranslatableText("gui.done")) {
            @Override
            public void onClick(double x, double y) {

                for (AbstractButtonWidget button: buttons) {
                    if (button instanceof TextFieldWidget) {
                        if (button.isFocused()) {
                            button.changeFocus(false);
                        }
                    }
                }
//                handler.onConfigChanged(new ConfigChangedEvent.OnConfigChangedEvent(modName));
                client.openScreen(screen);
            }
        });
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);

//        int y = TOP_BAR_SIZE + LINEHEIGHT/2 - scrollAmount;
//        for (int i=0; i<this.options.size(); i++) {
//            if (y > TOP_BAR_SIZE - LINEHEIGHT/2 && y < height - BOTTOM_BAR_SIZE) {
//                textRenderer.draw(stack, new TranslatableText(options.get(i)).asOrderedText(), this.width / 2 -155, y+4, 0xffffff);
//                ((AbstractButtonWidget)this.buttons.get(i*2+1)).y = y;                                          // config elem
//                ((AbstractButtonWidget)this.buttons.get(i*2+1)).render(stack, mouseX, mouseY, partialTicks);
//                ((AbstractButtonWidget)this.buttons.get(i*2+2)).y = y;                                        // reset button
//                ((AbstractButtonWidget)this.buttons.get(i*2+2)).render(stack, mouseX, mouseY, partialTicks);
//            }
//            y += LINEHEIGHT;
//        }

//        y = TOP_BAR_SIZE + LINEHEIGHT/2 - scrollAmount;
//        for (String text: options) {
//            if (y > TOP_BAR_SIZE - LINEHEIGHT/2 && y < height - BOTTOM_BAR_SIZE) {
//                if (mouseX>this.width/2-155 && mouseX<this.width/2 && mouseY>y && mouseY<y+BUTTONHEIGHT) {
//                    String ttText = handler.getIConfig().getTooltip(text);
//                    if (ttText == null || ttText.isEmpty()) {
//                        y += LINEHEIGHT;
//                        continue;
//                    }
//                    TranslatableText tooltip=new TranslatableText(handler.getIConfig().getTooltip(text));
//                    int width = textRenderer.getWidth(tooltip);
//                    if (width == 0) {
//                        // do nothing
//                    } else if (width<=250) {
//                        renderTooltip(stack, tooltip, 0, mouseY);
//                    } else {
//                        List<OrderedText> lines = textRenderer.wrapLines(tooltip, 250);
//                        renderOrderedTooltip(stack, lines, 0, mouseY);
//                    }
//                }
//            }
//            y+=LINEHEIGHT;
//        }

        if (maxScroll > 0) {
            // fill(stack, width-5, TOP_BAR_SIZE, width, height - BOTTOM_BAR_SIZE, 0xc0c0c0);
            int pos = (int)((height - TOP_BAR_SIZE - BOTTOM_BAR_SIZE - BUTTONHEIGHT) * ((float)scrollAmount / maxScroll));
            // fill(stack, width-5, pos, width, pos+BUTTONHEIGHT, 0x303030);
            this.client.getTextureManager().bindTexture(WIDGETS_LOCATION);
            drawTexture(stack, width-5, pos+TOP_BAR_SIZE, 0, 66, 4, 20);
        }

        this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.disableDepthTest();
        drawTexture(stack, 0, 0, 0, 0, width, TOP_BAR_SIZE);
        drawTexture(stack, 0, height-BOTTOM_BAR_SIZE, 0, 0, width, BOTTOM_BAR_SIZE);

        drawCenteredString(stack, textRenderer, screenTitle, this.width/2, (TOP_BAR_SIZE - textRenderer.fontHeight)/2, 0xffffff);
        this.buttons.get(0).render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public Screen get() {
        return this;
    }
}
