package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/AppData/Local/TimelyToDos/todolist.db";
	private static Connection connection;
	
    public static void initializeDatabase() {
        try {
        	File dbDir = new File(System.getProperty("user.home") + "/AppData/Local/TimelyToDos");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            dbDir.setWritable(true, false);
            dbDir.setReadable(true, false);
            dbDir.setExecutable(true, false);
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load SQLite JDBC driver", e);
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
        	
            // Create the tasks table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS tasks (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "text TEXT NOT NULL, " +
                                    "color TEXT NOT NULL, " +
                                    "is_completed BOOLEAN NOT NULL DEFAULT 0)";
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static int addTask(String text, String color) {
        String sql = "INSERT INTO tasks(text, color) VALUES(?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, text);
            pstmt.setString(2, color);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
        return -1; // Return -1 if insertion failed
    }

    public static ResultSet getAllTasks() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM tasks");
        } catch (SQLException e) {
            System.out.println("Error getting tasks: " + e.getMessage());
            return null;
        }
    }

    public static void updateTask(int id, String text, String color, boolean isCompleted) {
        String sql = "UPDATE tasks SET text = ?, color = ?, is_completed = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, text);
            pstmt.setString(2, color);
            pstmt.setBoolean(3, isCompleted);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    public static void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }
    public static void updateTaskCompletion(int id, boolean isCompleted) {
        String sql = "UPDATE tasks SET is_completed = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, isCompleted);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating task completion: " + e.getMessage());
        }
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}

