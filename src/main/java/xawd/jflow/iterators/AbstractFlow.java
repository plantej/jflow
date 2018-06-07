/**
 *
 */
package xawd.jflow.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PrimitiveIterator.OfDouble;
import java.util.PrimitiveIterator.OfInt;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import xawd.jflow.iterators.construction.Numbers;
import xawd.jflow.iterators.impl.AccumulationFlow;
import xawd.jflow.iterators.impl.AppendFlow;
import xawd.jflow.iterators.impl.CombinedFlow;
import xawd.jflow.iterators.impl.FilteredFlow;
import xawd.jflow.iterators.impl.FlattenedFlow;
import xawd.jflow.iterators.impl.InsertFlow;
import xawd.jflow.iterators.impl.MapFlow;
import xawd.jflow.iterators.impl.MapToDoubleFlow;
import xawd.jflow.iterators.impl.MapToIntFlow;
import xawd.jflow.iterators.impl.MapToLongFlow;
import xawd.jflow.iterators.impl.PairFoldFlow;
import xawd.jflow.iterators.impl.SkipFlow;
import xawd.jflow.iterators.impl.SkipwhileFlow;
import xawd.jflow.iterators.impl.SlicedFlow;
import xawd.jflow.iterators.impl.TakeFlow;
import xawd.jflow.iterators.impl.TakewhileFlow;
import xawd.jflow.iterators.impl.ZipFlow;
import xawd.jflow.iterators.impl.consumption.ObjectCollectionConsumption;
import xawd.jflow.iterators.impl.consumption.ObjectMinMaxConsumption;
import xawd.jflow.iterators.impl.consumption.ObjectPredicateConsumption;
import xawd.jflow.iterators.impl.consumption.ObjectReductionConsumption;
import xawd.jflow.iterators.misc.DoubleWith;
import xawd.jflow.iterators.misc.IntWith;
import xawd.jflow.iterators.misc.LongWith;
import xawd.jflow.iterators.misc.Pair;
import xawd.jflow.iterators.misc.PredicatePartition;

/**
 * @author t
 */
public abstract class AbstractFlow<E> implements Flow<E>
{
	@Override
	public <R> AbstractFlow<R> map(final Function<? super E, R> f)
	{
		return new MapFlow.OfObject<>(this, f);
	}

	@Override
	public AbstractIntFlow mapToInt(final ToIntFunction<? super E> f)
	{
		return new MapToIntFlow.FromObject<>(this, f);
	}

	@Override
	public AbstractDoubleFlow mapToDouble(final ToDoubleFunction<? super E> f)
	{
		return new MapToDoubleFlow.FromObject<>(this, f);
	}

	@Override
	public AbstractLongFlow mapToLong(final ToLongFunction<? super E> f)
	{
		return new MapToLongFlow.FromObject<>(this, f);
	}

	@Override
	public <R> AbstractFlow<R> flatten(final Function<? super E, ? extends Flow<R>> mapping)
	{
		return new FlattenedFlow.OfObject<>(this, mapping);
	}

	@Override
	public AbstractIntFlow flattenToInts(final Function<? super E, ? extends IntFlow> mapping)
	{
		return new FlattenedFlow.OfInt<>(this, mapping);
	}

	@Override
	public AbstractLongFlow flattenToLongs(final Function<? super E, ? extends LongFlow> mapping)
	{
		return new FlattenedFlow.OfLong<>(this, mapping);
	}

	@Override
	public AbstractDoubleFlow flattenToDoubles(final Function<? super E, ? extends DoubleFlow> mapping)
	{
		return new FlattenedFlow.OfDouble<>(this, mapping);
	}

	@Override
	public <R> AbstractFlow<Pair<E, R>> zipWith(final Iterator<? extends R> other)
	{
		return new ZipFlow.OfObjects<>(this, other);
	}

	@Override
	public AbstractFlow<IntWith<E>> zipWith(final OfInt other)
	{
		return new ZipFlow.OfObjectAndInt<>(this, other);
	}

	@Override
	public AbstractFlow<DoubleWith<E>> zipWith(final OfDouble other)
	{
		return new ZipFlow.OfObjectAndDouble<>(this, other);
	}

	@Override
	public AbstractFlow<LongWith<E>> zipWith(final OfLong other)
	{
		return new ZipFlow.OfObjectAndLong<>(this, other);
	}

