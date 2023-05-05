package api.base.helpers;

import api.base.reporter.ExtentLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
This class is used to read json files.
 */
public class FileReader {
    String json;

    public String getTestJsonFile(String path) {
        return readJsonFile(path);
    }

    private String readJsonFile(String path) {
        try {
            json = Files.readString(Paths.get(path));
        } catch (IOException e)
        {
            ExtentLogger.fail("Exception: " + e.toString());
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.toString());
        }
        return json;
    }
}