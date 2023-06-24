import Inventory.Inventory;
import Inventory.productDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateDialog extends JDialog {

    private JButton btnUpdate;
    private JButton btnCancel;
    private JTextField textPId;
    private JTextField textPName;
    private JTextField textDescription;
    private JTextField textQuantity;
    private JLabel labelBId;
    private JLabel labelBName;
    private JLabel labelDescription;
    private JLabel labelQuantity;
    private JPanel panelUpdate;
    private JLabel statusmsg;
    public static int one_count=0;

    UpdateDialog(Inventory b){
        setTitle("Update Inventorys");
        setContentPane(panelUpdate);
        setMinimumSize(new Dimension(400,200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        one_count++;

        textPId.setText(""+b.getProductid());
        textPName.setText(""+b.getPname());
        textDescription.setText(b.getDescription());
        textQuantity.setText(""+b.getQuantity());

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                one_count--;
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(
                            textPId.getText().equals("")
                                    || textPName.getText().equals("")
                                    || textDescription.getText().equals("")
                                    || textQuantity.getText().equals("")
                    ){
                        statusmsg.setText("Enter ALL the field before adding");
                    }
                    else {
                        int initproductid = b.getProductid();
                        int productid = Integer.parseInt(textPId.getText());
                        String name = textPName.getText();
                        String Description = textDescription.getText();
                        int quantity = Integer.parseInt(textQuantity.getText());
                        productDAO bk = new productDAO();
                        Connection conn = bk.Connect();
                        PreparedStatement pst = conn.prepareStatement("UPDATE value SET  name=?, pro_id=?, quantity=?, Description=?  WHERE pro_id=?;");
                        pst.setString(1, name);
                        pst.setInt(2, productid);
                        pst.setInt(3, quantity);
                        pst.setString(4, Description);
                        pst.setInt(5, initproductid);
                        pst.executeUpdate();
                        ViewValues.model.setRowCount(0);
                        productDAO ba = new productDAO();
                        Connection con = ba.Connect();
                        Statement stmt = con.createStatement();

                        ResultSet res = stmt.executeQuery("SELECT * FROM value");
                        ViewValues.printTable(res);
                        stmt.close();
                        ba.Disconnect();

                        pst.close();
                        bk.Disconnect();
                        dispose();
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);

    }




}
