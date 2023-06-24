import java.io.BufferedReader;
import java.io.FileReader;
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

                query = "CREATE TABLE dictionary (word varchar(255));";
                stmt.executeUpdate(query);
                addWords(con);
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
                    query = "CREATE TABLE dictionary (word varchar(255));";
                    stmt.executeUpdate(query);
                    addWords(con);
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

    private void addWords(Connection con){
        try{
            String filePath = "All words.txt";
            String sql = "INSERT INTO dictionary VALUES (?);";
            PreparedStatement statement = con.prepareStatement(sql);
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                statement.setString(1, line);
                statement.executeUpdate();
            }
            reader.close();
            System.out.println("Words inserted successfully.");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
