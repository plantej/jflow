package xawd.jflow.abstractflows.skiptests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import xawd.jflow.AbstractDoubleFlow;
import xawd.jflow.abstractiterables.AbstractIterableDoubles;
import xawd.jflow.testutilities.IteratorExampleProvider;
import xawd.jflow.testutilities.IteratorTest;

/**
 * @author t
 */
public class AbstractDoubleFlowSkipTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final double[][] expectedOutcomesForDifferentIndexArguments = {
				{0, 1, 2, 3, 4},
				{1, 2, 3, 4},
				{2, 3, 4},
				{3, 4},
				{4},
				{},
		};

		final int nArgs = expectedOutcomesForDifferentIndexArguments.length;

		final AbstractIterableDoubles populated = getPopulatedDoubleTestIteratorProvider();
		final AbstractIterableDoubles empty = getEmptyDoubleTestIteratorProvider();

		IntStream.range(0, nArgs).forEach(i ->
		{
			assertDoubleIteratorAsExpected(expectedOutcomesForDifferentIndexArguments[i], createSkipIteratorProviderFrom(populated, i));
			assertDoubleIteratorAsExpected(new double[0], createSkipIteratorProviderFrom(empty, i));
		});

		IntStream.range(Constants.NEGATIVE_LOWER_BOUND, 0).forEach(i ->
		{
			assertThrows(IllegalArgumentException.class, () -> populated.iter().skip(i));
			assertThrows(IllegalArgumentException.class, () -> empty.iter().skip(i));
		});

		IntStream.range(nArgs, Constants.POSITIVE_UPPER_BOUND).forEach(i ->
		{
			assertDoubleIteratorAsExpected(new double[0], createSkipIteratorProviderFrom(populated, i));
			assertDoubleIteratorAsExpected(new double[0], createSkipIteratorProviderFrom(empty, i));
		});
	}

	private AbstractIterableDoubles createSkipIteratorProviderFrom(AbstractIterableDoubles src, int skipCount)
	{
		return new AbstractIterableDoubles() {
			@Override
			public AbstractDoubleFlow iter() {
				return src.iter().skip(skipCount);
			}
		};
	}
}