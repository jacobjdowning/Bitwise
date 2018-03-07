package com.downingjj.bitwise.blocks;

import com.downingjj.bitwise.Util.INamed;
import com.downingjj.bitwise.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jacob on 07/03/2018.
 */
public abstract class WiseBlock extends Block implements INamed {
    private final String NAME = null;
    public WiseBlock(String name, Material materialIn) {
        super(materialIn);

        setRegistryName(name);
        setUnlocalizedName(Reference.MOD_ID + "." + name);
    }

    public WiseBlock(String name){
        this(name, Material.ROCK);
    }

    @SideOnly(Side.CLIENT)
    public void initModel(){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
