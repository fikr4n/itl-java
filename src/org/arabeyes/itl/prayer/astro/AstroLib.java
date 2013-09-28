package org.arabeyes.itl.prayer.astro;

/**
 *  Astro functions
 *  
 *  Most of the astronomical values and formulas used in this file are
 *  based on the Jean Meeus subset of the VSOP87 planetary theory.
 *
 */
public class AstroLib {
	static final double L0[][] = { { 175347046, 0, 0 },
		{ 3341656, 4.6692568, 6283.07585 }, { 34894, 4.6261, 12566.1517 },
		{ 3497, 2.7441, 5753.3849 }, { 3418, 2.8289, 3.5231 },
		{ 3136, 3.6277, 77713.7715 }, { 2676, 4.4181, 7860.4194 },
		{ 2343, 6.1352, 3930.2097 }, { 1324, 0.7425, 11506.7698 },
		{ 1273, 2.0371, 529.691 }, { 1199, 1.1096, 1577.3435 },
		{ 990, 5.233, 5884.927 }, { 902, 2.045, 26.298 },
		{ 857, 3.508, 398.149 }, { 780, 1.179, 5223.694 },
		{ 753, 2.533, 5507.553 }, { 505, 4.583, 18849.228 },
		{ 492, 4.205, 775.523 }, { 357, 2.92, 0.067 },
		{ 317, 5.849, 11790.629 }, { 284, 1.899, 796.298 },
		{ 271, 0.315, 10977.079 }, { 243, 0.345, 5486.778 },
		{ 206, 4.806, 2544.314 }, { 205, 1.869, 5573.143 },
		{ 202, 2.4458, 6069.777 }, { 156, 0.833, 213.299 },
		{ 132, 3.411, 2942.463 }, { 126, 1.083, 20.775 },
		{ 115, 0.645, 0.98 }, { 103, 0.636, 4694.003 },
		{ 102, 0.976, 15720.839 }, { 102, 4.267, 7.114 },
		{ 99, 6.21, 2146.17 }, { 98, 0.68, 155.42 },
		{ 86, 5.98, 161000.69 }, { 85, 1.3, 6275.96 },
		{ 85, 3.67, 71430.7 }, { 80, 1.81, 17260.15 },
		{ 79, 3.04, 12036.46 }, { 71, 1.76, 5088.63 },
		{ 74, 3.5, 3154.69 }, { 74, 4.68, 801.82 }, { 70, 0.83, 9437.76 },
		{ 62, 3.98, 8827.39 }, { 61, 1.82, 7084.9 }, { 57, 2.78, 6286.6 },
		{ 56, 4.39, 14143.5 }, { 56, 3.47, 6279.55 },
		{ 52, 0.19, 12139.55 }, { 52, 1.33, 1748.02 },
		{ 51, 0.28, 5856.48 }, { 49, 0.49, 1194.45 },
		{ 41, 5.37, 8429.24 }, { 41, 2.4, 19651.05 },
		{ 39, 6.17, 10447.39 }, { 37, 6.04, 10213.29 },
		{ 37, 2.57, 1059.38 }, { 36, 1.71, 2352.87 },
		{ 36, 1.78, 6812.77 }, { 33, 0.59, 17789.85 },
		{ 30, 0.44, 83996.85 }, { 30, 2.74, 1349.87 },
		{ 25, 3.16, 4690.48 } };
	
	static final double L1[][] = { { 628331966747.0, 0, 0 },
		{ 206059, 2.678235, 6283.07585 }, { 4303, 2.6351, 12566.1517 },
		{ 425, 1.59, 3.523 }, { 119, 5.796, 26.298 },
		{ 109, 2.966, 1577.344 }, { 93, 2.59, 18849.23 },
		{ 72, 1.14, 529.69 }, { 68, 1.87, 398.15 }, { 67, 4.41, 5507.55 },
		{ 59, 2.89, 5223.69 }, { 56, 2.17, 155.42 }, { 45, 0.4, 796.3 },
		{ 36, 0.47, 775.52 }, { 29, 2.65, 7.11 }, { 21, 5.34, 0.98 },
		{ 19, 1.85, 5486.78 }, { 19, 4.97, 213.3 }, { 17, 2.99, 6275.96 },
		{ 16, 0.03, 2544.31 }, { 16, 1.43, 2146.17 },
		{ 15, 1.21, 10977.08 }, { 12, 2.83, 1748.02 },
		{ 12, 3.26, 5088.63 }, { 12, 5.27, 1194.45 }, { 12, 2.08, 4694 },
		{ 11, 0.77, 553.57 }, { 10, 1.3, 3286.6 }, { 10, 4.24, 1349.87 },
		{ 9, 2.7, 242.73 }, { 9, 5.64, 951.72 }, { 8, 5.3, 2352.87 },
		{ 6, 2.65, 9437.76 }, { 6, 4.67, 4690.48 } };
	
