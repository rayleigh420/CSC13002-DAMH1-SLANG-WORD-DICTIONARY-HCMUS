import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.List;

public class dictionary
{
    public static String DATA_DIR = "../asset/slang.txt";

    public static TreeMap<String, Set<String>> data, rawData;
    public static List<String> history;
    private static Scanner sc = new Scanner(System.in);

    public static void setUpDictionary() {
        loadData();
        printDictionary();

        history = new ArrayList<String>();
    }

    private static void loadData(){
        data = new TreeMap<String, Set<String>>();

        try(
            BufferedReader br = new BufferedReader(new FileReader(new File(DATA_DIR)))
        )
        {
            String w;
            while((w = br.readLine()) != null){
                String[] wordAndDef = w.split("`");

                if (wordAndDef.length == 2){
                    String[] definition = wordAndDef[1].split("\\|");
                    Set<String> def = new HashSet<>(Arrays.stream(definition).collect(Collectors.toSet()));
                    data.put(wordAndDef[0], def);
                }
            }
            rawData = new TreeMap<String, Set<String>>(data);   
            br.close();

        }
        catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }

    public static void printDictionary() {
        Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
        dictionary.forEach(item -> System.out.println(item.getKey() + "\t->\t" + item.getValue()));
    }

    public static void addHistory(String word){
        history.add(word);
    }

    public static void searchSlang(){
        System.out.println();
        System.out.print("Enter word you want to search: ");
        String word = sc.nextLine().trim().toUpperCase();
        addHistory(word);

        Set<String> def = data.get(word);
        if (def == null){
            Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
            dictionary.forEach(item -> {
                String w = item.getKey();
                if (w.contains(word.toUpperCase())){
                    System.out.println(w + ": " + item.getKey());
                }
            });
        }
        else {
            System.out.println("Founded!");
            System.out.println(word + ": " + def);
        }

    }

    public static void searchDefinition(){
        System.out.println();
        System.out.print("Enter definition you want to search: ");
        String def = sc.nextLine().trim();

        Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
        dictionary.forEach(item -> {
            Set<String> defList = item.getValue();
            for(String i : defList){
                if (i.contains(def) || i.contains(def.toUpperCase()) || i.contains(def.toLowerCase())){
                    System.out.println(i);
                }
            }
        });
    }

    public static void printHistory(){
        if (history.isEmpty()){
            System.out.println("History is empty");
        }
        else {
            for(String item : history){
                System.out.println(item);
            }
        }
    }

    public static void addSlangWord(){
        Set<String> definition = new HashSet<String>();
        System.out.println();
        System.out.print("Enter word you want to add: ");
        String word = sc.nextLine().trim().toUpperCase();

        String chosen = "0";
        if (data.containsKey(word)){
            System.out.println();
            System.out.println("Word exist in dictionary!");
            System.out.println("1. Overwrite Slang Word");
            System.out.println("2. Duplicate to new Slang Word");
            System.out.print("Enter your chosen: ");
            chosen = sc.nextLine();
        }
        
        int c = 0;
        System.out.println();
        System.out.print("Enter number of definition of Slang Word you want to add: ");
        c = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < c; i++){
            System.out.print("Enter " + i + " st definition: ");
            definition.add(sc.nextLine());
        }

        if (chosen.equals("0") || chosen.equals("1")){
            data.put(word, definition);
        }
        else if (chosen.equals("2")){
            data.put(word.toLowerCase(), definition);   
        }
        else{
            return;
        }

        System.out.println("Add new Slang Word Success!");
    }

    public static void editSlangWord(){
        Set<String> definition = new HashSet<String>();
        System.out.println();
        System.out.print("Enter word you want to edit: ");
        String word = sc.nextLine().trim().toUpperCase();

        if (!data.containsKey(word)){
            System.out.println("Word not exist!");
            return;
        }
        else {
            System.out.println(word + ": " + data.get(word));
        }

        String chosen;
        System.out.println("1. Add more definition for Slang Word");
        System.out.println("2. Create new definition for Slang Word");
        System.out.print("Enter your chosen: ");
        chosen = sc.nextLine();

        int c = 0;
        System.out.println();
        System.out.print("Enter number of definition of Slang Word you want to add: ");
        c = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < c; i++){
            System.out.print("Enter " + i + " st definition: ");
            definition.add(sc.nextLine());
        }
        
        if (chosen.equals("1")){
            definition.addAll(data.get(word));
        }
        else if (chosen.equals("2")){

        }
        else {
            return;
        }

        data.put(word, definition);
        System.out.println("Edit Slang Word Success!");
    }

    public static void deleteSlang(){
        System.out.println();
        System.out.print("Enter word you want to delete: ");
        String word = sc.nextLine().trim().toUpperCase();

        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Are you sure to delete word?: ");

        String choice = sc.nextLine();
        if (choice.equals("1")){
            if (data.remove(word) == null){
                System.out.println("Word not exist");
            }
            else {
                System.out.println("Delete Success!");
            }
        }
        else if (choice.equals("2")){
            return;
        }
        else {
            System.out.println("Invalid chosen!");
            return;
        }

    }

    public static void resetDictionary(){
        data = rawData;
        System.out.println("Rest Dictionary Success!");
    }

    public static void onThisDaySlang(){
        List<String> keysAsArray = new ArrayList<String>(data.keySet());
        Random r = new Random();
        String word = keysAsArray.get(r.nextInt(keysAsArray.size()));
        System.out.println(word + ": " + data.get(word));
    }

	public static void main(String[] args) throws IOException {
        setUpDictionary();
        String choice;

        while(true){
			System.out.println();
			System.out.println("1. Search Slang Word");
			System.out.println("2. Search Slang Word follow definition");
			System.out.println("3. Show Search History");
			System.out.println("4. Add new Slang Word");
			System.out.println("5. Edit Slang Word");
			System.out.println("6. Delete Slang Word");
			System.out.println("7. Reset Slang Word Dictionary");
            System.out.println("8. On this day Slang Word");
            System.out.println("9. Slang Word Quiz");
            System.out.println("10. Slang Word Quiz follow definition");
			System.out.println("Chose difference chosen to exit");
			System.out.print("Chose action: ");
			choice = sc.nextLine();

            if (choice.equals("1")){
                searchSlang();
            }
            else if (choice.equals("2")){
                searchDefinition();
            }
            else if (choice.equals("3")){
                printHistory();
            }
            else if (choice.equals("4")){
                addSlangWord();
            }
            else if (choice.equals("5")){
                editSlangWord();
            }
            else if (choice.equals("6")){
                deleteSlang();
                printDictionary();
            }
            else if (choice.equals("7")){
                resetDictionary();
            }
            else if (choice.equals("8")){
                onThisDaySlang();
            }
            else if (choice.equals("9")){
                System.out.println(choice);
            }
            else if (choice.equals("10")){
                System.out.println(choice);
            }
            else {
                break;
            }
        }
    }
}
