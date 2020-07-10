package controllers.worktime;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
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

	    Calendar today = Calendar.getInstance();
	    int current_year = today.get(Calendar.YEAR);
	    int current_month = today.get(Calendar.MONTH);

	    String tukihazime = current_year + "-" + (current_month + 1) +"-01";
	    Date from_date = Date.valueOf(tukihazime);
	    //to_dateの計算
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(from_date);
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    

	    String tukiowari = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
	    Date to_date = Date.valueOf(tukiowari);

	    List<Worktime> worktimes = em.createNamedQuery("getMyWorktime", Worktime.class)
	    		.setParameter("employee", login_employee)
	    		.setParameter("from_date", from_date)
	    		.setParameter("to_date", to_date)
	    		.getResultList();
	    Collections.reverse(worktimes);

	    em.close();

	    request.setAttribute("_token", request.getSession().getId());
		request.setAttribute("work_flag", work_flag);

		if (request.getSession().getAttribute("flush") != null) {
			request.setAttribute("flush", request.getSession().getAttribute("flush"));
			request.getSession().removeAttribute("flush");
		}
		request.setAttribute("worktimes", worktimes);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/worktime/index.jsp");
		rd.forward(request, response);
	}

}
