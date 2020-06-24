package controllers.iine.employees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class IineEmployeesIndex
 */
@WebServlet("/iine/employees/index")
public class IineEmployeesIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IineEmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManager em = DBUtil.createEntityManager();

		Report report = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
		List<Iine> iine_list = em.createNamedQuery("getReportIine", Iine.class)
				.setParameter("report", report)
				.getResultList();

		em.close();


		List<Employee> iine_employees = new ArrayList<Employee>();
		for(Iine iine : iine_list) {
			iine_employees.add(iine.getEmployee());
		}
		Collections.reverse(iine_employees);

		int iine_employees_count = iine_employees.size();

		int page;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch(NumberFormatException e) {
			page = 1;
		}

        List<Employee> iine_employees_for_pagination = new ArrayList<Employee>();
        try {
        	iine_employees_for_pagination = iine_employees.subList(15 * (page - 1), 15 * page);
        } catch(IndexOutOfBoundsException e) {
        	iine_employees_for_pagination = iine_employees.subList(15 * (page - 1), iine_employees_count);
        }


		request.setAttribute("iine_employees_count", iine_employees_count);
		request.setAttribute("page", page);
        request.setAttribute("iine_employees", iine_employees_for_pagination);
        request.setAttribute("report_id", request.getParameter("id"));

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/iine/employees/index.jsp");
		rd.forward(request, response);
	}

}
