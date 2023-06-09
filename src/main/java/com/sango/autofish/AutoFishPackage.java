package com.sango.autofish;

import com.sango.autofish.detector.FishSoundDetector;
import com.sango.autofish.detector.IFishDetector;
import com.sango.autofish.manipulator.IFishManipulator;
import com.sango.autofish.manipulator.PlayerFisher;
import com.sango.autofish.scheduler.AutoFishScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;

public class AutoFishPackage {
    private MinecraftClient MCClient;
    private AutoFish AFInitializer;

    private AutoFishScheduler Scheduler;

    private IFishDetector FishDetector;
    private IFishManipulator FishManipulator;

    public AutoFishPackage(AutoFish initializer, AutoFishScheduler scheduler) {
        AFInitializer = initializer;
        MCClient = MinecraftClient.getInstance();

        // TODO set FishDetector
        FishDetector = new FishSoundDetector();

        Scheduler = scheduler;

        // TODO set FishManipulator
        FishManipulator = new PlayerFisher(MCClient, Scheduler);
    }

    public void ProcessPacket(Packet<?> packet) {
        FishDetector.ProcessPacket(FishManipulator, packet, MCClient);
    }

    private boolean IsPlayerMainHandItemFishingRod(MinecraftClient client) {
        if (client== null || client.player == null) {
            return false;
        }

        Item item = client.player.getMainHandStack().getItem();
        return item == Items.FISHING_ROD || item instanceof FishingRodItem;
    }

    public void AtTickEnd(MinecraftClient client) {
        if (!IsPlayerMainHandItemFishingRod(client)) {
            return;
        }

        FishDetector.AtTickEnd(client);
    }
}
