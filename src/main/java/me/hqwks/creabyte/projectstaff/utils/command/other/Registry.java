package me.hqwks.creabyte.projectstaff.utils.command.other;

public interface Registry<T> {

    @SuppressWarnings("unchecked")
    void register(T... types);

}