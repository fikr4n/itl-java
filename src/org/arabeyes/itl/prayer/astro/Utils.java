package org.arabeyes.itl.prayer.astro;

public class Utils {
	
	public static final double INVALID_TRIGGER = -.999;
	
	public static final double PI = 3.1415926535898;
	
	public static final double DEG_TO_10_BASE = 1 / 15.0;
	
	public static final double CENTER_OF_SUN_ANGLE = -0.833370;
	
	public static final double ALTITUDE_REFRACTION = 0.0347;
	
	public static final double REF_LIMIT = 9999999;
	
	public static final double KAABA_LAT = 21.423333;
	
	public static final double KAABA_LONG = 39.823333;
	
	public static final double DEF_NEAREST_LATITUDE = 48.5;
	
	public static final double DEF_IMSAAK_ANGLE = 1.5;
	
	public static final double DEF_IMSAAK_INTERVAL = 10;
	
	public static final double DEFAULT_ROUND_SEC = 30;
	
	public static final double AGGRESSIVE_ROUND_SEC = 1;
	
	/* UTILITIES */
	public static double degToRad(double angle) {
		return angle * (PI / 180.0);
	}
	
	public static double radToDeg(double angle) {
		return angle / (PI / 180.0);
	}
}
