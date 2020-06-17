package models.iine_check;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import models.Employee;
import models.Iine;
import models.Report;
import utils.DBUtil;

public class IineChecker {

	public static Boolean iine_checker(Employee login_employee, Report report) {
		Boolean iine_flag = true;
		EntityManager em = DBUtil.createEntityManager();
		try {
			Iine iine = em.createNamedQuery("getIine", Iine.class)
					      .setParameter("employee", login_employee)
					      .setParameter("report", report)
					      .getSingleResult();

			if (iine != null) {
				iine_flag = false;
			}

		} catch(NoResultException e) {}

		em.close();
		return iine_flag;
	}

}
