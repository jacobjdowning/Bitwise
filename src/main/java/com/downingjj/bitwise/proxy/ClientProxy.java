package com.downingjj.bitwise.proxy;

import com.downingjj.bitwise.ModBlocks;
import com.downingjj.bitwise.Reference;
import com.downingjj.bitwise.blocks.WiseBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Jacob on 07/03/2018.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (WiseBlock b : ModBlocks.getAll()){
            b.initModel();
        }

        OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
    }
}
