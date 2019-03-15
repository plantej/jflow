/**
 *
 */
package com.github.maumay.jflow.iterators.factories;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import com.github.maumay.jflow.impl.ArraySource;
import com.github.maumay.jflow.impl.CollectionSource;
import com.github.maumay.jflow.impl.EmptyIterator;
import com.github.maumay.jflow.impl.FunctionSource;
import com.github.maumay.jflow.impl.IteratorWrapper;
import com.github.maumay.jflow.iterables.EnhancedIterable;
import com.github.maumay.jflow.iterators.DoubleIterator;
import com.github.maumay.jflow.iterators.EnhancedIterator;
import com.github.maumay.jflow.iterators.IntIterator;
import com.github.maumay.jflow.iterators.LongIterator;
import com.github.maumay.jflow.utils.Exceptions;
import com.github.maumay.jflow.utils.Tup;

/**
 * Static methods for building finite EnhancedIterator instances.
 *
 * @author ThomasB
 */
public final class Iter
{
	private Iter()
	{
	}

	/**
	 * Creates an empty EnhancedIterator of an inferred type.
	 *
	 * @param <E> The element type of the EnhancedIterator (it will be inferred from
	 *        the context of the method call).
	 * @return An empty EnhancedIterator.
	 */
	public static <E> EnhancedIterator<E> empty()
	{
		return new EmptyIterator.OfObject<>();
	}

	/**
	 * Construct a EnhancedIterator which wraps an iterator provided from an
	 * existing Collection.
	 *
	 * @param     <E> The upper bound on the source Collection element type.
	 * @param src Some Collection of elements.
	 * @return A EnhancedIterator instance wrapping an iterator constructed from the
	 *         source sequence.
	 */
	public static <E> EnhancedIterator<E> over(Collection<? extends E> src)
	{
		return new CollectionSource<>(src);
	}

	/**
	 * Construct a EnhancedIterator iterating over varargs elements.
	 *
	 * @param          <E> The least upper bound on the types of the passed
	 *                 elements.
	 * @param elements The elements to be iterated over.
	 * @return A EnhancedIterator iterating over the given elements.
	 */
	@SafeVarargs
	public static <E> EnhancedIterator<E> over(E... elements)
	{
		return new ArraySource.OfObject<>(elements);
	}

	/**
	 * Construct an Iterator traversing over all values in the given enumeration.
	 * 
	 * @param           <E> The enum type to traverse the values of.
	 * @param enumclass The class of the enum type to traverse the values of.
	 * @return An enhanced iterator traversing over all values of the given enum
	 *         type.
	 */
	public static <E extends Enum<E>> EnhancedIterator<E> over(Class<E> enumclass)
	{
		return Iter.over(enumclass.getEnumConstants());
	}

	// /**
	// * Build a EnhancedIterator reverse iterating over a List.
	// *
	// * @param <E> The upper type bound on the elements in the source.
	// * @param source The source List
	// * @return A EnhancedIterator reversing over the source starting with the last
	// * element.
	// */
	// public static <E> EnhancedIterator<E> reverse(List<? extends E> source)
	// {
	// return new ReversedSourceIterator.OfObject<>(source);
	// }

	/**
	 * Construct a EnhancedIterator reverse iterating over varargs elements.
	 *
	 * @param          <E> The least upper bound on the types of the passed
	 *                 elements.
	 * @param elements The elements to be reversed over.
	 * @return A EnhancedIterator reversing over the given elements starting with
	 *         the last element.
	 */
	@SafeVarargs
	public static <E> EnhancedIterator<E> reverse(E... elements)
	{
		return new ArraySource.OfObjectReversed<>(elements);
	}

