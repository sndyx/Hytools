package strawberry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

public class Client {

    private Client(){}

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void addChatMessage(String message){
        mc.thePlayer.addChatMessage(new ChatComponentText(message.replaceAll("&([abcdef123456789])", "§$1")));
    }

    public static Vec3 rayCast(double distance){
        return mc.thePlayer.rayTrace(distance, 0).hitVec;
    }

    public static Block getBlock(BlockPos pos){
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockByName(String name){
        return Block.getBlockFromName(name);
    }

    public static Item getItemByName(String name){
        return Item.getByNameOrId(name);
    }

    public static void renderBlock(BlockPos pos, Block block, float partialTicks){
        renderBlock(pos, block.getDefaultState(), partialTicks);
    }

    public static void renderBlock(BlockPos pos, IBlockState state, float partialTicks){
        final Entity renderViewEntity = mc.getRenderViewEntity();
        assert renderViewEntity != null;
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer renderer = tessellator.getWorldRenderer();
        final double d0 = renderViewEntity.lastTickPosX + ((renderViewEntity.posX - renderViewEntity.lastTickPosX) * partialTicks);
        final double d1 = renderViewEntity.lastTickPosY + ((renderViewEntity.posY - renderViewEntity.lastTickPosY) * partialTicks);
        final double d2 = renderViewEntity.lastTickPosZ + ((renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * partialTicks);
        renderer.setTranslation(-d0, -d1, -d2);
        renderer.begin(7, DefaultVertexFormats.BLOCK);
        final BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        blockRendererDispatcher.renderBlock(state, pos, mc.theWorld, renderer);
        tessellator.draw();
        renderer.setTranslation(0, 0, 0);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

}
