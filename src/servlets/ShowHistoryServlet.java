package servlets;
import model.Algorithm;
import model.Algorithm.SingleOperation;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * Servlet that displays operation history
 * @author Michal Szeler
 * @version 4.0
 */
public class ShowHistoryServlet extends HttpServlet {

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
        Object object = session.getAttribute("algorithm");
        Algorithm algorithm;

        if(object==null){
            algorithm = new Algorithm();
            session.setAttribute("algorithm", algorithm);
        }
        else {
            algorithm = (Algorithm) object;
        }

        HashMap<Integer, SingleOperation> operationHistory =  algorithm.getOperationHistory();
        if (operationHistory.isEmpty())
            out.println("<html>\n<body>\n<p>No operations have been performed</p>\n<a href=\\Servlets\\>Back</a>\n</body>\n</html>");
        else {
            out.println("<html>\n<body>\n<div>\n");
            Cookie[] cookies = req.getCookies();
            String lastResult=null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastResult"))
                    lastResult = cookie.getValue();
            }

            if(lastResult!=null)
                out.println("<div>\n<p>Last operation result: "+lastResult+"</p>\n</div>\n");

            operationHistory.forEach((key, value) -> {
                SingleOperation singleOperation = value;
                out.println("<div>\n<p>Operation id: " + key + "</p>\n<p>Operation type: " + singleOperation.getType() +
                        "</p>\n<p>Input text:\n" + singleOperation.getInput() + "</p>\n<p>Key:\n" + singleOperation.getKey() + "</p>\n<p>Result:\n" +
                        singleOperation.getOutput() + "</p>\n</div>");
            });
            out.println("\n</div>\n</body>\n</html>");
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
