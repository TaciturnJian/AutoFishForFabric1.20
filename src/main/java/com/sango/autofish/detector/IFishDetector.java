package com.sango.autofish.detector;

import com.sango.autofish.manipulator.IFishManipulator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

public interface IFishDetector {
    void AtTickEnd(MinecraftClient minecraft);

    void ProcessPacket(IFishManipulator manipulator, Packet<?> packet, MinecraftClient minecraft);
}
