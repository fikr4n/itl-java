package org.arabeyes.itl.prayer;

import org.arabeyes.itl.prayer.PrayerTimeCalc.InternalTimeType;
import org.arabeyes.itl.prayer.astro.Utils;

public class Method {
    
    /**
     * At certain locations and times of year, some prayer times do not occur
     * or otherwise are impossible to precisely calculate using conventional
     * means. These methods generally apply to locations with High latitudes
     * (near or above 49 degrees) or locations of Extreme proportion (near or
     * above 66 degrees).
     * <p>
     * Method Category Information:<ul>
     * 
     *         <li> Nearest Latitude (Aqrab Al-Bilaad): Calculate a prayer time
     *           using a safe latitude value. The recommended latitude by
     *           many schools of Fiqh is 48.5 degrees, but you can customize
     *           this by setting the "Method.setNearestLat()" method.
     *         </li>
     *         <li> Nearest Good Day (Aqrab Al-Ayyam): The library determines
     *           the closest previous or next day that the Fajr and Ishaa
     *           times occur and are both valid.
     *         </li>  
     *         <li> An [amount] of Night and Day: Unlike the above mentioned
     *           methods, the multiple methods in this category have no proof
     *           in traditional Shari'a (Fiqh) resources. These methods were
     *           introduced by modern day Muslim scholars and scientists for
     *           practical reasons only.
     *         </li>
     *         <li> Minutes from Shurooq/Maghrib: Use an interval time to
     *           calculate Fajr and Ishaa. This will set the values of Fajr
     *           and Ishaa to the same as the computed Shurooq and Maghrib
     *           respectively, then add or subtract the amount of minutes
     *           found in the "Method.getFajrInv" and "Method.getIshaaInv"
     *           methods.
     *          </li>
     *          </ul>
     */
    public static enum ExtremeLatitude {
        
        /**
         * None. If unable to calculate, leave only the invalid prayer
         *           time as 99:99.
         */
        NONE_EX,
        
        /**
         * Nearest Latitude: Apply to all prayer times always.
         */    
        LAT_ALL,
          
        /**
         * Nearest Latitude: Apply to Fajr and Ishaa times always.
         */
        LAT_ALWAYS,
        
        /**
         * Nearest Latitude: Apply to Fajr and Ishaa times but only if
         *                       the library has detected that the current
         *                       Fajr or Ishaa time is invalid.
         */
        LAT_INVALID,
        
        /**
         * Nearest Good Day: Apply to all prayer times always.
         */    
        GOOD_ALL,
        
        /**
         * Nearest Good Day: Apply to Fajr and Ishaa times but only if
         *                       the library has detected that the current
         *                       Fajr or Ishaa time is invalid. This is the
         *                       default method. (Default)
         */
        GOOD_INVALID,
        
        /**
         * 1/7th of Night: Apply to Fajr and Ishaa times always.
         */
        SEVEN_NIGHT_ALWAYS,

        /**
         * 1/7th of Night: Apply to Fajr and Ishaa times but only if
         *                       the library has detected that the current
         *                       Fajr or Ishaa time is invalid.
         *
         */
        SEVEN_NIGHT_INVALID,
        
        /**
         * 1/7th of Day: Apply to Fajr and Ishaa times always.
         */
        SEVEN_DAY_ALWAYS,
        
        /**
         * 1/7th of Day: Apply to Fajr and Ishaa times but only if the
         *                       library has detected that the current Fajr
         *                       or Ishaa time is invalid.
         */
        SEVEN_DAY_INVALID,
        
        /**
         * Half of the Night: Apply to Fajr and Ishaa times always.
         */
        HALF_ALWAYS,
        
        /**
         * Half of the Night: Apply to Fajr and Ishaa times but only
         *                         if the library has detected that the
         *                         current Fajr or Ishaa time is
         *                         invalid.
         */    
        HALF_INVALID,
        
        /**
         * Minutes from Shorooq/Maghrib: Apply to Fajr and Ishaa times always.
         */
        MIN_ALWAYS,

        /**
         * Minutes from Shorooq/Maghrib: Apply to Fajr and Ishaa times but only if
         *                       the library has detected that the
         *                       current Fajr or Ishaa time is invalid.
         *
         */
        MIN_INVALID,
        
        /**
         * Nearest Good Day: Different good days for Fajr and Ishaa (Not
         * implemented)
         */
        GOOD_DIF

    }
    
    /**
     * Madhhab is used for Asr prayer calculation. 
     */
    public static enum Madhhab {

        /**
         * Assr prayer shadow ratio: use Shaa'fi mathhab (default)
         */
        SHAFII,
        
        /**
         * Assr prayer shadow ratio: use Hanafi mathhab
         */
        HANAFI

    }
    
    /**
     * This class is used to round prayer times (seconds).
     */
    public static enum Rounding {
        
