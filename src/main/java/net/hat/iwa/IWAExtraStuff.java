package net.hat.iwa;

public class IWAExtraStuff {

    public static float getWaveArmorChances(int waves) {
        switch (waves) {
            case 0:
                return 0.30F;
            case 1:
                return 0.32F;
            case 2:
                return 0.34F;
            case 3:
                return 0.36F;
            case 4:
                return 0.38F;
            case 5:
                return 0.40F;
            case 6:
                return 0.42F;
            case 7:
                return 0.48F;
        }
        return 0;
    }
}
