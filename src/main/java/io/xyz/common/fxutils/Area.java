/**
 * 
 */
package io.xyz.common.fxutils;

import static java.lang.Math.sqrt;

import io.xyz.common.geometry.Point;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * @author t
 *
 */
public final class Area 
{
	/**
	 * @param centre of circle C
	 * @param radius of circle C
	 * @return the bounds of the circumscribed square of C
	 */
	public static Bounds ofCircumscribedSquare(Point centre, double radius)
	{
		double sideLen = 2*radius;
		return new BoundingBox(
				centre.x - radius, centre.y - radius, sideLen, sideLen
				);
	}
	
	public static Bounds ofInscribedSquare(Point centre, double radius)
	{
		double sideLen = sqrt(2)*radius, halfSideLen = 0.5*sideLen;
		return new BoundingBox(
				centre.x - halfSideLen, centre.y - halfSideLen, sideLen, sideLen
				);
	}
}