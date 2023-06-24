import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class layout extends JFrame {
    private JLabel statusmsg;
    private JPanel welcome;
    private JPanel content;
    private JPanel footer;
    private JButton btnViewBooks;
    private JButton btnaddBook;
    private JButton btnexit;


    layout(){
        setTitle("Home Page");
        setContentPane(welcome);
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        btnViewBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewValues v = new ViewValues();
                dispose();
            }
        });

        btnaddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBooks ab = new AddBooks();
                dispose();
            }
        });


        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                statusmsg.setText("Welcome To Inventory");
            }
        };
        btnViewBooks.addMouseListener(listener);
        btnaddBook.addMouseListener(listener);
        btnexit.addMouseListener(listener);


        btnViewBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("View and Search Values from here");
            }
        });
        btnaddBook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Values to the Inventory can be added here");
            }
        });

        btnexit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Exit to the login page");
            }
        });
        setVisible(true);
    }
}
