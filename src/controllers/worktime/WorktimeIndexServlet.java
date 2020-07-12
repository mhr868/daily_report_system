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

		//sessionscopeに置く日付の情報
	    LocalDate session_date = LocalDate.now().withDayOfMonth(1);
	    if (request.getSession().getAttribute("session_date") != null) {
	    	session_date = (LocalDate)request.getSession().getAttribute("session_date");
	    }

	    String cursor = request.getParameter("cursor");
	    if (cursor != null) {
	    	switch (cursor) {
	    	case "back": session_date = session_date.minusMonths(1);
	    	break;
	    	case "forward": session_date = session_date.plusMonths(1);
	    	break;
	    	}
	    }
	    //viewに渡すDate型の情報
	    Date for_page = Date.valueOf(session_date);

	    request.getSession().setAttribute("session_date", session_date);
		request.setAttribute("date_for_page", for_page);

		//月のページリンクの表示切替
		boolean is_max_month = false;
		if ((LocalDate.now().isAfter(session_date) || LocalDate.now().isEqual(session_date)) && LocalDate.now().isBefore(session_date.plusMonths(1))) {
			is_max_month = true;
		}

		// 出勤/退勤ボタンの表示切替 work_flagがtrueなら仕事中、falseなら休み中
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

	    //月の末日の計算
	    LocalDate end = session_date.plusMonths(1).minusDays(1);
	    List<Worktime> worktimes = new ArrayList<Worktime>();

	    while (true) {
	    	Date d = Date.valueOf(session_date);
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

	    	if (session_date.getDayOfMonth() == end.getDayOfMonth()) {
	    		break;
	    	}
	    	session_date = session_date.plusDays(1);
	    }

	    em.close();



	    request.setAttribute("_token", request.getSession().getId());
	    request.setAttribute("is_max_month", is_max_month);
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
