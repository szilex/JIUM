package servlets;

import model.Algorithm;
import model.AlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that processes encryption/decryption request sent from main page and displays result
 * @author Michal Szeler
 * @version 4.0
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
        HttpSession session = req.getSession(true);
        Object object = session.getAttribute("algorithm");
        Algorithm algorithm;

        if(object==null){
            algorithm = new Algorithm();
            session.setAttribute("algorithm", algorithm);
        } else {
            algorithm = (Algorithm) object;
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
                    } catch(AlgorithmException e){
                        resp.sendError(resp.SC_BAD_REQUEST, e.toString());
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
