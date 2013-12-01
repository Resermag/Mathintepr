package Inlogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    /**
     * @param args
     */

    static JTextField Inp = new JTextField();
    private static JFrame Main = new JFrame("Калькулятор");
    static JLabel Info = new JLabel("Введите математическое выражение");
    private static JButton Run = new JButton("Вычислить");
    static JLabel Result = new JLabel("");

    public static void main(String[] args) {
        Main.setBounds(0, 0, 400, 200);
        Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main.setVisible(true);
        Main.setLayout(new GridLayout(5, 0));
        Main.add(Info);
        Inp.setSize(100, 50);
        Main.add(Inp);
        Main.add(Run);
        Main.add(Result);
        Run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Inp.getText().equals("")) {
                    Result.setText("Пустое поле");
                    Inp.requestFocusInWindow();
                } else
                    Parser.eval(Inp.getText());
            }
        });
    }

}
