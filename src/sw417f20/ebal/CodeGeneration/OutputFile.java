package sw417f20.ebal.CodeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {

    public File OutputFile;

    public OutputFile(String filepath, String content) throws IOException {
        File OutputFile = CreateFile(filepath);
        FileWriter fileWriter = new FileWriter(OutputFile);
        fileWriter.write(content);
    }

    private File CreateFile(String filepath) throws IOException {
        File outputFile;

        if(!filepath.contains(".ino")) {
            if(filepath.contains(".")) {
                //TODO: Handle that filepath has a different file extension than ".ino"
            }
            else {
                filepath += ".ino";
            }
        }
        outputFile = new File(filepath);
        boolean fileExists = outputFile.createNewFile();
        if(fileExists) {
            // This does not currently have any effect, but could be used to check if user truly wants to overwrite
            System.out.println("File: " + filepath + " already exists");
        }
        else {
            System.out.println("File: " + filepath + " created");
        }
        return outputFile;
    }
}
