package controllers.worktime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import models.Employee;
import models.Worktime;
import utils.DBUtil;

public class Test {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		EntityManager em = DBUtil.createEntityManager();

		Employee login_employee = em.find(Employee.class, 5);
		System.out.println(login_employee.getName());

		Calendar today = Calendar.getInstance();
	    int current_year = today.get(Calendar.YEAR);
	    int current_month = today.get(Calendar.MONTH);

	    String tukihazime = current_year + "-" + (current_month + 1) +"-01";
	    Date start = Date.valueOf(tukihazime);
	    //月初から月末日を求める
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(start);
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.DAY_OF_MONTH, -1);

	    List<Worktime> worktimes_month = new ArrayList<>();
	    String year_month = current_year + "-" + (current_month + 1) + "-";
	    int end_day = cal.get(Calendar.DATE);

	    for (int day = 1; day <= end_day; day++) {
	    	String s = year_month + day;
	    	Date d = Date.valueOf(s);
	    	try {
	    		Worktime w = em.createNamedQuery("getMyWorktimeToday", Worktime.class)
	    				.setParameter("employee", login_employee)
	    				.setParameter("date", d)
	    				.getSingleResult();
	    		if (w != null) {
	    			worktimes_month.add(w);
	    		}
	    	} catch (NoResultException e) {
	    		Worktime w = new Worktime();
	    		w.setDate(d);
	    		worktimes_month.add(w);
	    	}
	    }
	    
	    for (Worktime w : worktimes_month) {
	    	System.out.println(w.getDate());
	    	System.out.println(w.getEmployee().getName());
	    	System.out.println(w.getWorktime_begin());
	    	System.out.println(w.getWorktime_finish());
	    }
	}

}
