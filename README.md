## SimpleAPI

The mod that provide the json (maybe also js) API for Minecraft Modding.

## File Structure
All the definition json should go to the `defs` folder under your mod assets,
the `assets/modid/defs` folders.

For `Item`, it goes `items` sub-folder: `assets/modid/defs/items`

For `Block`, it goes `blocks` sub-folder: `assets/modid/defs/blocks`

For `Entity`, it goes `entities` sub-folder: `assets/modid/defs/entities`

All the JSON contained by these 3 folders above, will be loaded into Minecraft.

## API details
All the JSON files in definition folder, should be a JSONArray looked like:
````
[
	{component-definition-a},
	{component-definition-b}
	...
]
````

Each component definition should have a property `id` which is unique to others.

[Capability definition](docs/Capabilities.md)
[Component definition for items](docs/Item.md)