	static final double L2[][] = { { 52919, 0, 0 },
		{ 8720, 1.0721, 6283.0758 }, { 309, 0.867, 12566.152 },
		{ 27, 0.05, 3.52 }, { 16, 5.19, 26.3 }, { 16, 3.68, 155.42 },
		{ 10, 0.76, 18849.23 }, { 9, 2.06, 77713.77 }, { 7, 0.83, 775.52 },
		{ 5, 4.66, 1577.34 }, { 4, 1.03, 7.11 }, { 4, 3.44, 5573.14 },
		{ 3, 5.14, 796.3 }, { 3, 6.05, 5507.55 }, { 3, 1.19, 242.73 },
		{ 3, 6.12, 529.69 }, { 3, 0.31, 398.15 }, { 3, 2.28, 553.57 },
		{ 2, 4.38, 5223.69 }, { 2, 3.75, 0.98 } };
	
	static final double L3[][] = { { 289, 5.844, 6283.076 }, { 35, 0, 0 },
		{ 17, 5.49, 12566.15 }, { 3, 5.2, 155.42 }, { 1, 4.72, 3.52 },
		{ 1, 5.3, 18849.23 }, { 1, 5.97, 242.73 } };
	
	static final double L4[][] = { { 114.0, 3.142, 0.0 },
		{ 8.0, 4.13, 6283.08 }, { 1.0, 3.84, 12566.15 } };
	
	static final double L5[][] = { { 1, 3.14, 0 } };
	
	static final double B0[][] = {
		
		{ 280, 3.199, 84334.662 }, { 102, 5.422, 5507.553 }, { 80, 3.88, 5223.69 },
		{ 44, 3.7, 2352.87 }, { 32, 4, 1577.34 } };
	
	static final double B1[][] = {
		
		{ 9, 3.9, 5507.55 }, { 6, 1.73, 5223.69 } };
	
	static final double R0[][] = { { 100013989, 0, 0 },
		{ 1670700, 3.0984635, 6283.07585 }, { 13956, 3.05525, 12566.1517 },
		{ 3084, 5.1985, 77713.7715 }, { 1628, 1.1739, 5753.3849 },
		{ 1576, 2.8469, 7860.4194 }, { 925, 5.453, 11506.77 },
		{ 542, 4.564, 3930.21 }, { 472, 3.661, 5884.927 },
		{ 346, 0.964, 5507.553 }, { 329, 5.9, 5223.694 },
		{ 307, 0.299, 5573.143 }, { 243, 4.273, 11790.629 },
		{ 212, 5.847, 1577.344 }, { 186, 5.022, 10977.079 },
		{ 175, 3.012, 18849.228 }, { 110, 5.055, 5486.778 },
		{ 98, 0.89, 6069.78 }, { 86, 5.69, 15720.84 },
		{ 86, 1.27, 161000.69 }, { 85, 0.27, 17260.15 },
		{ 63, 0.92, 529.69 }, { 57, 2.01, 83996.85 },
		{ 56, 5.24, 71430.7 }, { 49, 3.25, 2544.31 }, { 47, 2.58, 775.52 },
		{ 45, 5.54, 9437.76 }, { 43, 6.01, 6275.96 }, { 39, 5.36, 4694 },
		{ 38, 2.39, 8827.39 }, { 37, 0.83, 19651.05 },
		{ 37, 4.9, 12139.55 }, { 36, 1.67, 12036.46 },
		{ 35, 1.84, 2942.46 }, { 33, 0.24, 7084.9 }, { 32, 0.18, 5088.63 },
		{ 32, 1.78, 398.15 }, { 28, 1.21, 6286.6 }, { 28, 1.9, 6279.55 },
		{ 26, 4.59, 10447.39 } };
	
