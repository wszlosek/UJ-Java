package uj.java.kindergarten;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Kindergarten {

    private static int childCount;
    private static final ArrayList<ChildImpl> children = new ArrayList<>();
    private static final ArrayList<Fork> forks = new ArrayList<>();
    private static BufferedReader reader;

    public static void main(String[] args) throws IOException {
        init();
        final var fileName = args[0];
        System.out.println("File name: " + fileName);
        //TODO: read children file, and keep children NOT hungry!

        readInformationFromFile(args[0]);
        childrenAssign();
    }

    private static void readInformationFromFile(String file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        childrenCount();
    }

    private static void childrenCount() throws IOException {
        childCount = Integer.parseInt(reader.readLine());
    }

    private static void childrenAssign() throws IOException {
        createForks();
        createChildren();
        initNeighbours();
        handOutForksToChildren();
        startAction();
    }

    private static void initNeighbours() {
        children.get(0).initNeighbour(
                children.get(childCount - 1), children.get(1)
        );
        for (int i = 1; i < childCount - 1; i++) {
            children.get(i).initNeighbour(
                    children.get(i - 1), children.get(i + 1)
            );
        }
        children.get(childCount - 1).initNeighbour(
                children.get(childCount - 2), children.get(0)
        );
    }

    private static void createForks() {
        for (int i = 0; i < childCount; i++) {
            forks.add(new Fork());
        }
    }

    private static void createChildren() throws IOException {
        for (int i = 0; i < childCount; i++) {
            String[] line = reader.readLine().split(" ");
            children.add(new ChildImpl(line[0], Integer.parseInt(line[1])));
        }
    }

    private static void handOutForksToChildren() {
        for (int i = 0; i < childCount; i++) {
            if (i % 2 == 0) {
                children.get(i).initForks(forks.get(i), forks.get((i + 1) % childCount));
            } else {
                children.get(i).initForks(forks.get((i + 1) % childCount), forks.get(i));
            }
        }
    }

    private static void startAction() {
        for (var child : children) {
            new Thread(child::doAction).start();
        }
    }

    private static void init() throws IOException {
        Files.deleteIfExists(Path.of("out.txt"));
        System.setErr(new PrintStream(new FileOutputStream("out.txt")));
        new Thread(Kindergarten::runKindergarten).start();
    }

    private static void runKindergarten() {
        try {
            Thread.sleep(10100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            List<String> errLines = Files.readAllLines(Path.of("out.txt"));
            System.out.println("Children cries count: " + errLines.size());
            errLines.forEach(System.out::println);
            System.exit(errLines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
