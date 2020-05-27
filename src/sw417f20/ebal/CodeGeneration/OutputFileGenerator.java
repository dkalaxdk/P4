package sw417f20.ebal.CodeGeneration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class OutputFileGenerator {

    //TODO: Refactor to use a stream/StreamWriter, then make wrapper class
    /**
     * Generates a single master file, and zero or more slave files
     * @param files HashMap of strings, each entry should be the name and content of a single file
     * @throws IOException if an error occurred during IO
     */
    public OutputFileGenerator(HashMap<String, String> files, String sourceFile) throws IOException {
        String filePath = GetFilePath() + sourceFile + "/";

        File directory = new File(filePath);
        directory.mkdirs();

        for (String name : files.keySet()) {
            String content = files.get(name);

            String path = filePath + name + "/";
            File file = new File(path);
            file.mkdirs();

            GenerateFile(path + name, content);
        }
    }

    /**
     * Generates file with the given content at given filepath
     * @param filePath Absolute path of the file
     * @param content Content of the file
     * @return File at the given path with the given path
     * @throws IOException if an error occurred during IO
     */
    public File GenerateFile(String filePath, String content) throws IOException {
        OutputFile outputFile = new OutputFile(filePath, content);
        return outputFile.OutputFile;
    }

    /**
     * Returns the path of the folder where generated files are placed
     * @return String containing the resulting path
     */
    private String GetFilePath() {
        return new File("").getAbsolutePath() + "/EBALArduino/";
    }
}
