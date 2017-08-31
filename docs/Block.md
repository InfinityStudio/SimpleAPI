## Block Json Object

`id`: The id of the block, should be identical.

`material`: The mateiral of the block. See [Material](Material.md)

`capabilites`: An optional property, see [Capabilities](Capabilities.md)

### Optional properties

| Property     | Default Value                                     | Range                              | Description                                                                                                                                            | Example |
| ------------ | ------------------------------------------------- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ | ------- |
| lightOpacity | 16                                                | 0-16                               | sets how much light is subtracted when going through this block This is only used if isOpaqueCube() returns false                                      | 14      |
| lightLevel   | 0.0                                               | 0.0-1.0                            | sets how much light is emitted from the block. 0 means no light.                                                                                       | 0.5     |
| harvestLevel | 0                                                 | "wood", "stone", "iron", "diamond" | tool: "pickaxe", "axe", "shovel". sets the tool and the tool level to break a block. If you don't use this, the break level is defined by the material | "wood"  |
| unbreakable  | false                                             | true, false                        | makes the block unbreakable in survival .                                                                                                              | true    |
| hardness     | depends on material (stone:1.5; obsidian:50.0)    | All Float                          | sets how long it takes to break the block.                                                                                                             | 4.0     |
| resistance   | depends on material (stone:10.0; obsidian:2000.0) | All Float                          | sets the block's resistance against explosions                                                                                                         | 300     |
| stepSound    | depends on material                               | String                             | sets the step sound of a block .                                                                                                                       | "stone" |

    largely ref from:
    https://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/block-materials/
    https://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-8/first-block/

example

```(json)
{
    "id":"some-id",
    "type": "some-type",
    "capability": {},
}
```