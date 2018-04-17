package com.downingjj.bitwise.tileentity;

import net.minecraft.block.state.IBlockState;
/**
 * Created by Jacob on 12/03/2018.
 */
public class RedstoneConstantTile extends RedstonePowerTile{

    public int cycle(){
        power ++;
        if(power > 15){
            power = 0;
        }
        markDirty();
        if(world != null){
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
        return power;
    }

    public int hitDigit(int digit){
        power ^= digit;
        markDirty();
        if(world != null){
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
        return power;
    }

}
