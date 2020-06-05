package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmpsDestroyServlet
 */
@WebServlet("/employees/destroy")
public class EmpsDestroyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpsDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//csrf
		//セッションスコープからidとってきて、em.find()
		//DB更新　フラッシュ
		String _token = request.getParameter("_token");
		if(_token != null && _token.equals(request.getSession().getId())) {

			EntityManager em = DBUtil.createEntityManager();
			Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id_to_be_updated")));

			e.setDelete_flag(1);
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			e.setUpdated_at(currentTime);

			em.getTransaction().begin();
			em.getTransaction().commit();
			em.close();

			request.getSession().setAttribute("flush", "削除が完了しました");
			request.getSession().removeAttribute("employee_id_to_be_updated");

			response.sendRedirect(request.getContextPath() + "/employees/index");
		}
	}
}
