import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        FilesAnalyzer filesAnalyzer = new FilesAnalyzer();
        filesAnalyzer.recursiveBuild(path);
        GraphOperations.printByTopologicalSorted(filesAnalyzer.getValues());
    }
}