package controllers.iine;

import java.io.IOException;

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
 * Servlet implementation class IineDestroyServlet
 */
@WebServlet("/iine/destroy")
public class IineDestroyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IineDestroyServlet() {
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

			Employee login_employee = (Employee)(request.getSession().getAttribute("login_employee"));
			Report report = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));

			Iine iine = em.createNamedQuery("getIine", Iine.class)
					      .setParameter("employee", login_employee)
					      .setParameter("report", report)
					      .getSingleResult();

			report.setIine_count(report.getIine_count() - 1);

			em.getTransaction().begin();
			em.remove(iine);
			em.getTransaction().commit();
			em.close();

			request.getSession().setAttribute("flush", "いいねを取り消しました");

			response.sendRedirect(request.getContextPath() + "/reports/index");
		}
	}

}
