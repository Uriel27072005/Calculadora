import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DoomCalculator extends JFrame implements ActionListener, KeyListener {

    private JTextField display;
    private JPanel panel;
    private StringBuilder currentInput;
    private boolean resetDisplay;

    public DoomCalculator() {
        setTitle("CalDOOMadora");
        setSize(300, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

       
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(Color.BLACK);
        
        
        int topMargin = 20;
        imagePanel.setBorder(BorderFactory.createEmptyBorder(topMargin, 0, 0, 0));

        ImageIcon leftIcon = new ImageIcon("C:\\Users\\zapup\\OneDrive\\Escriptori\\Calculadora\\cacodemonio.izquierda.png"); // Cambia "izquierda.png" al nombre de tu archivo de imagen de cacodemon izquierdo
        JLabel leftLabel = new JLabel(leftIcon);
        imagePanel.add(leftLabel, BorderLayout.WEST);

        // Load and resize logo
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\zapup\\OneDrive\\Escriptori\\Calculadora\\logo_bien.png"); // Utilizar la imagen cargada
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 135, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(logoLabel, BorderLayout.CENTER);

        ImageIcon rightIcon = new ImageIcon("C:\\Users\\zapup\\OneDrive\\Escriptori\\Calculadora\\cacodemonio.derecha.png"); // Cambia "derecha.png" al nombre de tu archivo de imagen de cacodemon derecho
        JLabel rightLabel = new JLabel(rightIcon);
        imagePanel.add(rightLabel, BorderLayout.EAST);

        
        add(imagePanel, BorderLayout.NORTH);

        // Initialize display
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 24));  
        display.setBackground(Color.BLACK);
        display.setForeground(Color.GREEN);
        display.setBorder(BorderFactory.createLineBorder(Color.RED, 3)); 
        display.addKeyListener(this); 

        
        Dimension displaySize = new Dimension(400, 50);
        display.setPreferredSize(displaySize);

        
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5)); 
        panel.setBackground(Color.DARK_GRAY);

        
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "←", "DOOM"
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Monospaced", Font.BOLD, 18));  
            button.setBackground(Color.RED);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
            button.addActionListener(this);
            panel.add(button);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(display, BorderLayout.NORTH);
        centerPanel.add(panel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        currentInput = new StringBuilder();
        resetDisplay = false;

        
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                display.requestFocusInWindow();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("DOOM")) {
            runDoom();
            return;
        }

        if (command.equals("C")) {
            currentInput.setLength(0);
            display.setText("");
            resetDisplay = false;
            return;
        }

        if (command.equals("←")) {
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1);
                display.setText(currentInput.toString());
            }
            return;
        }

        if (command.equals("=")) {
            calculateResult();
            return;
        }

        if ("/*-+".contains(command)) {
            if (resetDisplay) {
                resetDisplay = false;
            }
            if (currentInput.length() > 0) {
                currentInput.append(" ").append(command).append(" ");
                display.setText(currentInput.toString());
            }
        } else {
            if (resetDisplay) {
                currentInput.setLength(0);
                resetDisplay = false;
            }
            currentInput.append(command);
            display.setText(currentInput.toString());
        }
    }

    private void calculateResult() {
        try {
            String result = eval(currentInput.toString());
            display.setText(result);
            currentInput.setLength(0);
            currentInput.append(result);
            resetDisplay = false;
        } catch (Exception ex) {
            display.setText("Error");
            currentInput.setLength(0);
        }
    }

    
    private String eval(String expression) throws Exception {
        String[] tokens = expression.split(" ");
        double result = Double.parseDouble(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double operand = Double.parseDouble(tokens[i + 1]);

            switch (operator) {
                case "+": result += operand; break;
                case "-": result -= operand; break;
                case "*": result *= operand; break;
                case "/": result /= operand; break;
                default: throw new Exception("Unknown operator: " + operator);
            }
        }

        return String.valueOf(result);
    }

    @SuppressWarnings("deprecation")
    private void runDoom() {
        String doomPath = "C:\\Users\\zapup\\OneDrive\\Escriptori\\Calculadora\\rerelease\\DOOM.exe";
        try {
            System.out.println("Trying to run DOOM from: " + doomPath);
            Process process = Runtime.getRuntime().exec(doomPath);
            process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        char keyChar = e.getKeyChar();

        if (Character.isDigit(keyChar) || keyChar == '.') {
            if (resetDisplay) {
                currentInput.setLength(0);
                resetDisplay = false;
            }
            currentInput.append(keyChar);
            display.setText(currentInput.toString());
        } else if (keyChar == '+' || keyChar == '-' || keyChar == '*' || keyChar == '/') {
            if (resetDisplay) {
                resetDisplay = false;
            }
            if (currentInput.length() > 0) {
                currentInput.append(" ").append(keyChar).append(" ");
                display.setText(currentInput.toString());
            }
        } else if (keyChar == KeyEvent.VK_ENTER || keyChar == '=') {
            calculateResult();
        } else if (keyChar == KeyEvent.VK_BACK_SPACE) {
            if (currentInput.length() > 0) {
                currentInput.setLength(currentInput.length() - 1);
                display.setText(currentInput.toString());
            }
        } else if (keyChar == KeyEvent.VK_ESCAPE) {
            currentInput.setLength(0);
            display.setText("");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DoomCalculator calculator = new DoomCalculator();
            calculator.setVisible(true);
        });
    }
}

