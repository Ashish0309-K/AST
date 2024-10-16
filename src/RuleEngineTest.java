// RuleEngineTest.java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleEngineTest {

    public static void main(String[] args) {
        // Test case 1: Create and evaluate individual rules
        String rule1 = "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)";
        Node ruleAST1 = RuleEngine.createRule(rule1);

        Map<String, Object> userData1 = Map.of("age", 35, "department", "Sales", "salary", 60000, "experience", 3);
        boolean result1 = RuleEngine.evaluateRule(ruleAST1, userData1);
        System.out.println("Test 1 Evaluation: " + result1); 

        // Test case 2: Combine multiple rules
        String rule2 = "age < 25 AND department = 'Marketing'";
        Node ruleAST2 = RuleEngine.createRule(rule2);

        List<Node> rules = new ArrayList<>();
        rules.add(ruleAST1);
        rules.add(ruleAST2);

        Node combinedAST = RuleEngine.combineRules(rules);
        Map<String, Object> userData2 = Map.of("age", 24, "department", "Marketing", "salary", 40000, "experience", 2);
        boolean result2 = RuleEngine.evaluateRule(combinedAST, userData2);
        System.out.println("Test 2 Evaluation: " + result2); 
    }
}
