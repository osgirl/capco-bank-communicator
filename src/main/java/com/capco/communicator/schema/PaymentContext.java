package com.capco.communicator.schema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;


@Entity
public class PaymentContext {

    public PaymentContext() {
    }

    public PaymentContext(String resource, State state, Date createdAt, String channel) {
        this.resource = resource;
        this.state = state;
        this.createdAt = createdAt;
        this.channel = channel;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String resource;

    private State state;

    private Date createdAt;

    @ManyToOne
    private Payment payment;

    private String channel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
