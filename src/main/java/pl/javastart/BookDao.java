package pl.javastart;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookDao {

    private static final String URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";
    private Connection connection;

    public BookDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found");
        } catch (SQLException e) {
            System.out.println("Could not establish connection");
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Book book)throws InputMismatchException {
        final String query = "INSERT INTO library.books SET title = ?, author = ?, year = ?, isbn = ?";

        try {
            PreparedStatement prepStmt = connection.prepareStatement(query);
            prepStmt.setString(1, book.getTitle());
            prepStmt.setString(2, book.getAuthor());
            prepStmt.setInt(3, book.getYear());
            prepStmt.setString(4, book.getIsbn());
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not save record");
            e.printStackTrace();
        }catch (InputMismatchException e){
            System.out.println("Wrong type");
            e.printStackTrace();
        }
    }

    public Book read(String isbn){
        final String sql = "select ID, title, author, year,isbn from library.books where isbn = ?";

        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, isbn);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                Book book = new Book();
                book.setId(result.getInt(1));
                book.setTitle(result.getString(2));
                book.setAuthor(result.getString(3));
                book.setYear(result.getInt(4));
                book.setIsbn(result.getString(5));
                System.out.println(book.toString());
                return book;
            }
        } catch (SQLException e) {
            System.out.println("Could not get employee");
        }
        return null;
    }

    public void update(Book book) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        final String sql = "update library.books set title=?, author=?, year=?, isbn = ? where id = ?";
        PreparedStatement prepStmt = connection.prepareStatement(sql);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getYear());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setLong(5, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not update record");
            e.printStackTrace();
        }catch (InputMismatchException e){
            System.out.println("Wrong type");
        }
    }

    public void delete(Long id) {
        final String sql = "delete from library.books where id = ?";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not delete row");
        }
    }
}
