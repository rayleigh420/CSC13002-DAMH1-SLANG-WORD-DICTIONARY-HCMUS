import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Vector;

public class dictionary
{
    public static String RAW_DATA_DIR = "../asset/rawData/slang.txt";
    public static String DATA_DIR = "../asset/data/dictionary.txt";
    public static String HISTORY = "../asset/history/history.txt";

    public static TreeMap<String, Set<String>> data;
    public static Vector<String> history;
    private static Scanner sc = new Scanner(System.in);

    public static void setUpDictionary() {
        loadData(DATA_DIR);
        loadHistory();
        printDictionary();
    }

    private static void loadData(String PATH){
        data = new TreeMap<String, Set<String>>();

        try(
            BufferedReader br = new BufferedReader(new FileReader(new File(PATH)))
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
            br.close();

        }
        catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }

    private static void saveData(){
        try(FileWriter fw = new FileWriter(new File(DATA_DIR))){
            Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
            for(Map.Entry<String, Set<String>> item : dictionary){
                fw.write(item.getKey() + "`");
                Set<String> definition = item.getValue();
                String lastDef = definition.stream().reduce((a, b) -> b).get();
                for (String def : definition){
                    if (def.equals(lastDef)){
                        fw.write(def + "\n");
                    }
                    else {
                        fw.write(def + "| ");
                    }
                }
            };

            fw.close();
        }
        catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }

    private static void loadHistory(){
        history = new Vector<String>();

        try(
            BufferedReader br = new BufferedReader(new FileReader(new File(HISTORY)))
        ){
            String w;
            while((w = br.readLine()) != null){
                history.add(w);
            }  
            br.close();

        }catch(IOException e){
            System.out.println("Error message: " + e);
        }
        
    }

    private static void saveHistory(){
        try(FileWriter fw = new FileWriter(new File(HISTORY))){
            for (String item : history){
                fw.write(item + "\n");
            }
            fw.close();
        }
        catch (IOException e){
            System.out.println("Error message: " + e);
        }
    }

    public static void printDictionary() {
        Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
        dictionary.forEach(item -> System.out.println(item.getKey() + ": " + item.getValue()));
    }

    public static void addHistory(String word){
        history.add(word);
        saveHistory();
    }

    public static void searchSlang(){
        System.out.println();
        System.out.print("Enter word you want to search: ");
        String word = sc.nextLine().trim().toUpperCase();
        addHistory(word);

        Boolean flag = false;

        Set<String> def = data.get(word);
        if (def == null){
            Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
            for(Map.Entry<String,Set<String>> item: dictionary){
                String w = item.getKey();
                if (w.contains(word.toUpperCase())){
                    flag = true;
                    System.out.println(w + ": " + item.getKey());
                }
            }

            if (flag == false){
                System.out.println("Word not exist! Can not find!");
            }
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

        Boolean flag = false;
        Set<Map.Entry<String, Set<String>>> dictionary = data.entrySet();
        for(Map.Entry<String,Set<String>> item: dictionary){
            Set<String> defList = item.getValue();
            for(String i : defList){
                if (i.contains(def) || i.contains(def.toUpperCase()) || i.contains(def.toLowerCase())){
                    flag = true;
                    System.out.println(i);
                }
            }

        }

        if(flag == false){
            System.out.println("Can not find Slang Word has this definition!");
        }
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
            int k = i + 1;
            System.out.print("Enter " + k + "st definition: ");
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
        saveData();
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
            int k = i + 1;
            System.out.print("Enter " + k + "st definition: ");
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
        saveData();
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
        saveData();
    }

    public static void resetDictionary(){
        loadData(RAW_DATA_DIR);
        saveData();
        System.out.println();
        System.out.println("Rest Dictionary Success!");
    }

    public static String onThisDaySlang(Boolean quiz){
        List<String> keysAsArray = new ArrayList<String>(data.keySet());
        Random r = new Random();
        String word = keysAsArray.get(r.nextInt(keysAsArray.size()));
        if (quiz == false){
            System.out.println();
            System.out.println("On this day Slang Word!");
            System.out.println(word + ": " + data.get(word));
        }
        return word;
    }

    public static void slangWordQuiz(){
        TreeMap<String, Set<String>> quiz = new TreeMap<String, Set<String>>();
        for(int i = 0; i < 4; i++){
            String word = onThisDaySlang(true);
            quiz.put(word, data.get(word));
        }
        int randQuiz = new Random().nextInt(4);

        List<String> keysAsArray = new ArrayList<String>(quiz.keySet());
        String quesWord = keysAsArray.get(randQuiz);

        System.out.println();
        System.out.println("Slang Word Quiz");
        System.out.println("Choose definition of this Slang Word: " + quesWord);

        int i = 1;
        Set<Map.Entry<String, Set<String>>> q = quiz.entrySet();
        for(Map.Entry<String,Set<String>> item: q){
            if (i % 2 == 1){
                System.out.print(i + ". " + item.getValue().iterator().next() + "\t\t\t");
            }
            else {
                System.out.println(i + ". " + item.getValue().iterator().next());
            }
            i++;
        }

        int chose;
        System.out.print("Choose correct answer: ");
        chose = Integer.parseInt(sc.nextLine()) - 1;
        if (chose >= 0 && chose < 4){
            if (chose == randQuiz){
                System.out.println("Correct! Congratulation <3 <3 <3");
            }
            else {
                int ans = randQuiz + 1;
                System.out.println("Incorrect! Good luck for next time!");
                System.out.println("Correct answer is: " + ans + ". " + quiz.get(quesWord));
            }
        }
        else {
            System.out.println("Invalid answer! Choose answer you see in the console please!");
            return;
        }
        
    }

    public static void slangDefinitionQuiz(){
        TreeMap<String, Set<String>> quiz = new TreeMap<String, Set<String>>();
        for(int i = 0; i < 4; i++){
            String word = onThisDaySlang(true);
            quiz.put(word, data.get(word));
        }
        int randQuiz = new Random().nextInt(4);

        List<String> keysAsArray = new ArrayList<String>(quiz.keySet());
        String quesWord = keysAsArray.get(randQuiz);

        System.out.println();
        System.out.println("Slang Word Quiz follow definition");
        System.out.println("Choose Slang Word has this definition: " + quiz.get(quesWord).iterator().next());

        int i = 1;
        Set<Map.Entry<String, Set<String>>> q = quiz.entrySet();
        for(Map.Entry<String,Set<String>> item: q){
            if (i % 2 == 1){
                System.out.print(i + ". " + item.getKey() + "\t\t");
            }
            else {
                System.out.println(i + ". " + item.getKey());
            }
            i++;
        }

        int chose;
        System.out.print("Choose correct answer: ");
        chose = Integer.parseInt(sc.nextLine()) - 1;
        if (chose >= 0 && chose < 4){
            if (chose == randQuiz){
                System.out.println("Correct! Congratulation <3 <3 <3");
            }
            else {
                int ans = randQuiz + 1;
                System.out.println("Incorrect! Good luck for next time!");
                System.out.println("Correct answer is: " + ans + ". " + quesWord);
            }
        }
        else {
            System.out.println("Invalid answer! Choose answer you see in the console please!");
            return;
        }
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
                onThisDaySlang(false);
            }
            else if (choice.equals("9")){
                slangWordQuiz();
            }
            else if (choice.equals("10")){
                slangDefinitionQuiz();
            }
            else {
                break;
            }
        }
    }
}
