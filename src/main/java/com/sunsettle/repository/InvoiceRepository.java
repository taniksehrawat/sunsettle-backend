package com.sunsettle.repository;

import com.sunsettle.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findBySiteId(Long siteId);

    List<Invoice> findBySiteIdAndYear(Long siteId, Integer year);

    List<Invoice> findBySiteIdAndMonthNumberAndYear(Long siteId, Integer monthNumber, Integer year);

    List<Invoice> findByClientId(Long clientId);  // 🔥 ADD THIS
}

