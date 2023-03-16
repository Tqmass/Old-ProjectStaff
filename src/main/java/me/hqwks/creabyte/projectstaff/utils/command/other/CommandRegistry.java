package me.hqwks.creabyte.projectstaff.utils.command.other;


import me.hqwks.creabyte.projectstaff.utils.command.Command;

public interface CommandRegistry extends Registry<Command> {

    @SuppressWarnings("unchecked")
    void register(Command... commands);
}