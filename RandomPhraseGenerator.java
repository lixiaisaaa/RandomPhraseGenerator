package comprehensive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This is a program that user can randomly generate phrase by input grammar
 * file.
 *
 * @author Robert Li
 *
 */
public class RandomPhraseGenerator {
    public static void main(String[] args) {
        if (args.length < 2) { // Arguments Checking
            System.out.println("Usage: <filename> <number of phrases>");
        } else {
            try {
                File file = new File(args[0]);
                FileReader fr = new FileReader(file);
                int nPhrases = Integer.parseInt(args[1]);
                BufferedReader br = new BufferedReader(fr);
                Map<String, List<String>> map = new HashMap<>();
                String s;
                Map<String, Random> randomMap = new HashMap<>();

                // File Parsing.
                while ((s = br.readLine()) != null) {
                    if (s.equals("{")) {
                        String key = br.readLine();
                        String val;
                        List<String> values = new ArrayList<>();
                        while (!(val = br.readLine()).equals("}")) {
                            values.add(val);
                        }
                        map.put(key, values);
                        randomMap.put(key, new Random());
                    }
                }

                // Calculating Phrases.
                List<String> phrases = new ArrayList<>();
                int i = 0;
                while (i < nPhrases) {
                    String randomPhrase = getRandomPhrase(new ArrayList<>(map.get("<start>")), map,
                            randomMap.get("<start>").nextInt(map.get("<start>").size()), randomMap);
                    if (!phrases.contains(randomPhrase)) {
                        phrases.add(randomPhrase);
                        i++;
                    }
                }

                // Printing Phrases.
                for (i = 0; i < nPhrases; i++) {
                    System.out.println(phrases.get(i));
                }
            } catch (FileNotFoundException fe) { // File Not Found Exception Handled
                System.out.println("File Not Found");
            } catch (Exception e) { // Other Exceptions
                e.printStackTrace();
            }
        }
    }

    /// Recursive function to get a random phrase.
    /// It computes by recursively calling back when a terminal is found.
    private static String getRandomPhrase(List<String> strings, Map<String, List<String>> map, int idx,
                                          Map<String, Random> randomMap) {
        List<String> tokens = breakString(strings.get(idx));
        strings.remove(idx);
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> tokenIterator = tokens.iterator(); tokenIterator.hasNext();) {
            String token = tokenIterator.next();
            if (map.containsKey(token)) {
                sb.append(getRandomPhrase(new ArrayList<>(map.get(token)), map,
                        randomMap.get(token).nextInt(map.get(token).size()), randomMap));
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    /// Function to break a multi token string into
    /// terminals and non-terminals.
    private static List<String> breakString(String s) {
        String cs = s;
        List<String> result = new ArrayList<>();
        int index = cs.indexOf('<');
        int index2;
        while (index >= 0) {
            String token = cs.substring(0, index);
            if (token.length() > 0) {
                result.add(token);
            }
            index2 = cs.indexOf('>');
            result.add(cs.substring(index, index2 + 1));
            cs = cs.substring(index2 + 1);
            index = cs.indexOf('<');
        }
        if (cs.length() > 0) {
            result.add(cs);
        }
        return result;
    }
}