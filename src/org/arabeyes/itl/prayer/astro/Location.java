package org.arabeyes.itl.prayer.astro;

public class Location {
    
    static final double DEFAULT_SEA_LEVEL = 0;
    
    static final double DEFAULT_PRESSURE = 1010;
    
    static final double DEFAULT_TEMPERATURE = 10;
    
    private double degreeLong; 
    
    private double degreeLat;
    
    private double gmtDiff;
    
    private int dst; 
    
    private double seaLevel; 
    
    private double pressure; 
    
    private double temperature;
    
    /**
     * default constructor of location object. Latitude, Longitude,
     * GMT difference and day saving time flag are required. Other
     * settings (sea level, pressure, temperature) are given standard
     * astronomical values and can be set later using setters.
     * 
     * 
     * @param degreeLat latitude in degrees
     * @param degreeLong longitude in degrees
     * @param gmtDiff difference with GMT
     * @param dst day saving time (1 to add one hour, 2 to add two, 0
     * if none, etc..)
     */
    public Location(double degreeLat, double degreeLong, double gmtDiff, int dst) {
        this.degreeLong = degreeLong;
        this.degreeLat = degreeLat;
        this.gmtDiff = gmtDiff;
        this.dst = dst;
        
        this.seaLevel = DEFAULT_SEA_LEVEL;
        this.pressure = DEFAULT_PRESSURE;
        this.temperature = DEFAULT_TEMPERATURE;
    }
    
    public Location(Location src) {
        degreeLat = (src.degreeLat);
        degreeLong = (src.degreeLong);
        gmtDiff = (src.gmtDiff);
        dst = (src.dst);
        seaLevel = (src.seaLevel);
        pressure = (src.pressure);
        temperature = (src.temperature);
    }
    
    public double getDegreeLat() {
        return degreeLat;
    }
    
    /**
     * 
     * @param degreeLat Latitude in decimal degree.
     */
    public void setDegreeLat(double degreeLat) {
        this.degreeLat = degreeLat;
    }
    
    public double getDegreeLong() {
        return degreeLong;
    }
    
    /**
     * 
     * @param degreeLong Longitude in decimal degree.
     */
    public void setDegreeLong(double degreeLong) {
        this.degreeLong = degreeLong;
    }
    
    public int getDst() {
        return dst;
    }
    

    /**
     * Daylight savings time switch (0 if not used). Set
     * this to 1 should add 1 hour to all the calculated
     * prayer times 
     * @param dst
     */
    public void setDst(int dst) {
        this.dst = dst;
    }
    
    public double getGmtDiff() {
        return gmtDiff;
    }
    
    /**
     * 
     * @param gmtDiff  GMT difference at <b>regular time</b>.
     */
    public void setGmtDiff(double gmtDiff) {
        this.gmtDiff = gmtDiff;
    }
    
    public double getPressure() {
        return pressure;
    }
    
    /**
    * @param pressure Atmospheric pressure in millibars (the
    * astronomical standard value is 1010 (<code>Location.DEFAULT_PRESSURE</code>))
    */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    
    
    public double getSeaLevel() {
        return seaLevel;
    }
    
    /**
     * 
     * @param seaLevel Height above Sea level in meters
     */
    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    /**
     * 
     * @param temperature Temperature in Celsius degree (the 
     * astronomical standard value is 10)
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
