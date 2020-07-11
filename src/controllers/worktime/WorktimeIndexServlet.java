package controllers.worktime;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

	    //仕事中かどうか　work_flag
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

	    //月初
	    LocalDate start = LocalDate.now().withDayOfMonth(1);
	    int current_month = start.getMonthValue();
	    LocalDate end = start.plusMonths(1).minusDays(1);
	    List<Worktime> worktimes = new ArrayList<Worktime>();

	    while (true) {
	    	Date d = Date.valueOf(start);
	    	try {
	    		Worktime w = em.createNamedQuery("getMyWorktimeToday", Worktime.class)
		    			.setParameter("employee", login_employee)
		    			.setParameter("date", d)
		    			.getSingleResult();
	    		if (w != null) {
		    		worktimes.add(w);
	    		}
	    	} catch (NoResultException e) {
	    		Worktime w = new Worktime();
	    		w.setDate(d);
	    		worktimes.add(w);
	    	}

	    	if (start.getDayOfMonth() == end.getDayOfMonth()) {
	    		break;
	    	}
	    	start = start.plusDays(1);
	    }

	    em.close();

	    request.setAttribute("_token", request.getSession().getId());
		request.setAttribute("work_flag", work_flag);

		if (request.getSession().getAttribute("flush") != null) {
			request.setAttribute("flush", request.getSession().getAttribute("flush"));
			request.getSession().removeAttribute("flush");
		}
		request.setAttribute("month", current_month);
		request.setAttribute("worktimes", worktimes);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/worktime/index.jsp");
		rd.forward(request, response);
	}

}
