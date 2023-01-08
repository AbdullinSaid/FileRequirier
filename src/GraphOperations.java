import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class with operations for graphs
 */
public class GraphOperations {
    private final static Map<FileGraphNode, FileGraphNode> parent;
    static {
        parent = new HashMap<>();
    }
    private static FileGraphNode cycleStart;
    private static FileGraphNode cycleEnd;

    /**
     * DFS algorithm that checks for cycles in a graph
     * @param node graph node
     * @return is there a cycle
     */
    private static boolean dfsCheckCycles(FileGraphNode node) {
        node.setUsed(1);
        for (FileGraphNode to : node.getChildrenList()) {
            if (to.getFilePath().toFile().exists()) {
                if (to.usedEquals(1)) {
                    cycleEnd = node;
                    cycleStart = to;
                    return true;
                } else {
                    if (to.usedEquals(0)) {
                        parent.put(node, to);
                        return dfsCheckCycles(to);
                    }
                }
            }
        }
        node.setUsed(2);
        return false;
    }

    /**
     * Topological Sort algorithm
     * @param node graph node
     * @param answer list of Topological sorted graph
     */
    private static void dfsTopologicalSort(FileGraphNode node, List<FileGraphNode> answer) {
        node.setUsed(1);
        for (FileGraphNode to : node.getChildrenList()) {
            if (to.usedEquals(0)) {
                dfsTopologicalSort(to, answer);
            }
        }
        answer.add(node);
    }

    private static void init(Collection<FileGraphNode> graph) {
        for (FileGraphNode node : graph) {
            node.setUsed(0);
        }
        parent.clear();
        cycleStart = null;
        cycleEnd = null;
    }

    /**
     * Check cycles in graph and print one found if found
     * @param graph graph
     * @return is there a cycle
     */
    public static boolean checkCycles(Collection<FileGraphNode> graph) {
        init(graph);

        for (FileGraphNode node : graph) {
            if (dfsCheckCycles(node)) {
                break;
            }
        }

        if (cycleStart == null) {
            return false;
        } else {
            System.out.println("Found cycle:");
            System.out.print(cycleStart.getFilePath() + " ");
            while (cycleStart != cycleEnd) {
                cycleStart = parent.get(cycleStart);
                System.out.print(cycleStart.getFilePath() + " ");
            }
            System.out.println();
            return true;
        }
    }

    /**
     * Topological Sort and printing file graph by it
     * @param graph graph
     */
    public static void printByTopologicalSorted(Collection<FileGraphNode> graph) {
        if (checkCycles(graph)) {
            return;
        }
        for (FileGraphNode node : graph) {
            node.setUsed(0);
        }
        List<FileGraphNode> answer = new ArrayList<>();
        for (FileGraphNode node : graph) {
            if (node.usedEquals(0)) {
                dfsTopologicalSort(node, answer);
            }
        }
        Collections.reverse(answer);
        for (FileGraphNode node : answer) {
            try (BufferedReader br = new BufferedReader(new FileReader(node.getFilePath().toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Exception occurred while printing file {}", ex);
            }
        }
    }
}
