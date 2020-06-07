import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' || c == '!' || c == '^' || c == '>' || c == '<' || c == '≥' || c == '≠' || c == '≤' || c == '=');
    }

    void calculate() {
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        try {
            Scanner sc = new Scanner(new FileInputStream("expressions.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter("Answers.txt"));

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);

                    if (Character.isDigit(c)) {
                        int num = 0;
                        while (Character.isDigit(c)) {
                            num = num * 10 + (c - '0');
                            i++;
                            if (i < line.length())
                                c = line.charAt(i);
                            else
                                break;
                        }
                        i--;
                        values.push(num);
                    } else if (c == '(') {
                        operators.push(c);
                    } else if (c == ')') {
                        while (operators.peek() != '(') {
                            int answer = performOp(values, operators);
                            values.push(answer);
                        }
                        operators.pop();

                    } else if (isOperator(c)) {
                        while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                            int answer = performOp(values, operators);
                            values.push(answer);
                        }
                        operators.push(c); // current operator on stack
                    }
                }

                // perform all operations left

                while (!operators.isEmpty()) {
                    int answer = performOp(values, operators);
                    values.push(answer);
                }
                pw.println(line + "\nResult: " + values.pop() + "\n");

            }
            sc.close();
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private int performOp(Stack<Integer> values, Stack<Character> operators) {

        int a = values.pop();
        int b;
        char operator = operators.pop();

        boolean neg = false;
        if (values.isEmpty() || (!(operators.isEmpty()) && operators.peek() == '(')) {
            neg = true;
        }


        switch (operator) {
            case '!':
                return factorial(a);
            case '^':
                b = values.pop();
                return power(b, a);
            case '*':
                b = values.pop();
                return b * a;
            case '/':
                b = values.pop();
                if (a == 0) return -1;
                else return b / a;
            case '+':
                b = values.pop();
                return b + a;
            case '-':
                if (neg) return -a;
                b = values.pop();
                return b - a;
            case '<':
                b = values.pop();
                if (b < a) return 1;
                else return 0;
            case '>':
                b = values.pop();
                if (b > a) return 1;
                else return 0;
            case '≤':
                b = values.pop();
                if (b < a || b == a) return 1;
                else return 0;
            case '≥':
                b = values.pop();
                if (b > a || b == a) return 1;
                else return 0;
            case '≠':
                b = values.pop();
                if (b == a) return 0;
                else return 1;
            case '=':
                b = values.pop();
                if (b == a) return 1;
                else return 0;
        }

        return -1;
    }

    private int precedence(char c) {

        switch (c) {
            case '!':
                return 6;
            case '^':
                return 5;
            case '*':
            case '/':
                return 4;
            case '+':
            case '-':
                return 3;
            case '<':
            case '>':
            case '≤':
            case '≥':
                return 2;
            case '≠':
            case '=':
                return 1;
        }
        return -1;
    }


    static int factorial(int num) {
        if (num <= 1) {
            return 1;
        } else return num * factorial(num - 1);
    }

    static int power(int b, int a) {
        if (a == 0) return 1;
        int result = b;
        for (int i = 1; i < a; i++) {
            result = result * b;
        }
        return result;
    }

    public static void main(String[] args) {
        Calculator cal = new Calculator();
        cal.calculate();
    }
}
