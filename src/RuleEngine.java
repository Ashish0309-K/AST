import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RuleEngine {

    
    public static Node createRule(String rule) {
        rule = rule.trim();
        
        if (rule.startsWith("(") && rule.endsWith(")")) {
            rule = rule.substring(1, rule.length() - 1).trim(); 
        }

        
        int index = findMainOperatorIndex(rule);
        if (index != -1) {
            String operator = rule.substring(index, index + (rule.substring(index).startsWith("AND") ? 3 : 2));  
            String left = rule.substring(0, index).trim();
            String right = rule.substring(index + operator.length()).trim();
            return new Node("operator", operator, createRule(left), createRule(right));
        } else {
            
            return new Node("operand", rule);
        }
    }

    // Finds the index of the main logical operator (AND/OR) in the rule string
    private static int findMainOperatorIndex(String rule) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < rule.length(); i++) {
            char c = rule.charAt(i);
            if (c == '(') {
                stack.push(c);  
            } else if (c == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();  
                }
            } else if (stack.isEmpty()) {
               
                if (rule.startsWith("AND", i)) {
                    return i;
                } else if (rule.startsWith("OR", i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Combines multiple rules into one AST using an OR operator
    public static Node combineRules(List<Node> rules) {
        if (rules.isEmpty()) return null;

        Node combinedAST = rules.get(0);
        for (int i = 1; i < rules.size(); i++) {
            combinedAST = new Node("operator", "OR", combinedAST, rules.get(i));
        }
        return combinedAST;
    }

    // Evaluates the AST against user data
    public static boolean evaluateRule(Node ast, Map<String, Object> data) {
        if (ast.type.equals("operand")) {
            return evaluateOperand(ast.value, data);
        } else if (ast.type.equals("operator")) {
            if (ast.value.equals("AND")) {
                return evaluateRule(ast.left, data) && evaluateRule(ast.right, data);
            } else if (ast.value.equals("OR")) {
                return evaluateRule(ast.left, data) || evaluateRule(ast.right, data);
            }
        }
        return false;
    }

    private static boolean evaluateOperand(String operand, Map<String, Object> data) {
        String[] parts = operand.split(" ");
        String attribute = parts[0];
        String operator = parts[1];
        Object value = parts[2].replace("'", "");  

        // Check if the data contains the attribute
        if (!data.containsKey(attribute)) {
            System.out.println("Missing attribute in user data: " + attribute);
            return false;
        }

        Object dataValue = data.get(attribute);

        // Ensure dataValue is not null before comparison
        if (dataValue == null) {
            System.out.println("Value for attribute '" + attribute + "' is null.");
            return false;
        }

        switch (operator) {
            case ">": return (int) dataValue > Integer.parseInt(value.toString());
            case "<": return (int) dataValue < Integer.parseInt(value.toString());
            case "=": return dataValue.equals(value);
            default: return false;
        }
    }
}
