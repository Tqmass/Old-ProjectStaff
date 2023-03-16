package me.hqwks.creabyte.projectstaff.utils.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class SubCommand {

    @Getter
    private final List<SubCommand> subCommands = new ArrayList<>();
    @Setter
    @Getter
    private String argument;
    @Getter
    @Setter
    private List<String> aliases;
    @Getter
    @Setter
    private String permission = "";
    @Getter
    @Setter
    private boolean consoleOnly = false;
    @Getter
    @Setter
    private boolean playerOnly = false;
    @Getter
    @Setter
    private boolean disabled;


    public SubCommand() {
        this(null, null);
    }

    public SubCommand(String argument) {
        this(argument, Collections.emptyList());
    }

    public SubCommand(String argument, List<String> aliases) {
        this.argument = argument;
        this.aliases = aliases;

        if (this.getClass().isAnnotationPresent(CommandInfo.class)) {
            CommandInfo annotation = this.getClass().getAnnotation(CommandInfo.class);
            setArgument(annotation.name());

            if (annotation.aliases().length != 0) {
                String[] alias = annotation.aliases();
                List<String> a = new ArrayList<>(Arrays.asList(alias));
                setAliases(a);
            }

            if (!annotation.permission().isEmpty())
                setPermission(annotation.permission());

        }
    }

    public void execute(CommandSender sender, String[] args) {

        if (isDisabled()) {
            sender.sendMessage(ChatColor.RED + "This command is currently disabled!");
            return;
        }

        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You cannot do this.");
            return;
        }

        if (isConsoleOnly() && sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "You cannot do this.");
            return;
        }

        if (!getPermission().isEmpty()) {
            if (!sender.hasPermission(getPermission())) {
                sender.sendMessage(ChatColor.RED + "No Permission.");
                return;
            }
        }

        List<SubCommand> subCommands = getSubCommands();
        if (args.length == 0 || subCommands.isEmpty()) {
            runCommand(sender, args);
            return;
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
            runCommand(sender, args);
    }

    public abstract void runCommand(CommandSender sender, String[] args);

    public void addSubCommands(SubCommand... subCommands) {
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    private void runSubCommand(SubCommand subCommand, CommandSender sender, String[] args) {
        subCommand.execute(sender, args);
    }

    public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

}