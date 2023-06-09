package com.sango.autofish.detector;

import com.sango.autofish.manipulator.IFishManipulator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;

public class FishSoundDetector implements IFishDetector {
    public static final double HOOKSOUND_DISTANCESQ_THRESHOLD = 25D;

    @Override
    public void AtTickEnd(MinecraftClient minecraft) {

    }


    @Override
    public void ProcessPacket(IFishManipulator manipulator, Packet<?> packet, MinecraftClient minecraft) {
        // check if we can process this packet
        if (!(packet instanceof PlaySoundS2CPacket sound_packet) || minecraft.player == null || minecraft.player.fishHook == null) {
            return;
        }

        // check if the name of the sound is our target
        SoundEvent sound_event = sound_packet.getSound().value();
        String sound_name = sound_event.getId().toString();
        if (!(sound_name.equalsIgnoreCase("minecraft:entity.fishing_bobber.splash") || sound_name.equalsIgnoreCase("entity.fishing_bobber.splash"))){
            return;
        }

        // check if the distance is suitable
        if (minecraft.player.fishHook.squaredDistanceTo(sound_packet.getX(),sound_packet.getY(),sound_packet.getZ()) < HOOKSOUND_DISTANCESQ_THRESHOLD){
            manipulator.CatchFish();
        }
    }
}
