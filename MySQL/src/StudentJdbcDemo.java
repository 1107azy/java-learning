import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentJdbcDemo {
    // 数据库连接信息（MySQL 8.0+）
    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";         // 你的MySQL用户名
    private static final String PASSWORD = "123456";   // 你的MySQL密码

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. 获取数据库连接（JDBC4.0+ 自动加载驱动，无需 Class.forName）
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("===== 数据库连接成功 =====");

            // 2. 插入3条学生记录
            insertStudents(conn);

            // 3. 查询所有学生并打印
            queryAllStudents(conn);

            // 4. 根据ID更新分数（更新ID=1的学生分数为99.5）
            updateStudentScore(conn, 1, 99.5);

            // 5. 根据ID删除学生（删除ID=3的学生）
            deleteStudentById(conn, 3);

            // 6. 再次查询，验证更新和删除结果
            System.out.println("\n===== 操作后最终数据 =====");
            queryAllStudents(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入3条学生记录
     */
    private static void insertStudents(Connection conn) throws SQLException {
        String sql = "INSERT INTO students(name, age, score) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // 第一条
        pstmt.setString(1, "张三");
        pstmt.setInt(2, 20);
        pstmt.setDouble(3, 90.0);
        int row1 = pstmt.executeUpdate();

        // 第二条
        pstmt.setString(1, "李四");
        pstmt.setInt(2, 21);
        pstmt.setDouble(3, 85.5);
        int row2 = pstmt.executeUpdate();

        // 第三条
        pstmt.setString(1, "王五");
        pstmt.setInt(2, 19);
        pstmt.setDouble(3, 88.0);
        int row3 = pstmt.executeUpdate();

        System.out.println("\n===== 插入操作 =====");
        System.out.println("成功插入 " + (row1 + row2 + row3) + " 条记录");
        pstmt.close();
    }

    /**
     * 查询所有学生并打印
     */
    private static void queryAllStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("\n===== 查询所有学生 =====");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            double score = rs.getDouble("score");
            System.out.println("ID：" + id + "，姓名：" + name + "，年龄：" + age + "，分数：" + score);
        }

        rs.close();
        pstmt.close();
    }

    /**
     * 根据ID更新学生分数
     */
    private static void updateStudentScore(Connection conn, int id, double newScore) throws SQLException {
        String sql = "UPDATE students SET score = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, newScore);
        pstmt.setInt(2, id);

        int rows = pstmt.executeUpdate();
        System.out.println("\n===== 更新操作 =====");
        System.out.println("成功更新 " + rows + " 条记录，ID=" + id + " 的分数已修改为：" + newScore);
        pstmt.close();
    }

    /**
     * 根据ID删除学生
     */
    private static void deleteStudentById(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);

        int rows = pstmt.executeUpdate();
        System.out.println("\n===== 删除操作 =====");
        System.out.println("成功删除 " + rows + " 条记录，删除的学生ID：" + id);
        pstmt.close();
    }
}
