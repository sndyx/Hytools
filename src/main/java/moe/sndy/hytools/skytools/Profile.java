package moe.sndy.hytools.skytools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import moe.sndy.hytools.core.Resources;
import moe.sndy.hytools.util.ApiTools;
import moe.sndy.hytools.util.ApiRequest;
import moe.sndy.hytools.util.NbtTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile extends GuiScreen {

    String player;
    ArrayList<SkyblockProfile> profiles;

    public Profile(String player){
        this.player = player;
    }

    @Override
    public void initGui() {
        profiles = new ArrayList<SkyblockProfile>();
        init();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(!profiles.isEmpty()){
            SkyblockProfile profile = profiles.get(0);
            if(profile.inventory != null) {
                int x = 0;
                int y = 0;
                GL11.glPushMatrix();
                GL11.glScalef(1.3f ,1.3f ,1.3f);
                for(ItemStack item : profile.inventory) {
                    if(item != null) {
                        itemRender.renderItemIntoGUI(item, 100 + x * 20, 100 + y * 20);
                    }
                    x++;
                    if(x == 9) {
                        x = 0;
                        y += 1;
                    }
                }
                GL11.glPopMatrix();
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void displayError(String error){
        Minecraft.getMinecraft().thePlayer.closeScreen();
        Minecraft.getMinecraft().displayGuiScreen(new Error(error));
    }

    public void displayError(String... error){
        Minecraft.getMinecraft().thePlayer.closeScreen();
        Minecraft.getMinecraft().displayGuiScreen(new Error(error));
    }

    public void init() {
        String uuid = new ApiRequest("https://api.mojang.com/users/profiles/minecraft/" + player).get();
        if (!uuid.equals("error")) {
            uuid = uuid.substring(17 + player.length(), 49 + player.length());
            String playerData = new ApiRequest("https://api.hypixel.net/player?key=" + Resources.API_KEY + "&uuid=" + uuid).get();
            if (!playerData.equals("error") && playerData.charAt(11) == 't') {
                if (playerData.charAt(25) != 'n') {
                    JsonObject stats = new JsonParser().parse(playerData).getAsJsonObject();
                    stats = stats.get("player").getAsJsonObject().get("stats").getAsJsonObject();
                    if (stats.has("SkyBlock")) {
                        String profilesJson = stats.get("SkyBlock").getAsJsonObject().get("profiles").toString();
                        Matcher m = Pattern.compile("\\{(.*?)}").matcher(profilesJson);
                        while (m.find()) {
                            if (!m.group(1).contains("{")) {
                                String id = m.group(1).substring(m.group(1).indexOf("profile_id") + 13);
                                id = id.substring(0, id.indexOf(",") - 1);
                                String name = m.group(1).substring(m.group(1).indexOf("cute_name") + 12, m.group(1).length() - 1);
                                try {
                                    loadProfile(id, name, uuid);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    displayError("Something went wrong");
                                    return;
                                }
                            }
                        }
                        return;
                    }
                    displayError("Player '" + player + "' has", "never played SkyBlock");
                    return;
                }
                displayError("Player '" + player + "' has", "never joined Hypixel");
                return;
            }
            displayError("Something went wrong");
            return;
        }
        displayError("Player '" + player + "'does", "not exist");
    }

    public void loadProfile(String id, String name, String uuid) throws IOException {
        final ApiRequest profileRequest = new ApiRequest("https://api.hypixel.net/skyblock/profile?key=" + Resources.API_KEY + "&profile=" + id);
        final String profileJson = ApiTools.getNode(profileRequest.get(), "profile", "members", uuid);
        System.out.println(profileJson);
        final String invData = ApiTools.getAwkwardNode(profileJson, "inv_contents", "data").replaceAll("\"", "");
        final String armorData = ApiTools.getNode(profileJson, "inv_armor", "data").replaceAll("\"", "");
        System.out.println(invData);
        final NBTTagCompound invNBT = NbtTools.decompressNBT(invData);
        final NBTTagCompound armorNBT = NbtTools.decompressNBT(armorData);
        profiles.add(new SkyblockProfile(NbtTools.nbtToItems(invNBT), NbtTools.nbtToItems(armorNBT)));
    }

}