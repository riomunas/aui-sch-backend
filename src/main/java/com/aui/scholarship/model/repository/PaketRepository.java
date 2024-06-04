package com.aui.scholarship.model.repository;

import com.aui.scholarship.model.entity.PaketEntity;
import com.aui.scholarship.model.response.MyPaketInfo;
import com.aui.scholarship.model.response.PaketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaketRepository extends JpaRepository<PaketEntity, String> {
  @Query(value = """
        select
          p.id as id, p.name as name, p.description as description, pr.price as price
        from pakets p
        inner join prices pr on pr.level = p.default_level
        order by p.default_level
       """,
      nativeQuery = true)
  List<PaketInfo> findAllPaket();

  @Query(value = """
        select
          p.id as id, p.name as name, p.description as description, pr.price as price
        from pakets p
        inner join prices pr on pr.level = p.default_level
        where p.id = :paketId
        order by p.default_level
        limit 1
       """,
      nativeQuery = true)
  Optional<PaketInfo> findPaketById(UUID paketId);

  @Query(value = """
        select
          t.order_id as id ,
          p."name" as name,
          pstart.price as startPrice,
          pcurrent.price as currentPrice,
          t.settlement_time as date
        from pakets p
        inner join transactions t on t.paket_id = p.id and t.status_code = '200'
        inner join claims c on c.order_id != t.order_id
        inner join prices pstart on pstart."level" = p.default_level
        left join prices pcurrent on pcurrent."level" = least(p.default_level + (extract(year from CURRENT_TIMESTAMP) - extract(year from cast(t.settlement_time as date))),	12)
        where t.user_id = :userId
        and t.deleted_at is null
      """,
      nativeQuery = true)
  List<MyPaketInfo> findAllMyPaket(UUID userId);

  @Query(value = """
        select
          t.order_id as id ,
          p."name" as name,
          pstart.price as startPrice,
          pcurrent.price as currentPrice,
          t.settlement_time as date,
          c.id as claimId
        from pakets p
        inner join transactions t on t.paket_id = p.id and t.status_code = '200'
        left join claims c on c.order_id = t.order_id
        inner join prices pstart on pstart."level" = p.default_level
        left join prices pcurrent on pcurrent."level" = least(p.default_level + (extract(year from CURRENT_TIMESTAMP) - extract(year from cast(t.settlement_time as date))),	12)
        where t.user_id = :userId
        and t.deleted_at is null
      """,
      nativeQuery = true)
  List<MyPaketInfo> findAllMyPaketForDashboard(UUID userId);


  @Query(value = """
        select
          t.order_id as id ,
          p."name" as name,
          pstart.price as startPrice,
          pcurrent.price as currentPrice,
          t.settlement_time as date
        from
          pakets p
        inner join transactions t on
          t.paket_id = p.id
          and t.status_code = '200'
        inner join prices pstart on
          pstart."level" = p.default_level
        left join prices pcurrent on
          pcurrent."level" = least(p.default_level + (extract(year from CURRENT_TIMESTAMP) - extract(year from cast(t.settlement_time as date))),	12)
        where
          t.order_id = :orderId
        and t.deleted_at is null
      """,
      nativeQuery = true)
  Optional<MyPaketInfo> findMyPaketByOrderId(UUID orderId);


  @Query(value = """
        select
          t.order_id as id ,
          p."name" as name,
          pstart.price as startPrice,
          pcurrent.price as currentPrice,
          t.settlement_time as date
        from pakets p
        inner join transactions t on t.paket_id = p.id and t.status_code = '200'
        inner join claims c on c.order_id != t.order_id
        inner join prices pstart on pstart."level" = p.default_level
        left join prices pcurrent on pcurrent."level" = least(p.default_level + (extract(year from CURRENT_TIMESTAMP) - extract(year from cast(t.settlement_time as date))),	12)
        where t.user_id = :userId
        and t.deleted_at is null
        and (EXTRACT(year FROM AGE(CAST(t.settlement_time as date))) >= p.claim_allowed_year)
      """,
      nativeQuery = true)
  List<MyPaketInfo> findAllMyReadyToClaimPaket(UUID userId);
}


