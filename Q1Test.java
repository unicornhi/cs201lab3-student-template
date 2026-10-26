
import java.util.*;

public class Q1Test {

    public static void main(String[] args) {

        String result, traversal1, traversal2, traversal3;

        traversal1 = "1-2";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "3-2-1";
        traversal3 = "2-3-1";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "3-1-2-5-4";
        traversal2 = "2-1-4-5-3";
        traversal3 = "1-2-3-4-5";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "10-20-30-40-50";
        traversal2 = "20-10-30-40-50";
        traversal3 = "10-50-40-30-20";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();
    }

    static class Node {

        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] <= arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    static Node insert(Node root, int value) {
        if (root == null) {
            return new Node(value);
        }
        if (value < root.value) {
            root.left = insert(root.left, value);
        } else if (value > root.value) {
            root.right = insert(root.right, value);
        }
        return root;
    }

    static void traversePre(Node root, List<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.value);
        traversePre(root.left, result);
        traversePre(root.right, result);
    }

    static void traverseIn(Node root, List<Integer> result) {
        if (root == null) {
            return;
        }
        traverseIn(root.left, result);
        result.add(root.value);
        traverseIn(root.right, result);
    }

    static void traversePost(Node root, List<Integer> result) {
        if (root == null) {
            return;
        }
        traversePost(root.left, result);
        traversePost(root.right, result);
        result.add(root.value);
    }

    static boolean matches(List<Integer> list, int[] arr) {
        if (list.size() != arr.length) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (list.get(i) != arr[i]) {
                return false;
            }
        }
        return true;
    }

    static int[] parseTraversal(String traversal) {
        String[] parts = traversal.split("-");
        int[] result = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }

        return result;
    }

    public static String verify(String traversal1, String traversal2, String traversal3) {
        int[][] arrays;

        try {
            arrays = new int[][]{
                parseTraversal(traversal1),
                parseTraversal(traversal2),
                parseTraversal(traversal3)
            };
        } catch (NumberFormatException e) {
            return "Invalid traversals";
        }

        // Check lengths and node sets
        if (arrays[0].length != arrays[1].length
                || arrays[1].length != arrays[2].length
                || arrays[0].length == 0) {
            return "Invalid traversals";
        }

        Set<Integer> expected = new HashSet<>();
        for (int value : arrays[0]) {
            if (!expected.add(value)) {
                return "Invalid traversals";
            }
        }

        for (int i = 1; i < 3; i++) {
            Set<Integer> current = new HashSet<>();
            for (int value : arrays[i]) {
                current.add(value);
            }
            if (!current.equals(expected)
                    || current.size() != arrays[i].length) {
                return "Invalid traversals";
            }
        }

        // Identify possible inorder traversal
        for (int in = 0; in < 3; in++) {
            if (!isSorted(arrays[in])) {
                continue;
            }

            int first = (in + 1) % 3;
            int second = (in + 2) % 3;

            // Try both Preorder/Postorder assignments
            int[][] assignments = {
                {first, second},
                {second, first}
            };

            for (int[] assignment : assignments) {
                int pre = assignment[0];
                int post = assignment[1];

                // Construct BST from preorder
                Node root = null;
                for (int value : arrays[pre]) {
                    root = insert(root, value);
                }

                // Generate traversals
                List<Integer> preorder = new ArrayList<>();
                List<Integer> inorder = new ArrayList<>();
                List<Integer> postorder = new ArrayList<>();

                traversePre(root, preorder);
                traverseIn(root, inorder);
                traversePost(root, postorder);

                // Verify all traversals
                if (matches(preorder, arrays[pre])
                        && matches(inorder, arrays[in])
                        && matches(postorder, arrays[post])) {

                    String[] names = new String[3];
                    names[in] = "Inorder";
                    names[pre] = "Preorder";
                    names[post] = "Postorder";

                    return "Traversal 1 - " + names[0]
                            + ", Traversal 2 - " + names[1]
                            + ", Traversal 3 - " + names[2];
                }
            }
        }

        return "Invalid traversals";
    }
}
