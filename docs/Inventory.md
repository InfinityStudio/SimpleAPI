## Inventory

This capability provide the `net.simpleapi.inventory.Inventory` interface.

Really basic sample:

    {
        "name": "inv-sample",
        "store": true,
        "type": "inventory",
        "construct": {
            "default": 10
        }
    }

It will construct a slot group named "default" with size 10.

You can also have various named slots:

    {
        "name": "furnace-sample",
        "store": true,
        "type": "inventory",
        "construct": {
            "fuel": 1,
            "ingredients": 2
        }
    }

And, if you want to limit the stack size and item:

    {
        "name": "furnace-sample",
        "store": true,
        "type": "inventory",
        "construct": {
            "fuel": {
                "size": 1,
                "limit": 32,
                "valid": ["minecraft:coal", "minecraft:wood"],
            },
            "ingredients": {
                "size": 4,
                "limit": 128,
                "invalid": ["minecraft:coal", "minecraft:wood"],
            }
        }
    }

The `limit` property set the max stack size of the fuel slot
The `valid` property will only accept the items in it (with `ResourceLocation` format)
The `invalid` property will deny the items in it (with `ResourceLocation` format)

