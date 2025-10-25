# Disable Elytra Outside The End

The mod does what it says in the name

since v2.0:
- **[YACL](https://modrinth.com/mod/yacl) is required**
- Mod would sync config with server and disable itself if mod is not installed on server (different mod-loaders works fine)
- Firework boosting can be disabled instead of full flight block
- Mod is **not** version-agnostic now

TODO:
- [ ] Config reload config on server (on integrated server you can edit config in-game via YACL)
- [ ] Spigot/Paper plugin (why not)
- [ ] Custom dimensions list

> I'm not planning to backport mod to <1.21.1, since networking got changed and I don't want to get into it.
> 
> _But source code is open, so you can do it by yourself._

It's not recommended to use v1.x, since it's buggy and terrible.