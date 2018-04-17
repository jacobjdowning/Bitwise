package com.downingjj.bitwise.tileentity;

import net.minecraft.block.state.IBlockState;

/**
 * Created by Jacob on 13/04/2018.
 */
public class GateTile extends RedstonePowerTile {

    public int performOperation( int input1, int input2, int op){
        switch(op){
            case 0:
                setPower(input1 & input2);
                break;
            case 1:
                setPower(input1 | input2);
                break;
            case 2:
            default:
                setPower(input1 ^ input2);
        }
        this.markDirty();
        if (world != null) {
            IBlockState state = getWorld().getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
        return getPower();
    }

}