	/**
	 * Build a finite length EnhancedIterator from a function which accepts a
	 * positive integer argument representing a sequence index.
	 *
	 * @param                  <E> The target type of the indexing function.
	 * @param indexingFunction A function whose domain is the natural numbers.
	 * @param indexBound       The upper bound (exclusive) of the index range. It
	 *                         must be non-negative otherwise an exception will be
	 *                         thrown.
	 * @return A EnhancedIterator built from apply the indexing function to a
	 *         bounded range of natural numbers.
	 */
	public static <E> EnhancedIterator<E> byIndexing(IntFunction<E> indexingFunction,
			int indexBound)
	{
		Exceptions.requireArg(indexBound >= 0);
		return new FunctionSource.OfObject<>(indexingFunction, indexBound);
	}

	/**
	 * Converts an Optional into an Iterator, if the input is present then returns a
	 * single element Iterator containing that element else an empty Iterator is
	 * returned.
	 * 
	 * @param     <E> The type of element wrapped by the optional.
	 * @param src the optional value to convert into an iterator.
	 * @return a single element iterator wrapping the wrapped element if it exists,
	 *         an empty iterator otherwise.
	 */
	public static <E> EnhancedIterator<E> option(Optional<? extends E> src)
	{
		return src.isPresent() ? Iter.over(src.get()) : Iter.empty();
	}

	/**
	 * Creates an enhanced iterator traversing the values of some {@link Map}
	 * 
	 * @param     <V> The type of the values in the given Map.
	 * @param src the map encapsulating the values to traverse.
	 * @return an iterator traversing the input values.
	 */
	public static <V> EnhancedIterator<V> values(Map<?, ? extends V> src)
	{
		return Iter.over(src.values());
	}

	/**
	 * Creates an enhanced iterator traversing the keys of some {@link Map}
	 * 
	 * @param     <K> The type of the keys in the given Map.
	 * @param src the map encapsulating the keys to traverse.
	 * @return an iterator traversing the input keys.
	 */
	public static <K> EnhancedIterator<K> keys(Map<? extends K, ?> src)
	{
		return Iter.over(src.keySet());
	}

	/**
	 * Creates an enhanced iterator traversing the entry pairs of some {@link Map}
	 * 
	 * @param     <K> The type of the keys in the source map.
	 * @param     <V> the type of the values in the source map.
	 * @param src the map encapsulating the entries to traverse.
	 * @return an iterator traversing the key, value pairs of the map.
	 */
	public static <K, V> EnhancedIterator<Tup<K, V>> entries(Map<K, V> src)
	{
		return Iter.over(src.entrySet()).map(entry -> Tup.of(entry.getKey(), entry.getValue()));
	}

	/**
	 * Flattens a sequence of stacked sequences by returning an iterator traversing
	 * all elements of every sequence.
	 * 
	 * @param source A sequence of sequences.
	 * @return An iterator traversing all elements contained in each element in the
	 *         source
	 */
	public static <E> EnhancedIterator<E> flatten(Iterable<? extends Iterable<? extends E>> source)
	{
		return Iter.wrap(source.iterator()).flatMap(Iterable::iterator);
	}

	// Ints
	/**
	 * Creates an empty IntEnhancedIterator.
	 *
	 * @return An empty IntEnhancedIterator.
	 */
	public static IntIterator emptyInts()
	{
		return EmptyIterator.ofInt();
	}

	/**
	 * Construct an IntEnhancedIterator iterating over varargs primitive integers.
	 *
	 * @param integers The integers to be iterated over.
	 * @return An IntEnhancedIterator iterating over the given integers.
	 */
	public static IntIterator ints(int... integers)
	{
		return new ArraySource.OfInt(integers);
	}

	/**
	 * Construct a IntEnhancedIterator reverse iterating over varargs elements.
	 *
	 * @param elements The elements to be reversed over.
	 * @return A IntEnhancedIterator reversing over the given elements starting with
	 *         the last element.
	 */
	public static IntIterator reverseInts(int... elements)
	{
		return new ArraySource.OfIntReversed(elements);
	}

