package eu.vibemc.lifesteal.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import eu.vibemc.lifesteal.Main;
import eu.vibemc.lifesteal.bans.BanLocalUtil;
import eu.vibemc.lifesteal.bans.models.Ban;
import eu.vibemc.lifesteal.other.Config;
import org.bukkit.OfflinePlayer;

import java.io.IOException;

public class BansCommands {
    public static CommandAPICommand getAllBansCommands() {
        return new CommandAPICommand("bans")
                .withPermission("lifesteal.bans")
                .executes((sender, args) -> {
                    sender.sendMessage("§6§lBans:");
                    for (Ban ban : BanLocalUtil.findAllLocalBans()) {
                        sender.sendMessage("§c" + Main.getInstance().getServer().getOfflinePlayer(ban.getPlayerUUID()).getName());
                    }
                })
                .withSubcommand(BansCommands.getRemoveBanCommand());
    }

    private static CommandAPICommand getRemoveBanCommand() {
        return new CommandAPICommand("remove")
                .withPermission("lifesteal.bans.remove")
                .withArguments(new OfflinePlayerArgument("player"))
                .executes((sender, args) -> {
                    OfflinePlayer player = (OfflinePlayer) args[0];
                    try {
                        if (player.getName() == null) {
                            sender.sendMessage(Config.getMessage("playerNotFound"));
                        } else {
                            if (BanLocalUtil.deleteLocalBan(player.getUniqueId())) {
                                sender.sendMessage(Config.getMessage("banRemoved").replace("${player}", player.getName()));

                            } else {
                                sender.sendMessage(Config.getMessage("playerNotBanned"));
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
