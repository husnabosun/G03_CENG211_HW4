package data.factory;

import data.models.Box;
import data.models.FixedBox;
import data.models.RegularBox;
import data.models.UnchangingBox;
import enums.Letter;
import tools.SpecialTool;
import java.util.*;

public class BoxFactory {

    private static final Random random = new Random();

    public static Box createRandomBox() {

        int chance = random.nextInt(100);
        List<Letter> faces = FaceFactory.createRandomFaces();

        if (chance < 85) {
            return createRegularBox(faces);
        }
        else if (chance < 95) {
            return createUnchangingBox(faces);
        }
        else {
            return createFixedBox(faces);
        }
    }

    private static Box createRegularBox(List<Letter> faces) {
        RegularBox box = new RegularBox(faces);
        SpecialTool tool = ToolFactory.createForRegularBox();
        box.setTool(tool);
        return box;
    }

    private static Box createUnchangingBox(List<Letter> faces) {
        UnchangingBox box = new UnchangingBox(faces);
        SpecialTool tool = ToolFactory.createForUnchangingBox();
        box.setTool(tool);
        return box;
    }
    private static Box createFixedBox(List<Letter> faces) {
        FixedBox box = new FixedBox(faces);
        box.setTool(null);          // always empty
        return box;
    }

}
