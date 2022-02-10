package uj.java.gvt;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Version {

    private final long VERSION_NUMBER;
    private final String VERSION_MESSAGE;
    private final ArrayList<String> FILES;

    public Version() {
        this.VERSION_NUMBER = 0;
        this.VERSION_MESSAGE = "";
        this.FILES = new ArrayList<>();
    }

    public Version(String versionMessage, ArrayList<String> files) {
        this.VERSION_NUMBER = -1;
        this.VERSION_MESSAGE = versionMessage;
        this.FILES = files;
    }

    public Version(long numberOfVersion, String versionMessage, ArrayList<String> files) {
        this.VERSION_NUMBER = numberOfVersion;
        this.VERSION_MESSAGE = versionMessage;
        this.FILES = files;
    }

    public void createVersionDirectory(Path pathToVersions) {
        try {
            Files.createDirectory(pathToVersions);
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    public void createFileDirectory(Path pathToVersions) {
        try {
            Files.createDirectory(pathToVersions.resolve(String.valueOf(VERSION_NUMBER)));
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    public void createVersionAboutFile(Path pathToVersions, Path pathToFileWithInfo) {
        try {
            Files.createFile(pathToVersions.resolve(String.valueOf(VERSION_NUMBER)).resolve(pathToFileWithInfo));
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    public void writeInAboutFile(Path pathToVersions, Path pathToFileWithInfo) {
        try (FileWriter writer = new FileWriter(String.valueOf(
                pathToVersions.resolve(String.valueOf(VERSION_NUMBER)).resolve(pathToFileWithInfo)
        ))) {
            writer.write(VERSION_MESSAGE + "\n");
            writer.write(FILES.toString() + "\n");
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    public boolean isFile(String filename) {
        return FILES.contains(filename);
    }

    public void addFile(String filename, Path pathToVersions, Path pathToFileWithInfo) {
        Path from = Paths.get(filename);
        Path to = Paths.get(String.valueOf(pathToVersions), String.valueOf(VERSION_NUMBER), filename);
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            writeSystemError(e);
        }
        writeInAboutFile(pathToVersions, pathToFileWithInfo);
    }

    public void initNewVersionFiles(Path pathToVersions, Path pathToFileWithInfo) {
        createFileDirectory(pathToVersions);
        createVersionAboutFile(pathToVersions, pathToFileWithInfo);
        writeInAboutFile(pathToVersions, pathToFileWithInfo);
    }

    private void writeSystemError(Exception e) {
        writeErrorMessage();
        e.printStackTrace();
        returnExitCode();
    }

    public long numberOfVersion() {
        return VERSION_NUMBER;
    }

    private void writeErrorMessage() {
        System.out.println("Underlying system problem. See ERR for details.");
    }

    private void returnExitCode() {
        System.exit(-3);
    }

    public String versionMessage() {
        return VERSION_MESSAGE;
    }

    public ArrayList<String> files() {
        return FILES;
    }

}
