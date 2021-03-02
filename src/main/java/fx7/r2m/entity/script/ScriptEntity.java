package fx7.r2m.entity.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fx7.r2m.access.Context;
import fx7.r2m.entity.ContextEntity;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;

@SerializableAs("ScriptEntity")
public class ScriptEntity extends ContextEntity implements ConfigurationSerializable
{
	private static final String PARAM_NAME = "name";

	private static final String PARAM_ACTIONS = "actions";
	private List<RestActionStub> actionStubs = new ArrayList<>();

	private List<RestAction> actions;

	public ScriptEntity(String name, List<RestActionStub> actions)
	{
		super(Context.SCRIPT, name);
		if (actions != null)
			this.actionStubs = actions;
		else
			this.actionStubs = new ArrayList<>();
	}

	public List<RestAction> getRestActions() throws RestException
	{
		List<RestAction> actions = this.actions;
		if (actions == null)
		{
			actions = new ArrayList<>();
			for (RestActionStub as : actionStubs)
			{
				actions.add(as.toRestAction());
			}
			this.actions = actions;
		}
		return actions;
	}

	public static ScriptEntity deserialize(Map<String, Object> serialized)
	{
		return new ScriptEntity(//
				(String) serialized.get(PARAM_NAME), //
				(List<RestActionStub>) serialized.get(PARAM_ACTIONS));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> serialized = new HashMap<>();

		serialized.put(PARAM_NAME, getEntityName());
		serialized.put(PARAM_ACTIONS, actionStubs);

		return serialized;
	}
}
