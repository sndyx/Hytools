package moe.sndy.hytools.core;

import moe.sndy.hytools.skytools.Profile;
import moe.sndy.hytools.strawberry.Strawberry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;

@Mod(modid = Resources.MODID, version = Resources.VERSION)
public final class Core {

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        try {
            Strawberry.compile();
        } catch (InvocationTargetException e){
            e.getCause().printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new Profile(event.message.getUnformattedText().substring(event.message.getUnformattedText().lastIndexOf(" ") + 1)));
    }

}
