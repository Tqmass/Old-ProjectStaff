package me.hqwks.creabyte.projectstaff.utils.command;

import lombok.Getter;
import lombok.Setter;
import me.hqwks.creabyte.projectstaff.utils.TranslateColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class Command extends org.bukkit.command.Command {
    @Getter
    private final List<SubCommand> subCommands = new ArrayList<>();
    @Getter
    @Setter
    public boolean consoleOnly = false;
    @Getter
    @Setter
    public boolean playerOnly = false;
    @Getter
    @Setter
    public boolean disabled = false;
    @Getter
    @Setter
    public String permissionNode = "";
    private TabCompleter completer;

    public Command() {
        this("1");
    }

    public Command(String name) {
        this(name, "", Collections.emptyList());
    }

    public Command(String name, String description) {
        this(name, description, Collections.emptyList());
    }

    public Command(String name, List<String> aliases) {
        this(name, "", aliases);
    }

    public Command(String name, String description, List<String> aliases) {
        super(name, description, "/" + name, aliases);

        boolean hasInfo = getClass().isAnnotationPresent(CommandInfo.class);
        if (hasInfo) {
            CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
            setName(annotation.name());

            if (annotation.consoleOnly())
                setConsoleOnly(true);

            if (annotation.playerOnly())
                setPlayerOnly(true);

            if (annotation.disabled())
                setDisabled(true);

            if (!annotation.description().isEmpty())
                setDescription(annotation.description());

            List<String> a = new ArrayList<>(Arrays.asList(annotation.aliases()));
            if (!a.get(0).isEmpty()) {
                setAliases(a);
            }

            if (!annotation.permission().isEmpty())
                setPermissionNode(annotation.permission());
        }

    }

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public void onCommand(CommandSender sender, String label, String[] args) {
        onCommand(sender, args);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (isDisabled()) {
            sender.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &e&l➜ &c¡Este comando está desactivado!"));
            return true;
        }

        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(TranslateColor.Code("&c¡Este comando no puede ser ejectuado en consola!"));
            return true;
        }

        if (isConsoleOnly() && sender instanceof Player) {
            sender.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &e&l➜ &c¡Este comando solo puede ser ejectuado por la consola!"));
            return true;
        }

        String permNode = getPermissionNode();
        if (!permNode.isEmpty() && !sender.hasPermission(permNode)) {
            sender.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &e&l➜ &c¡No tienes permisos para hacer esto!"));
            return true;
        }

        List<SubCommand> subCommands = getSubCommands();
        if (args.length == 0 || subCommands.isEmpty()) {
            onCommand(sender, label, args);
            return true;
        }

        String arg = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        Optional<SubCommand> command = subCommands.stream().filter(subCmd -> {
            if (subCmd.getArgument().equalsIgnoreCase(arg))
                return true;

            List<String> aliases = subCmd.getAliases();
            if (aliases == null || aliases.isEmpty())
                return false;

            return aliases.contains(arg.toLowerCase());
        }).findFirst();

        if (command.isPresent())
            runSubCommand(command.get(), sender, newArgs);
        else
            onCommand(sender, label, args);

        return true;
    }

    private void runSubCommand(SubCommand subCommand, CommandSender sender, String[] args) {
        subCommand.execute(sender, args);
    }

    @Deprecated
    public boolean hasPermission(CommandSender sender, String perm) {
        Server server = Bukkit.getServer();
        Player p = server.getPlayer(sender.getName());
        if (p == null) {
            return server.getConsoleSender().hasPermission(perm);
        } else {
            return p.hasPermission(perm);
        }
    }

    public void ifHasPermission(CommandSender sender, String perm, Consumer<CommandSender> consumer) {
        if (sender.hasPermission(perm))
            consumer.accept(sender);
    }

    public void ifNotHasPermission(CommandSender sender, String perm, Consumer<CommandSender> consumer) {
        if (!sender.hasPermission(perm))
            consumer.accept(sender);
    }

    public void ifPlayer(CommandSender sender, Consumer<Player> consumer) {
        if (sender instanceof Player)
            consumer.accept((Player) sender);
    }

    public void ifConsole(CommandSender sender, Consumer<ConsoleCommandSender> consumer) {
        if (sender instanceof ConsoleCommandSender)
            consumer.accept((ConsoleCommandSender) sender);
    }

    public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public void addSubCommands(SubCommand... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    public void setTabComplete(BiFunction<CommandSender, String[], List<String>> function) {
        this.completer = (sender, command, alias, args) -> {
            if (alias.equalsIgnoreCase(getName()) || getAliases().contains(alias.toLowerCase())) {
                return function.apply(sender, args);
            }
            return null;
        };
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (completer == null || this.completer.onTabComplete(sender, this, alias, args) == null) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            ArrayList<String> matchedPlayers = new ArrayList<>();
            sender.getServer().getOnlinePlayers().stream()
                    .filter(player -> senderPlayer == null || senderPlayer.canSee(player) && StringUtil.startsWithIgnoreCase(player.getName(), lastWord))
                    .forEach(player -> matchedPlayers.add(player.getName()));

            matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        }
        return this.completer.onTabComplete(sender, this, alias, args);
    }
}