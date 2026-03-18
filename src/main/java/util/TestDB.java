package util;

public class TestDB {
    public static void main(String[] args) {
        try {
            System.out.println("Testing DB Connection...");
            DBConnection.getConnection();
            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
