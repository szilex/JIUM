package servlets;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Servlet that displays operation history
 * @author Michal Szeler
 * @version 5.0
 */
public class ShowHistoryServlet extends HttpServlet {

    /**
     * Field that allows to create Connection based on configuration from web.xml
     */
    @Resource(name="jdbc/operation_history")
    private javax.sql.DataSource ds;

    /**
     * Method that processes both HTTP GET and POST requests
     * Depending on user input, it encrypts/decrypts data or send an error if some data is missing/incorrect
     * @param req HTTP Servlet request
     * @param resp HTTP Servlet response
     * @throws ServletException Exception thrown if Servlet-specific error occurs
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=ISO-8859-2");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(true);
        Object object = session.getAttribute("connection");
        Connection connection=null;

        if(object==null){
            try {
                connection = ds.getConnection();
                session.setAttribute("connection", connection);
            } catch (SQLException e) {
                if(connection!=null)
                    try{
                        connection.close();
                    } catch (SQLException e1) {}
                resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
            }
        } else {
            connection = (Connection) object;
        }

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM operations");

            out.println("<html>\n<body>");

            Cookie[] cookies = req.getCookies();
            String lastResult=null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastResult"))
                    lastResult = cookie.getValue();
            }

            if(lastResult!=null)
                out.println("<div>\n<p>Last operation result: "+lastResult+"</p>\n</div>\n");

            int counter=0;
            while (resultSet.next()) {
                out.println("<div>\n<p>Operation id: " + resultSet.getInt("operation_ID") + "</p>\n<p>Operation type: " +
                        resultSet.getString("type") + "</p>\n<p>Input text:\n" + resultSet.getString("input_text") +
                        "</p>\n<p>Key:\n" + resultSet.getString("key") + "</p>\n<p>Result:\n" +
                        resultSet.getString("output_text") + "</p>\n</div>");
                counter++;
            }

            if(counter==0){
                out.println("<p>No operations have been performed</p>");
            }

            out.println("</body>\n</html>");
            resultSet.close();
            statement.close();
        } catch (SQLException|NullPointerException e) {
            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    /**
     * Method that processes HTTP GET request by invoking processRequest method
     * @param req HTTP Servlet request
     * @param resp HTTP Servlet response
     * @throws ServletException Exception thrown if Servlet-specific error occurs
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Method that processes HTTP POST request by invoking processRequest method
     * @param req HTTP Servlet request
     * @param resp HTTP Servlet response
     * @throws ServletException Exception thrown if Servlet-specific error occurs
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