	static final double R1[][] = {
		
		{ 103019, 1.10749, 6283.07585 }, { 1721, 1.0644, 12566.1517 },
		{ 702, 3.142, 0 }, { 32, 1.02, 18849.23 }, { 31, 2.84, 5507.55 },
		{ 25, 1.32, 5223.69 }, { 18, 1.42, 1577.34 },
		{ 10, 5.91, 10977.08 }, { 9, 1.42, 6275.96 }, { 9, 0.27, 5486.78 } };
	
	static final double R2[][] = {
		
		{ 4359, 5.7846, 6283.0758 }, { 124, 5.579, 12566.152 }, { 12, 3.14, 0 },
		{ 9, 3.63, 77713.77 }, { 6, 1.87, 5573.14 }, { 3, 5.47, 18849 }
		
	};
	
	static final double R3[][] = { { 145, 4.273, 6283.076 },
		{ 7, 3.92, 12566.15 } };
	
	static final double R4[] = { 4, 2.56, 6283.08 };
	
	static final double PE[][] = { { -171996, -174.2, 92025, 8.9 },
		{ -13187, -1.6, 5736, -3.1 }, { -2274, -0.2, 977, -0.5 },
		{ 2062, 0.2, -895, 0.5 }, { 1426, -3.4, 54, -0.1 },
		{ 712, 0.1, -7, 0 }, { -517, 1.2, 224, -0.6 },
		{ -386, -0.4, 200, 0 }, { -301, 0, 129, -0.1 },
		{ 217, -0.5, -95, 0.3 }, { -158, 0, 0, 0 }, { 129, 0.1, -70, 0 },
		{ 123, 0, -53, 0 }, { 63, 0, 0, 0 }, { 63, 0.1, -33, 0 },
		{ -59, 0, 26, 0 }, { -58, -0.1, 32, 0 }, { -51, 0, 27, 0 },
		{ 48, 0, 0, 0 }, { 46, 0, -24, 0 }, { -38, 0, 16, 0 },
		{ -31, 0, 13, 0 }, { 29, 0, 0, 0 }, { 29, 0, -12, 0 },
		{ 26, 0, 0, 0 }, { -22, 0, 0, 0 }, { 21, 0, -10, 0 },
		{ 17, -0.1, 0, 0 }, { 16, 0, -8, 0 }, { -16, 0.1, 7, 0 },
		{ -15, 0, 9, 0 }, { -13, 0, 7, 0 }, { -12, 0, 6, 0 },
		{ 11, 0, 0, 0 }, { -10, 0, 5, 0 }, { -8, 0, 3, 0 },
		{ 7, 0, -3, 0 }, { -7, 0, 0, 0 }, { -7, 0, 3, 0 }, { -7, 0, 3, 0 },
		{ 6, 0, 0, 0 }, { 6, 0, -3, 0 }, { 6, 0, -3, 0 }, { -6, 0, 3, 0 },
		{ -6, 0, 3, 0 }, { 5, 0, 0, 0 }, { -5, 0, 3, 0 }, { -5, 0, 3, 0 },
		{ -5, 0, 3, 0 }, { 4, 0, 0, 0 }, { 4, 0, 0, 0 }, { 4, 0, 0, 0 },
		{ -4, 0, 0, 0 }, { -4, 0, 0, 0 }, { -4, 0, 0, 0 }, { 3, 0, 0, 0 },
		{ -3, 0, 0, 0 }, { -3, 0, 0, 0 }, { -3, 0, 0, 0 }, { -3, 0, 0, 0 },
		{ -3, 0, 0, 0 }, { -3, 0, 0, 0 }, { -3, 0, 0, 0 } };
	
