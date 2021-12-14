package gg.mooncraft.minecraft.bedwars.lobby.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import de.simonsator.partyandfriendsgui.PAFGUIPlugin;
import de.simonsator.partyandfriendsgui.api.datarequest.DataRequestPlayerInfo;
import de.simonsator.partyandfriendsgui.api.datarequest.party.PartyData;
import de.simonsator.partyandfriendsgui.api.datarequest.party.PartyDataCallBackRunnable;
import de.simonsator.partyandfriendsgui.api.datarequest.party.PartyDataRequestCallbackAPI;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PartyUtilities {

    public static @NotNull CompletableFuture<PartyData> getParty(@NotNull Player player) {
        CompletableFuture<PartyData> completableFuture = new CompletableFuture<>();
        PartyDataRequestCallbackAPI partyDataRequestCallbackAPI = (PartyDataRequestCallbackAPI) getCommunication().getTask(PartyDataRequestCallbackAPI.class);
        partyDataRequestCallbackAPI.fetchPartyData(player, new PartyDataCallBackRunnable() {
            @Override
            public void onCallback(Player player, PartyData partyData, int i) {
                if (!partyData.DOES_EXIST) {
                    completableFuture.completeExceptionally(new IllegalStateException("The party doesn't exist!"));
                    return;
                }
                completableFuture.complete(partyData);
            }

            @Override
            public void onTimeout(int pId) {
                completableFuture.completeExceptionally(new IllegalStateException("The party cannot be retrieved in time!"));
            }
        });
        return completableFuture;
    }

    public static @NotNull CompletableFuture<Player> getPartyLeader(@NotNull Player player) {
        return getParty(player).thenApply(partyData -> Bukkit.getPlayer(partyData.getPartyLeader().PLAYER_UUID));
    }

    public static @NotNull CompletableFuture<List<Player>> getPartyMembers(@NotNull Player player, boolean includeLeader) {
        return getParty(player).thenApply(partyData -> {
            List<Player> playerList = new ArrayList<>();
            for (DataRequestPlayerInfo dataRequestPlayerInfo : partyData.getAllPlayers()) {
                if (!includeLeader && dataRequestPlayerInfo.PLAYER_UUID.equals(partyData.getPartyLeader().PLAYER_UUID)) {
                    continue;
                }
                Player onlinePlayer = Bukkit.getPlayer(dataRequestPlayerInfo.PLAYER_UUID);
                if (onlinePlayer == null) continue;
                playerList.add(onlinePlayer);
            }
            return playerList;
        });
    }

    @UnmodifiableView
    private static @NotNull BungeecordCommunication getCommunication() {
        PAFGUIPlugin pafguiPlugin = (PAFGUIPlugin) Bukkit.getPluginManager().getPlugin("PartyAndFriendsGUI");
        if (pafguiPlugin == null) {
            throw new IllegalStateException("Failed to find PartyAndFriendsGUI plugin!");
        }
        BungeecordCommunication bungeecordCommunication = Bukkit.getServer().getMessenger().getIncomingChannelRegistrations(pafguiPlugin).stream()
                .filter(pluginMessageListenerRegistration -> pluginMessageListenerRegistration.getChannel().equalsIgnoreCase(BungeecordCommunication.CHANNEL))
                .findFirst()
                .map(pluginMessageListenerRegistration -> (BungeecordCommunication) pluginMessageListenerRegistration.getListener())
                .stream()
                .findFirst()
                .orElse(null);
        if (bungeecordCommunication == null) {
            throw new IllegalStateException("Failed to find PartyAndFriendsGUI plugin channel!");
        }
        return bungeecordCommunication;
    }
}