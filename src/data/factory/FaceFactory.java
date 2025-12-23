package data.factory;
import enums.Letter;
import java.util.*;

public class FaceFactory {
    public static final int INITIAL_FACE_CAPACITY = 6;
    private static final Random random = new Random();

    public static List<Letter> createRandomFaces() {
        List<Letter> faces = new ArrayList<>(INITIAL_FACE_CAPACITY);
        Map<Letter, Integer> counts = new HashMap<>();
        Letter[] letters = Letter.values();

        while (faces.size() < INITIAL_FACE_CAPACITY ) {
            Letter chosen = letters[random.nextInt(letters.length)];

            Integer usedCount = counts.get(chosen);

            if (usedCount == null) {
                faces.add(chosen);
                counts.put(chosen, 1);
            }
            else if (usedCount < 2) {
                faces.add(chosen);
                counts.put(chosen, usedCount + 1);
            }
        }
        return faces;
    }
}
