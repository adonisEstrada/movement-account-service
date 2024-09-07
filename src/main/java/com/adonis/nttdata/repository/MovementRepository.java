package com.adonis.nttdata.repository;

import com.adonis.nttdata.entity.Movement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovementRepository extends CrudRepository<Movement, UUID> {

    @Query(value = "select distinct m.transaction_date, p.name, ba.account_number, ba.account_type, ba.initial_balance, ba.status, \n" +
            "case when m.movement_type ='OUTGOING' then m.amount * (-1) else m.amount end,m.current_balance \n" +
            "from movements m \n" +
            "join accounts ba on ba.account_id = m.account_id \n" +
            "join clients c on c.person_id = ba.client_id \n" +
            "join persons p on p.person_id = c.person_id \n" +
            "where c.person_id = :personId \n" +
            "and m.transaction_date BETWEEN :initDate  AND :endDate \n" +
            "group by 1,2,2,3,4,5,6,7,8 \n" +
            "order by 1", nativeQuery = true)
    List<Object[]> getMovementByClientAndDates(@Param("personId") UUID personId,
                                               @Param("initDate") Date initDate,
                                               @Param("endDate") Date endDate);
}
