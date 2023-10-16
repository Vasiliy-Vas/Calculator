import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Calculator {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.length() == 0) {
                throw new Exception("Калькулятор не принимает на вход пустую строку");
            }

            int countOperand = 0;
            Character operand = null;
            StringBuilder sbLeft = new StringBuilder();
            StringBuilder sbRight = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ' ') {
                    continue;
                }
                if (line.charAt(i) == '.' || line.charAt(i) == ',') {
                    throw new Exception("Калькулятор работает только с целыми числами");
                }
                if (String.valueOf(line.charAt(i)).matches("[-+*/]")) {
                    operand = line.charAt(i);
                    countOperand++;
                    continue;
                }
                if (countOperand > 0) {
                    sbRight.append(line.charAt(i));
                } else {
                    sbLeft.append(line.charAt(i));
                }
            }

            if (sbLeft.isEmpty() || sbRight.isEmpty() || operand == null) {
                throw new Exception("Строка не является математической операцией");
            }

            if (countOperand > 1) {
                throw new Exception("Неправильный формат математической операции - два операнда и один оператор (+, -, /, *)\"");
            }

            boolean isRomanSystem = isRomanSystem(sbLeft.toString(), sbRight.toString());
            boolean isArabicSystem = isArabicSystem(sbLeft.toString(), sbRight.toString());
            if (!isRomanSystem && !isArabicSystem) {
                throw new Exception("Используются одновременно разные системы счисления");
            }

            if (isRomanSystem && operand == '-') {
                throw new Exception("В римской системе нет отрицательных чисел");
            }

            String resultRoman = "";
            if (isRomanSystem) {
                int a = romanToArabic(sbLeft.toString());
                int b = romanToArabic(sbRight.toString());
                int result = calculateResult(a, b, operand);
                if (result < 1) {
                    throw new Exception("Результатом работы калькулятора с римскими числами могут быть только положительные числа");
                }
                resultRoman = arabicToRoman(result);
            }

            int resultArabic = 0;
            if (isArabicSystem) {
                int a = Integer.parseInt(sbLeft.toString());
                int b = Integer.parseInt(sbRight.toString());
                if ((a < 1 || a > 10) || (b < 1 || b > 10)) {
                    throw new Exception("Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более");
                }
                resultArabic = calculateResult(a, b, operand);
            }

            System.out.println(isRomanSystem ? resultRoman : resultArabic);
        }
    }

    public static boolean isRomanSystem(String left, String right) {
        boolean result = true;
        try {
            int a = romanToArabic(left);
            int b = romanToArabic(right);
        } catch (IllegalArgumentException e) {
            result = false;
        }
        return result;
    }

    public static boolean isArabicSystem(String left, String right) {
        boolean result = true;
        try {
            int a = Integer.parseInt(left);
            int b = Integer.parseInt(right);
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    public static int romanToArabic(String roman) {
        String romanNumeral = roman.toUpperCase();

        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        while (romanNumeral.length() > 0 && i < romanNumerals.size()) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(roman + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    public static String arabicToRoman(int number) {
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (number > 0 && i < romanNumerals.size()) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static int calculateResult(int a, int b, char c) throws Exception {
        return switch (c) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> throw new Exception("Неподдерживаемая математическая операция");
        };
    }
}
