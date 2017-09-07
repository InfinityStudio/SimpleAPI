## Block Json Object

`id`: The id of the block, should be identical.

`states`: The property containing [BlockStates](#blockstates) of the block. 

`properties`: The properties of the block.

`capabilites`: An optional property, see [Capabilities](Capabilities.md)

### BlockStates

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

| Property   | Default Value                                     | Range                    | Description                                                                                                       | Example                                             |
| ---------- | ------------------------------------------------- | ------------------------ | ----------------------------------------------------------------------------------------------------------------- | --------------------------------------------------- |
| material   | No default, required field                        | [Material](#material)    | The various material related behavior of block                                                                    | "grass"                                             |
| stepSound  | "stone"                                           | [SoundType](#soundtype)  | Sets the step sound of a block .                                                                                  | "wood"                                              |
| harvest    | {}                                                | [Harvest](#harvest)      | Provide the harvest behavior of block.                                                                            | {"level": "stone", "tool": "pickaxe"}               |
| bounding   | [0, 0, 0, 1, 1, 1]                                | 6 doubles                | The outline box of the block                                                                                      | [0, 0, 0, 1, 0.5625, 1]                             |
| collision  | \[[0, 0, 0, 1, 1, 1]]                             | Array of 6 doubles array | The collision box of the block                                                                                    | \[[0, 0, 0, 0.5, 0.5625, 0.5], [0.6,0.6,0.6,1,1,1]] |
| opacity    | 16                                                | 0-16                     | Sets how much light is subtracted when going through this block This is only used if isOpaqueCube() returns false | 14                                                  |
| brightness | 0.0                                               | 0.0-1.0                  | Sets how much light is emitted from the block. 0 means no light.                                                  | 0.5                                                 |
| hardness   | depends on material (stone:1.5; obsidian:50.0)    | All Float                | Sets how long it takes to break the block. Stone:1.5. Obsidian:50.0. Bedrock: -1                                  | 4.0                                                 |
| resistance | depends on material (stone:10.0; obsidian:2000.0) | All Float                | Sets the block's resistance against explosions                                                                    | 300                                                 |

### Harvest

The json object represents the harvest information of block.

| Property | Default Value | Range                                                                  | Description                           | Example |
| -------- | ------------- | ---------------------------------------------------------------------- | ------------------------------------- | ------- |
| level    | "wood"        | "wood", "stone", "iron", "diamond"                                     | What level of tool could harvest this | "iron"  |
| tool     | ""            | The id of the tools, for default minecraft: "pickaxe", "axe", "shovel" | What kind of tool could harvest this  | "axe"   |

largely ref from:
<https://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-8/first-block/>

### Material

**this section is WIP, I'm filling the table**

The string or object represent the material of block.

Object format:

| Property   | Default Value | Range                                     | Description                                             |
| ---------- | ------------- | ----------------------------------------- | ------------------------------------------------------- |
| isLiquid   | false         | true, false                               | If this material is type of liquid                      |
| flammbale  | false         | true, false                               | If this block can catch on fire                         |
| replacable | false         | true, false                               | If this block is like tall grass or air can be replaced |
| mobility   | ""            | "destroy", "block", "ignore", "push_only" | The behavior of the block pushed by piston              |
| isSolid    | true          | true, false                               | If this is a solid block. This is arguably...           |

Minecraft commonly used (in my opinion) built-in material:

| Material | BlockLight(√) | Flammbale(x) | Replacable(x) | Solid(√) | Mobility(normal) | translucent | tool |
| -------- | ------------- | ------------ | ------------- | -------- | ---------------- | ----------- | ---- |
| air      | x             |              | √             | x        |                  | √           |      |
| grass    |               |              |               |          |                  | x           |      |
| ground   |               |              |               |          |                  | x           |      |
| wood     |               | √            |               |          |                  |             |      |
| rock     |               |              |               |          |                  |             | √    |
| iron     |               |              |               |          |                  |             | √    |
| water    | x             |              | √             | x        | destroy          | √           |      |
| plants   | x             | √            |               | x        | destroy          |             |      |
| cloth    |               | √            |               |          |                  |             |      |
| leaves   |               | √            |               |          | destroy          |             |      |
| sand     |               |              |               |          |                  |             |      |
| fire     | x             |              | √             | x        | destroy          |             |      |
| ice      |               |              |               |          |                  |             |      |
| snow     | x             |              | √             | x        | destroy          |             | √    |
| glass    |               |              |               |          |                  |             |      |
| clay     |               |              |               |          |                  |             |      |
| cake     |               |              |               |          | destroy          |             |      |
| gourd    |               |              |               |          | destroy          |             |      |

Minecraft special usage (in my opinion) built-in material:

| Material       | BlockLight(√) | Flammbale(x) | Replacable(x) | Solid(√) | Mobility(normal) | tool |
| -------------- | ------------- | ------------ | ------------- | -------- | ---------------- | ---- |
| lava           | x             |              | √             | x        | destroy          |      |
| anvil          |               |              |               |          | block            | √    |
| vine           | x             | √            | √             | x        | destroy          |      |
| sponge         |               |              |               |          |                  |      |
| circuits       | x             |              |               | x        | destroy          |      |
| carpet         | x             | √            |               | x        |                  |      |
| redstone light |               |              |               |          |                  |      |
| tnt            |               | √            |               |          |                  |      |
| coral          |               |              |               |          | destroy          |      |
| packed ice     |               |              |               |          |                  |      |
| crafted snow   |               |              |               |          |                  | √    |
| cactus         |               |              |               |          |                  |      |
| dragon egg     |               |              |               |          | destroy          |      |
| web            |               |              |               |          | destroy          | √    |
| piston         |               |              |               |          |                  |      |
| barrier        |               |              |               |          | block            | √    |
| structure void | x             |              |               |          |                  |      |
| portal         | x             |              |               | x        | block            |      |

largely ref from:
<https://bedrockminer.jimdo.com/modding-tutorials/advanced-modding/block-materials/>

### SoundType

Minecraft have these built-in sound type: (too lazy to fill all the sound event)

| name   | volume | pitch | break | step | place | hit | fall |
| ------ | ------ | ----- | ----- | ---- | ----- | --- | ---- |
| wood   | 1      | 1     |       |      |       |     |      |
| ground | 1      | 1     |       |      |       |     |      |
| plant  | 1      | 1     |       |      |       |     |      |
| stone  | 1      | 1     |       |      |       |     |      |
| glass  | 1      | 1     |       |      |       |     |      |
| cloth  | 1      | 1     |       |      |       |     |      |
| sand   | 1      | 1     |       |      |       |     |      |
| snow   | 1      | 1     |       |      |       |     |      |
| ladder | 1      | 1     |       |      |       |     |      |
| anvil  | 0.3    | 1     |       |      |       |     |      |
| slime  | 1      | 1     |       |      |       |     |      |

To register own sound type, create file under the folder def/blocks/sound-types/ with file [soundTyle].json

Then the file format is 

    {
        "volume": 1.0,
        "pitch": 1.0,
        "break": "soundName",
        "step": "soundName",
        "place": "soundName",
        "hit": "soundName",
        "fall": "soundName"
    }

To view current built-in sound: [Sounds](Sounds.md) (NOT DONE YET)
