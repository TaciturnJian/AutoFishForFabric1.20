package com.sango.autofish;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AutoFish implements ModInitializer {
    private MinecraftClient MCClient;

    private static AutoFish instance;

    @Override
    public void onInitialize() {
        if (instance == null) instance = this;
        MCClient = MinecraftClient.getInstance();
    }

    public void ProcessPacket(Packet<?> packet) {
        // check if we can process this packet
        if (!(packet instanceof PlaySoundS2CPacket sound_packet) || MCClient.player == null || MCClient.player.fishHook == null) {
            return;
        }

        // check if the name of the sound is our target
        SoundEvent sound_event = sound_packet.getSound().value();
        String sound_name = sound_event.getId().toString();
        if (!(sound_name.equalsIgnoreCase("minecraft:entity.fishing_bobber.splash") || sound_name.equalsIgnoreCase("entity.fishing_bobber.splash"))) {
            return;
        }

        // check if the distance is suitable
        if (MCClient.player.fishHook.squaredDistanceTo(sound_packet.getX(), sound_packet.getY(), sound_packet.getZ()) < 250) {
            UseFishingRod();
            UseFishingRod();
        }
    }

    public static AutoFish getInstance() {
        return instance;
    }

    private void UseFishingRod() {
        if (MCClient == null || MCClient.world == null || MCClient.player == null || MCClient.interactionManager == null) {
            return;
        }

        Hand player_hand = Hand.MAIN_HAND;
        ActionResult result = MCClient.interactionManager.interactItem(MCClient.player, player_hand);
        if (!result.isAccepted()) {
            return;
        }

        if (result.shouldSwingHand()) {
            MCClient.player.swingHand(player_hand);
        }

        MCClient.gameRenderer.firstPersonRenderer.resetEquipProgress(player_hand);
    }
}
