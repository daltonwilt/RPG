package com.outcast.rpg.util.exception;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandException;
import org.bukkit.entity.LivingEntity;


public class CastException extends CommandException {

    public CastException(LivingEntity user, TextComponent message) {
        super("Failed attempting to cast skill.");
        user.sendMessage(message);
    }

}
