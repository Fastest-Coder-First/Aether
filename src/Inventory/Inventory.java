package Inventory;

public class Inventory {
    private int Pro_id;
    private String pname;
    private String description;
    private int quantity;

    public int getProductid() {
        return Pro_id;
    }

    public void setProductid(int Pro_id) {
        this.Pro_id = Pro_id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String bname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String author) {
        this.description = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
