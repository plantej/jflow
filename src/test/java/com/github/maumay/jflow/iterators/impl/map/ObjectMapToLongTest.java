/**
 * 
 */
package com.github.maumay.jflow.iterators.impl.map;

import java.util.List;

import com.github.maumay.jflow.impl.AbstractLongIterator;
import com.github.maumay.jflow.testutilities.AbstractAdapterTest;

/**
 * @author thomasb
 *
 */
public final class ObjectMapToLongTest extends AbstractAdapterTest<String, AbstractLongIterator>
{
	@Override
	protected List<Case<String, AbstractLongIterator>> getTestCases()
	{
		Adapter<String, AbstractLongIterator> adapter = iter -> iter.mapToLong(Long::parseLong);
		return list(new Case<>(list(), adapter, list()),
				new Case<>(list("0", "1"), adapter, list(0L, 1L)));
	}
}
