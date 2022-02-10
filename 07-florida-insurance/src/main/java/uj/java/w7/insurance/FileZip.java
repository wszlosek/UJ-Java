package uj.java.w7.insurance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

public class FileZip {
    private final String ZIPFILENAME = "FL_insurance.csv.zip";
    private final String CSVFILENAME = "FL_insurance.csv";

    public BufferedReader unzip() {
        try {
            ZipFile zipFile = new ZipFile(ZIPFILENAME);
            return new BufferedReader(
                    new InputStreamReader(
                            zipFile.getInputStream(zipFile.getEntry(CSVFILENAME))
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
