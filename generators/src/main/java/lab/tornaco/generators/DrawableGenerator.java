package lab.tornaco.generators;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;

public class DrawableGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static final String INPUT_PATH = PROJECT_DIR + "/comb/src/main/res/drawable";
    private static final String OUTPUT_JAVA_PATH = PROJECT_DIR + "/comb/src/main/java/github/tornaco/practice/honeycomb/core/server/build/Drawables.java";

    public static void main(String... args) throws DocumentException, IOException {
        createFile();
        writeHeader();
        writeVars();
        writeQuota();
    }

    private static void createFile() throws IOException {
        Files.createParentDirs(new File(OUTPUT_JAVA_PATH));
        new File(OUTPUT_JAVA_PATH).delete();
        new File(OUTPUT_JAVA_PATH).createNewFile();
    }

    private static void writeHeader() throws IOException {
        String header = "package github.tornaco.practice.honeycomb.core.server.build;\n" +
                "\n" +
                "public class Drawables {\n";
        Files.asByteSink(new File(OUTPUT_JAVA_PATH), FileWriteMode.APPEND)
                .write(header.getBytes());
    }

    private static void writeVars() throws IOException {
        File dir = new File(INPUT_PATH);
        File[] drawables = dir.listFiles();
        for (File file : drawables) {
            String line = String.format("    public static final String %s = \"%s\";\n", Files.getNameWithoutExtension(file.getPath()).toUpperCase(), Files.getNameWithoutExtension(file.getPath()));
            Files.asByteSink(new File(OUTPUT_JAVA_PATH), FileWriteMode.APPEND)
                    .write(line.getBytes());
        }
    }

    private static void writeQuota() throws IOException {
        Files.asByteSink(new File(OUTPUT_JAVA_PATH), FileWriteMode.APPEND)
                .write("}".getBytes());
    }
}
