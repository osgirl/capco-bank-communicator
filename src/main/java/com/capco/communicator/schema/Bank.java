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

    private String outputChannel;

    protected Bank() {
    }

    public Bank(String code, String name, String outputChannel) {
        this.code = code;
        this.name = name;
        this.outputChannel = outputChannel;
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

    public String getOutputChannel() {
        return outputChannel;
    }

    public void setOutputChannel(String outputChannel) {
        this.outputChannel = outputChannel;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, code='%s', name='%s']", id,
                code, name);
    }

}
