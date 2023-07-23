package me.syncwrld.cmdextensor.framework;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class CommandFramework {

    private final Plugin plugin;

    public CommandFramework(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        final Server server = plugin.getServer();
        final Reflections reflections = new Reflections("net.starvel.minas");

        Field commandField = null;

        try {
            commandField = server.getClass().getDeclaredField("commandMap");
            commandField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        CommandMap commandMap = null;
        try {
            commandMap = (CommandMap) commandField.get(server);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Set<Class<? extends SimpleCommand>> commandsClassSet = reflections.getSubTypesOf(SimpleCommand.class);

        Command command;
        for (Class<? extends SimpleCommand> currentCommandClassExtended : commandsClassSet) {
            final CommandBuilder commandProperties = currentCommandClassExtended.getDeclaredAnnotation(CommandBuilder.class);

            if (commandProperties == null) {
                throw new PropertiesNotFoundException(currentCommandClassExtended.getCanonicalName());
            }

            Method method = null;

            try {
                method = currentCommandClassExtended.getDeclaredMethod("runCommand", CommandSender.class, String[].class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            String commandName = commandProperties.command();
            String[] commandAliasesArray = commandProperties.aliases();
            boolean limitConsole = commandProperties.onlyPlayers();

            method.setAccessible(true);
            command = instantiateCommand(commandName, currentCommandClassExtended, method, limitConsole);
            commandMap.register(commandName, command);

            for (String currentAlias : commandAliasesArray) {
                command = instantiateCommand(currentAlias, currentCommandClassExtended, method, limitConsole);
                commandMap.register(currentAlias, command);
            }
        }
    }

    private Command instantiateCommand(String command, Class<? extends SimpleCommand> extendedClassCommand, Method method, boolean limit) {
        return new Command(command) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                try {
                    if (limit) {
                        if (!(commandSender instanceof Player)) {
                            commandSender.sendMessage("§cApenas jogadores podem usar este comando. (" + command + ").");
                            return false;
                        }
                    } else {
                        method.invoke(extendedClassCommand.newInstance(), commandSender, strings);
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        };
    }

}
