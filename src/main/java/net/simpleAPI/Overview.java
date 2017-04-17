package net.simpleAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author ci010
 */
public interface Overview<T>
{
	@Nullable
	T getById(int id);

	@Nullable
	T getByName(@Nonnull String name);

	@Nonnull
	Collection<? extends T> getAll();

	T[] toArray();

	int size();

	@Nonnull
	Collection<String> allPresent();
}
