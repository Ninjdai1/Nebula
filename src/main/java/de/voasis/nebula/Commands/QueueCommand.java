package de.voasis.nebula.Commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.voasis.nebula.Data.Data;
import de.voasis.nebula.Data.Messages;
import de.voasis.nebula.Maps.GamemodeQueue;
import de.voasis.nebula.Nebula;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueueCommand implements SimpleCommand {

    private MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (invocation.source() instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(mm.deserialize(Messages.USAGE_QUEUE));
                return;
            }
            switch (args[0]) {
                case "leave" -> leaveQueue(player);
                case "join" -> {
                    if (args.length == 2) {
                        Nebula.util.joinQueue(player, args[1]);
                    } else {
                        player.sendMessage(mm.deserialize(Messages.USAGE_QUEUE));
                    }
                }
                default -> player.sendMessage(mm.deserialize(Messages.USAGE_QUEUE));
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            return List.of("join", "leave");
        }
        if (args.length == 1) {
            return Stream.of("join", "leave")
                    .filter(command -> command.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && "join".equalsIgnoreCase(args[0])) {
            return Data.gamemodeQueueMap.stream()
                    .map(GamemodeQueue::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private void leaveQueue(Player player) {
        if (!Nebula.util.isInAnyQueue(player)) {
            player.sendMessage(mm.deserialize(Messages.NOT_IN_QUEUE));
            return;
        }
        for(GamemodeQueue queue :Data.gamemodeQueueMap) {
            if (queue.getInQueue().contains(player)) {
                player.sendMessage(mm.deserialize(Messages.REMOVED_FROM_QUEUE.replace("<queue>", queue.getName())));
                queue.getInQueue().remove(player);
            }
        }
    }
}