	static final int SINCOEFF[][] = { { 0, 0, 0, 0, 1 }, { -2, 0, 0, 2, 2 },
		{ 0, 0, 0, 2, 2 }, { 0, 0, 0, 0, 2 }, { 0, 1, 0, 0, 0 },
		{ 0, 0, 1, 0, 0 }, { -2, 1, 0, 2, 2 }, { 0, 0, 0, 2, 1 },
		{ 0, 0, 1, 2, 2 }, { -2, -1, 0, 2, 2 }, { -2, 0, 1, 0, 0 },
		{ -2, 0, 0, 2, 1 }, { 0, 0, -1, 2, 2 }, { 2, 0, 0, 0, 0 },
		{ 0, 0, 1, 0, 1 }, { 2, 0, -1, 2, 2 }, { 0, 0, -1, 0, 1 },
		{ 0, 0, 1, 2, 1 }, { -2, 0, 2, 0, 0 }, { 0, 0, -2, 2, 1 },
		{ 2, 0, 0, 2, 2 }, { 0, 0, 2, 2, 2 }, { 0, 0, 2, 0, 0 },
		{ -2, 0, 1, 2, 2 }, { 0, 0, 0, 2, 0 }, { -2, 0, 0, 2, 0 },
		{ 0, 0, -1, 2, 1 }, { 0, 2, 0, 0, 0 }, { 2, 0, -1, 0, 1 },
		{ -2, 2, 0, 2, 2 }, { 0, 1, 0, 0, 1 }, { -2, 0, 1, 0, 1 },
		{ 0, -1, 0, 0, 1 }, { 0, 0, 2, -2, 0 }, { 2, 0, -1, 2, 1 },
		{ 2, 0, 1, 2, 2 }, { 0, 1, 0, 2, 2 }, { -2, 1, 1, 0, 0 },
		{ 0, -1, 0, 2, 2 }, { 2, 0, 0, 2, 1 }, { 2, 0, 1, 0, 0 },
		{ -2, 0, 2, 2, 2 }, { -2, 0, 1, 2, 1 }, { 2, 0, -2, 0, 1 },
		{ 2, 0, 0, 0, 1 }, { 0, -1, 1, 0, 0 }, { -2, -1, 0, 2, 1 },
		{ -2, 0, 0, 0, 1 }, { 0, 0, 2, 2, 1 }, { -2, 0, 2, 0, 1 },
		{ -2, 1, 0, 2, 1 }, { 0, 0, 1, -2, 0 }, { -1, 0, 1, 0, 0 },
		{ -2, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 }, { 0, 0, 1, 2, 0 },
		{ 0, 0, -2, 2, 2 }, { -1, -1, 1, 0, 0 }, { 0, 1, 1, 0, 0 },
		{ 0, -1, 1, 2, 2 }, { 2, -1, -1, 2, 2 }, { 0, 0, 3, 2, 2 },
		{ 2, -1, 0, 2, 2 } };
	
	public static double getRefraction(final Location loc, double sunAlt) {
		double part1, part2;
		
		part1 = (loc.getPressure() / 1010.0)
		* (283 / (273 + loc.getTemperature()));
		part2 = 1.02 / (Utils.radToDeg(Math.tan(Utils.degToRad(sunAlt + (10.3/(sunAlt + 5.11))))) + 0.0019279);
		return (part1 * part2) / 60.0;
	}
	
	public static double getJulianDay(final SimpleGregorianDate date, double gmt) {
		
		double jdB = 0, jdY, jdM, JD;
		
		jdY = date.getYear();
		jdM = date.getMonthCompat();
		
		if (date.getMonthCompat() <= 2) {
			jdY--;
			jdM += 12;
		}
		
		if (date.getYear() < 1)
			jdY++;
		
		if ((date.getYear() > 1582)
				|| ((date.getYear() == 1582) && ((date.getMonthCompat() > 10) || ((date
						.getMonthCompat() == 10) && (date.getDay() >= 4)))))
			jdB = 2 - Math.floor(jdY / 100.0) + Math.floor((jdY / 100.0) / 4.0);
		
		JD = Math.floor(365.25 * (jdY + 4716.0))
		+ Math.floor(30.6001 * (jdM + 1)) + (date.getDay() + (-gmt) / 24.0)
		+ jdB - 1524.5;
		
		return JD;
	}
	
