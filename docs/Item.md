## Item Json Object

`id`: The id of the item, should be identical.

`type`: The type of the item. See [Types And Properties](#types-and-properties).

`creativeTab`: (**optional**) The creative tab of this item.

`capabilites`: See [Capabilities](Capabilities.md)

### Types And Properties

For each type, it could have different properties.
These properties should be directly located at this item JSONObject.

**All type specific properties are optional!**

```(json)
{
    "id":"some-id",
    "type": "a-type",
    "a-type-specific-property": "value",
    "another-type-specific-property": "value",
    ...
}
```

#### Stack

The type `stack` indicates that this is a common stack of items.

It has these type specific properties:

| Property     | Default Value | Range                       | Description                          | Example                         |
| ------------ | ------------- | --------------------------- | ------------------------------------ | ------------------------------- |
| maxStackSize | 64            | All Integer                 | The max stackable size of this item. | 32                              |
| subTypes     | \[]           | JSONArray containing String | All the subtypes' id of item.        | ["gold", "iron", "red", "blue"] |

Example:

```(json)
{
    "id": "example-stack-item",
    "type": "stack",
    "maxStackSize": 32,
    "subTypes": ["wood", "iron", "stone"]
}
```

#### Consumable

The type `consumable` indicates that this is a no-stackable but consumable item.

It has these type specific properties:

| Property  | Default Value | Range       | Description                      | Example |
| --------- | ------------- | ----------- | -------------------------------- | ------- |
| maxDamage | 16            | All Integer | The max durability of this item. | 300     |

Example:

```(json)
{
    "id": "example-comsumable-item",
    "type": "comsumable",
    "maxDamage": 8
}
```

#### Tool

The type `tool` indicates that the item is created by Minecraft template of tool.

It has these type specific properties:

| Property        | Default Value | Range                                                              | Description                                             | Example                              |
| --------------- | ------------- | ------------------------------------------------------------------ | ------------------------------------------------------- | ------------------------------------ |
| attackDamage    | 0             | All Integer                                                        | The attack damage of this tool. (TODO: formula require) | 10                                   |
| attackSpeed     | 0             | All Integer                                                        | The attack speed of this tool. (TODO: formula require)  | 10                                   |
| material        | iron          | The enum ToolMaterial, default: {wood, stone, iron, gold, diamond} | The material name of this tool.                         | "gold"                               |
| effectiveBlocks | \[]           | The JSONArray containing block register names.                     | The effective block of this tool.                       | ["minecraft:dirt", "minecraft:rock"] |

Example:

```(json)
{
    "id": "example-comsumable-item",
    "type": "tool",
    "attackDamage": 3,
    "attackSpeed": 3
}
```

#### Food

The type `food` indicates that the item is create by Minecraft template of food.

It has these type specific properties:

| Property     | Default Value | Range                               | Description                                                                 | Example                                          |
| ------------ | ------------- | ----------------------------------- | --------------------------------------------------------------------------- | ------------------------------------------------ |
| healAmount   | 0             | Integer from 0-20                   | The amount of healing of this food.                                         | 10                                               |
| saturation   | 0.0           | Float from 0 - 20                   | The amount of saturation of this food.                                      | 10                                               |
| alwaysEdible | false         | Boolean                             | If this food can be eaten when player is full.                              | true                                             |
| isWolfFood   | false         | Boolean                             | If this item is can be eaten by food.                                       | true                                             |
| potionEffect | null          | {effect:string, probability:number} | The potion effect and probability of this potion effect active after eaten. | {"effect": "minecraft:speed" ,"probability":0.3} |

Example:

```(json)
{
    "id": "example-food-item",
    "type": "food",
    "healAmount": 3,
    "saturation": 3,
    "alwaysEdible": true,
    "potionEffect": {
        "effect": "minecraft:speed",
        "probability": 0.3
    }
}
```

### Creative Tabs Ids

-   buildingBlocks
-   decorations
-   redstone
-   transportation
-   misc
-   food
-   tools
-   combat
-   brewing
-   materials
-   inventory
