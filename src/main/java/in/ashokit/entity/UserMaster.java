package in.ashokit.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="USER_MASTER")
public class UserMaster {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	private String fullName;
	private String emailId;
	private Long mobileNo;
	private String gender;
	private LocalDate dob;
	private String ssn;
	private String password;
	private String accStatus;
	private String createdBy;
	private String updatedBy;
	@CreationTimestamp
	@Column(name="createdDate",updatable = false)
	private LocalDate createdDate;
	@CreationTimestamp
	@Column(name="updateddate",insertable = false)
	private LocalDate updateddate;

	
}
