package com.liuyun.github;

import com.liuyun.github.model.NFA;
import com.liuyun.github.model.Transition;
import com.liuyun.github.utils.Constants;
import com.liuyun.github.utils.StringUtils;
import java.util.Stack;

public class Thompson {

    /**
     * Compile regular expression
     *
     * @param regex
     * @return
     */
    public static NFA compile(String regex) {
        // Verify that the input string is a regular expression
        if (!StringUtils.validRegEx(regex)) {
            System.out.println("Invalid Regular Expression Input.");
            return new NFA();
        }
        // Operator stack
        Stack<Character> operators = new Stack();
        // Operand stack
        Stack<NFA> operands = new Stack();
        // Concat stack
        Stack<NFA> concatStack = new Stack();
        // Concat lag
        boolean ccflag = true;
        // Current operator and character
        char op, c;
        int paraCount = 0;
        NFA nfa1, nfa2;

        for (int i = 0; i < regex.length(); i++) {
            c = regex.charAt(i);
            if (StringUtils.alphabet(c)) {
                operands.push(new NFA(c));
                if (ccflag) {
                    // Replace concatenation with '.'
                    operators.push('.');
                } else {
                    ccflag = true;
                }
            } else {
                if (c == ')') {
                    ccflag = true;
                    if (paraCount == 0) {
                        System.out.println("Error: More end paranthesis than beginning paranthesis");
                        System.exit(1);
                    } else {
                        paraCount--;
                    }
                    // Process the operator stack until an opening parenthesis is encountered
                    while (!operators.empty() && operators.peek() != '(') {
                        op = operators.pop();
                        if (op == '.') {
                            nfa2 = operands.pop();
                            nfa1 = operands.pop();
                            operands.push(concat(nfa1, nfa2));
                        } else if (op == '|') {
                            nfa2 = operands.pop();
                            if (!operators.empty() && operators.peek() == '.') {
                                concatStack.push(operands.pop());
                                while (!operators.empty() && operators.peek() == '.') {
                                    concatStack.push(operands.pop());
                                    operators.pop();
                                }
                                nfa1 = concat(concatStack.pop(), concatStack.pop());
                                while (concatStack.size() > 0) {
                                    nfa1 = concat(nfa1, concatStack.pop());
                                }
                            } else {
                                nfa1 = operands.pop();
                            }
                            operands.push(union(nfa1, nfa2));
                        }
                    }
                } else if (c == '*') {
                    operands.push(kleene(operands.pop()));
                    ccflag = true;
                } else if (c == '(') {
                    operators.push(c);
                    paraCount++;
                    ccflag = false;
                } else if (c == '|') {
                    operators.push(c);
                    ccflag = false;
                }
            }
        }
        while (operators.size() > 0) {
            if (operands.empty()) {
                System.out.println("Error: imbalanace in operands and operators");
                System.exit(1);
            }
            op = operators.pop();
            if (op == '.') {
                nfa2 = operands.pop();
                nfa1 = operands.pop();
                operands.push(concat(nfa1, nfa2));
            } else if (op == '|') {
                nfa2 = operands.pop();
                if (!operators.empty() && operators.peek() == '.') {
                    concatStack.push(operands.pop());
                    while (!operators.empty() && operators.peek() == '.') {
                        concatStack.push(operands.pop());
                        operators.pop();
                    }
                    nfa1 = concat(concatStack.pop(), concatStack.pop());
                    while (concatStack.size() > 0) {
                        nfa1 = concat(nfa1, concatStack.pop());
                    }
                } else {
                    nfa1 = operands.pop();
                }
                operands.push(union(nfa1, nfa2));
            }
        }
        return operands.pop();
    }


    /**
     * Union Operation
     *
     * @param n
     * @param m
     * @return
     */
    public static NFA union(NFA n, NFA m) {
        // The number of states of the new NFA is the number of states of the original NFA plus 2
        NFA result = new NFA(n.states.size() + m.states.size() + 2);
        // Add an empty transition from 0 to 1
        result.transitions.add(new Transition(0, 1, Constants.EPSILON));
        // Copy the transfer function of n into the new NFA
        for (Transition t : n.transitions) {
            result.transitions.add(new Transition(t.from + 1, t.to + 1, t.symbol));
        }
        // Add an empty transition from the final state of n to the final state of the new NFA
        result.transitions.add(new Transition(n.states.size(), n.states.size() + m.states.size() + 1, Constants.EPSILON));
        // Add an empty transition from 0 to the initial state of m
        result.transitions.add(new Transition(0, n.states.size() + 1, Constants.EPSILON));
        // Copy the transfer function of m to the new NFA
        for (Transition t : m.transitions) {
            result.transitions.add(new Transition(t.from + n.states.size() + 1, t.to + n.states.size() + 1, t.symbol));
        }
        // Add an empty transition from m's final state to the new NFA's final state
        result.transitions.add(new Transition(m.states.size() + n.states.size(), n.states.size() + m.states.size() + 1, Constants.EPSILON));
        // Set the final state of the new NFA
        result.finalState = n.states.size() + m.states.size() + 1;
        return result;
    }


    /**
     * Concat Operation
     *
     * @param n
     * @param m
     * @return
     */
    public static NFA concat(NFA n, NFA m) {
        int nsize = n.states.size();
        // Delete the initial state of m
        m.states.remove(0);
        // Copy the transfer function of m to n
        for (Transition t : m.transitions) {
            n.transitions.add(new Transition(t.from + nsize - 1, t.to + nsize - 1, t.symbol));
        }
        // Add the state of m to n
        for (Integer s : m.states) {
            n.states.add(s + nsize - 1);
        }
        // Set the final state of n
        n.finalState = nsize + m.states.size() - 1;
        return n;
    }

    /**
     * Kleene Operation
     *
     * @param n
     * @return
     */
    public static NFA kleene(NFA n) {
        // The number of states of the new NFA is the number of states of the original NFA plus 2
        NFA result = new NFA(n.states.size() + 2);
        // Add an empty transition from 0 to 1
        result.transitions.add(new Transition(0, 1, Constants.EPSILON));
        // Copy the transfer function of the original NFA to the new NFA
        for (Transition t : n.transitions) {
            result.transitions.add(new Transition(t.from + 1, t.to + 1, t.symbol));
        }
        // Add an empty transition from the original NFA final state to the new NFA final state
        result.transitions.add(new Transition(n.states.size(), n.states.size() + 1, Constants.EPSILON));
        // Add an empty transition from the final state of the original NFA to the initial state
        result.transitions.add(new Transition(n.states.size(), 1, Constants.EPSILON));
        // Add an empty transition from the initial state of the new NFA to the final state
        result.transitions.add(new Transition(0, n.states.size() + 1, Constants.EPSILON));
        // Set the final state of the new NFA
        result.finalState = n.states.size() + 1;
        return result;
    }

    public static void main(String[] args) {
        NFA nfa = Thompson.compile("a(b|c)*");
        nfa.display();
    }

}
