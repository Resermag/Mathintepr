package Inlogic;

import java.util.Arrays;
import java.util.LinkedList;


public class Parser {
    private static int i;
    private static String Inpu;
    private static int Numbofbr = 0;
    private static int Typeoferr = 0;
    private static int Posoferr = 0;
    private static int Numbofpoint = 0;

    private static boolean isDelim(char c) {
        return c == ' ';
    }

    private static void Error_info() {
        switch (Typeoferr) {
            case 1:
                GUI.Result.setText("Количество закрывающих скобок больше, чем открывающих");
                GUI.Inp.requestFocusInWindow();
                GUI.Inp.setCaretPosition(Posoferr);
                break;
            case 2:
                GUI.Result.setText("Количество открывающих скобок больше закрывающих");
                break;
            case 3:
                GUI.Result.setText("Два оператора подряд");
                GUI.Inp.requestFocusInWindow();
                GUI.Inp.setCaretPosition(Posoferr);
                break;
            case 4:
                GUI.Result.setText("Начало с оператора");
                GUI.Inp.requestFocusInWindow();
                GUI.Inp.setCaretPosition(Posoferr);
                break;
            case 5:
                GUI.Result.setText("Деление на ноль");
            case 6:
                GUI.Result.setText("Неверный формат функции");
                GUI.Inp.requestFocusInWindow();
                GUI.Inp.setCaretPosition(Posoferr);
            case 7:
                GUI.Result.setText("Нельзя извлекать квадратный корень из отрицательного числа");
                GUI.Inp.requestFocusInWindow();
                GUI.Inp.setCaretPosition(Posoferr);
                default:
                    GUI.Result.setText("Неизвестная ошибка");
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '*' || c == '/' || c == '%' || c == '^'
                || c == '-';
    }

    private static double Fsqrt() {
        double Res = 0;
        if (Inpu.charAt(i) != '-') {
            Res = Math.pow(Parsdoub(), 0.5);
            i++;
        } else {
            Typeoferr=7;
            Posoferr=i;
        }
        return Res;
    }

    private static LinkedList<Double> parseEloffunc() {
        LinkedList<Double> Res = new LinkedList<Double>();
        while (Inpu.charAt(i) != ')' && (Character.isDigit(Inpu.charAt(i)) || Inpu.charAt(i) == '-')) {
            if (Inpu.charAt(i) == '-') {
                i++;
                Res.add((-1) * Parsdoub());
            } else {
                Res.add(Parsdoub());
            }
            if (Inpu.charAt(i + 1) == ',') {
                i += 2;
            } else {
                i++;
            }
        }
        return Res;
    }


    private static double Fminormax(String operand) {
        double Res;
        Object[] temp;
        temp = parseEloffunc().toArray();
        Arrays.sort(temp);
        if (operand.equals("min")) {
            Res = Parsdoub(temp[0].toString());
        } else {
            Res = Parsdoub(temp[temp.length - 1].toString());
        }
        return Res;
    }

    private static double Fsum() {
        double Res = 0;
        LinkedList<Double> Slag = new LinkedList<Double>();
        Slag.addAll(parseEloffunc());
        while (Slag.size() != 0) {
            Res = Res + Slag.getFirst();
            Slag.removeFirst();
        }
        return Res;
    }

    private static double Funct() {
        double Res = 0;
        String operand = "";
        while (i < Inpu.length()
                && Character.isLetter(Inpu.charAt(i))) {
            operand += Inpu.charAt(i++);
        }
        if (Inpu.charAt(i++) != '(') {
            Typeoferr = 6;
            Posoferr = i;
        } else {
            if (!Character.isDigit(Inpu.charAt(i)) && Inpu.charAt(i) != '-') {
                Typeoferr = 6;
                Posoferr = i;
            } else {
                if (operand.equals("sqrt")) {
                    Res = Fsqrt();
                }
                if (operand.equals("max") || operand.equals("min")) {
                    Res = Fminormax(operand);
                }
                if (operand.equals("sum")) {
                    Res = Fsum();
                }
            }
            if (Inpu.charAt(i) != ')' && Typeoferr == 0) {
                Typeoferr = 6;
                Posoferr = i;
            }
        }
        return Res;
    }

    private static double Parsdoub() {
        String operand = "";
        while (i < Inpu.length()
                && (Character.isDigit(Inpu.charAt(i)) || (Inpu.charAt(i) == '.'))) {
            if (Inpu.charAt(i) == '.') {
                Numbofpoint++;
            }
            operand += Inpu.charAt(i++);
        }
        --i;
        return Double.parseDouble(operand);
    }

    private static double Parsdoub(String In) {
        String operand = "";
        int j = 0;
        boolean min = false;
        if (In.charAt(j) == '-') {
            min = true;
            j++;
        }
        while (j < In.length()
                && (Character.isDigit(In.charAt(j)) || (In.charAt(j) == '.'))) {
            if (In.charAt(j) == '.') {
                Numbofpoint++;
            }
            operand += In.charAt(j++);
        }
        --j;
        if (!min) {
            return Double.parseDouble(operand);
        } else {
            return (-1) * Double.parseDouble(operand);
        }
    }

    private static int priority(char op) {

        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    private static void processOperator(LinkedList<Double> st, char op) {
        Double r = st.removeLast();
        Double l = st.removeLast();
        switch (op) {
            case '+':
                st.add(l + r);
                break;
            case '-':
                st.add(l - r);
                break;
            case '*':
                st.add(l * r);
                break;
            case '/':
                st.add(l / r);
                break;
            case '%':
                st.add(l % r);
                break;
            case '^':
                st.add(Math.pow(l, r));
                break;
        }
    }


    public static void eval(String s) {
        Inpu = s;
        LinkedList<Double> st = new LinkedList<Double>();
        LinkedList<Character> op = new LinkedList<Character>();
        for (i = 0; i < Inpu.length(); i++) {
            Numbofpoint = 0;
            char c = Inpu.charAt(i);
            if (isDelim(c) && Typeoferr == 0)
                continue;
            if (Character.isLetter(c)) {
                st.add(Funct());
                continue;
            }
            if (c == '(' && Typeoferr == 0) {
                op.add('(');
                Numbofbr++;
            } else if (c == ')') {
                Numbofbr--;
                if (Numbofbr < 0) {
                    Typeoferr = 1;
                    Posoferr = i;
                    break;
                }
                while (op.getLast() != '(')
                    processOperator(st, op.removeLast());
                op.removeLast();
            } else if (isOperator(c)) {
                if (i == 0 && c != '-') {
                    Typeoferr = 4;
                    Posoferr = i;
                    break;
                }
                if (i != 0)
                    if (isOperator(Inpu.charAt(i - 1))) {
                        Typeoferr = 3;
                        Posoferr = i;
                        break;
                    }
                if (c != '-') {
                    while (!op.isEmpty()
                            && priority(op.getLast()) >= priority(c))
                        processOperator(st, op.removeLast());
                    op.add(c);
                } else {
                    if (i == 0 || Inpu.charAt(i - 1) == '(') {
                        if (isOperator(Inpu.charAt(i + 1))) {
                            Typeoferr = 3;
                            Posoferr = i;
                            break;
                        }
                        i++;
                        st.add((-1) * Parsdoub());
                    } else {
                        while (!op.isEmpty()
                                && priority(op.getLast()) >= priority(c))
                            processOperator(st, op.removeLast());
                        op.add(c);
                    }
                }
            } else {
                st.add(Parsdoub());
            }
        }
        if (Numbofbr > 0) {
            Typeoferr = 2;
        }
        while (!op.isEmpty() && Typeoferr == 0)
            processOperator(st, op.removeLast());
        if (st.getFirst().isInfinite()) {
            Typeoferr = 5;
        }
        if (Typeoferr == 0) {
            GUI.Result.setText(st.get(0).toString());
        } else {
            Error_info();
        }
    }
}