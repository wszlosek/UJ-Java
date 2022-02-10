package uj.pwj2020.introduction;

public class Banner {

    public String[] toAscii(char letter) {

        String[] result = switch (letter) {
            case 'a', 'A' -> new String[]{
                    "   #   ",
                    "  # #  ",
                    " #   # ",
                    "#     #",
                    "#######",
                    "#     #",
                    "#     #"
            };

            case 'b', 'B' -> new String[]{
                    " ######  ",
                    " #     # ",
                    " #     # ",
                    " ######  ",
                    " #     # ",
                    " #     # ",
                    " ######  "
            };

            case 'c', 'C' -> new String[]{
                    " #####  ",
                    "#     # ",
                    "#       ",
                    "#       ",
                    "#       ",
                    "#     # ",
                    " #####  "
            };

            case 'd', 'D' -> new String[]{
                    "###### ",
                    "#     #",
                    "#     #",
                    "#     #",
                    "#     #",
                    "#     #",
                    "###### "
            };

            case 'e', 'E' -> new String[]{
                    " ####### ",
                    " #       ",
                    " #       ",
                    " #####   ",
                    " #       ",
                    " #       ",
                    " ####### "
            };

            case 'f', 'F' -> new String[]{
                    "#######",
                    "#      ",
                    "#      ",
                    "#####  ",
                    "#      ",
                    "#      ",
                    "#      "
            };

            case 'g', 'G' -> new String[]{
                    "  #####  ",
                    " #     # ",
                    " #       ",
                    " #  #### ",
                    " #     # ",
                    " #     # ",
                    "  #####  "
            };

            case 'h', 'H' -> new String[]{
                    "#     #",
                    "#     #",
                    "#     #",
                    "#######",
                    "#     #",
                    "#     #",
                    "#     #"
            };

            case 'i', 'I' -> new String[]{
                    " ### ",
                    "  #  ",
                    "  #  ",
                    "  #  ",
                    "  #  ",
                    "  #  ",
                    " ### "
            };

            case 'j', 'J' -> new String[]{
                    "      # ",
                    "      # ",
                    "      # ",
                    "      # ",
                    "#     # ",
                    "#     # ",
                    " #####  "
            };

            case 'k', 'K' -> new String[]{
                    "#    #",
                    "#   # ",
                    "#  #  ",
                    "###   ",
                    "#  #  ",
                    "#   # ",
                    "#    #"
            };

            case 'l', 'L' -> new String[]{
                    " #       ",
                    " #       ",
                    " #       ",
                    " #       ",
                    " #       ",
                    " #       ",
                    " ####### "
            };

            case 'm', 'M' -> new String[]{
                    "#     # ",
                    "##   ## ",
                    "# # # # ",
                    "#  #  # ",
                    "#     # ",
                    "#     # ",
                    "#     # "
            };

            case 'n', 'N' -> new String[]{
                    "#     #",
                    "##    #",
                    "# #   #",
                    "#  #  #",
                    "#   # #",
                    "#    ##",
                    "#     #"
            };

            case 'o', 'O' -> new String[]{
                    " ####### ",
                    " #     # ",
                    " #     # ",
                    " #     # ",
                    " #     # ",
                    " #     # ",
                    " ####### "
            };

            case 'u', 'U' -> new String[]{
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    " #####  "
            };

            case 'p', 'P' -> new String[]{
                    "######  ",
                    "#     # ",
                    "#     # ",
                    "######  ",
                    "#       ",
                    "#       ",
                    "#       "
            };

            case 'q', 'Q' -> new String[]{
                    " #####  ",
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    "#   # # ",
                    "#    #  ",
                    " #### # "
            };

            case 'r', 'R' -> new String[]{
                    "######  ",
                    "#     # ",
                    "#     # ",
                    "######  ",
                    "#   #   ",
                    "#    #  ",
                    "#     # "
            };

            case 's', 'S' -> new String[]{
                    " #####  ",
                    "#     # ",
                    "#       ",
                    " #####  ",
                    "      # ",
                    "#     # ",
                    " #####  "
            };

            case 't', 'T' -> new String[]{
                    "####### ",
                    "   #    ",
                    "   #    ",
                    "   #    ",
                    "   #    ",
                    "   #    ",
                    "   #    "
            };

            case 'w', 'W' -> new String[]{
                    "#     # ",
                    "#  #  # ",
                    "#  #  # ",
                    "#  #  # ",
                    "#  #  # ",
                    "#  #  # ",
                    " ## ##  "
            };

            case 'v', 'V' -> new String[]{
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    "#     # ",
                    " #   #  ",
                    "  # #   ",
                    "   #    "
            };

            case 'x', 'X' -> new String[]{
                    "#     # ",
                    " #   #  ",
                    "  # #   ",
                    "   #    ",
                    "  # #   ",
                    " #   #  ",
                    "#     # "
            };

            case 'y', 'Y' -> new String[]{
                    "#     # ",
                    " #   #  ",
                    "  # #   ",
                    "   #    ",
                    "   #    ",
                    "   #    ",
                    "   #    "
            };

            case 'z', 'Z' -> new String[]{
                    "####### ",
                    "     #  ",
                    "    #   ",
                    "   #    ",
                    "  #     ",
                    " #      ",
                    "####### "
            };

            default -> throw new IllegalStateException("Unexpected value: " + letter);
        };

        return result;

    }


    public String[] toBanner(String input) {

        if (input == null) {
            return new String[0];
        }

        input = input.toUpperCase();
        String[] lines = new String[]{"", "", "", "", "", "", ""};

        for (int i = 0; i < 7; i++) {
            for (char ch : input.toCharArray()) {
                 if (ch == ' ') {
                     lines[i] += "    ";
                 } else {
                     lines[i] += toAscii(ch)[i];
                 }

            }
        }

        return lines;

    }

}
