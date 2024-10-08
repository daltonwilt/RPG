package com.outcast.rpg.service.party;

import com.outcast.rpg.RPG;
import com.outcast.rpg.party.Party;

import java.util.HashSet;
import java.util.Set;

public class PartyService {

    private final RPG plugin;

    private final Set<Party> parties = new HashSet<>();

    public PartyService(RPG plugin) {
        this.plugin = plugin;
    }

    public Set<Party> getParties() {
        return this.parties;
    }
    public void addParty(Party party) {
        this.parties.add(party);
    }
    public void removeParty(Party party) {
        this.parties.remove(party);
    }

}
