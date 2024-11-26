package com.outcast.rpg.config.loot;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CurrencyLootConfig {

    @Setting("currency")
    public String CURRENCY = "vault:coin";

    @Setting("minimum")
    public Double MINIMUM = 0.0d;

    @Setting("maximum")
    public Double MAXIMUM = 120.0d;

    public CurrencyLootConfig() {}

}