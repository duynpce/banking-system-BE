package com.example.banking_system.domain.card.entity;

import com.example.banking_system.domain.card.constant.CardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "personal_card_details")
@PrimaryKeyJoinColumn(name = "card_id")
@EqualsAndHashCode(callSuper = false)
public class PersonalCard extends CardDetails {

    @Column(name = "reward_points", nullable = false)
    private int rewardPoint = 0;

    public PersonalCard() {
        setCard(new Card());
    }

    public PersonalCard(String pinCode,String cardNumber, String cardHolder, CardType type, CardPrivilege privilege) {
        setCard(new Card(pinCode, cardNumber,cardHolder , type, privilege));
    }
}
