package main.java.servlets;

import main.java.client.Operations;
import main.java.client.OperationsService;
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
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Servlet that processes encryption/decryption request sent from main page and displays result
 * @author Michal Szeler
 * @version 6.0
 */
public class ProcessDataServlet extends HttpServlet {

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

        try{
            Operations service = new OperationsService().getPort(Operations.class);
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
                        String outputText = service.encrypt(inputText,publicKeyText);
                        if(outputText.contains("exception")){
                            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, outputText);
                        }
                        else{
                            out.println("<html>\n<body>\n<div>\n<p>Operation type: " + operationType + "</p>\n<p>Input text:\n" + inputText +
                                    "</p>\n<p>Key:\n"+ publicKeyText +"</p>\n<p>Result:\n"+  outputText +"</p>\n</div>\n</body>\n</html>");

                            Cookie cookie = new Cookie("lastResult", outputText);
                            resp.addCookie(cookie);

                            try{
                                insertRecord(operationType,inputText,publicKeyText,outputText);
                            } catch (PersistenceException e) {
                                resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                            }
                        }
                    }
                } else if(operationType.equals("decrypt")){
                    if(privateKeyText==null || privateKeyText.trim().length() == 0){
                        resp.sendError(resp.SC_BAD_REQUEST, "Private Key Text Field is empty");
                    } else {
                        String outputText = service.decrypt(inputText,privateKeyText);
                        if(outputText.contains("exception")){
                            resp.sendError(resp.SC_BAD_REQUEST, outputText);
                        }
                        else{
                            out.println("<html>\n<body>\n<div>\n<p>Operation type: " + operationType + "</p>\n<p>Input text:\n" + inputText +
                                    "</p>\n<p>Key:\n"+ privateKeyText +"</p>\n<p>Result:\n"+  outputText +"</p>\n</div>\n</body>\n</html>");
                            Cookie cookie = new Cookie("lastResult", outputText);
                            resp.addCookie(cookie);

                            try{
                                insertRecord(operationType,inputText,privateKeyText,outputText);
                            } catch (PersistenceException e) {
                                resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
                            }
                        }
                    }
                } else {
                    resp.sendError(resp.SC_BAD_REQUEST, "Incorrect operation");
                }
            }
        } catch(WebServiceException e){
            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    /**
     * Method inserts new record into database
     * @param operationType String representing type performed operation
     * @param inputText String representing text before processing
     * @param keyText String representing key used during procedure
     * @param outputText String representing text after processing
     * @throws SQLException Exception thrown if data could not be inserted into database
     */
    private void insertRecord(String operationType, String inputText, String keyText, String outputText) throws PersistenceException {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        int lastIndex = entityManager.createQuery("select o from OperationsEntity o").getResultList().size();

        OperationsEntity entity = new OperationsEntity();
        entity.setOperationId(lastIndex);
        entity.setInputText(inputText);
        entity.setOperationType(operationType);
        entity.setOperationKey(keyText);
        entity.setOutputText(outputText);

        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
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
