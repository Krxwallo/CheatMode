![Curseforge Banner](https://mc.lookonthebrightsi.de/cheat_mode/images/main_trimmed.png)

[![Versions](http://cf.way2muchnoise.eu/versions/MC_cheat-mode_latest.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/cheat-mode)
[![Downloads](http://cf.way2muchnoise.eu/short_cheat-mode.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/cheat-mode)

# Cheat Mode

Cheat Mode is a small mod that allows players to access the creative inventory from the survival mode. In general, there are two different modes that can be switched between in the config file:

* <strong>The "open directly" mode:</strong> This is set by default. When it is enabled, the creative inventory will automatically open when the player "tries" to open the survival inventory (presses E).
* <strong>The "open on button press" mode</strong>: This can be enabled in the config. When it is enabled, a button will appear in the survival inventory which will open the creative inventory.

### Developers
If you want to add this mod to your development environment, add this to your ```build.gradle``` file:

```gradle
repositories {
     maven {
          name = "Cursemaven"
          url = "https://www.cursemaven.com"
     }

     // ....OTHER REPOSITORIES SUCH AS JEI...
}

dependencies {
     // ..... OTHER DEPENDENCIES ...

     compile "curse.maven:cm-398168:FILE_ID"
}
```
Replace FILE_ID with the corresponding file id targeted towards your minecraft version. For a list see the [official curseforge page](https://www.curseforge.com/minecraft/mc-mods/cheat-mode).
