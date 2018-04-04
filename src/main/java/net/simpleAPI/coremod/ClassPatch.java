package net.simpleAPI.coremod;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author ci010
 */
public class ClassPatch extends ClassVisitor {
	private MethodPatch[] patches;
	private String name;

	public ClassPatch(String name, MethodPatch... patches) {
		super(Opcodes.ASM4);
		this.patches = patches;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ClassPatch accept(ClassVisitor visitor)
	{
		this.cv = visitor;
		return this;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		for (MethodPatch patch : patches)
			if (patch.match(name, desc))
				return patch.apply(super.visitMethod(access, name, desc, signature, exceptions));
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public static class MethodPatch extends MethodVisitor {
		private String name, obfName, descriptor;

		boolean match(String name, String desc)
		{
			return (this.name.equals(name) || obfName.equals(name)) && this.descriptor.equals(desc);
		}

		public MethodPatch(String name, String obfName, String descriptor)
		{
			super(Opcodes.ASM4);
			this.name = name;
			this.obfName = obfName;
			this.descriptor = descriptor;
		}

		public MethodPatch apply(MethodVisitor visitor)
		{
			this.mv = visitor;
			return this;
		}

		public final void fallbackVisitMethod(int opcode, String owner, String name, String desc, boolean itf)
		{
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}
}
