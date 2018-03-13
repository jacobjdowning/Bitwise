package com.downingjj.bitwise.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Jacob on 12/03/2018.
 */
public class Util {
    public static <T extends TileEntity>T getTEfromPos(IBlockAccess world, BlockPos pos, Class<T> clazz){
        if(!clazz.isInstance(world.getTileEntity(pos))){
            throw new RuntimeException("TE retrieved was not the class provided");
        }else{
            return clazz.cast(world.getTileEntity(pos));
        }
    }
}
