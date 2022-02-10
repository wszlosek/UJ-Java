package uj.java.pwj2020.spreadsheet;

import java.util.Arrays;

public class Spreadsheet {

    private Cell[][] cells;
    private int width;
    private int height;

    private void initializeSpreadsheet(String[][] input) {
        this.width = input[0].length;
        this.height = input.length;
        cells = new Cell[this.height][this.width];
    }

    private void initializeCells(String[][] input) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                cells[i][j] = new Cell(input[i][j]);
            }
        }
    }

    private Cell changeReference(Cell cell) {
        while (cell.status == Cell.Status.REFERENCE) {
            int indexOfColumn = cell.characterToColumnNumber();
            int indexOfLine = Integer.parseInt(cell.value.substring(2)) - 1;
            cell = cells[indexOfLine][indexOfColumn];
        }
        return cell;
    }

    private void changeReferencesOnSpreadsheet() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                cells[i][j] = changeReference(cells[i][j]);
            }
        }
    }

    private String[] getArgumentsFromFormula(String formula) {
        String[] arguments = formula.substring(4)
                .replaceAll("[ ()]", "")
                .split(",");

        Cell[] argCells = {new Cell(arguments[0]), new Cell(arguments[1])};

        for (int i = 0; i < arguments.length; i++) {
            argCells[i] = changeReference(argCells[i]);
            arguments[i] = argCells[i].value;
        }
        return arguments;
    }

    private String changeFormula(Cell cell) {
        if (cell.status == Cell.Status.FORMULA) {
            String operation = cell.getOperationFromFormula();
            String[] arguments = getArgumentsFromFormula(cell.value);
            int firstArgument = Integer.parseInt(arguments[0]);
            int secondArgument = Integer.parseInt(arguments[1]);
            return String.valueOf(solveFormula(firstArgument, secondArgument, operation));
        }
        return cell.value;
    }

    private void changeFormulasOnSpreadsheet() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
               cells[i][j].value = changeFormula(cells[i][j]);
            }
        }
    }

    private int solveFormula(int firstArgument, int secondArgument, String operation) {
        return switch (operation) {
            case "ADD" -> firstArgument + secondArgument;
            case "SUB" -> firstArgument - secondArgument;
            case "MUL" -> firstArgument * secondArgument;
            case "DIV" -> firstArgument / secondArgument;
            case "MOD" -> firstArgument % secondArgument;
            default -> 0;
        };
    }

    private String[][] cellArrayToStringArray(String[][] stringArray) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                stringArray[i][j] = cells[i][j].value;
            }
        }
        return stringArray;
    }

    public String[][] calculate(String[][] input) {
        initializeSpreadsheet(input);
        initializeCells(input);
        changeReferencesOnSpreadsheet();
        changeFormulasOnSpreadsheet();
        return cellArrayToStringArray(input);
    }

    @Override
    public String toString() {
        return "Spreadsheet{" +
                "cells=" + Arrays.toString(cells) +
                '}';
    }
}
