package com.tridevmc.davincisvessels.common.network.message;

import com.tridevmc.davincisvessels.DavincisVesselsMod;
import com.tridevmc.davincisvessels.common.entity.EntityShip;
import com.tridevmc.compound.network.message.Message;
import com.tridevmc.compound.network.message.RegisteredMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.LogicalSide;

/**
 * Created by darkevilmac on 1/29/2017.
 */
@RegisteredMessage(channel = "davincisvessels", destination = LogicalSide.SERVER)
public class RequestSubmerseMessage extends Message {

    public EntityShip ship;
    public boolean doSumberse;

    public RequestSubmerseMessage(EntityShip ship, boolean doSumberse) {
        super();
        this.ship = ship;
        this.doSumberse = doSumberse;
    }

    public RequestSubmerseMessage() {
        super();
    }

    @Override
    public void handle(EntityPlayer sender) {
        if (ship != null) {
            if (doSumberse && !ship.canSubmerge()) {
                if (sender instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) sender).connection.disconnect(new TextComponentString("Invalid submerse request!" +
                            "\nCheating to go underwater... reconsider your life choices."));
                    if (sender != null && sender.getGameProfile() != null)
                        DavincisVesselsMod.LOG.warn("A user tried to submerse in a vessel that can't, user info: " + sender.getGameProfile().toString());
                }
                return;
            }

            ship.setSubmerge(doSumberse);
            // TODO: Achievements are gone.
            //sender.addStat(DavincisVesselsContent.achievementSubmerseShip);
        }
    }
}
