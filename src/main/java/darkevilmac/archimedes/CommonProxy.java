package darkevilmac.archimedes;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import darkevilmac.archimedes.gui.ASGuiHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public CommonPlayerTicker playerTicker;
    public CommonHookContainer hookContainer;

    public CommonHookContainer getHookContainer() {
        return new CommonHookContainer();
    }

    public void registerKeyHandlers(ArchimedesConfig cfg) {
    }

    public void registerEventHandlers() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ArchimedesShipMod.instance, new ASGuiHandler());

        playerTicker = new CommonPlayerTicker();
        FMLCommonHandler.instance().bus().register(playerTicker);
        MinecraftForge.EVENT_BUS.register(hookContainer = getHookContainer());
    }

    public void registerRenderers() {
    }

}
