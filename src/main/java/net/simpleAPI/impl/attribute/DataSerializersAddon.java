package net.simpleAPI.impl.attribute;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;

/**
 * @author ci010
 */
public class DataSerializersAddon
{
	public static final DataSerializer<Long> LONG;
	public static final DataSerializer<Short> SHORT;
	public static final DataSerializer<Double> DOUBLE;
	public static final DataSerializer<Character> CHARACTER;

	static
	{
		DataSerializers.registerSerializer(LONG = new DataSerializer<Long>()
		{
			@Override
			public void write(PacketBuffer buf, Long value)
			{
				buf.writeLong(value);
			}

			@Override
			public Long read(PacketBuffer buf) throws IOException
			{
				return buf.readLong();
			}

			@Override
			public DataParameter<Long> createKey(int id) {return new DataParameter<Long>(id, this);}
		});
		DataSerializers.registerSerializer(SHORT = new DataSerializer<Short>()
		{
			@Override
			public void write(PacketBuffer buf, Short value)
			{
				buf.writeShort(value);
			}

			@Override
			public Short read(PacketBuffer buf) throws IOException
			{
				return buf.readShort();
			}

			@Override
			public DataParameter<Short> createKey(int id)
			{
				return new DataParameter<Short>(id, this);
			}
		});
		DataSerializers.registerSerializer(DOUBLE = new DataSerializer<Double>()
		{
			@Override
			public void write(PacketBuffer buf, Double value)
			{
				buf.writeDouble(value);
			}

			@Override
			public Double read(PacketBuffer buf) throws IOException
			{
				return buf.readDouble();
			}

			@Override
			public DataParameter<Double> createKey(int id)
			{
				return new DataParameter<Double>(id, this);
			}
		});
		DataSerializers.registerSerializer(CHARACTER = new DataSerializer<Character>()
		{
			@Override
			public void write(PacketBuffer buf, Character value)
			{
				buf.writeChar(value);
			}

			@Override
			public Character read(PacketBuffer buf) throws IOException
			{
				return buf.readChar();
			}

			@Override
			public DataParameter<Character> createKey(int id)
			{
				return new DataParameter<Character>(id, this);
			}
		});
	}
}
