package org.arabeyes.itl.newmethod;

public class CalcMethod {
    /* In original C: The order of struct members below guarantees
     * that the compiler doesn't add padding bytes automatically
     * on 64-bit architectures */

    MethodId id;         /* Method ID */
    String name;   /* Full name of the method */
    IshaFlag isha_type;  /* Angle or offset? */
    double fajr;       /* Fajr angle */
    double isha;       /* Value for Isha angle/offset */

    CalcMethod(MethodId id, String name, IshaFlag isha_type,
               double fajr, double isha) {
        this.id = id;
        this.name = name;
        this.isha_type = isha_type;
        this.fajr = fajr;
        this.isha = isha;
    }

    public CalcMethod(CalcMethod src) {
        this.id = src.id;
        this.name = src.name;
        this.isha_type = src.isha_type;
        this.fajr = src.fajr;
        this.isha = src.isha;
    }

    public static CalcMethod fromStandard(MethodId method) {
        for (CalcMethod i : PrayerModule.calc_methods) {
            if (i.id == method) {
                return new CalcMethod(i); // copy
            }
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }

    public MethodId getId() {
        return id;
    }

    public CalcMethod setId(MethodId id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CalcMethod setName(String name) {
        this.name = name;
        return this;
    }

    public IshaFlag getIshaType() {
        return isha_type;
    }

    public CalcMethod setIshaType(IshaFlag ishaType) {
        this.isha_type = ishaType;
        return this;
    }

    public double getFajr() {
        return fajr;
    }

    public CalcMethod setFajr(double fajr) {
        this.fajr = fajr;
        return this;
    }

    public double getIsha() {
        return isha;
    }

    public CalcMethod setIsha(double isha) {
        this.isha = isha;
        return this;
    }
}
