package controllers.worktime;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Worktime;
import utils.DBUtil;

/**
 * Servlet implementation class WorktimeUpdateServlet
 */
@WebServlet("/worktime/update")
public class WorktimeUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorktimeUpdateServlet() {
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

			Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
			Date today = new Date(System.currentTimeMillis());

			Worktime worktime = em.createNamedQuery("getMyWorktimeToday", Worktime.class)
					.setParameter("employee", login_employee)
					.setParameter("date", today)
					.getSingleResult();

			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			worktime.setWorktime_finish(currentTime);
			worktime.setUpdated_at(currentTime);

			em.getTransaction().begin();
			em.getTransaction().commit();
			em.close();

			request.getSession().setAttribute("flush", "退勤しました。");

			response.sendRedirect(request.getContextPath() + "/worktime/index");

		}
	}

}
