## Attributes

This capability provide the `net.simpleapi.attributes.Attributes` interface.

Really basic sample:

    {
        "name": "atr-sample",
        "store": true,
        "type": "attributes",
        "construct": {
            "strength": {
                "type": "float",
                "mode": "lazy"
            }
        }
    }

This will create an float attribute named `strength` into the capability.

This data will auto save/load to/from NBT when Minecraft save/load.

Also, this data will be auto-sync on both (client/server) side **ONLY when the client open a Container with GUI!**.

To make it sync realtime (if you want to show this data like player's hunger or health in hub), change the mode into `constantly`: 

    {
        "name": "atr-sample",
        "store": true,
        "type": "attributes",
        "construct": {
            "strength": {
                "type": "float",
                "mode": "constantly"
            }
        }
    }

To change the type of data, just change the `type` property:

    {
        "name": "atr-sample",
        "store": true,
        "type": "attributes",
        "construct": {
            "strength": {
                "type": "int",
                "mode": "constantly"
            }
        }
    }

Currently supporting data type: `int, float, short, boolean, string, long, double, enum`

Notice that we support `enum`, this is a special case, you need to have the `full classpath` of this enum class:

    {
        "name": "atr-sample",
        "store": true,
        "type": "attributes",
        "construct": {
            "strength": {
                "type": "org.someorg.proj.EnumClassName",
                "mode": "constantly"
            }
        }
    }

Now there is only two `mode`: `lazy` and `constantly`. The effect is described as above.

This only affects when the data will be sync.