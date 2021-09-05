package cpp.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class CppConfigScreen extends Screen {
    private final Screen screen;

    protected CppConfigScreen(Screen screen) {
        super(new TranslatableText("cpp.config.title"));
        this.screen = screen;
    }
}
