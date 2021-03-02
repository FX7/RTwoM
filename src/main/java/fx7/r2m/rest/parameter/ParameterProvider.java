package fx7.r2m.rest.parameter;

import fx7.r2m.rest.RestException;

public interface ParameterProvider<T>
{
	public boolean hasMore();

	public T peek() throws RestException;

	public T consume() throws RestException;
}
