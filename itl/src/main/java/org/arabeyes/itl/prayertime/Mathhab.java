package org.arabeyes.itl.prayertime;

public enum Mathhab {
    /**
     * 1: Shaf'i (default)
     */
    SHAFII {
        @Override
        public int assrRatio() {
            return 1;
        }
    },
    /**
     * 2: Hanafi
     */
    HANAFI {
        @Override
        public int assrRatio() {
            return 2;
        }
    };

    public abstract int assrRatio();
}
