package com.example.banking_system.card.service.domain;

import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.card.repository.CardPrivilegeRepository;
import com.example.banking_system.card.validator.CardPrivilegeValidator;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardPrivilegeService {
    private final CardPrivilegeRepository cardPrivilegeRepository;
    private final CardPrivilegeValidator cardPrivilegeValidator;
    private final CardPrivilegeMapper CardPrivilegeMapper;

    public CardPrivilege create(CreateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = CardPrivilegeMapper.toEntity(request);
        cardPrivilegeValidator.validateCreate(cardPrivilege);

        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public CardPrivilege update(UpdateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = findByCode(request.getCode());
        cardPrivilegeValidator.validateUpdate(request, cardPrivilege);
        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public void deleteCardPrivilegeByCode(String code) {
        CardPrivilege cardPrivilege = findByCode(code);

        cardPrivilegeRepository.delete(cardPrivilege);
    }

    public CardPrivilege findByCode(String privilegeCode) {
        return cardPrivilegeRepository.findById(privilegeCode).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public boolean existsByCode(String code) {
        return cardPrivilegeRepository.existsById(code);
    }

}
