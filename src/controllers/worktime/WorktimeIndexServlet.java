package controllers.worktime;

import java.io.IOException;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Worktime;
import utils.DBUtil;

/**
 * Servlet implementation class WorktimeIndexServlet
 */
@WebServlet("/worktime/index")
public class WorktimeIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorktimeIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManager em = DBUtil.createEntityManager();

		// work_flagがtrueなら仕事中、falseなら休み中
		boolean work_flag = false;
		Employee login_employee = (Employee)(request.getSession().getAttribute("login_employee"));
	    Date worktime_date = new Date(System.currentTimeMillis());

	    try{
	    	Worktime worktime_today = em.createNamedQuery("getMyWorktimeToday", Worktime.class)
					.setParameter("employee", login_employee)
					.setParameter("date", worktime_date)
					.getSingleResult();

	    	if(worktime_today != null) {
	    		work_flag = true;
	    	}

	    } catch(NoResultException e) {

	    }

	    request.setAttribute("_token", request.getSession().getId());
		request.setAttribute("work_flag", work_flag);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/worktime/index.jsp");
		rd.forward(request, response);
	}

}
