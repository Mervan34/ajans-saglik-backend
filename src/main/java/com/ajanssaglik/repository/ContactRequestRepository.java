package com.ajanssaglik.repository;

import com.ajanssaglik.model.ContactRequest;
import com.ajanssaglik.model.RequestStatus;
import com.ajanssaglik.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    List<ContactRequest> findByStatus(RequestStatus status);
    List<ContactRequest> findByRequestType(RequestType requestType);
    List<ContactRequest> findByProductId(Long productId);
    List<ContactRequest> findAllByOrderByCreatedAtDesc();
}
