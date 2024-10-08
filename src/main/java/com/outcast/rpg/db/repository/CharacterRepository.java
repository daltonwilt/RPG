package com.outcast.rpg.db.repository;

import com.outcast.rpg.character.Character;
import com.outcast.rpg.db.CachedHibernateRepository;
import com.google.inject.Singleton;

import java.util.UUID;

@Singleton
public class CharacterRepository extends CachedHibernateRepository<Character, UUID> {
    public CharacterRepository() {
        super(Character.class);
    }
}
