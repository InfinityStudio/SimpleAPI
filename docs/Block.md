## Block Json Object

`id`: The id of the block, should be identical.

`states`: The property containing [BlockStates Object](#block_states_json_object) of the block. 

`capabilites`: An optional property, see [Capabilities](Capabilities.md)

### Block States Json Object

This object will define the various states of block.

You have to have a state named default. Or, you write the block state property to the **root level**.

Common case:

    {
        "id": "eg_block",
        "states": {
            "default": {
                "lightOpacity": 1,
                "lightLevel": 0.5,
                // other default properties all here
            },
            "someOtherState": {
                // some other property
            }
        },
    }

All property goes **root level**:

    {
        "id": "eg_block",
        "states": {
            "someOtherState": {
                // some other property
            }
        },
        "lightOpacity": 1,
        "lightLevel": 0.5,
        // other default properties all here
    }

Then you could have some other custom states.

The state itself contains several property:

| Property     | Default Value                                     | Range                             | Description                                                                                                       | Example                               |
| ------------ | ------------------------------------------------- | --------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------------------------- |
| material     | No default, required field                        | [Material](#material)             | The various material related behavior of block                                                                    | "grass"                               |
| harvest      | {}                                                | [Harvest Object](#harvest_object) | Provide the harvest behavior of block.                                                                            | {"level": "stone", "tool": "pickaxe"} |
| lightOpacity | 16                                                | 0-16                              | Sets how much light is subtracted when going through this block This is only used if isOpaqueCube() returns false | 14                                    |
| lightLevel   | 0.0                                               | 0.0-1.0                           | Sets how much light is emitted from the block. 0 means no light.                                                  | 0.5                                   |
| unbreakable  | false                                             | true, false                       | Toggle the block unbreakable in survival.                                                                         | true                                  |
| hardness     | depends on material (stone:1.5; obsidian:50.0)    | All Float                         | Sets how long it takes to break the block.                                                                        | 4.0                                   |
| resistance   | depends on material (stone:10.0; obsidian:2000.0) | All Float                         | Sets the block's resistance against explosions                                                                    | 300                                   |
| stepSound    | depends on material                               | String                            | Sets the step sound of a block .                                                                                  | "stone"                               |

### Harvest Object

The json object represents the harvest information of block.

| Property | Default Value | Range                                                                  | Description                           | Example |
| -------- | ------------- | ---------------------------------------------------------------------- | ------------------------------------- | ------- |
| level    | "wood"        | "wood", "stone", "iron", "diamond"                                     | What level of tool could harvest this | "iron"  |
| tool     | ""            | The id of the tools, for default minecraft: "pickaxe", "axe", "shovel" | What kind of tool could harvest this  | "axe"   |

largely ref from:
<https://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-8/first-block/>

### Material

**this section is WIP**

The string or object represent the material of block.

Object format:

| Property   | Default Value | Range                                               | Description                                             |
| ---------- | ------------- | --------------------------------------------------- | ------------------------------------------------------- |
| isLiquid   | false         | true, false                                         | If this material is type of liquid                      |
| flammbale  | false         | true, false                                         | If this block can catch on fire                         |
| replacable | false         | true, false                                         | If this block is like tall grass or air can be replaced |
| mobility   | "normal"      | "normal", "destroy", "block", "ignore", "push_only" | What kind of tool could harvest this                    |
| isSolid    | true          | true, false                                         | If this is a solid block. This is arguably...           |

Minecraft commonly used (in my opinion) built-in material:

| Material | BlockLight | Flammbale | Replacable | Solid | Mobility |
| -------- | ---------- | --------- | ---------- | ----- | -------- |
| air      | false      | false     | true       | false | 0        |
| grass    | false      | false     | true       | false | 0        |
| ground   | false      | false     | true       | false | 0        |
| wood     | false      | false     | true       | false | 0        |
| rock     | false      | false     | true       | false | 0        |
| iron     | false      | false     | true       | false | 0        |
| water    | false      | false     | true       | false | 0        |
| plants   | false      | false     | true       | false | 0        |
| cloth    | false      | false     | true       | false | 0        |
| leaves   | false      | false     | true       | false | 0        |
| sand     | false      | false     | true       | false | 0        |
| fire     | false      | false     | true       | false | 0        |
| ice      | false      | false     | true       | false | 0        |
| snow     | false      | false     | true       | false | 0        |
| glass    | false      | false     | true       | false | 0        |
| clay     | false      | false     | true       | false | 0        |
| cake     | false      | false     | true       | false | 0        |
| gourd    | false      | false     | true       | false | 0        |

Minecraft special usage (in my opinion) built-in material:

| Material       | BlockLight | Flammbale | Replacable | Solid | Mobility |
| -------------- | ---------- | --------- | ---------- | ----- | -------- |
| lava           | false      | false     | true       | false | 0        |
| anvil          | false      | false     | true       | false | 0        |
| vine           | false      | false     | true       | false | 0        |
| sponge         | false      | false     | true       | false | 0        |
| circuits       | false      | false     | true       | false | 0        |
| carpet         | false      | false     | true       | false | 0        |
| redstone light | false      | false     | true       | false | 0        |
| tnt            | false      | false     | true       | false | 0        |
| coral          | false      | false     | true       | false | 0        |
| packed ice     | false      | false     | true       | false | 0        |
| crafted snow   | false      | false     | true       | false | 0        |
| cactus         | false      | false     | true       | false | 0        |
| dragon egg     | false      | false     | true       | false | 0        |
| web            | false      | false     | true       | false | 0        |
| piston         | false      | false     | true       | false | 0        |
| barrier        | false      | false     | true       | false | 0        |
| structure void | false      | false     | true       | false | 0        |

largely ref from:
<https://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/block-materials/>