	/**
	 * Build a finite length IntEnhancedIterator from a function which accepts a
	 * positive integer argument representing a sequence index.
	 *
	 * @param indexingFunction A function whose domain is the natural numbers.
	 * @param indexBound       The upper bound (exclusive) of the index range. It
	 *                         must be non-negative otherwise an exception will be
	 *                         thrown.
	 * @return A IntEnhancedIterator built from apply the indexing function to a
	 *         bounded range of natural numbers.
	 */
	public static IntIterator intsByIndexing(IntUnaryOperator indexingFunction, int indexBound)
	{
		Exceptions.requireArg(indexBound >= 0);
		return new FunctionSource.OfInt(indexingFunction, indexBound);
	}

	// Doubles

	/**
	 * Creates an empty DoubleEnhancedIterator.
	 *
	 * @return An empty DoubleEnhancedIterator.
	 */
	public static DoubleIterator emptyDoubles()
	{
		return EmptyIterator.ofDouble();
	}

	/**
	 * Construct an DoubleEnhancedIterator iterating over varargs primitive doubles.
	 *
	 * @param doubles The doubles to be iterated over.
	 * @return An DoubleEnhancedIterator iterating over the given doubles.
	 */
	public static DoubleIterator doubles(double... doubles)
	{
		return new ArraySource.OfDouble(doubles);
	}

	/**
	 * Construct a DoubleEnhancedIterator reverse iterating over varargs elements.
	 *
	 * @param elements The elements to be reversed over.
	 * @return A DoubleEnhancedIterator reversing over the given elements starting
	 *         with the last element.
	 */
	public static DoubleIterator reverseDoubles(double... elements)
	{
		return new ArraySource.OfDoubleReversed(elements);
	}

	/**
	 * Build a finite length DoubleEnhancedIterator from a function which accepts a
	 * positive integer argument representing a sequence index.
	 *
	 * @param indexingFunction A function whose domain is the natural numbers.
	 * @param indexBound       The upper bound (exclusive) of the index range. It
	 *                         must be non-negative otherwise an exception will be
	 *                         thrown.
	 * @return A DoubleEnhancedIterator built from apply the indexing function to a
	 *         bounded range
	 */
	public static DoubleIterator doublesByIndexing(IntToDoubleFunction indexingFunction,
			int indexBound)
	{
		Exceptions.requireArg(indexBound >= 0);
		return new FunctionSource.OfDouble(indexingFunction, indexBound);
	}

	// Longs

	/**
	 * Creates an empty LongEnhancedIterator.
	 *
	 * @return An empty LongEnhancedIterator.
	 */
	public static LongIterator emptyLongs()
	{
		return EmptyIterator.ofLong();
	}

	/**
	 * Construct an LongEnhancedIterator iterating over varargs primitive longs.
	 *
	 * @param longs The longs to be iterated over.
	 * @return An LongEnhancedIterator iterating over the given longs.
	 */
	public static LongIterator longs(long... longs)
	{
		return new ArraySource.OfLong(longs);
	}

	/**
	 * Construct a LongEnhancedIterator reverse iterating over varargs elements.
	 *
	 * @param elements The elements to be reversed over.
	 * @return A LongEnhancedIterator reversing over the given elements starting
	 *         with the last element.
	 */
	public static LongIterator reverseLongs(long... elements)
	{
		return new ArraySource.OfLongReversed(elements);
	}

	/**
	 * Build a finite length LongEnhancedIterator from a function which accepts a
	 * positive integer argument representing a sequence index.
	 *
	 * @param indexingFunction A function whose domain is the natural numbers.
	 * @param indexBound       The upper bound (exclusive) of the index range. It
	 *                         must be non-negative otherwise an exception will be
	 *                         thrown.
	 * @return A LongEnhancedIterator built from apply the indexing function to a
	 *         bounded range of natural numbers.
	 */
	public static LongIterator longsByIndexing(IntToLongFunction indexingFunction, int indexBound)
	{
		Exceptions.requireArg(indexBound >= 0);
		return new FunctionSource.OfLong(indexingFunction, indexBound);
	}

