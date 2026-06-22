package com.example.crm.sequence;


import com.example.crm.entity.SequenceEntity;
import com.example.crm.repository.SequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SequenceService {
    private final SequenceRepository repository;

    //================= CORE LOGIC =================
    @Transactional
    public long getNext(SequenceType type) {

        SequenceEntity seq = repository.findById(type.getDbName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Sequence not found: " + type.getDbName())
                );

        seq.setValue(seq.getValue() + 1);
        repository.save(seq);

        return seq.getValue();
    }

    protected String format(SequenceType type, long value) {

        LocalDate now = LocalDate.now();

        String month = String.format("%02d", now.getMonthValue());
        String year  = String.valueOf(now.getYear());
        String number = String.format("%09d", value);

        return type.getPrefix()
                + "/" + month + "-" + year
                + "/" + number;
    }

    //================= PUBLIC METHODS =================

    public String getNextInvoiceNo() {
        long val = getNext(SequenceType.INVOICE);
        return format(SequenceType.INVOICE, val);
    }

    public String getNextOrderNo() {
        long val = getNext(SequenceType.ORDER);
        return format(SequenceType.ORDER, val);
    }
}

