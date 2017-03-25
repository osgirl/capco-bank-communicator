package com.capco.communicator.schema;

import com.capco.communicator.PaymentFormat;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;


@Entity
public class PaymentContext extends AbstractProcessingContext {

    public static final String F_ID = "id";
    public static final String F_RESOURCE = "resource";
    public static final String F_PAYLOAD = "payload";
    public static final String F_STATE = "state";
    public static final String F_CREATED_AT = "createdAt";
    public static final String F_PAYMENT = "payment";
    public static final String F_CHANNEL = "channel";
    public static final String F_PAYMENT_FORMAT = "paymentFormat";

    public PaymentContext() {
    }

    public PaymentContext(String resource, State state, Date createdAt, String channel) {
        this.resource = resource;
        this.state = state;
        this.createdAt = createdAt;
        this.channel = channel;
    }

    @Override
    public void addErrorLog(String log) {
        getErrorLog().add(log);
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column( length = 1000 )
    private String resource;

    @Column( length = 1000 )
    private byte[] payload;

    private State state;

    private Date createdAt;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    private Payment payment;

    private String channel;

    private PaymentFormat paymentFormat;

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

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public PaymentFormat getPaymentFormat() {
        return paymentFormat;
    }

    public void setPaymentFormat(PaymentFormat paymentFormat) {
        this.paymentFormat = paymentFormat;
    }

}
