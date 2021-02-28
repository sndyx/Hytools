package strawberry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class Server {

    private Server(){}

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendChatMessage(String message){
        mc.thePlayer.sendChatMessage(message);
    }

    public static void setFlying(boolean flying){
        mc.thePlayer.capabilities.isFlying = flying;
        mc.thePlayer.sendPlayerAbilities();
    }

    public static void addVelocity(double x, double y, double z){
        addVelocity(x, y, z, false);
    }

    public static void addVelocity(double x, double y, double z, boolean preserveFlying){
        boolean flying = false;
        if(preserveFlying) flying = mc.thePlayer.capabilities.isFlying;
        setFlying(true);
        mc.thePlayer.addVelocity(x, y, z);
        setFlying(flying);
    }

    public static void setVelocity(double x, double y, double z){
        setVelocity(x, y, z, false);
    }

    public static void setVelocity(double x, double y, double z, boolean preserveFlying){
        boolean flying = false;
        if(preserveFlying) flying = mc.thePlayer.capabilities.isFlying;
        setFlying(true);
        mc.thePlayer.setVelocity(x, y, z);
        setFlying(flying);
    }

    public static EntityPlayer getPlayerByName(String name){
        return mc.theWorld.getPlayerEntityByName(name);
    }

    public static EntityPlayer[] getPlayers(){
        return mc.theWorld.playerEntities.toArray(new EntityPlayer[0]);
    }

    public static String getScoreboardTitle(){
        return mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getName();
    }

    public static String[] getScoreboardContents(){
        return mc.theWorld.getScoreboard().getTeamNames().toArray(new String[0]);
    }

}
