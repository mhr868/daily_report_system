package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmpsUpdateServlet
 */
@WebServlet("/employees/update")
public class EmpsUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpsUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//csrf token
		//em呼び出し
		//セッションスコープからidとってきてem.find() eにつめる
		//セッションスコープ消す
		//eにフォームから受け取った値をつめてく
		//バリデーション
		//db更新してindex用にセッションスコープにフラッシュ仕込む
		String _token = request.getParameter("_token");
	    if(_token != null && _token.equals(request.getSession().getId())) {

	    	EntityManager em = DBUtil.createEntityManager();
	    	Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id_to_be_updated")));

	    	//現在の値と異なる社員番号が入力されていたら
	    	//重複チェックを行う指定をする
	    	Boolean code_duplicate_check = true;
            if(e.getCode().equals(request.getParameter("code"))) {
            	code_duplicate_check = false;
            } else {
            	e.setCode(request.getParameter("code"));
            }

            //パスワード欄に入力があったら
            //パスワードの入力値チェックを行う指定をする
            Boolean password_check_flag = true;
            String password = request.getParameter("password");
            if(password == null || password.equals("")) {
            	password_check_flag = false;
            } else {
            	e.setPassword(
            			EncryptUtil.getPasswordEncrypt(password, (String)this.getServletContext().getAttribute("salt"))
            			);
            }

            e.setName(request.getParameter("name"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setUpdated_at(currentTime);

            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);
            if(errors.size() > 0) {
            	em.close();

            	request.setAttribute("employee", e);
            	request.setAttribute("errors", errors);
            	request.setAttribute("_token", request.getSession().getId());

            	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
            	rd.forward(request, response);
            } else {
            	em.getTransaction().begin();
                em.getTransaction().commit();
    	    	em.close();

    	    	request.getSession().setAttribute("flush", "更新を完了しました");
                request.getSession().removeAttribute("employee_id_to_be_updated");
    	    	response.sendRedirect(request.getContextPath() + "/employees/index");
            }


	    }
	}
}
