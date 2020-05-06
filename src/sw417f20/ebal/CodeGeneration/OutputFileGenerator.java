package sw417f20.ebal.CodeGeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class OutputFileGenerator {
    public ArrayList<File> Files;

    private String filePath;

    /**
     * Use this constructor, if the content or path of the files
     * is not known, when creating this class.
     *
     * To add files, use the AddFile method.
     */
//    public OutputFileGenerator() {
//        Files = new ArrayList<File>();
//        filePath = getFilePath();
//    }

    //TODO: Refactor to use a stream/StreamWriter, then make wrapper class
    /**
     * Generates a single master file, and zero or more slave files
     * @param fileContents ArrayList of strings, each string should be the content of a single file
     * @throws IOException if an error occurred during IO
     */
//    public OutputFileGenerator(ArrayList<String> fileContents) throws IOException {
//        Files = new ArrayList<File>();
//        String filePath = getFilePath();
//        // Generate master file
//        File masterFile = GenerateFile(filePath + "master", fileContents.get(0));
//        Files.add(masterFile);
//        // Generate slave files
//        for(int i = 1; i < fileContents.size(); i++) {
//            String filename = filePath + "slave" + i;
//            File slaveFile = GenerateFile(filename, fileContents.get(i));
//            Files.add(slaveFile);
//        }
//    }

    public OutputFileGenerator(HashMap<String, String> files, String sourceFile) {
        String filePath = getFilePath() + sourceFile + "/";

        File directory = new File(filePath);
        directory.mkdirs();

        for (String name : files.keySet()) {
            String content = files.get(name);

            String path = filePath + name + "/";
            File file = new File(path);
            file.mkdirs();

            path += name;

            try {
                GenerateFile(path, content);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add a file to the list of files.
     * Use to add files after construction of the class.
     * @param filename Name or absolute path of the file
     * @param content Content of the file
     * @throws IOException if an error occurred during IO
     */
//    public void AddFile(String filename, String content) throws IOException {
//        // Check if the given file path is an absolute file path
//        boolean isFilePath = new File(filename).isAbsolute();
//        File file;
//        if(isFilePath) {
//            file = GenerateFile(filename, content);
//        }
//        else {
//            // if path is a filename, rather than an absolute path,
//            // make it into an absolute path.
//            String filePath = getFilePath() + filename + "/";
//
//            File directory = new File(filePath);
//            directory.mkdirs();
//
//            file = GenerateFile(filePath + filename, content);
//        }
//        Files.add(file);
//    }

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
    private String getFilePath() {
        return new File("").getAbsolutePath() + "/EBALArduino/";
    }
}