        /**
         *  No Rounding. second is set to the amount of computed seconds.
         */
        NONE,
        
        /**
         * Normal Rounding. If seconds are equal to 30 or above, add 1 minute. Sets "Prayer.seconds" to zero. 
         */
        NORMAL,
            
        /**
         * Special Rounding. Similar to normal rounding but we always round down for Shurooq and Imsaak times. (default) 
         */
        SPECIAL,
            
        /**
         * Aggressive Rounding. Similar to Special Rounding but we add 1 minute if the seconds value are equal to 1 second or more. 
         */
        AGRESSIVE

    }
    
    // End of inner classes
    
    protected double fajrAng; 
    protected double ishaaAng; 
    protected double imsaakAng;
    protected int fajrInv;
    protected int ishaaInv;
    protected int imsaakInv;
    protected Rounding round;
    protected Madhhab mathhab;
    protected double nearestLat;
    protected ExtremeLatitude extremeLatitude;
    protected boolean offset;
    protected double fajrOffset;
    protected double shurooqOffset;
    protected double thuhrOffset;
    protected double assrOffset;
    protected double maghribOffset;
    protected double ishaaOffset;
    
    public static final Method NONE = new Method(0.0, 0.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL, Madhhab.SHAFII,
            Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     * Egyptian General Authority of Survey<br />
     * <ul>
     * <li>Fajr Angle      = 20</li>
     * <li>Ishaa Angle     = 18</li>
     * <li>Used in:
     *  Indonesia, Iraq, Jordan, Lebanon, Malaysia, Singapore, Syria, parts of Africa, 
     *  parts of United States</li>
     *  </ul>
     */
    public static final Method EGYPT_SURVEY = new Method(20, 18, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL,
            Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     * University of Islamic Sciences, Karachi (Shaf'i)<br />
     * <ul><li>      Fajr Angle      = 18</li>                                 
     *       <li>Ishaa Angle     = 18</li>                                 
     *       <li>Used in:        Iran, Kuwait, parts of Europe
     *       </li></ul>
     */ 
    public static final Method KARACHI_SHAF = new Method(18, 18, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL,
            Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     *   University of Islamic Sciences, Karachi (Hanafi)<br />   
     *   <ul><li>Fajr Angle      = 18</li>                                 
     *   <li>Ishaa Angle     = 18</li>                                 
     *   <li>Used in:        Afghanistan, Bangladesh, India</li>
     *   </ul>                                
     */
    public static final Method KARACHI_HANAF = new Method(18, 18, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
            Rounding.SPECIAL, Madhhab.HANAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     *  Islamic Society of North America<br />
     *   <ul><li>Fajr Angle      = 15</li>                                 
     *   <li>Ishaa Angle     = 15</li>                                 
     *   <li>Used in:        Canada, Parts of UK, parts of United States</li>
     *   </ul>                  
     */
    public static final Method NORTH_AMERICA = new Method(15, 15, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
            Rounding.SPECIAL, Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     *   Muslim World League (MWL)<br />
     *   <ul><li>Fajr Angle      = 18</li>                                 
     *   <li>Ishaa Angle     = 17</li>                                 
     *   <li>Used in:        parts of Europe, Far East, parts of United States</li>
     *   </ul>
     *              
     */
    public static final Method MUSLIM_LEAGUE = new Method(18, 17, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
            Rounding.SPECIAL, Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     *  Om Al-Qurra University<br />
     *   <ul><li>Fajr Angle      = 19</li>                                 
     *   <li>Ishaa Angle     = 0 (not used)</li>                       
     *   <li>Ishaa Interval  = 90 minutes from Al-Maghrib prayer  
     *                     but set to 120 during Ramadan.</li>     
     *   <li>Used in:        Saudi Arabia</li>
     *   </ul>                         
     */
    public static final Method UMM_ALQURRA = new Method(19, 0, Utils.DEF_IMSAAK_ANGLE, 0, 90, 0, Rounding.SPECIAL,
            Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    /**
     *   Fixed Ishaa Angle Interval (always 90)<br />
     *   <ul><li>Fajr Angle      = 19.5</li>                               
     *   <li>Ishaa Angle     = 0 (not used)</li>                       
     *   <li>Ishaa Interval  = 90 minutes from Al-Maghrib prayer.</li> 
     *   <li>Used in:          Bahrain, Oman, Qatar, United Arab Emirates</li>
     *   </ul> 
     */
    public static final Method FIXED_ISHAA = new Method(19.5, 0, Utils.DEF_IMSAAK_ANGLE, 0, 90, 0,
            Rounding.SPECIAL, Madhhab.SHAFII, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0, 0, 0, 0, 0, 0);
    
    protected Method() {
    }
    
    /**
     * Build a method object containing all the information needed to compute
     * prayer time.
     *  
     * @param fajrAng Fajr angle
     * @param ishaaAng Ishaa angle 
     * @param imsaakAng The angle difference between Imsaak and Fajr (default
     *  is 1.5)
     * @param fajrInv Fajr Interval is the amount of minutes between Fajr and 
     *  Shurooq (0 if not used) 
     * @param ishaaInv Ishaa Interval is the amount if minutes between Ishaa and
     *  Maghrib (0 if not used)
     * @param imsaakInv Imsaak Interval is the amount of minutes between Imsaak
     *  and Fajr. The default is 10 minutes before Fajr if Fajr Interval is set
     * @param round Method used for rounding seconds
     * @param mathhab mathhab for calculating assr prayer shadow ratio
     * @param nearestLat Latitude Used for the 'Nearest Latitude' extreme
     *  methods. The default is 48.5
     * @param extreme Extreme latitude calculation method (@see ExtremeLatitude) 
     * @param offset Enable Offsets switch (set this to true to activate). This
     *  option allows you to add or subtract any amount of minutes to the daily
     *  computed prayer times based on values (in minutes) for each prayer in
     *  the next xxxOffset parameters     
     *  For Example: If you want to add 30 seconds to Maghrib and subtract 2
     *  minutes from Ishaa:<br /> 
     *   <code>method.setOffset(true); 
     *   method.setMaghribOffset(0.5); 
     *   method.setIshaaOffset(-2); </code>     
     * @param fajrOffset fajr offset
     * @param shurooqOffset shurooq offset
     * @param thuhrOffset thuhr offset
     * @param assrOffset assr offset
     * @param maghribOffset maghrib offset
     * @param ishaaOffset ishaa offset
     */
    public Method(double fajrAng, double ishaaAng, double imsaakAng,
            int fajrInv, int ishaaInv, int imsaakInv, Rounding round, Madhhab mathhab,
            double nearestLat, ExtremeLatitude extreme, boolean offset, double fajrOffset,
            double shurooqOffset, double thuhrOffset, double assrOffset,
            double maghribOffset, double ishaaOffset) {
        this.fajrAng = fajrAng;
        this.ishaaAng = ishaaAng;
        this.imsaakAng = imsaakAng;
        this.fajrInv = fajrInv;
        this.ishaaInv = ishaaInv;
        this.imsaakInv = imsaakInv;
        this.round = round;
        this.mathhab = mathhab;
        this.nearestLat = nearestLat;
        this.extremeLatitude = extreme;
        this.offset = offset;
        
        this.fajrOffset = fajrOffset;
        this.shurooqOffset = shurooqOffset;
        this.thuhrOffset = thuhrOffset;
        this.assrOffset = assrOffset;
        this.maghribOffset = maghribOffset;
        this.ishaaOffset = ishaaOffset;
        
    }
    
    public double getAssrOffset() {
        return assrOffset;
    }
    
    public ExtremeLatitude getExtremeLatitude() {
        return extremeLatitude;
    }
    
    public double getFajrAng() {
        return fajrAng;
    }
    
    public int getFajrInv() {
        return fajrInv;
    }
    
    public double getFajrOffset() {
        return fajrOffset;
    }
    
    public double getImsaakAng() {
        return imsaakAng;
    }
    
    public int getImsaakInv() {
        return imsaakInv;
    }
    
    public double getIshaaAng() {
        return ishaaAng;
    }
    
    public int getIshaaInv() {
        return ishaaInv;
    }
    
    public double getIshaaOffset() {
        return ishaaOffset;
    }
    
    public double getMaghribOffset() {
        return maghribOffset;
    }
    
    public Madhhab getMathhab() {
        return mathhab;
    }
    
    public double getNearestLat() {
        return nearestLat;
    }
    
    public boolean getOffset() {
        return offset;
    }
    
    /*
     fajrAng, ishaaAng, imsaakAng, fajrInv = 0, ishaaInv = 0, imsaakInv = 0, round = 2, mathhab = 1,
     nearestLat =  Utils.DEF_NEAREST_LATITUDE , extreme = 5, offset = 0, offList = null
     */
    
    public double getOffset(InternalTimeType prayer) {
        if (prayer == InternalTimeType.FAJR)
            return getFajrOffset();
        if (prayer == InternalTimeType.SUNRISE)
            return getShurooqOffset();
        if (prayer == InternalTimeType.ZUHR)
            return getThuhrOffset();
        if (prayer == InternalTimeType.ASR)
            return getAssrOffset();
        if (prayer == InternalTimeType.MAGHRIB)
            return getMaghribOffset();
        if (prayer == InternalTimeType.ISHA)
            return getIshaaOffset();
        return 0;
    }
    
    public Rounding getRound() {
        return round;
    }
    
    public double getShurooqOffset() {
        return shurooqOffset;
    }
    
    public double getThuhrOffset() {
        return thuhrOffset;
    }
    
}
