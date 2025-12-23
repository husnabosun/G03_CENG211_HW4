package data.factory;
import tools.*;
import java.util.Random;

public class ToolFactory {

    private static final Random random = new Random();

    // RegularBox: for every tool %15 possibility rest of them are null
    public static SpecialTool createForRegularBox() {
        int randInt = random.nextInt(100); // 0..99
        switch (randInt / 15) {
            case 0:  // 0–14
                return new PlusShapeStamp();
            case 1:  // 15–29
                return new MassRowStamp();
            case 2:  // 30–44
                return new MassColumnStamp();
            case 3:  // 45–59
                return new BoxFlipper();
            case 4:  // 60–74
                return new BoxFixer();
            default: // 75–99
                return null; // %25 boş
        }
    }

    // UnchangingBox: for every tool % 20 possibility
    public static SpecialTool createForUnchangingBox() {
        int randInt = random.nextInt(100);

        switch (randInt / 20) {
            case 0:  // 0–19
                return new PlusShapeStamp();
            case 1:  // 20–39
                return new MassRowStamp();
            case 2:  // 40–59
                return new MassColumnStamp();
            case 3:  // 60–79
                return new BoxFlipper();
            default: // 80–99
                return new BoxFixer();
        }
    }

    // FixedBox: %0 tool
    public static SpecialTool createForFixedBox() {
        return null;
    }
}