/**
 * 
 */
package com.github.maumay.jflow.impl;

import java.util.OptionalInt;

import com.github.maumay.jflow.utils.Option;

/**
 * @author thomasb
 *
 */
public class UnknownSize extends AbstractIteratorSize
{
	private static final UnknownSize INSTANCE = new UnknownSize();

	private UnknownSize()
	{
		super(SizeType.UNKNOWN);
	}

	public static UnknownSize instance()
	{
		return INSTANCE;
	}

	@Override
	public void decrement()
	{
		// Nothing to do.
	}

	@Override
	public OptionalInt getUpperBound()
	{
		return Option.emptyInt();
	}

	@Override
	public OptionalInt getLowerBound()
	{
		return Option.emptyInt();
	}

	@Override
	public OptionalInt getExactSize()
	{
		return Option.emptyInt();
	}

	@Override
	public UnknownSize copy()
	{
		// Don't need to copy here since it is a singleton.
		return this;
	}
}
