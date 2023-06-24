import java.sql.*;
import java.util.ArrayList;

public class DBconfig {
    private String url = "jdbc:mysql://localhost:3306/";
    private String user = DBCredential.user;
    private String pass = DBCredential.pass;
    DBconfig(){
        try{
            Connection con = DriverManager.getConnection(url, user, pass);
            String query = "SHOW DATABASES;";
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);
            ArrayList<String> list = new ArrayList<>();
            while(res.next()){
                list.add(res.getString(1));
            }
            if(!list.contains("inventory")){
                query = "CREATE DATABASE inventory;";
                stmt.executeUpdate(query);

                query = "USE inventory;";
                stmt.executeUpdate(query);

                query = "CREATE TABLE value (\n" +
                        "  name VARCHAR(20),\n" +
                        "  pro_ID INT(6) PRIMARY KEY,\n" +
                        "  quantity INT(5),\n" +
                        "  Description varchar(200)\n" +
                        ");";
                stmt.executeUpdate(query);
                addProducts(con);
            }
            else{
                query = "USE inventory;";
                stmt.executeUpdate(query);

                query = "SHOW TABLES;";
                res =stmt.executeQuery(query);
                list.clear();
                while(res.next()){
                    list.add(res.getString(1));
                }
                if(!list.contains("values")){
                    query = "CREATE TABLE value (\n" +
                            "  name VARCHAR(20),\n" +
                            "  pro_ID INT(6) PRIMARY KEY,\n" +
                            "  quantity INT(5),\n" +
                            "  Description varchar(200)\n" +
                            ");";
                    stmt.executeUpdate(query);
                    addProducts(con);
                }
            }
            res.close();
            stmt.close();
            con.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addProducts(Connection con) {
        try {
            String sql = "INSERT INTO value (name, pro_ID, quantity, Description) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = con.prepareStatement(sql);

            // Sample product values
            String[][] products = {
                    {"Sony Camera", "100001", "50", "108MP, with detailed specifications."},
                    {"Samsung Smart watch", "100002", "20", "An round OLED display, with heart and  oxygen rate sensor."},
                    {"Sony Smart LED TV", "100003", "10", "75'inch 4K display, with 120Hz refresh rate."},
                    {"Apple iPhone 12", "100004", "30", "6.1'inch OLED display, with 5G support."},
                    {"Apple MacBook Pro", "100005", "15", "13'inch 4K display, with 16GB RAM."}

            };

            for (String[] product : products) {
                statement.setString(1, product[0]);
                statement.setInt(2, Integer.parseInt(product[1]));
                statement.setInt(3, Integer.parseInt(product[2]));
                statement.setString(4, product[3]);
                statement.executeUpdate();
            }

            System.out.println("Products inserted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
