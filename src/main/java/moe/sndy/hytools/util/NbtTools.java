package moe.sndy.hytools.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class NbtTools {

    /**
     * Decompresses and parses Compressed and GZipped data.
     *
     * @param compressedNBT compressed NBT data
     * @return              NBT tag
     * @throws IOException  failure to read NBT data
     */
    public static NBTTagCompound decompressNBT(String compressedNBT) throws IOException {
        byte[] nbtData = unzipBytes(Base64.decodeBase64((compressedNBT)));
        return CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(nbtData)));
    }

    public static ItemStack[] nbtToItems(NBTTagCompound nbt) {
        if(nbt.hasKey("i")){
            NBTTagList invData = nbt.getTagList("i", Constants.NBT.TAG_COMPOUND);
            ItemStack[] items = new ItemStack[invData.tagCount()];
            for(int i = 0; i < invData.tagCount() ;  i++){
                if(invData.getCompoundTagAt(i) != null){
                    items[i] = ItemStack.loadItemStackFromNBT(invData.getCompoundTagAt(i));
                } else {
                    items[i] = null;
                }
            }
            return items;
        }
        return new ItemStack[0];
    }

    private static byte[] unzipBytes(byte[] compressed) throws IOException {
        InputStream in = new GZIPInputStream(new ByteArrayInputStream(compressed));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = in.read()) != -1) {
            out.write(nextByte);
        }
        return out.toByteArray();
    }

}