	@Override
	public <E2, R> AbstractFlow<R> combineWith(final Iterator<? extends E2> other, final BiFunction<? super E, ? super E2, R> f)
	{
		return new CombinedFlow.OfObjects<>(this, other, f);
	}

	@Override
	public AbstractFlow<IntWith<E>> enumerate()
	{
		return zipWith(Numbers.natural());
	}

	@Override
	public AbstractFlow<E> slice(final IntUnaryOperator f)
	{
		return new SlicedFlow.OfObject<>(this, f);
	}

	@Override
	public AbstractFlow<E> take(final int n)
	{
		return new TakeFlow.OfObject<>(this, n);
	}

	@Override
	public AbstractFlow<E> takeWhile(final Predicate<? super E> predicate)
	{
		return new TakewhileFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> drop(final int n)
	{
		return new SkipFlow.OfObject<>(this, n);
	}

	@Override
	public AbstractFlow<E> dropWhile(final Predicate<? super E> predicate)
	{
		return new SkipwhileFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> filter(final Predicate<? super E> predicate)
	{
		return new FilteredFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> append(final Iterator<? extends E> other)
	{
		return new AppendFlow.OfObject<>(this, other);
	}

	@Override
	public AbstractFlow<E> insert(final Iterator<? extends E> other)
	{
		return new InsertFlow.OfObject<>(this, other);
	}

	@Override
	public AbstractFlow<E> accumulate(final BinaryOperator<E> accumulator)
	{
		return new AccumulationFlow.OfObject<>(this, accumulator);
	}

	@Override
	public <R> AbstractFlow<R> accumulate(final R id, final BiFunction<R, E, R> accumulator)
	{
		return new AccumulationFlow.OfObjectWithMixedTypes<>(this, id, accumulator);
	}

	@Override
	public <R> AbstractFlow<R> pairFold(final BiFunction<? super E, ? super E, R> foldFunction)
	{
		return new PairFoldFlow.OfObject<>(this, foldFunction);
	}

	@Override
	public Optional<E> minByKey(final ToDoubleFunction<? super E> key)
	{
		return ObjectMinMaxConsumption.findMin(this, key);
	}

	@Override
	public <C extends Comparable<C>> Optional<E> minByObjectKey(final Function<? super E, C> key)
	{
		return ObjectMinMaxConsumption.findMin(this, key);
	}

	@Override
	public Optional<E> maxByKey(final ToDoubleFunction<? super E> key)
	{
		return ObjectMinMaxConsumption.findMax(this, key);
	}

	@Override
	public <C extends Comparable<C>> Optional<E> maxByObjectKey(final Function<? super E, C> key)
	{
		return ObjectMinMaxConsumption.findMax(this, key);
	}

	@Override
	public boolean areAllEqual()
	{
		return ObjectPredicateConsumption.allEqual(this);
	}

	@Override
	public boolean allMatch(final Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.allMatch(this, predicate);
	}

	@Override
	public boolean anyMatch(final Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.anyMatch(this, predicate);
	}

	@Override
	public boolean noneMatch(final Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.noneMatch(this, predicate);
	}

	@Override
	public PredicatePartition<E> partition(final Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.partition(this, predicate);
	}

	@Override
	public long count()
	{
		return ObjectReductionConsumption.count(this);
	}

	@Override
	public <R> R reduce(final R id, final BiFunction<R, E, R> reducer)
	{
		return ObjectReductionConsumption.reduce(this, id, reducer);
	}

	@Override
	public Optional<E> reduce(final BinaryOperator<E> reducer)
	{
		return ObjectReductionConsumption.reduce(this, reducer);
	}

	@Override
	public <C extends Collection<E>> C toCollection(final Supplier<C> collectionFactory)
	{
		return ObjectCollectionConsumption.toCollection(this, collectionFactory);
	}

	@Override
	public <K, V> Map<K, V> toMap(final Function<? super E, K> keyMapper, final Function<? super E, V> valueMapper)
	{
		return ObjectCollectionConsumption.toMap(this, keyMapper, valueMapper);
	}

	@Override
	public <K> Map<K, List<E>> groupBy(final Function<? super E, K> classifier)
	{
		return ObjectCollectionConsumption.groupBy(this, classifier);
	}
}