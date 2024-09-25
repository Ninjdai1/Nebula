package de.voasis.serverHandlerProxy.Commands.SubCommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.voasis.serverHandlerProxy.Maps.Messages;
import de.voasis.serverHandlerProxy.Maps.ServerInfo;
import de.voasis.serverHandlerProxy.ServerHandlerProxy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StopCommand implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 2) {
            source.sendMessage(Component.text("Usage: /stop <externalServerName> <InstanceName>", NamedTextColor.RED));
            return;
        }
        String externalServerName = args[0];
        String Name = args[1];
        ServerInfo temp = null;
        for (ServerInfo i : ServerHandlerProxy.dataHolder.serverInfoMap) {
            if(externalServerName.equals(i.getServerName())) {
                temp = i;
            }
        }

        if (source instanceof ConsoleCommandSource && temp != null || hasPermission(invocation)) {
            ServerHandlerProxy.externalServerCreator.stop(temp, Name);
            source.sendMessage(Component.text("Stopping server instance...", NamedTextColor.AQUA));
        } else {
            source.sendMessage(Component.text(Messages.norights, NamedTextColor.RED));
        }
    }
    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("velocity.admin");
    }
    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }
}