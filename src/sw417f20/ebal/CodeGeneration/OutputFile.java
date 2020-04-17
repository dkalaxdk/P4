package sw417f20.ebal.CodeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {

    public File OutputFile;

    /**
     * Creates a new file at the given file path, then writes the given content to the file.
     * @param filepath Path of the file
     * @param content Content of the file
     * @throws IOException
     */
    public OutputFile(String filepath, String content) throws IOException {
        File OutputFile = CreateFile(filepath);
        FileWriter fileWriter = new FileWriter(OutputFile);
        fileWriter.write(content);
        fileWriter.close();
    }

    /**
     * Creates a new file a the given file path, adds the extension '.ino'
     * if it is not already present.
     *
     * @param filepath Path of the file
     * @return File at the given file path
     * @throws IOException
     */
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
        boolean fileCreated = outputFile.createNewFile();
        if(fileCreated) {
            System.out.println("File: " + filepath + " created");
        }
        else {
            // This does not currently have any effect, but could be used to check if user truly wants to overwrite
            System.out.println("File: " + filepath + " already exists");
        }
        return outputFile;
    }
}