	public static void getAstroValuesByDay(double julianDay,
			final Location loc, Astro astro, Astro topAstro) {
		AstroDay ad = new AstroDay();
		
		if (astro.getJd() == julianDay - 1) {
			astro.getRa()[0] = astro.getRa()[1];
			
			astro.getRa()[1] = astro.getRa()[2];
			astro.getDec()[0] = astro.getDec()[1];
			astro.getDec()[1] = astro.getDec()[2];
			astro.getSid()[0] = astro.getSid()[1];
			astro.getSid()[1] = astro.getSid()[2];
			astro.getDra()[0] = astro.getDra()[1];
			astro.getDra()[1] = astro.getDra()[2];
			astro.getRsum()[0] = astro.getRsum()[1];
			astro.getRsum()[1] = astro.getRsum()[2];
			computeAstroDay(julianDay + 1, ad);
			astro.getRa()[2] = ad.getRa();
			astro.getDec()[2] = ad.getDec();
			astro.getSid()[2] = ad.getSidtime();
			astro.getDra()[2] = ad.getDra();
			astro.getRsum()[2] = ad.getRsum();
		} else if (astro.getJd() == julianDay + 1) {
			
			astro.getRa()[2] = astro.getRa()[1];
			astro.getRa()[1] = astro.getRa()[0];
			astro.getDec()[2] = astro.getDec()[1];
			astro.getDec()[1] = astro.getDec()[0];
			astro.getSid()[2] = astro.getSid()[1];
			astro.getSid()[1] = astro.getSid()[0];
			astro.getDra()[2] = astro.getDra()[1];
			astro.getDra()[1] = astro.getDra()[0];
			astro.getRsum()[2] = astro.getRsum()[1];
			astro.getRsum()[1] = astro.getRsum()[0];
			computeAstroDay(julianDay - 1, ad);
			astro.getRa()[0] = ad.getRa();
			astro.getDec()[0] = ad.getDec();
			astro.getSid()[0] = ad.getSidtime();
			astro.getDra()[0] = ad.getDra();
			astro.getRsum()[0] = ad.getRsum();
		} else if (astro.getJd() != julianDay) {
			computeAstroDay(julianDay - 1, ad);
			astro.getRa()[0] = ad.getRa();
			astro.getDec()[0] = ad.getDec();
			astro.getSid()[0] = ad.getSidtime();
			astro.getDra()[0] = ad.getDra();
			astro.getRsum()[0] = ad.getRsum();
			computeAstroDay(julianDay, ad);
			astro.getRa()[1] = ad.getRa();
			astro.getDec()[1] = ad.getDec();
			astro.getSid()[1] = ad.getSidtime();
			astro.getDra()[1] = ad.getDra();
			astro.getRsum()[1] = ad.getRsum();
			computeAstroDay(julianDay + 1, ad);
			astro.getRa()[2] = ad.getRa();
			astro.getDec()[2] = ad.getDec();
			astro.getSid()[2] = ad.getSidtime();
			astro.getDra()[2] = ad.getDra();
			astro.getRsum()[2] = ad.getRsum();
		}
		
		astro.setJd(julianDay);
		computeTopAstro(loc, astro, topAstro);
		
	}
	
