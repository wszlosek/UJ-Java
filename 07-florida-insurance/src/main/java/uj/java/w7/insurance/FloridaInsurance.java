package uj.java.w7.insurance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FloridaInsurance {

    private static BufferedReader reader;
    private final static String COUNT = "count.txt";
    private final static String MOST_VALUABLE = "most_valuable.txt";
    private final static String TIV_2012 = "tiv2012.txt";

    public static void main(String[] args) {
        var f = new FileZip();
        reader = f.unzip();
        var lines = dataFromFile();

        countCountry(lines);
        sumInsurance(lines);
        mostValuable(lines);
    }

    private static List<InsuranceEntry> dataFromFile() {
        return reader.lines().skip(1).map(element -> createInsuranceEntry(element.split(","))).toList();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private static InsuranceEntry createInsuranceEntry(String[] el) {
        return new InsuranceEntry(el[0], el[1], el[2], el[3], el[4], el[5], el[6], new BigDecimal(el[7]),
                new BigDecimal(el[8]), el[9], el[10], el[11], el[12], el[13], el[14], el[15], el[16], el[17]
        );
    }

    private static void countCountry(List<InsuranceEntry> lines) {
        var result = lines
                .stream()
                .filter(distinctByKey(InsuranceEntry::county))
                .count();

        writeInFile(COUNT, "", String.valueOf(result));
    }

    private static void sumInsurance(List<InsuranceEntry> lines) {
        var result = lines
                .stream()
                .map(InsuranceEntry::tiv2012)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        writeInFile(TIV_2012, "", result.toString());
    }

    private static void mostValuable(List<InsuranceEntry> lines) {
        var result = new StringBuilder();
        lines.stream()
                .map(el -> new Pair(el.county(), el.tiv2012().subtract(el.tiv2011())))
                .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(10)
                .forEach(
                        i -> result
                                .append(i.getKey())
                                .append(",")
                                .append(i.getValue().toString())
                                .append("\n")
                );

        writeInFile(MOST_VALUABLE, "country,value", result.toString());
    }

    private static void writeInFile(String fileName, String heading, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            if (!heading.isEmpty()) {
                writer.append(heading).append("\n");
            }

            writer.append(text.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
