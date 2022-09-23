package com.revature.reimbursements;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reimbursement")
public class Reimbursement {
    
    @Id // indicates a primary key
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "submitted")
    private String submitted;

    @Column(name = "resolved")
    private String resolved;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UUID authorId;

    
    @ManyToOne
    @JoinColumn(name = "resolver_id")
    private UUID resolverId;
    
    @OneToOne
    @JoinColumn(name = "status_id")
    private ReimbursementStatus status;

    @OneToOne
    @JoinColumn(name = "type_id")
    private ReimbursementType type;

    public Reimbursement() {
        super();
    }




}
