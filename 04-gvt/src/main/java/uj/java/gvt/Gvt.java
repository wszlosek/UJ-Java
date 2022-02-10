package uj.java.gvt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class Gvt {

    private static Operation operation;
    private static final Path GVT_EXTENSION = Path.of(".gvt");
    private static final Path PATH_TO_VERSIONS = Path.of(String.valueOf(GVT_EXTENSION), "versions");
    private static final Path PATH_TO_CURRENT_VERSION_NUMBER = Path.of(String.valueOf(PATH_TO_VERSIONS), "currentVersionNumber.txt");
    private static final Path FILE_WITH_INFO = Path.of("aboutVersion.txt");
    private static long currentVersionNumber;
    private static ArrayList<String> currentFiles;

    private static Operation operationFromCommand(String command) {
        return Operation.valueOf(command.toUpperCase());
    }

    public static void main(String... args) {
        if (args.length == 0) {
            writeErrorMessage("Please specify command.");
            returnExitCode(1);
        }

        currentFiles = new ArrayList<>();
        operation = operationFromCommand(args[0]);

        switch (operation) {
            case INIT -> init();
            case ADD -> add(args);
            case COMMIT -> commit(args);
            case DETACH -> detach(args);
            case VERSION -> version(args);
            case HISTORY -> history(args);
            case CHECKOUT -> checkout(args);
            default -> {
                writeErrorMessage("Unknown command + " + args[0] + ".");
                returnExitCode(1);
            }
        }
    }

    private static String stringConcatenation(String... args) {
        var result = new StringBuilder();
        for (var s : args) {
            result.append(s);
        }
        return result.toString();
    }

    private static void createVersionNumberFile() {
        try {
            Files.createFile(PATH_TO_CURRENT_VERSION_NUMBER);
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    private static void updateVersionNumberFile(long newVersionNumber) {
        try (var writer = new FileWriter(String.valueOf(PATH_TO_CURRENT_VERSION_NUMBER))) {
            writer.write(String.valueOf(newVersionNumber));
        } catch (IOException e) {
            writeSystemError(e);
        }
        synchronizeVariableAndVersionFile();
    }

    private static void synchronizeVariableAndVersionFile() {
        try (var fileWriter = new BufferedReader(new FileReader(String.valueOf(PATH_TO_CURRENT_VERSION_NUMBER)))) {
            currentVersionNumber = Long.parseLong(fileWriter.readLine());
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

    private static void initVersionNumberFile() {
        createVersionNumberFile();
        updateVersionNumberFile(0L);
    }

    private static void judgeInitialization() {
        if (!directoryIsInitialized()) {
            writeErrorMessage("Current directory is not initialized. Please use \"init\" command to initialize.");
            returnExitCode(-2);
        }
        synchronizeVariableAndVersionFile();
    }

    private static void judgeArrayLength(String[] args, int exitCode) {
        if (args.length < 2) {
            writeErrorMessage(stringConcatenation("Please specify file to ", operation.commandText(), "."));
            returnExitCode(exitCode);
        }
    }

    private static void judgeFileExistence(String filename, int exitCode) {
        if (!fileExists(filename)) {
            writeErrorMessage(stringConcatenation("File ", filename, " not found."));
            returnExitCode(exitCode);
        }
    }

    private static void writeSystemError(Exception e) {
        writeOtherProblem(e, "Underlying system problem. See ERR for details.", -3);
    }

    private static void writeErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    private static void returnExitCode(int exitCode) {
        System.exit(exitCode);
    }

    private static void writeSuccess(String prefix, String fileName, String action) {
        if (!fileName.isEmpty()) {
            System.out.println(stringConcatenation(prefix, " ", fileName, " ", action, " successfully."));
        } else {
            System.out.println(stringConcatenation(prefix, " ", action, " successfully."));
        }
    }

    private static void writeOtherProblem(Exception e, String message, int exitCode) {
        writeErrorMessage(message);
        e.printStackTrace();
        returnExitCode(exitCode);
    }

    private static void initNewVersion() {
        String message = "GVT initialized.\n";
        var newVersion = new Version(currentVersionNumber, message, new ArrayList<>());
        newVersion.createVersionDirectory(PATH_TO_VERSIONS);
        newVersion.initNewVersionFiles(PATH_TO_VERSIONS, FILE_WITH_INFO);
        initVersionNumberFile();
    }

    private static void init() {
        if (directoryIsInitialized()) {
            writeErrorMessage("Current directory is already initialized.");
            returnExitCode(10);
        }

        try {
            Files.createDirectory(GVT_EXTENSION);
            initNewVersion();
            writeSuccess("Current directory", "", "initialized");
        } catch (IOException e) {
            writeOtherProblem(e, "Underlying system problem. See ERR for details.", -3);
        }
    }

    private static boolean directoryIsInitialized() {
        return Files.exists(GVT_EXTENSION);
    }

    private static boolean fileExists(String fileName) {
        return Files.exists(Path.of(fileName));
    }

    private static void initNewVersionAfterUpdates(String filename, String message, ArrayList<String> files, String deletedFile) {
        var newVersion = new Version(currentVersionNumber, message, files);
        newVersion.initNewVersionFiles(PATH_TO_VERSIONS, FILE_WITH_INFO);
        newVersion.addFile(filename, PATH_TO_VERSIONS, FILE_WITH_INFO);
        addFilesFromPreviousVersion(currentVersionNumber, deletedFile);
    }

    private static void add(String[] args) {
        judgeInitialization();
        judgeArrayLength(args, 20);
        judgeFileExistence(args[1], 21);

        try {
            var currentVersion = importVersion(currentVersionNumber);
            if (currentVersion.isFile(args[1])) {
                writeErrorMessage(stringConcatenation("File ", args[1], " already added."));
                return;
            }
            updateCurrentFiles(currentVersion, args[1]);
            updateVersionNumberFile(currentVersionNumber + 1);

            String message = stringConcatenation("Added file: ", args[1], "\n", messageFromCommand(args));
            initNewVersionAfterUpdates(args[1], message, currentFiles, "");
            writeSuccess("File", args[1], "added");

        } catch (Exception e) {
            writeOtherProblem(e, stringConcatenation("File ", args[1], " cannot be added, see ERR for details."), 22);
        }
    }

    private static String importMessage(long numberOfVersion) {
        Path fromPath = PATH_TO_VERSIONS.resolve(String.valueOf(numberOfVersion)).resolve(FILE_WITH_INFO);
        String result;

        try (var fileReader = new BufferedReader(new FileReader(fromPath.toString()))) {
            result = stringConcatenation(fileReader.readLine(), "\n", fileReader.readLine());
        } catch (IOException e) {
            result = "";
        }

        return result;
    }

    private static String getNthLineFromFile(int n, BufferedReader fileReader) throws IOException {
        while (n >= 1) {
            fileReader.readLine();
            n -= 1;
        }
        return fileReader.readLine();
    }

    private static ArrayList<String> importListOfFiles(long numberOfVersion) {
        Path fromPath = PATH_TO_VERSIONS.resolve(String.valueOf(numberOfVersion)).resolve(FILE_WITH_INFO);
        String result;
        try (var fileReader = new BufferedReader(new FileReader(fromPath.toString()))) {
            result = getNthLineFromFile(2, fileReader);
        } catch (IOException e) {
            result = "";
        }

        return stringToArrayList(result);
    }

    private static Version importVersion(long numberOfVersion) throws FileNotFoundException {
        return new Version(
                importMessage(numberOfVersion),
                importListOfFiles(numberOfVersion)
        );
    }

    private static ArrayList<String> stringToArrayList(String stringList) {
        if (stringList.equals("[]")) {
            return new ArrayList<>();
        }
        stringList = stringList.replace("[", "").replace("]", "");
        return new ArrayList<>(Arrays.asList(stringList.split(", ")));
    }

    private static String messageFromCommand(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-m")) {
                return args[i + 1];
            }
        }
        return "";
    }

    private static void commit(String[] args) {
        judgeInitialization();
        judgeArrayLength(args, 50);
        judgeFileExistence(args[1], 51);

        try {
            var currentVersion = importVersion(currentVersionNumber);
            if (!currentVersion.isFile(args[1])) {
                writeErrorMessage(stringConcatenation("File ", args[1], " is not added to gvt."));
                return;
            }

            updateVersionNumberFile(currentVersionNumber + 1);
            String message = stringConcatenation("Committed file: ", args[1], "\n", messageFromCommand(args));
            initNewVersionAfterUpdates(args[1], message, currentVersion.files(), "");
            writeSuccess("File", args[1], "committed");

        } catch (Exception e) {
            writeOtherProblem(e, stringConcatenation("File ", args[1], " cannot be committed, see ERR for details."), -52);
        }
    }

    private static void detach(String[] args) {
        judgeInitialization();
        judgeArrayLength(args, 30);

        try {
            var currentVersion = importVersion(currentVersionNumber);
            if (!currentVersion.isFile(args[1])) {
                writeErrorMessage(stringConcatenation("File ", args[1], " is not added to gvt."));
                return;
            }

            updateVersionNumberFile(currentVersionNumber + 1);
            String message = stringConcatenation("Detached file: ", args[1], "\n", messageFromCommand(args));
            initNewVersionAfterUpdates(args[1], message, removeFileFromList(args[1]), args[1]);

            writeSuccess("File", args[1], "detached");

        } catch (FileNotFoundException e) {
            writeOtherProblem(e, stringConcatenation("File ", args[1], " cannot be detached, see ERR for details."), 31);
        }
    }

    private static ArrayList<String> removeFileFromList(String filename) {
        var result = new ArrayList<String>();
        for (var s : currentFiles) {
            if (!s.equals(filename)) {
                result.add(s);
            }
        }
        return result;
    }

    private static void version(String[] args) {
        judgeInitialization();
        if (args.length > 1 &&
                (isNotNumeric(args[1]) || Long.parseLong(args[1]) < 0 || Long.parseLong(args[1]) > currentVersionNumber)) {
            writeErrorMessage(stringConcatenation("Invalid version number: ", args[1]));
            returnExitCode(60);
        }

        long versionNumber = (args.length < 2) ? currentVersionNumber : Long.parseLong(args[1]);
        try {
            System.out.print(
                    stringConcatenation("Version: ", String.valueOf(versionNumber), "\n", importVersion(versionNumber).versionMessage())
            );
        } catch (FileNotFoundException e) {
            writeSystemError(e);
        }
    }

    private static boolean isNotNumeric(String suspect) {
        return !suspect.matches("-?\\d+(\\.\\d+)?");
    }

    private static void history(String[] args) {
        judgeInitialization();
        long startIndex = (args.length > 2 && args[1].equals("-last"))
                ? currentVersionNumber - Long.parseLong(args[2]) + 1
                : 0;

        for (long i = startIndex; i <= currentVersionNumber; i++) {
            try {
                System.out.println(stringConcatenation(String.valueOf(i), ": ", firstLineOfMessage(importVersion(i).versionMessage())));
            } catch (FileNotFoundException e) {
                writeSystemError(e);
            }
        }
    }

    private static String firstLineOfMessage(String message) {
        return message.split("\n", 2)[0];
    }

    private static void checkout(String[] args) {
        judgeInitialization();
        if (isNotNumeric(args[1]) || Long.parseLong(args[1]) < 0 || Long.parseLong(args[1]) > currentVersionNumber) {
            writeErrorMessage(stringConcatenation("Invalid version number: ", args[1], "."));
            returnExitCode(40);
        }

        long versionNumber = Long.parseLong(args[1]);
        var srcVersion = new Version();
        try {
            srcVersion = importVersion(versionNumber);
        } catch (FileNotFoundException e) {
            writeSystemError(e);
        }

        srcVersion.files().forEach(file -> {
            Path from = Paths.get(String.valueOf(PATH_TO_VERSIONS), String.valueOf(versionNumber), file);
            Path to = Paths.get(file);
            copyFileFromTo(from, to);
        });

        writeSuccess("Version", String.valueOf(versionNumber), "checked out");
    }

    private static void addFilesFromPreviousVersion(long currentVersionNumber, String deletedFile) {
        long prevVersionNumber = currentVersionNumber - 1;
        var prevVersion = new Version();
        try {
            prevVersion = importVersion(prevVersionNumber);
        } catch (FileNotFoundException e) {
            writeSystemError(e);
        }

        prevVersion.files().forEach(file -> {
            var from = Paths.get(String.valueOf(PATH_TO_VERSIONS), String.valueOf(prevVersionNumber), file);
            var to = Paths.get(String.valueOf(PATH_TO_VERSIONS), String.valueOf(currentVersionNumber), file);
            if (!file.equals(deletedFile) && !Files.exists(to)) {
                copyFileFromTo(from, to);
            }
        });
    }

    private static void updateCurrentFiles(Version currentVersion, String deletedElement) {
        currentFiles = currentVersion.files();
        if (!currentFiles.contains(deletedElement)) {
            currentFiles.add(deletedElement);
        }
    }

    private static void copyFileFromTo(Path from, Path to) {
        try {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            writeSystemError(e);
        }
    }

}
