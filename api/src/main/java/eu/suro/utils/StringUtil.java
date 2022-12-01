package eu.suro.utils;

import eu.suro.locale.Language;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@UtilityClass
public class StringUtil {
    private final String PROGRESS_CURR_COLOR  = "§3";
    private final String PROGRESS_TOTAL_COLOR = "§8";
    private final int LINE_LENGTH = 70; //длина строки для StringToCenter

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private final Calendar CALENDAR = Calendar.getInstance();

    private final TreeMap<Integer, String> ROMAN_MAP = new TreeMap<>();

    static {
        ROMAN_MAP.put(1000, "M");
        ROMAN_MAP.put(900, "CM");
        ROMAN_MAP.put(500, "D");
        ROMAN_MAP.put(400, "CD");
        ROMAN_MAP.put(100, "C");
        ROMAN_MAP.put(90, "XC");
        ROMAN_MAP.put(50, "L");
        ROMAN_MAP.put(40, "XL");
        ROMAN_MAP.put(10, "X");
        ROMAN_MAP.put(9, "IX");
        ROMAN_MAP.put(5, "V");
        ROMAN_MAP.put(4, "IV");
        ROMAN_MAP.put(1, "I");
    }

/*
    public String proccess(String cur) { //фикс белых сообщений //todo решить вопрос
        StringBuilder finalString = newlocale StringBuilder();
        StringBuilder colorCode = newlocale StringBuilder(), templates = newlocale StringBuilder();
        for (int i = 0; i < cur.length();) {
            if (cur.charAt(i) == '§' && i + 1 < cur.length()) {
                colorCode = newlocale StringBuilder();
                while (i + 1 < cur.length() && cur.charAt(i) == '§') {
                    colorCode.append(cur.charAt(i)).append(cur.charAt(i + 1));
                    i += 2;
                }
            }
            if (i >= cur.length())
                break;

            if (cur.charAt(i) == '%') {
                if (i + 1 < cur.length() && cur.charAt(i + 1) == 's') {
                    if (i + 2 >= cur.length() || cur.charAt(i + 2) == ' ') {
                        finalString
                                .append(colorCode)
                                .append(cur.charAt(i))
                                .append(cur.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                }
                templates.append(cur.charAt(i));
                if (templates.length() != 1) {
                    finalString.append(colorCode)
                            .append(templates);
                    templates.setLength(0);
                }
                ++i;
                continue;
            }

            if (cur.charAt(i) != ' ') {
                if (templates.length() != 0)
                    templates.append(cur.charAt(i));
                else
                    finalString.append(colorCode).append(cur.charAt(i));
            } else {
                if (templates.length() != 0) {
                    for (int j = 0; j < templates.length(); ++j)
                        finalString.append(colorCode).append(templates.charAt(j));

                    templates.setLength(0);
                }
                finalString.append(cur.charAt(i));
            }
            ++i;
        }

        return finalString.toString();
    }
 */

    @Deprecated // выше замена
    public String proccess(String cur) {
        StringBuilder n = new StringBuilder();
        StringBuilder now = new StringBuilder();
        for (int i = 0; i < cur.length();) {
            if (cur.charAt(i) == '§' && i + 1 < cur.length()) {
                now = new StringBuilder();
                while (i + 1 < cur.length() && cur.charAt(i) == '§') {
                    now.append(cur.charAt(i)).append(cur.charAt(i + 1));
                    i += 2;
                }
            }
            if (i >= cur.length()) {
                break;
            }

            if (cur.charAt(i) != ' ')
                n.append(now).append(cur.charAt(i));
            else
                n.append(cur.charAt(i));

            ++i;
        }
        return n.toString();
    }

