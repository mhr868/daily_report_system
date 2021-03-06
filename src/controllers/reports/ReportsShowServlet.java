package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import models.Worktime;
import models.iine_check.IineChecker;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManager em = DBUtil.createEntityManager();

		Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
		
		//日報の出勤情報
		Worktime worktime = new Worktime();		
		try {
			worktime = em.createNamedQuery("getMyWorktimeToday", Worktime.class)
				.setParameter("employee", r.getEmployee())
				.setParameter("date", r.getReport_date())
				.getSingleResult();
		} catch(NoResultException e){

		}

		em.close();

		Employee login_employee = (Employee)(request.getSession().getAttribute("login_employee"));
        Boolean iine_flag = IineChecker.iine_checker(login_employee, r);

		request.setAttribute("report", r);
		request.setAttribute("worktime", worktime);
		request.setAttribute("iine_flag", iine_flag);
		request.setAttribute("_token", request.getSession().getId());

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
	}

}
