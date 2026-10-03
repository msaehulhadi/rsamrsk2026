package widget;

import javax.swing.*;
import java.awt.*;

public class Toast {

    private static final String APP_NAME = "🩺 SIMRS Khanza";

//    public static void show(String message, ToastType type, int durationMillis) {
    public static void show(Window parent, String message, ToastType type, int durationMillis){
        JWindow window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0)); // support transparansi

        // Warna lembut & ikon
        Color backgroundColor;
        Icon icon;

        switch (type) {
            case SUCCESS:
                backgroundColor = new Color(198, 239, 206); // pastel green
                icon = UIManager.getIcon("OptionPane.informationIcon");
                break;
            case ERROR:
                backgroundColor = new Color(255, 205, 210); // pastel red
                icon = UIManager.getIcon("OptionPane.errorIcon");
                break;
            case WARNING:
                backgroundColor = new Color(255, 245, 204); // pastel yellow
                icon = UIManager.getIcon("OptionPane.warningIcon");
                break;
            case INFO:
            default:
                backgroundColor = new Color(204, 229, 255); // pastel blue
                icon = UIManager.getIcon("OptionPane.informationIcon");
                break;
        }

        // Header label
        JLabel titleLabel = new JLabel(APP_NAME);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        titleLabel.setForeground(Color.DARK_GRAY);

//        // Baris isi
//        JLabel iconLabel = new JLabel(icon);
//        JTextArea textLabel = new JTextArea(message);
////        textLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
//        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        textLabel.setForeground(Color.DARK_GRAY);
//        textLabel.setOpaque(false);
//        textLabel.setEditable(false);
//        textLabel.setFocusable(false);
//        textLabel.setWrapStyleWord(true);
//        textLabel.setLineWrap(true);
//        textLabel.setColumns(25); // lebar maksimum

        // Baris isi
        JLabel iconLabel = new JLabel(icon);

        JTextArea textLabel = new JTextArea(message);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(Color.DARK_GRAY);
        textLabel.setOpaque(false);
        textLabel.setEditable(false);
        textLabel.setFocusable(false);
        textLabel.setWrapStyleWord(true);
        textLabel.setLineWrap(true);
        textLabel.setColumns(25);       // lebar minimal
        textLabel.setRows(1);             // tinggi minimal

        // Scroll pane untuk text
        JScrollPane scrollPane = new JScrollPane(textLabel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Buat background scroll transparan biar nyatu
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

//        // Atur ukuran maksimum toast
//        scrollPane.setPreferredSize(new Dimension(300, 150)); // Lebar & tinggi maksimum
//        scrollPane.setMaximumSize(new Dimension(300, 150));

        // Hitung tinggi ideal teks
        textLabel.setColumns(25); // lebar kira-kira
        textLabel.setRows(0);     // biar tinggi menyesuaikan
        Dimension textSize = textLabel.getPreferredSize();

        // Tentukan batas maksimal tinggi
        int maxHeight = 150;
        int finalHeight = Math.min(textSize.height + 20, maxHeight); // +10 padding

        scrollPane.setPreferredSize(new Dimension(300, finalHeight));

        // Panel baris isi
        JPanel rowPanel = new JPanel(new BorderLayout(5, 0));
        rowPanel.setOpaque(false);
        rowPanel.add(iconLabel, BorderLayout.WEST);
        rowPanel.add(scrollPane, BorderLayout.CENTER);        
        
//        JPanel rowPanel = new JPanel(new BorderLayout(5, 0));
//        rowPanel.setOpaque(false);
//        rowPanel.add(iconLabel, BorderLayout.WEST);
//        rowPanel.add(textLabel, BorderLayout.CENTER);

//        // Panel utama
//        JPanel content = new JPanel() {
//            protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
////                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
//                g2.setColor(backgroundColor);
//                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
//                g2.dispose();
//                super.paintComponent(g);
//            }
//        };

        // Panel utama dengan background + shadow
        JPanel content = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                int arc = 20;
                int shadowSize = 6;

                // Gambar shadow (bayangan hitam transparan)
                g2.setColor(new Color(0, 0, 0, 80)); // warna shadow
                g2.fillRoundRect(shadowSize, shadowSize,
                        getWidth() - shadowSize, getHeight() - shadowSize,
                        arc, arc);

                // Gambar background utama
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth() - shadowSize,
                        getHeight() - shadowSize, arc, arc);

                g2.dispose();
                super.paintComponent(g);
            }
        };


        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        content.setLayout(new BorderLayout(0, 5));
        content.add(titleLabel, BorderLayout.NORTH);
        content.add(rowPanel, BorderLayout.CENTER);

        window.setContentPane(content);
        window.pack();

        // Posisi kanan atas
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - window.getWidth() - 30;
        int y = 120;
        window.setLocation(x, y);
        window.setAlwaysOnTop(true);
        window.setOpacity(0f); // Mulai transparan
        window.setVisible(true);

        // Fade in (0.0 ➝ 1.0)
        Timer fadeIn = new Timer(20, null);
        fadeIn.addActionListener(e -> {
            float opacity = window.getOpacity();
            if (opacity < 1f) {
                window.setOpacity(Math.min(opacity + 0.05f, 1f));
            } else {
                fadeIn.stop();

                // Tunggu durasi utama, lalu mulai fade out
                new Timer(durationMillis, ev -> startFadeOut(window)).start();
            }
        });
        fadeIn.start();
    }

    private static void startFadeOut(JWindow window) {
        Timer fadeOut = new Timer(20, null);
        fadeOut.addActionListener(e -> {
            float opacity = window.getOpacity();
            if (opacity > 0f) {
                window.setOpacity(Math.max(opacity - 0.05f, 0f));
            } else {
                ((Timer) e.getSource()).stop();
                window.dispose();
            }
        });
        fadeOut.start();
    }

    // Versi default
//    public static void show(String message, ToastType type) {
//        show(message, type, 3000);
//    }
    public static void show(String message, ToastType type) {
        Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        show(window, message, type, 5000);
    }
    
    public static void showWDurasi(String message, ToastType type, int duration) {
        Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        show(window, message, type, duration);
    }

    public static void valid(JTextField teks,String message, ToastType type) {
        show("Maaf, "+message+" tidak boleh kosong...!!!",type);
        teks.requestFocus();
    }

}