    public boolean startsWithIgnoreCase(String string, String prefix) {
        if (string == null || string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public String onPercentBar(double currentValue, double total) {
        double length = 50.0D;
        double progress = currentValue / total * length;
        return PROGRESS_CURR_COLOR + StringUtils.repeat("|", (int)progress)
                + PROGRESS_TOTAL_COLOR + StringUtils.repeat("|", (int) (length - progress));
    }

    public int onPercent(int value, int max) {
        return (int)((value * 100.0f) / max);
    }

    public String onPercentString(int value, int max) {
        return onPercent(value, max) + "%";
    }

    public String getCompleteTime(int time) {
        long longVal = new BigDecimal(time).longValue();

        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int min = remainder / 60;

        remainder = remainder - min * 60;

        int sec = remainder;

        return String.format("%02d:%02d", min, sec);
    }

    public String getUTFNumber(int number){
        return switch (number) {
            case 1 -> "❶";
            case 2 -> "❷";
            case 3 -> "❸";
            case 4 -> "❹";
            case 5 -> "❺";
            case 6 -> "❻";
            case 7 -> "❼";
            case 8 -> "❽";
            case 9 -> "❾";
            case 10 -> "❿";
            default -> "0";
        };
    }

    public String getRomanNumber(int number) {
        int l = ROMAN_MAP.floorKey(number);
        if (number == l) {
            return ROMAN_MAP.get(number);
        }

        return ROMAN_MAP.get(l) + getRomanNumber(number-l);
    }

    public String getFormat(int number, String single, String lessFive, String others) {
        if (number % 100 > 10 && number % 100 < 15) {
            return others;
        }

        return switch (number % 10) {
            case 1 -> single;
            case 2, 3, 4 -> lessFive;
            default -> others;
        };
    }

    public String getCorrectWord(int time, String key, Language language) {
        List<String> msg = language.getList(key);

        if (msg.size() < 4) {
            return msg.get(0);
        }

        String single = msg.get(0) + msg.get(1);
        String lessfive = msg.get(0) + msg.get(2);
        String others = msg.get(0) + msg.get(3);

        return getFormat(time, single, lessfive, others);
    }

    public String getDate() {
        return DATE_FORMAT.format(CALENDAR.getTime());
    }

    public String changeEnding(String word, char ending) {
        return word.substring(0, word.length() - 1) + ending;
    }

    public static String stringToCenter(String text) {
        //return StringUtils.center(text, LINE_LENGHT); //если делать через этот метод, то он считаеет неправильно как-то
        if (text != null && text.length() <= LINE_LENGTH) {
            return StringUtils.repeat(" ", (LINE_LENGTH - textLength(text)) / 2) + text;
        }
        return text;
    }

    private int textLength(String text) {
        int count = 0;
        char[] array = text.toCharArray();
        for (char symbol : array) {
            if (symbol == '§') {
                count += 2;
            }
        }

        return text.length() - count;
    }

    public List<String> getAnimationTitle(String title, String code1, String code2, int spaces) {
        List<String> toReturn = new ArrayList<>();

        while (spaces >= 0) {
            StringBuilder cur = new StringBuilder();
            for (int i = 0; i < title.length(); ++i) {
                cur.append(title.charAt(i));
                cur.append(" ".repeat(spaces));

            }
            toReturn.add(cur.toString());
            --spaces;
        }
        for (int i = 0; i < title.length(); ++i) {
            toReturn.add(code1 + "§l" + title.substring(0, i) + code2 + "§l" + title.charAt(i)
                    + code1 + "§l" + title.substring(i + 1));
        }

        toReturn.add(title);

        return toReturn;
    }

    public List<String> getAnimation(String displayName) {
        List<String> animation = new ArrayList<>();

        String displayLine = displayName + "  ";
        char[] displayInfoArray = displayLine.toCharArray();
        char[] displayWorkArray = new char[displayInfoArray.length];
        int slotTextSee = 0;

        for (char sym : displayInfoArray) {
            int slot = displayInfoArray.length - 1;
            for (int g = 0; g < getSizeCharArray(displayWorkArray); g++) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < displayInfoArray.length; i++) {
                    if (displayWorkArray[i] == 0) {
                        if (i == slot) {
                            if (i == slotTextSee) {
                                displayWorkArray[slotTextSee] = sym;
                                line.append(sym);
                                slotTextSee++;
                            } else {
                                line.append("§e§l").append(sym);
                                slot -= 1;
                            }
                        } else {
                            line.append(" ");
                        }
                    } else {
                        line.append(displayWorkArray[i]);
                    }
                }

                animation.add(" §8§l» §6§l" + line);

            }
        }

        return animation;
    }

    private int getSizeCharArray(char[] array) {
        int i = 0;
        for (char arr : array) {
            if (arr == 0) {
                i++;
            }
        }
        return i;
    }

    public String getLineCode(int line) {
        StringBuilder builder = new StringBuilder();

        for (char c : String.valueOf(line).toCharArray()) {
            builder.append("§");
            builder.append(c);
        }

        return builder.toString();
    }

    public String getNumberFormat(int amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    public String getNumberFormat(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
