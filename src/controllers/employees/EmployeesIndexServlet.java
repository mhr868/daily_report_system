package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManager em = DBUtil.createEntityManager();

		//urlのパラメータからpage拾う　catch(NumberFormatException e)は数字以外のパラメータの値に対応
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch(NumberFormatException e) {}

		List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
				                     .setFirstResult(15 * (page - 1))//セット　ページの表示する最初
				                     .setMaxResults(15)//セット　ページの表示件数
                                     .getResultList();//ゲット　リスト

		long employees_count = em.createNamedQuery("getEmployeesCount", Long.class)
				                 .getSingleResult();//ゲット　

		em.close();

		//ページネーションでセットするもの　この３つ
		request.setAttribute("page", page);
		request.setAttribute("employees", employees);
		request.setAttribute("employees_count", employees_count);

		//フラッシュ　セッションスコープだっけ？
		if(request.getSession().getAttribute("flush") != null) {
			request.setAttribute("flush", request.getSession().getAttribute("flush"));
			request.getSession().removeAttribute("flush");
		}

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
	}

}
