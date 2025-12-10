//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main{
    private static final String url = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        System.out.println(USER);
        System.out.println(PASS);
    }
}
