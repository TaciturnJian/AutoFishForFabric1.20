package com.sango.autofish.manipulator;

import com.sango.autofish.scheduler.ActionType;
import com.sango.autofish.scheduler.AutoFishScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class PlayerFisher implements IFishManipulator {
    private final MinecraftClient MCClient;
    private final AutoFishScheduler Scheduler;

    public PlayerFisher(MinecraftClient client, AutoFishScheduler scheduler) {
        MCClient = client;
        Scheduler = scheduler;
    }

    @Override
    public void CatchFish() {
        UseFishingRod();
        UseFishingRod();
        //RecastRod();
    }

    @Override
    public void RecastRod() {
        if (Scheduler.isRecastQueued()) {
            return;         // prevent repeat recast
        }
        Scheduler.scheduleAction(ActionType.RECAST, 1500, () -> {
            if (MCClient == null || MCClient.player == null || MCClient.player.fishHook == null) {
                return;
            }

            Item item = MCClient.player.getMainHandStack().getItem();
            if (!(item == Items.FISHING_ROD || item instanceof FishingRodItem)) {
                return;
            }

            UseFishingRod();
        });
        UseFishingRod();
    }

    private void UseFishingRod() {
        if (MCClient ==null || MCClient.world == null || MCClient.player == null || MCClient.interactionManager == null) {
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
