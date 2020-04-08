package sw417f20.ebal.CodeGeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OutputFileGenerator {
    public ArrayList<File> Files;
    public OutputFileGenerator() {
        Files = new ArrayList<File>();
    }

    public OutputFileGenerator(ArrayList<String> fileContents) throws IOException {
        Files = new ArrayList<File>();
        String filePath = getFilePath();
        // Generate master file
        File masterFile = GenerateFile(filePath + "master", fileContents.get(0));
        Files.add(masterFile);

        for(int i = 1; i < fileContents.size(); i++) {
            String filename = filePath + "slave" + i;
            File slaveFile = GenerateFile(filename, fileContents.get(i));
            Files.add(slaveFile);
        }
    }

    /**
     *
     * @param filename Name or absolute path of the file
     * @param content Content of the file
     * @throws IOException if an error occurred during IO
     */
    public void AddFile(String filename, String content) throws IOException {
        boolean isFilePath = new File(filename).isAbsolute();
        File file;
        if(isFilePath) {
            file = GenerateFile(filename, content);
        }
        else {
            String filePath = getFilePath() + filename;
            file = GenerateFile(filePath, content);
        }
        Files.add(file);
    }

    /**
     *
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
     * @return String
     */
    private String getFilePath() {
        return new File("").getAbsolutePath() + "/TestFiles/";
    }
}
