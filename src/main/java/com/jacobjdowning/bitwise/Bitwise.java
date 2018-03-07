package com.jacobjdowning.bitwise;

import com.jacobjdowning.bitwise.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

/**
 * Created by Jacob on 07/03/2018.
 */

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, useMetadata = true)
public class Bitwise {
    @Mod.Instance
    public static Bitwise instance;

    @SidedProxy(clientSide = "com.downingjj.bitwise.proxy.ClientProxy", serverSide = "com.downingjj.bitwise.proxy.ServerProxy")
    public static IProxy proxy;

    private Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    }


}
