package fx7.r2m.rest.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

import fx7.r2m.Coordinator;
import fx7.r2m.rest.handler.player.RestPlayerHandler;
import fx7.r2m.rest.handler.script.RestScriptHandler;
import fx7.r2m.rest.handler.world.RestWorldHandler;

public class RestServer
{
	private final String BASE_CONTEXT = "/rest/v1/";

	private final Coordinator coordinator;

	private HttpServer server;
	private RestServerConfig config;

	public RestServer(Coordinator coordinator)
	{
		this.coordinator = coordinator;
		this.config = coordinator.getRestServerConfig();
	}

	public void startServer() throws IOException
	{
		server = HttpServer.create(new InetSocketAddress(getConfig().getHost(), getConfig().getPort()),
				getConfig().getBacklog());
		server.createContext(BASE_CONTEXT + RestPlayerHandler.CONTEXT.asContext(), new RestPlayerHandler(this.coordinator));
		server.createContext(BASE_CONTEXT + RestWorldHandler.CONTEXT.asContext(), new RestWorldHandler(this.coordinator));
		server.createContext(BASE_CONTEXT + RestScriptHandler.CONTEXT.asContext(), new RestScriptHandler(this.coordinator));
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
		server.setExecutor(threadPoolExecutor);
		server.start();
		coordinator.sendConsoleMessage(
				"RestServer startet at http://" + getConfig().getHost() + ":" + getConfig().getPort() + BASE_CONTEXT);
	}

	public RestServerConfig getConfig()
	{
		return config;
	}

	public void shutdownServer()
	{
		if (server != null)
			server.stop(7);
	}
}
