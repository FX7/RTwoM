package fx7.r2m;

import java.util.concurrent.Callable;

import fx7.r2m.entity.script.ScriptManager;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.server.RestServerConfig;

public interface Coordinator
{
	public RestServerConfig getRestServerConfig();

	public ScriptManager getScriptManager();

	public void sendConsoleMessage(String message);

	public <T> T callSyncMethod(Callable<T> callable) throws RestException;
}
