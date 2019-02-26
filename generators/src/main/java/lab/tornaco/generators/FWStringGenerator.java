package lab.tornaco.generators;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FWStringGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static final String INPUT_XML_PATH = PROJECT_DIR + "/comb/src/main/res/values/fw-strings.xml";
    private static final String OUTPUT_XML_PATH = PROJECT_DIR + "/comb/src/main/java/github/tornaco/practice/honeycomb/core/server/build/FWStrings.java";

    public static void main(String... args) throws DocumentException, IOException {
        createFile();
        writeHeader();
        writeVars();
        writeQuota();
    }

    private static void createFile() throws IOException {
        Files.createParentDirs(new File(OUTPUT_XML_PATH));
        new File(OUTPUT_XML_PATH).delete();
        new File(OUTPUT_XML_PATH).createNewFile();
    }

    private static void writeHeader() throws IOException {
        String header = "package github.tornaco.practice.honeycomb.core.server.build;\n" +
                "\n" +
                "public class FWStrings {\n";
        Files.asByteSink(new File(OUTPUT_XML_PATH), FileWriteMode.APPEND)
                .write(header.getBytes());
    }

    private static void writeVars() throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(INPUT_XML_PATH));
        Element resourceElement = document.getRootElement();
        List elements = resourceElement.elements();
        for (Object element : elements) {
            String resKey = ((Element) element).attribute("name").getValue();
            String line = String.format("public static final String %s = \"%s\";", resKey.toUpperCase(), resKey);
            Files.asByteSink(new File(OUTPUT_XML_PATH), FileWriteMode.APPEND)
                    .write("    ".getBytes());
            Files.asByteSink(new File(OUTPUT_XML_PATH), FileWriteMode.APPEND)
                    .write(line.getBytes());
            Files.asByteSink(new File(OUTPUT_XML_PATH), FileWriteMode.APPEND)
                    .write("\n".getBytes());
        }
    }

    private static void writeQuota() throws IOException {
        Files.asByteSink(new File(OUTPUT_XML_PATH), FileWriteMode.APPEND)
                .write("}".getBytes());
    }
}
