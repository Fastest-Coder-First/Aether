import Inventory.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewValues extends JFrame {
    private JPanel bookView;
    private JTable tableBooks;
    private JTextField textSearch;
    private JButton btnBack;
    private JLabel labelId;
    private JLabel labelName;
    private JLabel labelDescription;
    private JLabel labelQuantity;
    private JLabel dLabelId;
    private JLabel dLabelName;
    private JLabel dLabelDescription;
    private JLabel dLabelQuantity;
    private JButton btnRefresh;
    private JPanel panelPreview;
    private JPanel panelSearch;
    private JPanel panelBtn;
    private JLabel labelSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    public JLabel statusmsg;
    public static DefaultTableModel model;

    ViewValues(){
        setTitle("Values Available");
        setContentPane(bookView);
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        try{
            productDAO b = new productDAO();
            Connection conn = b.Connect();
            Statement stmt = conn.createStatement();

            ResultSet res = stmt.executeQuery("SELECT * FROM value");
            ResultSetMetaData rsmd = res.getMetaData();

            model = (DefaultTableModel) tableBooks.getModel();

            int cols = rsmd.getColumnCount();
            cols++;
            String[] colnames = new String[cols];
            colnames[0] = "S. No.";
            for(int i=1;i<cols;i++){
                colnames[i]= rsmd.getColumnName(i);
            }
            model.setColumnIdentifiers(colnames);
            printTable(res);
            b.Disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout h = new layout();
                dispose();
            }
        });

        tableBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableBooks.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) tableBooks.getModel();

                dLabelName.setText(model.getValueAt(i,1).toString());
                dLabelId.setText(model.getValueAt(i,2).toString());
                dLabelQuantity.setText(model.getValueAt(i,3).toString());
                dLabelDescription.setText(model.getValueAt(i,4).toString());

            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dLabelId.setText("----");
                dLabelName.setText("----");
                dLabelDescription.setText("----");
                dLabelQuantity.setText("----");
                textSearch.setText(null);
                tableBooks.getSelectionModel().clearSelection();

                try{
                    model.setRowCount(0);
                   productDAO ba = new productDAO();
                    Connection con = ba.Connect();
                    Statement stmt = con.createStatement();

                    ResultSet res = stmt.executeQuery("SELECT * FROM value");
                    ViewValues.printTable(res);
                    stmt.close();
                    ba.Disconnect();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        textSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String keyword = textSearch.getText();
                search(keyword);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dLabelId.getText().equals("----")){
                    statusmsg.setText("Select an entry before updating");
                }
                else if(UpdateDialog.one_count>=1){
                    statusmsg.setText("Execute or cancel the previous update window");
                }
                else{
                    try {
                        Inventory b = new Inventory();
                        b.setProductid(Integer.parseInt(dLabelId.getText()));
                        b.setPname(dLabelName.getText().toString());
                        b.setDescription(dLabelDescription.getText());
                        b.setQuantity(Integer.parseInt(dLabelQuantity.getText()));
                        UpdateDialog u = new UpdateDialog(b);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                statusmsg.setText("Inventory Database");
            }
        };
        btnBack.addMouseListener(listener);
        btnRefresh.addMouseListener(listener);
        btnUpdate.addMouseListener(listener);
        btnDelete.addMouseListener(listener);

        btnRefresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Refresh the fields");
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Go back to Home Page");
            }
        });
        btnUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Update the selected Record");
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statusmsg.setText("Delete the Selected Record");
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dLabelId.getText().equals("----")){
                    statusmsg.setText("Select an entry before Deleting");
                }
                else{
                    try{
                        String pro_ID = dLabelId.getText();
                        String query1 = "SELECT * FROM value WHERE pro_ID=? ;";
                        IssueDAO ib = new IssueDAO();
                        Connection conn = ib.Connect();
                        PreparedStatement pst = conn.prepareStatement(query1);
                        pst.setString(2,pro_ID);
                        ResultSet res = pst.executeQuery();

                        if(res.next()){
                            boolean flag = false;
                            do{
                                if(res.getString(6).equals("Pending")){
                                    flag = true;
                                    statusmsg.setText("Value Cannot be deleted. It is currently issued to some reader(s)");
                                    break;
                                }
                            }while(res.next());
                            if(!flag){
                                int ans = JOptionPane.showOptionDialog(bookView,"Are you sure to Delete this Value?","Confirm Deletion",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Yes, Do it","No, Don't"},1);
                                //fire query for deletion
                                if(ans==JOptionPane.YES_OPTION){
                                    String query2 = "DELETE FROM value WHERE  pro_ID=?;";
                                   productDAO bd = new productDAO();
                                    Connection conn1 = bd.Connect();
                                    PreparedStatement pst1 = conn1.prepareStatement(query2);
                                    pst1.setString(2, pro_ID);
                                    int count = pst1.executeUpdate();
                                    statusmsg.setText(count + " row(s) affected. Value deleted from Inventory.");
                                    pst1.close();
                                    bd.Disconnect();
                                    model.setRowCount(0);
                                   productDAO ba = new productDAO();
                                    Connection con = ba.Connect();
                                    Statement stmt = con.createStatement();

                                    ResultSet res1 = stmt.executeQuery("SELECT * FROM value");
                                    printTable(res1);
                                    stmt.close();
                                    ba.Disconnect();
                                }
                                else{
                                    statusmsg.setText("Deletion Canceled");
                                }
                            }
                            else{
                                statusmsg.setText("Value Cannot be deleted. It is currently issued to some reader(s)");
                            }
                        }
                        else{
                            int ans = JOptionPane.showOptionDialog(bookView,"Are you sure to Delete this Product?","Confirm Deletion",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Yes, Do it","No, Don't"},1);
                            //fire query for deletion
                            if(ans==JOptionPane.YES_OPTION){
                                String query2 = "DELETE FROM value WHERE  pro_ID=?;";
                               productDAO bd = new productDAO();
                                Connection conn1 = bd.Connect();
                                PreparedStatement pst1 = conn1.prepareStatement(query2);
                                pst1.setString(2, pro_ID);
                                int count = pst1.executeUpdate();
                                statusmsg.setText(count + " row(s) affected. Value deleted from Inventory.");
                                pst1.close();
                                bd.Disconnect();
                                model.setRowCount(0);
                               productDAO ba = new productDAO();
                                Connection con = ba.Connect();
                                Statement stmt = con.createStatement();

                                ResultSet res1 = stmt.executeQuery("SELECT * FROM value");
                                ViewValues.printTable(res1);
                                stmt.close();
                                ba.Disconnect();
                            }
                            else{
                                statusmsg.setText("Deletion Canceled");
                            }
                        }

                        pst.close();
                        ib.Disconnect();
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }
    public static void printTable(ResultSet res){
        try{
            String stdid,name,Description,quantity;
            int i=0;
            while(res.next()){
                i++;
                stdid=res.getString(1);
                name=res.getString(2);
                Description=res.getString(3);
                quantity=res.getString(4);
                String[] row = {i+"",stdid, name, Description, quantity};
                model.addRow(row);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void search(String str){
        model = (DefaultTableModel) tableBooks.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        tableBooks.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter("(?i)" + str));
    }
}
