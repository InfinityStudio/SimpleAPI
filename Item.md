## Item Json API
`id`: The id of the item, should be identical.

`type`: The type of the item. See [Types And Properties](#creative-tabs).
 
`creativeTab`: The creative tab of this item.

View [Sample](sample/README.md)
 
## Types And Properties
`stack`: Indicates that this is a common stack of items. 
 * `maxStackSize`: The max stackable size of this item.	
 * `maxMeta`: The max meta type of this item. Used for same item different icon.

`consumable`: Indicates that this is a no-stackable but consumable item.
 * `maxDamage`: The max durability of this item. 
 
`tool`: Indicates that the item is created by Minecraft template of tool.
 * `attackDamage`: The attack damage of this tool. (TODO: formula require)
 * `attackSpeed`: The attack speed of this tool. (TODO: formula require)
 * `material`: The material name of this tool.
 * `effectiveBlocks`: The `JSONArray` containing the minecraft block's `ResourceLocation`.

`food`: Indicates that the item is create by Minecraft template of food.
 * `healAmount`: The amount of healing of this food. (TODO: formula require)
 * `saturation`: The amount of saturation of this food. (TODO: formula require)
 * `isWolfFood`: If this item is can be eaten by food.
 
### Creative Tabs
- buildingBlocks 
- decorations 
- redstone 
- transportation 
- misc 
- food 
- tools 
- combat 
- brewing 
- materials 
- inventory
