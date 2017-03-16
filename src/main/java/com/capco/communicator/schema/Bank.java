package com.capco.communicator.schema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bank {

	@Id
	@GeneratedValue
	private Long id;

	private String code;

	private String name;

	protected Bank() {}

	public Bank(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%d, code='%s', name='%s']", id,
				code, name);
	}

}
