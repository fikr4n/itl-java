package org.arabeyes.itl.prayer;

import org.arabeyes.itl.prayer.Method.ExtremeLatitude;
import org.arabeyes.itl.prayer.Method.Madhhab;
import org.arabeyes.itl.prayer.Method.Rounding;

/**
 * A mutable version of {@link Method}.
 */
public class CustomMethod extends Method {
    
    public CustomMethod() {
    }

    /**
     * Copy constructor
     * @param orig method to copy from
     */
    public CustomMethod(Method orig) {
        this.fajrAng = orig.getFajrAng();
        this.ishaaAng = orig.getIshaaAng();
        this.imsaakAng = orig.getImsaakAng();
        this.fajrInv = orig.getFajrInv();
        this.ishaaInv = orig.getIshaaInv();
        this.imsaakInv = orig.getImsaakInv();
        this.round = orig.getRound();
        this.mathhab = orig.getMathhab();
        this.nearestLat = orig.getNearestLat();
        this.extremeLatitude = orig.getExtremeLatitude();
        this.offset = orig.getOffset();
        
        this.fajrOffset = orig.getFajrOffset();
        this.shurooqOffset = orig.getShurooqOffset();
        this.thuhrOffset = orig.getThuhrOffset();
        this.assrOffset = orig.getAssrOffset();
        this.maghribOffset = orig.getMaghribOffset();
        this.ishaaOffset = orig.getIshaaOffset();        
    }
    
    public void setAssrOffset(double assrOffset) {
        this.assrOffset = assrOffset;
    }
    
    /**
     * 
     * @param extreme Extreme latitude calculation method
     * @see ExtremeLatitude
     */
    public void setExtremeLatitude(ExtremeLatitude extreme) {
        this.extremeLatitude = extreme;
    }
    
    public void setFajrAng(double fajrAng) {
        this.fajrAng = fajrAng;
    }
    
    /**
     * 
     * @param fajrInv Fajr Interval is the amount of minutes between Fajr and 
     *  Shurooq (0 if not used)
     */
    public void setFajrInv(int fajrInv) {
        this.fajrInv = fajrInv;
    }
    
    public void setFajrOffset(double fajrOffset) {
        this.fajrOffset = fajrOffset;
    }
    
    /**
     * 
     * @param imsaakAng The angle difference between Imsaak and Fajr (default
     *  is 1.5)
     */
    public void setImsaakAng(double imsaakAng) {
        this.imsaakAng = imsaakAng;
    }
    
    /**
     * 
     * @param imsaakInv Imsaak Interval is the amount of minutes between Imsaak
     *  and Fajr. The default is 10 minutes before Fajr if Fajr Interval is set
     */
    public void setImsaakInv(int imsaakInv) {
        this.imsaakInv = imsaakInv;
    }
    
    public void setIshaaAng(double ishaaAng) {
        this.ishaaAng = ishaaAng;
    }
    
    /**
     * 
     * @param ishaaInv Ishaa Interval is the amount if minutes between Ishaa and
     *  Maghrib (0 if not used)
     */
    public void setIshaaInv(int ishaaInv) {
        this.ishaaInv = ishaaInv;
    }
    
    public void setIshaaOffset(double ishaaOffset) {
        this.ishaaOffset = ishaaOffset;        
    }
    
    public void setMaghribOffset(double maghribOffset) {
        this.maghribOffset = maghribOffset;
    }
    
    /**
     * 
     * @param mathhab mathhab for calculating assr prayer shadow ratio
     * @see Madhhab
     */
    public void setMathhab(Madhhab mathhab) {
        this.mathhab = mathhab;
    }
    
    /**
     * 
     * @param nearestLat Latitude Used for the 'Nearest Latitude' extreme
     *  methods. The default is 48.5
     */
    public void setNearestLat(double nearestLat) {
        this.nearestLat = nearestLat;
    }
    
    /**
     * 
     * @param offset Enable Offsets switch (set this to true to activate). This
     *  option allows you to add or subtract any amount of minutes to the daily
     *  computed prayer times based on values (in minutes) for each prayer in
     *  the next xxxOffset parameters     
     *  For Example: If you want to add 30 seconds to Maghrib and subtract 2
     *  minutes from Ishaa: 
     *   <code>method.setOffset(true); 
     *   method.setMaghribOffset(0.5); 
     *   method.setIshaaOffset(-2); </code>     
     */
    public void setOffset(boolean offset) {
        this.offset = offset;
    }
    
    /**
     * 
     * @param round Method used for rounding seconds
     * @see Rounding
     */
    public void setRound(Rounding round) {
        this.round = round;
    }
    
    public void setShurooqOffset(double shurooqOffset) {
        this.shurooqOffset = shurooqOffset;
    }
    
    public void setThuhrOffset(double thuhrOffset) {
        this.thuhrOffset = thuhrOffset;
    }
}
