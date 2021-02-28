package moe.sndy.hytools.skytools;

import moe.sndy.hytools.util.BitmapFontRenderer;
import moe.sndy.hytools.util.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Error extends GuiScreen {

    final String[] errors;

    public Error(String... errors){
        this.errors = errors;
    }

    public Error(String error){
        this.errors = new String[]{error};
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean hovered = false;
        if(mouseX > width / 2 - 60 && mouseX < width / 2 + 60 && mouseY > height / 2 + 60 && mouseY < height / 2 + 100) {
            hovered = true;
        }
        drawRect(width / 2 - 60, height / 2 + 100, width / 2 + 60, height / 2 - 100, 0xff36453e);
        drawRect(width / 2 - 55, height / 2 + 95, width / 2 + 55, height / 2 - 95, 0xff41534b);
        if(hovered){
            drawRect(width / 2 - 60, height / 2 + 100, width / 2 + 60, height / 2 + 60, 0xff9eccbd);
            drawRect(width / 2 - 58, height / 2 + 98, width / 2 + 58, height / 2 + 62, 0xff36453e);
        } else {
            drawRect(width / 2 - 60, height / 2 + 100, width / 2 + 60, height / 2 + 60, 0xff36453e);
        }
        BitmapFontRenderer renderer1 = new BitmapFontRenderer(Fonts.MONO_EXTRA_BOLD, 20);
        renderer1.drawCenteredString("Error", width / 2, height / 2 - 20, 0xff9eccbd);
        if(hovered) {
            renderer1.drawCenteredString("X", width / 2, height / 2 + 70, 0xff9eccbd);
        } else {
            renderer1.drawCenteredString("X", width / 2, height / 2 + 70, 0xff41534b);
        }
        BitmapFontRenderer renderer2 = new BitmapFontRenderer(Fonts.MONO_BOLD, 8);
        int y = height / 2;
        for(String error : errors) {
            renderer2.drawCenteredString(error, width / 2, y, 0xff26b49c);
            y += 10;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > width / 2 - 60 && mouseX < width / 2 + 60 && mouseY > height / 2 + 60 && mouseY < height / 2 + 100) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }
}
