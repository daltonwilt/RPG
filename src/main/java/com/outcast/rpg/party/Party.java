package com.outcast.rpg.party;

import com.outcast.rpg.RPG;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Party {

    // Add configurable amount of capped players per party
    private final RPG plugin;
    private Character leader;

    private boolean pvp = false;

    private final Set<Character> members = new HashSet<>();
    private final Set<Character> invites = new HashSet<>();

    public Party(@NotNull Character character, RPG plugin) {
        this.plugin = plugin;
        this.leader = character;
        this.members.add(character);
//        this.cManager = plugin.getChannelManager();
//        if(this.cManager != null) this.cManager.createChannel(this);
    }

    // Check if entity is member of the Party
    public boolean isMember(Character c) {
        return this.members.contains(c);
    }
    public boolean isPMember(Player player) {
        return false;
    }
    public boolean isLEMember(LivingEntity le) {
        return false;
    }

    public Set<Character> getMembers() {
        return this.members;
    }
    public void addMember(Character c) {
        this.members.add(c);
    }
    public void removeMember(Character c) {
        this.members.remove(c);
    }


    // Check if entity is invite to the Party
    public boolean isInvite(Character c) {
        return this.invites.contains(c);
    }
    public boolean isPInvite(Player player) {
        return false;
    }
    public boolean isLEInvite(LivingEntity le) {
        return false;
    }

    public Set<Character> getInvites() {
        return this.invites;
    }
    public void addInvite(Character c) {
        this.invites.add(c);
    }
    public void removeInvite(Character c) {
        this.invites.remove(c);
    }

    public Character getLeader() {
        return this.leader;
    }
    public void setLeader(@NotNull Character c) {
        this.leader = c;
        message(ChatColor.GRAY + "New party leader assigned.");
    }

    public Boolean isPvp() {
        return this.pvp;
    }
    public void togglePvp() {
        if(this.pvp) {
            this.pvp = false;
            message(ChatColor.GREEN + "Party PvP disabled.");
        } else {
            this.pvp = true;
            message(ChatColor.RED + "Party PvP enabled.");
        }
    }

    public void message(String message) {
        for(Character c : this.members) {
            // get player from character then send message ();
        }
    }

//    shareExp

//    update in the future to show on scoreboard

}
