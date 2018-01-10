/**
 * Copyright � 2018 Lhasa Limited
 * File created: 10 Jan 2018 by ThomasB
 * Creator : ThomasB
 * Version : $Id$
 */
package io.xyz.common.function;

import io.xyz.common.rangedescriptor.DoubleRangeDescriptor;

/**
 * @author ThomasB
 * @since 10 Jan 2018
 */
public interface DoubleCompressor
{
	double compress(DoubleRangeDescriptor xs);
}

/* ---------------------------------------------------------------------*
 * This software is the confidential and proprietary
 * information of Lhasa Limited
 * Granary Wharf House, 2 Canal Wharf, Leeds, LS11 5PS
 * ---
 * No part of this confidential information shall be disclosed
 * and it shall be used only in accordance with the terms of a
 * written license agreement entered into by holder of the information
 * with LHASA Ltd.
 * --------------------------------------------------------------------- */