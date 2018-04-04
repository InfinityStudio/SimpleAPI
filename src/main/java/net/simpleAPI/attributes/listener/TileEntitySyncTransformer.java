package net.simpleAPI.attributes.listener;

import com.google.common.collect.ImmutableList;
import net.simpleAPI.coremod.ClassPatch;
import net.simpleAPI.coremod.ClassTransformerBase;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * @author ci010
 */
public class TileEntitySyncTransformer extends ClassTransformerBase {
	@Override
	protected void buildClassPatch(ImmutableList.Builder<ClassPatch> builder) {
		System.out.println("BUILD PATCH TO TILEENTITY");
		builder.add(new ClassPatch("net.minecraft.tileentity.TileEntity",
				new ClassPatch.MethodPatch("onDataPacket", "onDataPacket",
						"(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/network/play/server" +
								"/SPacketUpdateTileEntity;)V")
				{
					@Override
					public void visitCode() {
						/*L0
						LINENUMBER 17 L0
						ALOAD 1
						ALOAD 2
						INVOKESTATIC net/simpleAPI/attributes/listener/TileSyncListener.$onDataPacket (Lnet/minecraft/network/NetworkManager;Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;)V
								L1
						LINENUMBER 18 L1
								RETURN
						L2
						LOCALVARIABLE this Lnet/minecraft/tileentity/TileEntity; L0 L2 0
						LOCALVARIABLE net Lnet/minecraft/network/NetworkManager; L0 L2 1
						LOCALVARIABLE pkt Lnet/minecraft/network/play/server/SPacketUpdateTileEntity; L0 L2 2
						MAXSTACK = 2
						MAXLOCALS = 3*/

						Label l0 = new Label();
						visitLabel(l0);
						visitIntInsn(Opcodes.ALOAD, 1);
						visitIntInsn(Opcodes.ALOAD, 2);
						visitMethodInsn(Opcodes.INVOKESTATIC,
								"net/simpleAPI/attributes/listener/TileSyncListener", "$onDataPacket",
								"(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;)V",
								false);
						Label l1 = new Label();
						visitLabel(l1);
						visitInsn(Opcodes.RETURN);

						Label l2 = new Label();
						visitLabel(l2);

						visitLocalVariable("this", "Lnet/minecraft/tileentity/TileEntity;", null, l0, l2, 0);
						visitLocalVariable("net", "Lnet/minecraft/tileentity/TileEntity;", null, l0, l2, 1);
						visitLocalVariable("pek", "Lnet/minecraft/tileentity/TileEntity;", null, l0, l2, 2);
						visitMaxs(2, 3);
					}
				},
				new ClassPatch.MethodPatch("getUpdatePacket", "c",
						"()Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;")
				{
					@Override
					public void visitCode() {
						/*L0
						LINENUMBER 23 L0
						ALOAD 0
						INVOKESTATIC net/simpleAPI/attributes/listener/TileSyncListener.$handle (Lnet/minecraft/tileentity/TileEntity;)Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;
						ARETURN
								L1
						LOCALVARIABLE this Lnet/minecraft/tileentity/TileEntity; L0 L1 0
						MAXSTACK = 1
						MAXLOCALS = 1*/

						Label l0 = new Label();
						visitLabel(l0);
						visitIntInsn(Opcodes.ALOAD, 0);
						visitMethodInsn(Opcodes.INVOKESTATIC,
								"net/simpleAPI/attributes/listener/TileSyncListener", "$getUpdatePacket",
								"(Lnet/minecraft/tileentity/TileEntity;)" +
										"Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;",
								false);
						visitInsn(Opcodes.ARETURN);
						Label l1 = new Label();
						visitLabel(l1);
						visitLocalVariable("this", "Lnet/minecraft/tileentity/TileEntity;", null, l0, l1, 0);
						visitMaxs(1, 1);
					}
				}
		));
	}
}
