package moe.sndy.hytools.skytools;

import net.minecraft.item.ItemStack;

public class SkyblockProfile {

    boolean apiEnabled;
    public final ItemStack[] inventory;
    public final ItemStack[] armor;

    public SkyblockProfile(ItemStack[] inventory, ItemStack[] armor){
        this.inventory = inventory;
        this.armor = armor;
    }

}
