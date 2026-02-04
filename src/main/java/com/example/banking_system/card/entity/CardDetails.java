package com.example.banking_system.card.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "card_details")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class CardDetails {
    @Id
    @Column(name = "card_id")
    private long cardId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
}