	/**
	 * Wraps an existing iterator in a EnhancedIterator to enable use of all extra
	 * functionality.
	 *
	 * @param     <E> The type of elements traversed by the source iterator.
	 * @param src The iterator to wrap.
	 * @return A flow wrapping the provided iterator.
	 */
	public static <E> EnhancedIterator<E> wrap(Iterator<? extends E> src)
	{
		return new IteratorWrapper.OfObject<>(src);
	}

	/**
	 * Construct an enhanced iterable which produces wrapped iterators sourced from
	 * an existing iterable object.
	 *
	 * @param     <E> The upper bound on the source iterable element type.
	 * @param src An object which can construct an iterator over it's elements.
	 * @return A enhanced iterable instance producing wrapped iterators sourced from
	 *         the input iterable.
	 */
	public static <E> EnhancedIterable<E> wrap(Iterable<? extends E> src)
	{
		return () -> wrap(src.iterator());
	}

	/**
	 * Builds an integer range between 0 and some provided upper bound.
	 *
	 * @param upperBound The upper bound (exclusive) on the iteration interval.
	 * @return Let n be the upper bound. If n is non-positive we return an empty
	 *         iteration otherwise we return an ordered iteration over the integers
	 *         contained in the half open interval:
	 *
	 *         <pre>
	 * 				[0,n)
	 *         </pre>
	 */
	public static IntIterator until(int upperBound)
	{
		return upperBound > 0 ? Iter.intsByIndexing(i -> i, upperBound) : Iter.emptyInts();
	}

	/**
	 * Builds an integer range between provided lower and upper bounds.
	 *
	 * @param low  The lower bound (inclusive) on the iteration interval.
	 *
	 * @param high The upper bound (exclusive) on the iteration interval.
	 * @return Let m be the lower bound and n be the upper bound. If (n - m) is
	 *         non-positive we return an empty iteration otherwise we return an
	 *         ordered iteration over the integers contained in the half open
	 *         interval:
	 *
	 *         <pre>
	 * 				[m,n)
	 *         </pre>
	 */
	public static IntIterator between(int low, int high)
	{
		return high > low ? Iter.intsByIndexing(i -> i + low, high - low) : Iter.emptyInts();
	}

	/**
	 * Builds an integer range between provided lower and upper bounds with a given
	 * step size.
	 *
	 * @param start The start bound (inclusive) on the iteration interval.
	 *
	 * @param end   The end bound (exclusive) on the iteration interval.
	 *
	 * @param step  The difference between consecutive integers in the iteration.
	 * @return Let m be the start bound and n be the end bound. If sign(n - m) is
	 *         not equal to sign(step) we return an empty iteration otherwise we
	 *         return an ordered iteration starting at m over the integers contained
	 *         in the half open interval [m, n) whose consecutive difference is
	 *         equal to the step size.
	 */
	public static IntIterator between(int start, int end, int step)
	{
		int length = end - start;
		int elementCount = (int) Math.ceil(abs((double) length / step));
		return signum(step) == signum(length)
				? Iter.intsByIndexing(i -> start + i * step, elementCount)
				: Iter.emptyInts();
	}

	/**
	 * Builds an iterator traversing over the boundary points of a partitioned
	 * interval.
	 *
	 * @param start         The start of the interval to be partitioned.
	 * @param end           The end of the interval to be partitioned.
	 * @param nSubIntervals The number of equal length subintervals to partition the
	 *                      interval into.
	 * @return Let m be the required number of subintervals and J the be interval.
	 *         Then partition J into m non-overlapping same length sub intervals say
	 *         {J_m}. Take the unique boundary points of all intervals in the family
	 *         {J_m} and return an iteration over them ordered on proximity to the
	 *         'start' of the interval J.
	 */
	public static DoubleIterator partition(double start, double end, int nSubIntervals)
	{
		if (nSubIntervals < 1) {
			throw new IllegalArgumentException();
		}
		double step = (end - start) / nSubIntervals;
		return until(nSubIntervals + 1).mapToDouble(i -> start + i * step);
	}
}
