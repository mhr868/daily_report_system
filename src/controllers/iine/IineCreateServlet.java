package controllers.iine;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Iine;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class IineCreateServlet
 */
@WebServlet("/iine/create")
public class IineCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IineCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {

        	EntityManager em = DBUtil.createEntityManager();
        	Iine iine = new Iine();

        	Employee login_employee = (Employee)(request.getSession().getAttribute("login_employee"));
        	iine.setEmployee(login_employee);

        	Report report = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));
            iine.setReport(report);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            iine.setCreated_at(currentTime);
            iine.setUpdated_at(currentTime);

            report.setIine_count(report.getIine_count() + 1);

            em.getTransaction().begin();
            em.persist(iine);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "いいねしました");
        	response.sendRedirect(request.getContextPath() + "/reports/index");
        }
	}

}
