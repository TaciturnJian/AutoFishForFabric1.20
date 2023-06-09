package com.sango.autofish;

import com.sango.autofish.scheduler.AutoFishScheduler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class AutoFish implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("SangoAutoFish");
    private AutoFishPackage autoFish;
    private AutoFishScheduler Scheduler;

    private static AutoFish instance;
    @Override
    public void onInitialize() {
        if (instance == null) instance = this;
        LOGGER.error("Loading Sango Auto Fish");
        try {
            Scheduler = new AutoFishScheduler();
            autoFish = new AutoFishPackage(this, Scheduler);
        }
        catch (Exception ex){
            LOGGER.error(ex.toString());
        }
    }

    public void AtTickEnd(MinecraftClient client) {
        LOGGER.error("At tick end!");
        if (client.world == null || client.player == null) {
            return;
        }

        autoFish.AtTickEnd(client);
        Scheduler.tick(client);
    }

    public void ProcessPacket(Packet<?> packet) {
        autoFish.ProcessPacket(packet);
    }

    public static AutoFish getInstance() {
        return instance;
    }
}
