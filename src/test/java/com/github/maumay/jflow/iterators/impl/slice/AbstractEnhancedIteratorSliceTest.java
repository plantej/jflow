/**
 *
 */
package com.github.maumay.jflow.iterators.impl.slice;

import static java.util.Arrays.asList;

import java.util.function.IntUnaryOperator;

import org.junit.jupiter.api.Test;

import com.github.maumay.jflow.iterators.impl2.AbstractEnhancedIterator;
import com.github.maumay.jflow.testutilities.AbstractEnhancedIterable;
import com.github.maumay.jflow.testutilities.IteratorExampleProvider;
import com.github.maumay.jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class AbstractEnhancedIteratorSliceTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		AbstractEnhancedIterable<String> populated = getObjectTestIteratorProvider();
		AbstractEnhancedIterable<String> empty = getEmptyObjectTestIteratorProvider();

		IntUnaryOperator allSlicedOperator = i -> i;
		assertObjectIteratorAsExpected(asList("0", "1", "2", "3", "4"),
				createSlicedIteratorProviderFrom(populated, allSlicedOperator));
		assertObjectIteratorAsExpected(asList(),
				createSlicedIteratorProviderFrom(empty, allSlicedOperator));

		IntUnaryOperator someSlicedOperator = i -> 2 * i;
		assertObjectIteratorAsExpected(asList("0", "2", "4"),
				createSlicedIteratorProviderFrom(populated, someSlicedOperator));
		assertObjectIteratorAsExpected(asList(),
				createSlicedIteratorProviderFrom(empty, someSlicedOperator));

		IntUnaryOperator noneSlicedOperator = i -> i + 5;
		assertObjectIteratorAsExpected(asList(),
				createSlicedIteratorProviderFrom(populated, noneSlicedOperator));
		assertObjectIteratorAsExpected(asList(),
				createSlicedIteratorProviderFrom(empty, noneSlicedOperator));
	}

	private <E> AbstractEnhancedIterable<E> createSlicedIteratorProviderFrom(
			AbstractEnhancedIterable<E> source, IntUnaryOperator slicemap)
	{
		return new AbstractEnhancedIterable<E>() {
			@Override
			public AbstractEnhancedIterator<E> iter()
			{
				return source.iter().slice(slicemap);
			}
		};
	}
}
