package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "worktime")

@NamedQueries({
	@NamedQuery(
			name = "getMyWorktimeToday",
			query = "SELECT w FROM Worktime AS w WHERE w.employee = :employee AND w.date = :date"
			)
})

@Entity
public class Worktime {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "worktime_begin", nullable = false)
	private Timestamp worktime_begin;

	@Column(name = "worktime_finish", nullable = true)
	private Timestamp worktime_finish;

	@Column(name = "created_at", nullable = false)
	private Timestamp created_at;

	@Column(name = "updated_at", nullable = false)
	private Timestamp updated_at;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getWorktime_begin() {
		return worktime_begin;
	}

	public void setWorktime_begin(Timestamp worktime_begin) {
		this.worktime_begin = worktime_begin;
	}

	public Timestamp getWorktime_finish() {
		return worktime_finish;
	}

	public void setWorktime_finish(Timestamp worktime_finish) {
		this.worktime_finish = worktime_finish;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}


}
