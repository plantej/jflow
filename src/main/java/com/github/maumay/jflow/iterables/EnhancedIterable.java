package com.github.maumay.jflow.iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.maumay.jflow.iterators.EnhancedIterator;

/**
 * Abstraction of iterable object which can construct enhanced iterators
 * ({@link EnhancedIterator}).
 *
 * @param <E> The type of element this object can iterate over.
 *
 * @author ThomasB
 */
@FunctionalInterface
public interface EnhancedIterable<E> extends Iterable<E>
{
	/**
	 * Constructs an iterator traversing the elements encapsulated by this object.
	 * 
	 * @return See above.
	 */
	EnhancedIterator<E> iter();

	@Override
	default EnhancedIterator<E> iterator()
	{
		return iter();
	}

	/**
	 * Finds the first element (if any) which satisfies a given predicate.
	 * 
	 * @param predicate The predicate which will be used to test elements.
	 * @return The first element to pass the predicate test if there is one, nothing
	 *         otherwise.
	 */
	default Optional<E> findFirst(Predicate<? super E> predicate)
	{
		return iter().filter(predicate).nextOption();
	}

	/**
	 * Finds the minimum element in the iterator created by {@link #iter()}
	 * according to the given comparator.
	 * 
	 * @param orderingFunction the complete ordering of this element type.
	 * @return see {@link EnhancedIterator#minOption(Comparator)}.
	 */
	default Optional<E> minOption(Comparator<? super E> orderingFunction)
	{
		return iter().minOption(orderingFunction);
	}

	/**
	 * Finds the maximum element in the iterator created from this iterable by
	 * {@link #iter()} according to the given comparator.
	 * 
	 * @param orderingFunction the complete ordering of this element type.
	 * @return see {@link EnhancedIterator#maxOption(Comparator)}.
	 */
	default Optional<E> maxOption(Comparator<? super E> orderingFunction)
	{
		return iter().maxOption(orderingFunction);
	}

	/**
	 * Calculates whether all elements in the iterator created by {@link #iter()}
	 * are the same according to {@link #equals(Object)}.
	 * 
	 * @return True if all elements are equal, false otherwise.
	 */
	default boolean areAllEqual()
	{
		return iter().areAllEqual();
	}

	/**
	 * Calculates whether every element in the iterator created by {@link #iter()}
	 * passes the supplied predicate.
	 * 
	 * @param condition The predicate test.
	 * @return true if all elements pass the test or the iterator is empty, false
	 *         otherwise.
	 */
	default boolean allMatch(Predicate<? super E> condition)
	{
		return iter().allMatch(condition);
	}

	/**
	 * Spawns an enhanced iterator and delegates to its
	 * {@link EnhancedIterator#anyMatch(Predicate)} method.
	 */
	/**
	 * Calculates at least one element in the iterator created by {@link #iter()}
	 * passes the supplied predicate.
	 * 
	 * @param condition The predicate test.
	 * @return true if at least one element passes the test, false otherwise.
	 */
	default boolean anyMatch(Predicate<? super E> condition)
	{
		return iter().anyMatch(condition);
	}

	/**
	 * Calculates whether all elements in the iterator created by {@link #iter()}
	 * fail the supplied predicate.
	 * 
	 * @param condition The predicate test.
	 * @return true if all elements fail the test or the iterator is empty, false
	 *         otherwise.
	 */
	default boolean noneMatch(Predicate<? super E> condition)
	{
		return iter().noneMatch(condition);
	}

	/**
	 * Groups the elements of the iterator created by {@link #iter()} according to
	 * some classification function.
	 * 
	 * @param classifier The classification function.
	 * @return see {@link EnhancedIterator#groupBy(Function)}
	 */
	default <K> Map<K, List<E>> groupBy(Function<? super E, K> classifier)
	{
		return iter().groupBy(classifier);
	}

	/**
	 * Folds the elements of the iterator created by {@link #iter()} into one value
	 * using a provided reduction operator and a starting value.
	 * 
	 * @param         <R> The type of the fold result.
	 * @param id      The initial value of the fold operation.
	 * @param reducer The reduction operator governing how two values are combined
	 *                into one.
	 * 
	 * @return see {@link EnhancedIterator#fold(Object, BiFunction)}
	 */
	default <R> R fold(R id, BiFunction<R, E, R> reducer)
	{
		return iter().fold(id, reducer);
	}

	/**
	 * Folds the elements of the iterator created by {@link #iter()} into one value
	 * using a given reduction function making an allowance for a potentially empty
	 * source.
	 * 
	 * @param reducer The reduction operator governing how two values are combined
	 *                into one.
	 * @return see {@link EnhancedIterator#foldOption(BinaryOperator)}
	 */
	default Optional<E> foldOption(BinaryOperator<E> reducer)
	{
		return iter().foldOption(reducer);
	}

	/**
	 * Folds the elements of the iterator created by {@link #iter()} into one value
	 * using a given reduction function throwing an exception if the source is
	 * empty.
	 * 
	 * @param reducer The reduction operator governing how two values are combined
	 *                into one.
	 * @return see {@link EnhancedIterator#fold(BinaryOperator)}
	 */
	default E fold(BinaryOperator<E> reducer)
	{
		return iter().fold(reducer);
	}

	/**
	 * Spawns an enhanced iterator and delegates to it's
	 * {@link EnhancedIterator#toMap(Function, Function)} method.
	 */
	default <K, V> Map<K, V> toMap(Function<? super E, ? extends K> keyMap,
			Function<? super E, ? extends V> valueMap)
	{
		return iter().toMap(keyMap, valueMap);
	}

	/**
	 * Spawns an enhanced iterator and delegates to it's
	 * {@link EnhancedIterator#associate(Function)} method.
	 */
	default <V> Map<E, V> associate(Function<? super E, ? extends V> valueMap)
	{
		return iter().associate(valueMap);
	}

	/**
	 * Spawns an enhanced iterator and delegates to it's
	 * {@link EnhancedIterator#toList()} method.
	 */
	default List<E> toList()
	{
		return toCollection(ArrayList::new);
	}

	/**
	 * Spawns an enhanced iterator and delegates to it's
	 * {@link EnhancedIterator#toSet()} method.
	 */
	default Set<E> toSet()
	{
		return toCollection(HashSet::new);
	}

	/**
	 * Spawns an enhanced iterator and delegates to it's
	 * {@link EnhancedIterator#toCollection(Supplier)} method.
	 */
	default <C extends Collection<E>> C toCollection(Supplier<C> collectionFactory)
	{
		return iter().toCollection(collectionFactory);
	}
}
