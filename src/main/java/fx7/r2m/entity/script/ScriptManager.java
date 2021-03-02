package fx7.r2m.entity.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fx7.r2m.rest.RestException;

public class ScriptManager
{
	private final Map<String, ScriptEntity> nameScript;

	public ScriptManager(List<ScriptEntity> scripts)
	{
		this.nameScript = initScriptMap(scripts);
	}

	private Map<String, ScriptEntity> initScriptMap(List<ScriptEntity> scripts)
	{
		Map<String, ScriptEntity> nameScript = new HashMap<>();

		if (scripts != null)
		{
			for (ScriptEntity se : scripts)
				nameScript.put(se.getEntityName(), se);
		}

		return nameScript;
	}

	public ScriptEntity getScript(String scriptName) throws RestException
	{
		if (scriptName == null || scriptName.isEmpty())
			throw RestException.invalidParameter("Script '" + scriptName + "' is unknown");

		ScriptEntity script = nameScript.get(scriptName);
		if (script == null || !script.getEntityName().equals(scriptName))
			throw RestException.invalidParameter("Script '" + scriptName + "' is unknown");

		return script;
	}

	public List<ScriptEntity> getScripts()
	{
		Collection<ScriptEntity> scripts = nameScript.values();
		List<ScriptEntity> access = new ArrayList<>();
		if (scripts != null)
			access.addAll(scripts);

		Collections.sort(access);
		return access;
	}
}
