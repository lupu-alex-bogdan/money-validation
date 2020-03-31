package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class CnpUtil {

    private static int[] hashTable = {2 , 7 , 9 , 1 , 4 , 6 , 3 , 5 , 8 , 2 , 7 , 9};
    private static int hashResult = 0;
    private static String cnpString;

    private CnpUtil() {

    }

    public static void validate(String cnp) {
        cnpString = String.valueOf(cnp);
        validateLength(cnpString);
        validateBirthDate();
        validateGender();
        validateCounty();
        validateControlDigit();
    }

    private static void validateCounty() {
        int county = Integer.parseInt(cnpString.substring(7, 9));
        if (county > 52) {
            throw new IllegalArgumentException("County code is incorrect!");
        }
    }

    private static void validateBirthDate()  {
        String date = cnpString.substring(1, 7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("CNP has invalid birth date!");
        }
    }

    private static void validateGender() {
        int gender = Character.getNumericValue(cnpString.charAt(0));
        int year = Integer.parseInt(cnpString.substring(1, 3));
        if (((gender != 1 && gender != 2) || year < 21 || year > 99) &&
                ((gender != 5 && gender != 6) || year < 0 || year > 20) &&
                gender != 7 && gender != 8) {
            throw new IllegalArgumentException("Gender is incorrect!");
        }
    }

    private static void validateControlDigit() {
        hashResult = 0;
        for (int i = 0; i < 12; i++) {
            hashResult = hashResult + (hashTable[i] * Character.getNumericValue(cnpString.charAt(i)));
        }
        hashResult = hashResult % 11;
        if (hashResult != Character.getNumericValue(cnpString.charAt(12)) ||
                hashResult == 10 && Character.getNumericValue(cnpString.charAt(12)) != 1) {
            throw new IllegalArgumentException("Control digit (last) is invalid!");
        }
    }

    private static void validateLength(String cnp) {
        if (cnp.length() != 13) {
            throw new IllegalArgumentException("CNP must have exactly 13 digits!");
        }
    }
}