	public static void computeAstroDay(double JD, AstroDay astroday) {
		
		int i = 0;
		double R, Gg, G;
		
		double tL, L;
		double tB, B;
		
		double X0, X1, X2, X3, X4;
		
		double U, E0, E, lamda, V0, V;
		
		double RAn, RAd, RA, DEC;
		
		double B0sum = 0, B1sum = 0;
		double R0sum = 0, R1sum = 0, R2sum = 0, R3sum = 0, R4sum = 0;
		double L0sum = 0, L1sum = 0, L2sum = 0, L3sum = 0, L4sum = 0, L5sum = 0;
		
		double xsum = 0, psi = 0, epsilon = 0;
		double deltaPsi, deltaEps;
		
		double JC = (JD - 2451545) / 36525.0;
		double JM = JC / 10.0;
		double JM2 = Math.pow(JM, 2);
		double JM3 = Math.pow(JM, 3);
		double JM4 = Math.pow(JM, 4);
		double JM5 = Math.pow(JM, 5);
		
		for (i = 0; i < 64; i++)
			L0sum += L0[i][0] * Math.cos(L0[i][1] + L0[i][2] * JM);
		for (i = 0; i < 34; i++)
			L1sum += L1[i][0] * Math.cos(L1[i][1] + L1[i][2] * JM);
		for (i = 0; i < 20; i++)
			L2sum += L2[i][0] * Math.cos(L2[i][1] + L2[i][2] * JM);
		for (i = 0; i < 7; i++)
			L3sum += L3[i][0] * Math.cos(L3[i][1] + L3[i][2] * JM);
		for (i = 0; i < 3; i++)
			L4sum += L4[i][0] * Math.cos(L4[i][1] + L4[i][2] * JM);
		L5sum = L5[0][0] * Math.cos(L5[0][1] + L5[0][2] * JM);
		
		tL = (L0sum + (L1sum * JM) + (L2sum * JM2) + (L3sum * JM3)
				+ (L4sum * JM4) + (L5sum * JM5))
				/ Math.pow(10, 8);
		
		L = limitAngle(Utils.radToDeg(tL));
		
		for (i = 0; i < 5; i++)
			B0sum += B0[i][0] * Math.cos(B0[i][1] + B0[i][2] * JM);
		for (i = 0; i < 2; i++)
			B1sum += B1[i][0] * Math.cos(B1[i][1] + B1[i][2] * JM);
		
		tB = (B0sum + (B1sum * JM)) / Math.pow(10, 8);
		B = Utils.radToDeg(tB);
		
		for (i = 0; i < 40; i++)
			R0sum += R0[i][0] * Math.cos(R0[i][1] + R0[i][2] * JM);
		for (i = 0; i < 10; i++)
			R1sum += R1[i][0] * Math.cos(R1[i][1] + R1[i][2] * JM);
		for (i = 0; i < 6; i++)
			R2sum += R2[i][0] * Math.cos(R2[i][1] + R2[i][2] * JM);
		for (i = 0; i < 2; i++)
			R3sum += R3[i][0] * Math.cos(R3[i][1] + R3[i][2] * JM);
		
		R4sum = R4[0] * Math.cos(R4[1] + R4[2] * JM);
		
		R = (R0sum + (R1sum * JM) + (R2sum * JM2) + (R3sum * JM3) + (R4sum * JM4))
		/ Math.pow(10, 8);
		
		G = limitAngle((L + 180));
		Gg = -B;
		
		X0 = 297.85036 + (445267.111480 * JC) - (0.0019142 * Math.pow(JC, 2))
		+ Math.pow(JC, 3) / 189474.0;
		X1 = 357.52772 + (35999.050340 * JC) - (0.0001603 * Math.pow(JC, 2))
		- Math.pow(JC, 3) / 300000.0;
		X2 = 134.96298 + (477198.867398 * JC) + (0.0086972 * Math.pow(JC, 2))
		+ Math.pow(JC, 3) / 56250.0;
		X3 = 93.27191 + (483202.017538 * JC) - (0.0036825 * Math.pow(JC, 2))
		+ Math.pow(JC, 3) / 327270.0;
		X4 = 125.04452 - (1934.136261 * JC) + (0.0020708 * Math.pow(JC, 2))
		+ Math.pow(JC, 3) / 450000.0;
		
		for (i = 0; i < 63; i++) {
			xsum += X0 * SINCOEFF[i][0];
			xsum += X1 * SINCOEFF[i][1];
			xsum += X2 * SINCOEFF[i][2];
			xsum += X3 * SINCOEFF[i][3];
			xsum += X4 * SINCOEFF[i][4];
			psi += (PE[i][0] + JC * PE[i][1])
			* Math.sin(Utils.degToRad(xsum));
			epsilon += (PE[i][2] + JC * PE[i][3])
			* Math.cos(Utils.degToRad(xsum));
			xsum = 0;
		}
		
		deltaPsi = psi / 36000000.0;
		deltaEps = epsilon / 36000000.0;
		
		U = JM / 10.0;
		E0 = 84381.448 - 4680.93 * U - 1.55 * Math.pow(U, 2) + 1999.25
		* Math.pow(U, 3) - 51.38 * Math.pow(U, 4) - 249.67
		* Math.pow(U, 5) - 39.05 * Math.pow(U, 6) + 7.12
		* Math.pow(U, 7) + 27.87 * Math.pow(U, 8) + 5.79
		* Math.pow(U, 9) + 2.45 * Math.pow(U, 10);
		E = E0 / 3600.0 + deltaEps;
		lamda = G + deltaPsi + (-20.4898 / (3600.0 * R));
		
		V0 = 280.46061837 + 360.98564736629 * (JD - 2451545) + 0.000387933
		* Math.pow(JC, 2) - Math.pow(JC, 3) / 38710000.0;
		V = limitAngle(V0) + deltaPsi * Math.cos(Utils.degToRad(E));
		
		RAn = Math.sin(Utils.degToRad(lamda)) * Math.cos(Utils.degToRad(E))
		- Math.tan(Utils.degToRad(Gg))
		* Math.sin(Utils.degToRad(E));
		RAd = Math.cos(Utils.degToRad(lamda));
		RA = limitAngle(Utils.radToDeg(Math.atan2(RAn, RAd)));
		
		DEC = Math.asin(Math.sin(Utils.degToRad(Gg))
				* Math.cos(Utils.degToRad(E))
				+ Math.cos(Utils.degToRad(Gg))
				* Math.sin(Utils.degToRad(E))
				* Math.sin(Utils.degToRad(lamda)));
		
		astroday.setRa(RA);
		astroday.setDec(DEC);
		astroday.setSidtime(V);
		astroday.setDra(0);
		astroday.setRsum(R);
		
	}
	
