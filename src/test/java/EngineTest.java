import net.simpleAPI.item.ItemCreate;
import net.simpleAPI.item.ItemUpdate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author ci010
 */
public class EngineTest
{
	String pre = "var ItemBlockDestroy = Java.type('ItemBlockDestroy');\n" +
			"var ItemBlockInteract = Java.type('ItemBlockInteract');\n" +
			"var ItemCreate = Java.type('ItemCreate');\n" +
			"var ItemEntityHandler = Java.type('ItemEntityHandler');\n" +
			"var ItemUpdate = Java.type('ItemUpdate');\n" +
			"var ItemUsing = Java.type('ItemUsing');";

	public void test() throws ScriptException, NoSuchMethodException
	{
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(pre);
		Object eval = engine.eval(
				" type = Java.extend(ItemUpdate, ItemCreate);" +
						" inst = new type() {\n" +
						"   onUpdate: function(stack, world, entity, slot, isSelect) {print('update');},\n" +
						"	onCreated: function(stack, world, player){print('create');}" +
						"};"
		);

		System.out.println(eval);
		System.out.println(eval.getClass());
		ItemCreate eval1 = (ItemCreate) eval;
		eval1.onCreated(null, null, null);
		ItemUpdate eval2 = (ItemUpdate) eval;
		eval2.onUpdate(null, null, null, 0, false);

		Object sec = engine.eval(
				" type = Java.extend(ItemUpdate, ItemCreate);" +
						" inst = new type() {\n" +
						"   onUpdate: function(stack, world, entity, slot, isSelect) {print('update2');},\n" +
						"	onCreated: function(stack, world, player){print('create2');}" +
						"};"
		);

		eval1 = (ItemCreate) sec;
		eval1.onCreated(null, null, null);
		eval2 = (ItemUpdate) sec;
		eval2.onUpdate(null, null, null, 0, false);
	}
}
