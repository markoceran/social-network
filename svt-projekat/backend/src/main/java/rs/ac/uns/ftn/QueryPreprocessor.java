package rs.ac.uns.ftn;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QueryPreprocessor {

    private static final Map<Character, String> cyrillicToLatinMap = new HashMap<>();

    static {
        cyrillicToLatinMap.put('А', "A");
        cyrillicToLatinMap.put('Б', "B");
        cyrillicToLatinMap.put('В', "V");
        cyrillicToLatinMap.put('Г', "G");
        cyrillicToLatinMap.put('Д', "D");
        cyrillicToLatinMap.put('Ђ', "Đ");
        cyrillicToLatinMap.put('Е', "E");
        cyrillicToLatinMap.put('Ж', "Ž");
        cyrillicToLatinMap.put('З', "Z");
        cyrillicToLatinMap.put('И', "I");
        cyrillicToLatinMap.put('Ј', "J");
        cyrillicToLatinMap.put('К', "K");
        cyrillicToLatinMap.put('Л', "L");
        cyrillicToLatinMap.put('М', "M");
        cyrillicToLatinMap.put('Н', "N");
        cyrillicToLatinMap.put('Њ', "Nj");
        cyrillicToLatinMap.put('О', "O");
        cyrillicToLatinMap.put('П', "P");
        cyrillicToLatinMap.put('Р', "R");
        cyrillicToLatinMap.put('С', "S");
        cyrillicToLatinMap.put('Т', "T");
        cyrillicToLatinMap.put('Ћ', "Ć");
        cyrillicToLatinMap.put('У', "U");
        cyrillicToLatinMap.put('Ф', "F");
        cyrillicToLatinMap.put('Х', "H");
        cyrillicToLatinMap.put('Ц', "C");
        cyrillicToLatinMap.put('Ч', "Č");
        cyrillicToLatinMap.put('Џ', "Dž");
        cyrillicToLatinMap.put('Ш', "Š");

        cyrillicToLatinMap.put('а', "a");
        cyrillicToLatinMap.put('б', "b");
        cyrillicToLatinMap.put('в', "v");
        cyrillicToLatinMap.put('г', "g");
        cyrillicToLatinMap.put('д', "d");
        cyrillicToLatinMap.put('ђ', "đ");
        cyrillicToLatinMap.put('е', "e");
        cyrillicToLatinMap.put('ж', "ž");
        cyrillicToLatinMap.put('з', "z");
        cyrillicToLatinMap.put('и', "i");
        cyrillicToLatinMap.put('ј', "j");
        cyrillicToLatinMap.put('к', "k");
        cyrillicToLatinMap.put('л', "l");
        cyrillicToLatinMap.put('м', "m");
        cyrillicToLatinMap.put('н', "n");
        cyrillicToLatinMap.put('њ', "nj");
        cyrillicToLatinMap.put('о', "o");
        cyrillicToLatinMap.put('п', "p");
        cyrillicToLatinMap.put('р', "r");
        cyrillicToLatinMap.put('с', "s");
        cyrillicToLatinMap.put('т', "t");
        cyrillicToLatinMap.put('ћ', "ć");
        cyrillicToLatinMap.put('у', "u");
        cyrillicToLatinMap.put('ф', "f");
        cyrillicToLatinMap.put('х', "h");
        cyrillicToLatinMap.put('ц', "c");
        cyrillicToLatinMap.put('ч', "č");
        cyrillicToLatinMap.put('џ', "dž");
        cyrillicToLatinMap.put('ш', "š");
    }

    public String preprocessQuery(String query) {
        StringBuilder sb = new StringBuilder();

        for (char c : query.toCharArray()) {
            if (cyrillicToLatinMap.containsKey(c)) {
                sb.append(cyrillicToLatinMap.get(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString().toLowerCase();
    }
}
