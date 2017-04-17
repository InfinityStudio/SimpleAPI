This folder contains various samples and is not done yet.

These samples/standard might change (due to the design problem).

`mod_entry.json` The sample of a mod json entry.

In this file, the basic object is the mod (namespace/modid).
Like `api_test` is the basic modid for the test mod, 
and it contains all the metadata/definition of the mods.

It plans to support the `mcmod.info` (fml mod default metadata) format for this.

There are some extra objects for game components: `item`, `block`, `entity` and other useful definition supporting in 
the future.

The property `item` is for defining some new items by json.
