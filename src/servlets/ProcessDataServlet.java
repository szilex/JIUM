package servlets;

import model.Algorithm;
import model.AlgorithmException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Servlet that processes encryption/decryption request sent from main page and displays result
 * @author Michal Szeler
 * @version 5.0
 */
public class ProcessDataServlet extends HttpServlet {

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
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=ISO-8859-2");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(true);
        Object object = session.getAttribute("connection");
        Connection connection = null;
        Algorithm algorithm=new Algorithm();

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

        String inputText = req.getParameter("inputText");
        String privateKeyText = req.getParameter("privateKeyText");
        String publicKeyText = req.getParameter("publicKeyText");
        String operationType = req.getParameter("operation");

        if (inputText==null || inputText.trim().length() == 0 || operationType==null || operationType.trim().length()==0){
            resp.sendError(resp.SC_BAD_REQUEST, "Input text area is empty");
        } else{
            if(operationType.equals("encrypt")){
                if(publicKeyText==null || publicKeyText.trim().length() == 0){
                    resp.sendError(resp.SC_BAD_REQUEST, "Public Key Text Field is empty");
                } else {
                    try{
                        String outputText = algorithm.encrypt(inputText,publicKeyText);
                        out.println("<html>\n<body>\n<div>\n<p>Operation type: " + operationType + "</p>\n<p>Input text:\n" + inputText +
                                "</p>\n<p>Key:\n"+ publicKeyText +"</p>\n<p>Result:\n"+  outputText +"</p>\n</div>\n</body>\n</html>");
                        Cookie cookie = new Cookie("lastResult", outputText);
                        resp.addCookie(cookie);

                        try{
                            insertRecord(connection,operationType,inputText,publicKeyText,outputText);
                        } catch (SQLException e) {
                            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                        }

                    } catch(AlgorithmException e){
                        resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                    }
                }
            } else if(operationType.equals("decrypt")){
                if(privateKeyText==null || privateKeyText.trim().length() == 0){
                    resp.sendError(resp.SC_BAD_REQUEST, "Private Key Text Field is empty");
                } else {
                    try{
                        String outputText = algorithm.decrypt(inputText,privateKeyText);
                        out.println("<html>\n<body>\n<div>\n<p>Operation type: " + operationType + "</p>\n<p>Input text:\n" + inputText +
                                "</p>\n<p>Key:\n"+ privateKeyText +"</p>\n<p>Result:\n"+  outputText +"</p>\n</div>\n</body>\n</html>");
                        Cookie cookie = new Cookie("lastResult", outputText);
                        resp.addCookie(cookie);

                        try{
                            insertRecord(connection,operationType,inputText,privateKeyText,outputText);
                        } catch (SQLException e) {
                            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                        }

                    } catch(AlgorithmException e){
                        resp.sendError(resp.SC_BAD_REQUEST, e.toString());
                    }
                }
            } else {
                resp.sendError(resp.SC_BAD_REQUEST, "Incorrect operation");
            }
        }
    }

    /**
     * Method inserts new record into database
     * @param connection SQL connection variable to contact with database
     * @param operationType String representing type performed operation
     * @param inputText String representing text before processing
     * @param keyText String representing key used during procedure
     * @param outputText String representing text after processing
     * @throws SQLException Exception thrown if data could not be inserted into database
     */
    private void insertRecord(Connection connection, String operationType, String inputText, String keyText, String outputText) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM operations");
        resultSet.next();
        int size=resultSet.getInt("row_count");
        resultSet.close();
        statement.executeUpdate("INSERT INTO operations VALUES ("+(size+1)+",'"+operationType+"','"+ inputText +"','"+ keyText +"','"+outputText+"')");
        statement.close();
    }

    /**
     * Method that processes HTTP GET request by invoking processRequest method
     * @param req HTTP Servlet request
     * @param resp HTTP Servlet response
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
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
