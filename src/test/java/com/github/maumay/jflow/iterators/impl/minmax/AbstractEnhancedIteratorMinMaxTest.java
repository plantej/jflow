/**
 *
 */
package com.github.maumay.jflow.iterators.impl.minmax;

import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.maumay.jflow.testutilities.AbstractEnhancedIterable;
import com.github.maumay.jflow.testutilities.IteratorExampleProvider;

/**
 * @author ThomasB
 *
 */
class AbstractEnhancedIteratorMinMaxTest extends IteratorExampleProvider
{
	@ParameterizedTest
	@MethodSource("minByKeyTestDataProvider")
	void testMinByKey(final Comparator<String> key, final String expectedPopulatedResult)
	{
		final AbstractEnhancedIterable<String> populated = getObjectTestIteratorProvider();
		final Optional<String> result = populated.iterator().minOption(key);
		assertTrue(result.isPresent());
		assertEquals(expectedPopulatedResult, result.get());

		final AbstractEnhancedIterable<String> empty = getEmptyObjectTestIteratorProvider();
		assertFalse(empty.iterator().minOption(key).isPresent());
	}

	static Stream<Arguments> minByKeyTestDataProvider()
	{
		return Stream.of(
				Arguments.of(Comparator.comparingDouble((String s) -> -parseDouble(s)),
						"4"),
				Arguments.of(Comparator
						.comparingDouble((String s) -> Double.POSITIVE_INFINITY), "0"),
				Arguments.of(Comparator
						.comparingDouble((String s) -> Double.NEGATIVE_INFINITY), "0"));
	}

	@ParameterizedTest
	@MethodSource("maxByKeyTestDataProvider")
	void testMax(final Comparator<String> key, final String expectedPopulatedResult)
	{
		final AbstractEnhancedIterable<String> populated = getObjectTestIteratorProvider();
		final Optional<String> result = populated.iterator().maxOption(key);
		assertTrue(result.isPresent());
		assertEquals(expectedPopulatedResult, result.get());

		final AbstractEnhancedIterable<String> empty = getEmptyObjectTestIteratorProvider();
		assertFalse(empty.iterator().maxOption(key).isPresent());
	}

	static Stream<Arguments> maxByKeyTestDataProvider()
	{
		return Stream.of(
				Arguments.of(Comparator.comparingDouble((String s) -> -parseDouble(s)),
						"0"),
				Arguments.of(Comparator
						.comparingDouble((String s) -> Double.POSITIVE_INFINITY), "0"),
				Arguments.of(Comparator
						.comparingDouble((String s) -> Double.NEGATIVE_INFINITY), "0"));
	}
}