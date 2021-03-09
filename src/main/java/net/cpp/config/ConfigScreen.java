package net.cpp.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.ModMenu;
import net.cpp.Craftingpp;
import net.cpp.api.TexturedButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ConfigScreen extends Screen {
    private static final Identifier RESET_BUTTON = new Identifier(ModMenu.MOD_ID, "textures/gui/configure_button.png");
    private static final Text RESET_TEXT = new TranslatableText("modmenu.cpp.reset");
    private final OptionsScreen parent;
    private final JsonObject value = new JsonObject();
    private final String key;
    private final JsonObject defaultValue;
    private final Map<Integer, Text> textY = new HashMap<>();
    private final Set<Integer> notSupport = new HashSet<>();
    private final Set<String> reloaded = new HashSet<>();
    private int drawY;
    private int panX;
    private int panWidth;

    protected ConfigScreen(OptionsScreen parent, String key, JsonObject defaultValue) {
        super(new LiteralText(key));
        this.parent = parent;
        this.key = key;
        for (Map.Entry<String, JsonElement> kv : Craftingpp.CONFIG.getConfig(key).entrySet()) {
            this.value.add(kv.getKey(), kv.getValue());
        }
        this.defaultValue = defaultValue;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void init() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(true);
        this.panX = this.width <= 395 + 50 + 50 + 80 ? 50 : (this.width - (395 + 80)) / 2 ;
        this.panWidth = Math.min(395, this.width - (this.panX + 80) - 50);

        // 重置按钮
        this.addButton(new TexturedButtonWidget(width - 40, 28, 20, 20, 0, 0, RESET_TEXT, RESET_BUTTON, 32, 64, button -> {
            for (Map.Entry<String, JsonElement> kv : defaultValue.entrySet()) {
                this.value.add(kv.getKey(), kv.getValue());
            }
            reloaded.clear();
        }, (buttonWidget, matrices, mouseX, mouseY) -> {
            TexturedButtonWidget button = (TexturedButtonWidget) buttonWidget;
            if (button.isJustHovered()) {
                this.renderTooltip(matrices, RESET_TEXT, mouseX, mouseY);
            } else if (button.isFocusedButNotHovered()) {
                this.renderTooltip(matrices, RESET_TEXT, button.x, button.y);
            }
        }) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

                this.active = !Objects.equals(value.toString(), defaultValue.toString());

                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                RenderSystem.color4f(1, 1, 1, 1f);
                super.renderButton(matrices, mouseX, mouseY, delta);
            }
        });

        // 文字和按钮
        this.drawY = 70;

        for (Map.Entry<String, JsonElement> kv: this.value.entrySet()) {
            this.textY.put(drawY, new TranslatableText("config.cpp." + kv.getKey()));
            this.addButtons(kv.getKey(), kv.getValue());
        }

        this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 28, 150, 20, ScreenTexts.CANCEL, (button) -> this.client.openScreen(this.parent)));
        this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20, ScreenTexts.DONE, (button) -> {
            if (!Objects.equals(value.toString(), Craftingpp.CONFIG.getConfig(key).toString())) {
                Craftingpp.CONFIG.changeConfig(key, value);
            }
            this.client.openScreen(this.parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.panX = this.width <= 395 + 50 + 50 + 80 ? 50 : (this.width - (395 + 80)) / 2 ;
        this.panWidth = Math.min(395, this.width - (this.panX + 80) - 50);

        drawTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2 - 40, 16, 16777215);

        // key
        for (Map.Entry<Integer, Text> kv : textY.entrySet()) {
            this.textRenderer.draw(matrices, Language.getInstance().reorder(kv.getValue()), (float) this.panX, (float) (kv.getKey() + 5), 16777215);
        }

        // not support
        for (int y : notSupport) {
            this.textRenderer.draw(matrices, new TranslatableText("config.cpp.notSupport"), (float) this.panX + 80, (float) (y + 5), 16777215);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void addButtons(String jk, JsonElement jv) {
        if (jv.isJsonObject()) {
            this.drawNotSupportText();
        }else if (jv.isJsonArray()) {
            this.addListWidget(jk, jv.getAsJsonArray());
        }else if (jv.isJsonNull()) {
            this.drawNotSupportText();
        }else if (jv.isJsonPrimitive()) {
            if (jv.getAsJsonPrimitive().isBoolean()) {
                this.addButtonWidget(jk);
            }else if (jv.getAsJsonPrimitive().isNumber()) {
                this.addNumberWidget(jk, jv.getAsNumber());
            }else if (jv.getAsJsonPrimitive().isString()) {
                this.addStringWidget(jk, jv.getAsString());
            }
        }else {
            this.drawNotSupportText();
        }
    }

    private void drawNotSupportText() {
        notSupport.add(drawY);
        this.drawY += 25;
    }

    private void addButtonWidget(String jk) {
        this.addButton(new AbstractButtonWidget(this.panX + 80, this.drawY, this.panWidth, 20, value.get(jk).getAsBoolean() ? ScreenTexts.YES : ScreenTexts.NO) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                value.addProperty(jk, !value.get(jk).getAsBoolean());
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                this.setMessage(value.get(jk).getAsBoolean() ? ScreenTexts.YES : ScreenTexts.NO );
                super.render(matrices, mouseX, mouseY, delta);
            }
        });
        this.drawY += 25;
    }

    private void addStringWidget(String jk, String jv) {
        this.addButton(new TextFieldWidget(this.textRenderer, this.panX + 80, this.drawY, this.panWidth, 20, new LiteralText(jk)) {

            @SuppressWarnings("unused")
            private final boolean init = init();
            private boolean init() {
                setMaxLength(2147483647);
                setText(jv);
                reloaded.add(jk);
                return true;
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if (this.isVisible() && !reloaded.contains(jk)) {
                    this.setText(defaultValue.get(jk).getAsString());
                    reloaded.add(jk);
                }
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void setSelected(boolean selected) {
                super.setSelected(selected);
                if (!Objects.equals(this.getText(), value.get(jk).getAsString())) {
                    value.addProperty(jk, this.getText());
                }
            }
        });
        this.drawY += 25;
    }

    private void addListWidget(String jk, JsonArray jv) {
        this.addButton(new TextFieldWidget(this.textRenderer, this.panX + 80, this.drawY, this.panWidth, 20, new LiteralText(jk)) {

            @SuppressWarnings("unused")
            private final boolean init = init();
            private boolean init() {
                this.setMaxLength(2147483647);
                this.setText(jv.toString());
                reloaded.add(jk);
                return true;
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if (this.isVisible() && !reloaded.contains(jk)) {
                    this.setText(defaultValue.get(jk).getAsJsonArray().toString());
                    reloaded.add(jk);
                }
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void setSelected(boolean selected) {
                super.setSelected(selected);
                if (!Objects.equals(this.getText(), value.get(jk).getAsJsonArray().toString())) {
                    String result = this.getText();
                    JsonArray jsonArray = new JsonArray();
                    for (String index : result.substring(1, result.length() - 1).split(",")) {
                        jsonArray.add(index.substring(1, index.length() - 1));
                    }
                    value.add(jk, jsonArray);
                }
            }
        });
        this.drawY += 25;
    }

    private void addNumberWidget(String jk, Number jv) {
        this.addButton(new TextFieldWidget(this.textRenderer, this.panX + 80, this.drawY, this.panWidth, 20, new LiteralText(jk)) {

            @SuppressWarnings("unused")
            private final boolean init = init();
            private boolean init() {
                this.setMaxLength(2147483647);
                this.setText(jv.toString());
                reloaded.add(jk);
                return true;
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if (this.isVisible() && !reloaded.contains(jk)) {
                    this.setText(defaultValue.get(jk).getAsNumber().toString());
                    reloaded.add(jk);
                }
                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void setSelected(boolean selected) {
                super.setSelected(selected);
                if (!Objects.equals(this.getText(), value.get(jk).getAsNumber().toString())) {
                    value.addProperty(jk, Integer.parseInt(this.getText()));
                }
            }
        });
        this.drawY += 25;
    }

}
