package main.java.servlets;

import main.java.database.OperationsEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Servlet that displays operation history
 * @author Michal Szeler
 * @version 6.0
 */
public class ShowHistoryServlet extends HttpServlet {

    /**
     * Method that processes both HTTP GET and POST requests
     * It shows all operations stored in database or message that table is empty
     * @param req HTTP Servlet request
     * @param resp HTTP Servlet response
     * @throws ServletException Exception thrown if Servlet-specific error occurs
     * @throws IOException Exception thrown if IO-specific error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=ISO-8859-2");
        PrintWriter out = resp.getWriter();
        try{
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            List<OperationsEntity> operations = entityManager.createQuery("select o from OperationsEntity o").getResultList();

            if(operations.size()!=0){
                try{
                    out.println("<html>\n<body>");
                    Cookie[] cookies = req.getCookies();
                    String lastResult=null;
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("lastResult"))
                            lastResult = cookie.getValue();
                    }

                    if(lastResult!=null)
                        out.println("<div>\n<p>Last operation result: "+lastResult+"</p>\n</div>\n");

                    for(OperationsEntity elem:operations) {
                        out.println("<div>\n<p>Operation id: " + elem.getOperationId() + "</p>\n<p>Operation type: " +
                                elem.getOperationType() + "</p>\n<p>Input text:\n" + elem.getInputText() +
                                "</p>\n<p>Key:\n" + elem.getOperationKey() + "</p>\n<p>Result:\n" +
                                elem.getOutputText() + "</p>\n</div>");
                    }

                    out.println("</body>\n</html>");
                }
                catch(NullPointerException e){
                    resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                }
            }
            else{
                out.println("<html>\n<body>\n<p>No operations have been performed</p>\n</body>\n</html>");
            }
        }
        catch(PersistenceException e){
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
