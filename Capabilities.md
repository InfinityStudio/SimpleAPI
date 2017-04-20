## Capabilities

The capabilities property should be a JSONArray containing all the capability definition.

#### Capability Definition

`name`: The name of this capability, recommend to use ResourceLocation format: `domain:name`. 
This should be identical with other capabilities in the same capability provider.

`store`: If this capability need to be serialized into nbt, and could be restore after game restart.

`type`: The type of capability. Normally, it should be the full class path of a capability type class like:
`org.packagename.cap.CapabilityType`. Some of the mod may support short name by assigning the name into `net
.simpleAPI.CapabilitiesBridge`. (the way to assign may be changed in the future)

`construct`: **(optional)** The JSONObject that used to create/init the capability. 
Some of the capabilities might need this, some of them might not.
**Notice that this property need the author of capability to support! Normal capability won't support this!**


#### Support the capability construct
This is a trick feature since that the Forge default implementation don't provide or allow you to do this.

In order to support the `construct`, you need to register a function that 
could build JSONObject into a Capability Object into `CapabilitiesBridge`.







