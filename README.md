RPG
------------------------------------
Welcome To the SHIT SHOW!

TODO
------------------------------------
- Test Database connection ( Setup SQL Connection )

- Setup Command Handler Ideally create a command class with a builder function. So we can create
  commands easily by utilizing a command configuration class and pass params to the class
  - CommandHandler
  - CommandService
  - Command


- Create Dynamic configuration storage
  - Ability to parse through multiple directories in order to find specific needed yml files
  - I.E.
    - Config
      - archetype
        - Archetypes.yml
      - attributes
        - Attributes.conf
      - sql_tables
        - SQL_Character.conf
      - RPGConfig.conf

Service:
  - This is where all the Managers/Service classes will be located (I.E. Damage Calculations, Expression Calculations)

facade:
  - This is where all the call methods will be located for commands (I.E. addExperience, removeExperience)