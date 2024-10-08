package com.outcast.rpg.command;

import com.outcast.rpg.RPG;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCommand extends Command implements CommandExecutor, PluginIdentifiableCommand {

    private static CommandMap commandMap;

    static {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch(IllegalAccessException | NoSuchFieldException e) {
            RPG.severe("Cause :: %s", e.getCause());
            RPG.severe("Error :: %s", e.getMessage());
            e.printStackTrace();
        }
    }

    @Getter
    private final HashMap<Integer, ArrayList<TabCommand>> tabComplete;
    private boolean register = false;

    public AbstractCommand(@NotNull String name) {
        super(name);

        assert commandMap != null;
        assert !name.isEmpty();

        tabComplete = new HashMap<>();
    }

    protected void setAliases(String... aliases) {
        if(aliases != null && (register || aliases.length > 0))
            setAliases(Arrays.stream(aliases).collect(Collectors.toList()));
    }

    protected void registerCommand() {
        if(!register) { register = commandMap.register(getPlugin().getName(), this); }
    }

    protected void addTabComplete(int indice, String... arg) {
        addTabComplete(indice, null, null, arg);
    }
    protected void addTabComplete(int indice, String permission, String[] beforeText, String... arg) {
        if (arg != null && arg.length > 0 && indice >= 0) {
            if (tabComplete.containsKey(indice)) {
                tabComplete.get(indice).addAll(Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll));
            }else {
                tabComplete.put(indice, Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll)
                );
            }
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {

        int indice = args.length - 1;

        if ((getPermission() != null && !sender.hasPermission(getPermission())) || tabComplete.isEmpty() || !tabComplete.containsKey(indice))
            return super.tabComplete(sender, alias, args);

        List<String> list = tabComplete.get(indice).stream()
                .filter(tabCommand -> tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1]))
                .filter(tabCommand -> tabCommand.getPermission() == null || sender.hasPermission(tabCommand.getPermission()))
                .map(TabCommand::getText)
                .filter(text -> text.startsWith(args[indice]))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        return list.isEmpty() ? super.tabComplete(sender, alias, args) : list;

    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String command, String[] arg) {
        if (getPermission() != null) {
            if (!commandSender.hasPermission(getPermission())) {
                if (getPermissionMessage() == null) {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                }else {
                    commandSender.sendMessage(getPermissionMessage());
                }
                return false;
            }
        }
        if (onCommand(commandSender, this, command, arg))
            return true;
        commandSender.sendMessage(ChatColor.RED + getUsage());
        return false;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return RPG.getInstance();
    }

    @Getter
    private static class TabCommand {
        private final int indice;
        private final String text;
        private final String permission;
        private final ArrayList<String> textAvant;

        private TabCommand(int indice, String text, String permission, String... textAvant) {
            this.indice = indice;
            this.text = text;
            this.permission = permission;
            if (textAvant == null || textAvant.length < 1) {
                this.textAvant = null;
            }else {
                this.textAvant = Arrays.stream(textAvant).collect(ArrayList::new,
                        ArrayList::add,
                        ArrayList::addAll);
            }
        }
    }

}