	public static void computeTopAstro(final Location loc, final Astro astro,
			Astro topAstro) {
		int i;
		double lHour, SP;
		double tU, tCos, tSin, tRA0, tRA, tDEC;
		
		for (i = 0; i < 3; i++) {
			lHour = limitAngle(astro.getSid()[i] + loc.getDegreeLong()
					- astro.getRa()[i]);
			
			SP = 8.794 / (3600 * astro.getRsum()[i]);
			
			tU = Math.atan(0.99664719 * Math.tan(Utils.degToRad(loc
					.getDegreeLat())));
			
			tCos = Math.cos(tU) + ((loc.getSeaLevel()) / 6378140.0)
			* Math.cos(Utils.degToRad(loc.getDegreeLat()));
			
			tSin = 0.99664719 * Math.sin(tU) + (loc.getSeaLevel() / 6378140.0)
			* Math.sin(Utils.degToRad(loc.getDegreeLat()));
			
			tRA0 = (((-tCos) * Math.sin(Utils.degToRad(SP)) * Math.sin(Utils
					.degToRad(lHour))) / (Math.cos(astro.getDec()[i]) - tCos
							* Math.sin(Utils.degToRad(SP))
							* Math.cos(Utils.degToRad(lHour))));
			
			tRA = astro.getRa()[i] + Utils.radToDeg(tRA0);
			
			tDEC = Utils.radToDeg(Math.atan2(
					(Math.sin(astro.getDec()[i]) - tSin
							* Math.sin(Utils.degToRad(SP)))
							* Math.cos(tRA0), Math.cos(astro.getDec()[i])
							- tCos * Math.sin(Utils.degToRad(SP))
							* Math.cos(Utils.degToRad(lHour))));
			
			topAstro.getRa()[i] = tRA;
			topAstro.getDec()[i] = tDEC;
			topAstro.getSid()[i] = astro.getSid()[i];
			topAstro.getDra()[i] = tRA0;
			topAstro.getRsum()[i] = astro.getRsum()[i];
			
		}
		
	}
	
	public static double limitAngle(double L) {
		double F;
		L /= 360.0;
		F = L - Math.floor(L);
		if (F > 0)
			return 360 * F;
		else if (F < 0)
			return 360 - 360 * F;
		else
			return L;
	}
	
	public static double limitAngle180(double L) {
		double F;
		L /= 180.0;
		F = L - Math.floor(L);
		if (F > 0)
			return 180 * F;
		else if (F < 0)
			return 180 - 180 * F;
		else
			return L;
	}
	
	public static double limitAngle111(double L) {
		double F;
		F = L - Math.floor(L);
		if (F < 0)
			return F += 1;
		return F;
	}
	
	public static double limitAngle180between(double L) {
		double F;
		L /= 360.0;
		F = (L - Math.floor(L)) * 360.0;
		if (F < -180)
			F += 360;
		else if (F > 180)
			F -= 360;
		return F;
	}
	
}
