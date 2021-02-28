package moe.sndy.hytools.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import moe.sndy.hytools.core.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author TheObliterator
 * @author sndy
 *
 * A class to create and draw true type
 * fonts onto the Minecraft game engine.
 */
public class BitmapFontRenderer {

    private final ResourceLocation bitmap;
    private final int[] xPos;
    private final int[] yPos;
    private final int startChar;
    private final FontMetrics metrics;

    /**
     * Instantiates the font, filling in default start
     * and end character parameters.
     *
     * 'new CustomFont(ModLoader.getMinecraftInstance(),
     *                              "Arial", 12);
     *
     * @param font the name of the font to be drawn.
     * @param size the size of the font to be drawn.
     */
    public BitmapFontRenderer(Object font, int size) {
        this(Minecraft.getMinecraft(), font, size, 0, 4000);
    }

    /**
     * Instantiates the font, pre-rendering a sprite
     * font image by using a true type font on a
     * bitmap. Then allocating that bitmap to the
     * Minecraft rendering engine for later use.
     *
     * 'new CustomFont(ModLoader.getMinecraftInstance(),
     *                              "Arial", 12, 32, 126);'
     *
     * @param mc        the Minecraft instance for the font to be bound to.
     * @param font      the name of the font to be drawn.
     * @param size      the size of the font to be drawn.
     * @param startChar the starting ASCII character id to be drawable. (Default 32)
     * @param endChar   the ending ASCII character id to be drawable. (Default 126)
     */
    public BitmapFontRenderer(Minecraft mc, Object font, int size, int startChar, int endChar) {
        this.startChar = startChar;
        xPos = new int[endChar - startChar];
        yPos = new int[endChar - startChar];
        BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        try {
            if (font instanceof String) {
                String fontName = (String) font;
                if (fontName.contains("/")) {
                    g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontName)).deriveFont((float) size));
                } else {
                    g.setFont(new Font(fontName, Font.PLAIN, size));
                }
            }
            else if (font instanceof InputStream) {
                g.setFont(Font.createFont(Font.TRUETYPE_FONT, (InputStream) font).deriveFont((float) size));
            }
            else if (font instanceof File) {
                g.setFont(Font.createFont(Font.TRUETYPE_FONT, (File) font).deriveFont((float) size));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, 256, 256);
        g.setColor(Color.white);
        metrics = g.getFontMetrics();
        int x = 2;
        int y = 2;
        for (int i = startChar; i < endChar; i++) {
            g.drawString("" + ((char) i), x, y + g.getFontMetrics().getAscent());
            xPos[i - startChar] = x;
            yPos[i - startChar] = y - metrics.getMaxDescent();
            x += metrics.stringWidth("" + (char) i) + 2;
            if (x >= 250 - metrics.getMaxAdvance()) {
                x = 2;
                y += metrics.getMaxAscent() + metrics.getMaxDescent() + size / 2;
            }
        }
        bitmap = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(Resources.MODID, new DynamicTexture(img));
    }

    /**
     * Draws a given string with an automatically
     * calculated shadow below it.
     *
     * @param text  the string to be drawn
     * @param x     the x position to start drawing
     * @param y     the y position to start drawing
     * @param color the color of the non-shadowed text (Hex)
     */
    public void drawShadowedString(String text, int x, int y, int color) {
        int l = color & 0xff000000;
        int shade = (color & 0xfcfcfc) >> 2;
        shade += l;
        drawString(text, x + 1, y + 1, shade);
        drawString(text, x, y, color);
    }

    /**
     * Draws a given centered string.
     *
     * @param text The string to be drawn
     * @param x The x position to start drawing
     * @param y The y position to start drawing
     * @param color The color of the non-shadowed text (Hex)
     */
    public void drawCenteredString(String text, int x, int y, int color){
        drawString(text, x - getStringWidth(text) / 2, y - getStringHeight(text) / 2, color);
    }

    /**
     * Draws a given string.
     *
     * @param text  the string to be drawn
     * @param x     the x position to start drawing
     * @param y     the y position to start drawing
     * @param color the color of the non-shadowed text (Hex)
     */
    public void drawString(String text, int x, int y, int color) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3553);
        Minecraft.getMinecraft().renderEngine.bindTexture(bitmap);
        float red = (color >> 16 & 0xff) / 255F;
        float green = (color >> 8 & 0xff) / 255F;
        float blue = (color & 0xff) / 255F;
        float alpha = (color >> 24 & 0xff) / 255F;
        GL11.glColor4f(red, green, blue, alpha);
        int startX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\') {
                char type = text.charAt(i + 1);
                if (type == 'n') {
                    y += metrics.getAscent() + 2;
                    x = startX;
                }
                i++;
                continue;
            }
            drawChar(c, x, y);
            x += metrics.getStringBounds("" + c, null).getWidth();
        }
    }

    /**
     * Returns the created FontMetrics
     * which is used to retrieve various
     * information about the True Type Font
     *
     * @return FontMetrics of the created font.
     */
    public FontMetrics getMetrics () {
        return metrics;
    }

    /**
     * Gets the drawing width of a given
     * string of string.
     *
     * @param text the string to be measured
     * @return     the width of the given string.
     */
    public int getStringWidth (String text) {
        return (int) getBounds(text).getWidth();
    }

    /**
     * Gets the drawing height of a given
     * string of string.
     *
     * @param text the string to be measured
     * @return     the height of the given string.
     */
    public int getStringHeight (String text) {
        return (int) getBounds(text).getHeight();
    }

    /**
     * A method that returns a Rectangle that
     * contains the width and height dimensions
     * of the given string.
     *
     * @param text the string to be measured
     * @return     rectangle containing width and height that
     *             the text will consume when drawn.
     */
    private Rectangle getBounds (String text) {
        int w = 0;
        int h = 0;
        int tw = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\') {
                char type = text.charAt(i + 1);
                if (type == 'n') {
                    h += metrics.getAscent() + 2;
                    if (tw > w)
                    {
                        w = tw;
                    }
                    tw = 0;
                }
                i++;
                continue;
            }
            tw += metrics.stringWidth("" + c);
        }
        if (tw > w) {
            w = tw;
        }
        h += metrics.getAscent();
        return new Rectangle(0, 0, w, h);
    }

    /**
     * Private drawing method used within other
     * drawing methods.
     */
    private void drawChar (char c, int x, int y) {
        Rectangle2D bounds = metrics.getStringBounds("" + c, null);
        drawTexturedModalRect(x, y, xPos[(byte) c - startChar], yPos[(byte) c - startChar], (int) bounds.getWidth(), (int) bounds.getHeight() + metrics.getMaxDescent());
    }

    public void drawTexturedModalRect (int x, int y, int u, int v, int width, int height) {
        float offsetWidth = 0.00390625F;
        float offsetHeight = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, 0).tex(u * offsetWidth, (v + height) * offsetHeight).endVertex();
        renderer.pos(x + width, y + height, 0).tex((u + width) * offsetWidth, (v + height) * offsetHeight).endVertex();
        renderer.pos(x + width, y, 0).tex((u + width) * offsetWidth, (v - 0.1) * offsetHeight).endVertex();
        renderer.pos(x, y, 0).tex(u * offsetWidth, ((v - 0.1) * offsetHeight)).endVertex();
        tessellator.draw();
    }

}
