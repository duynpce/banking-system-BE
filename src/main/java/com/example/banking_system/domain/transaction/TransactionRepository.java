package com.example.banking_system.domain.transaction;

import com.example.banking_system.domain.transaction.dto.GetTransactionReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

	@Query(value = """
			with bounds as (
				select
					case
						when :bucket = 'day' then date_trunc('day', cast(:startDateTime as timestamptz) at time zone 'UTC')
						when :bucket = 'week' then date_trunc('week', cast(:startDateTime as timestamptz) at time zone 'UTC')
						when :bucket = 'month' then date_trunc('month', cast(:startDateTime as timestamptz) at time zone 'UTC')
						else date_trunc('day', cast(:startDateTime as timestamptz) at time zone 'UTC')
					end as series_start,
					case
						when :bucket = 'day' then date_trunc('day', (cast(:endDateTime as timestamptz) at time zone 'UTC') - interval '1 microsecond')
						when :bucket = 'week' then date_trunc('week', (cast(:endDateTime as timestamptz) at time zone 'UTC') - interval '1 microsecond')
						when :bucket = 'month' then date_trunc('month', (cast(:endDateTime as timestamptz) at time zone 'UTC') - interval '1 microsecond')
						else date_trunc('day', (cast(:endDateTime as timestamptz) at time zone 'UTC') - interval '1 microsecond')
					end as series_end,
					case
						when :bucket = 'day' then interval '1 day'
						when :bucket = 'week' then interval '1 week'
						when :bucket = 'month' then interval '1 month'
						else interval '1 day'
					end as step_interval
			),
			bucket_series as (
				select gs.bucket_start
				from bounds b
				cross join lateral generate_series(b.series_start, b.series_end, b.step_interval) as gs(bucket_start)
			)
			select
				cast(bs.bucket_start as date) as startDate,
				cast(
					case
						when :bucket = 'day' then bs.bucket_start
						when :bucket = 'week' then bs.bucket_start + interval '6 day'
						when :bucket = 'month' then bs.bucket_start + interval '1 month - 1 day'
						else bs.bucket_start
					end as date
				) as endDate,
				coalesce(sum(case when t.receiver_id = :accountId then t.transferred_amount else 0 end), 0) as incomeAmount,
				coalesce(sum(case when t.sender_id = :accountId then t.transferred_amount else 0 end), 0) as outcomeAmount,
				coalesce(sum(case when t.receiver_id = :accountId and t.type = 'TRANSFER' then t.transferred_amount else 0 end), 0) as incomeTransferAmount,
				coalesce(sum(case when t.sender_id = :accountId and t.type = 'TRANSFER' then t.transferred_amount else 0 end), 0) as outcomeTransferAmount,
				coalesce(sum(case when t.receiver_id = :accountId and t.type = 'CASHBACK' then t.transferred_amount else 0 end), 0) as cashbackAmount,
				coalesce(sum(case when t.sender_id = :accountId and t.type = 'PAYMENT' then t.transferred_amount else 0 end), 0) as paymentAmount,
				coalesce(sum(case when t.receiver_id = :accountId and t.type = 'DEPOSIT' then t.transferred_amount else 0 end), 0) as depositAmount,
				coalesce(sum(case when t.sender_id = :accountId and t.type = 'WITHDRAWAL' then t.transferred_amount else 0 end), 0) as withdrawalAmount
			from bucket_series bs
			left join transactions t
				on (
					case
						when :bucket = 'day' then date_trunc('day', t.created_at at time zone 'UTC')
						when :bucket = 'week' then date_trunc('week', t.created_at at time zone 'UTC')
						when :bucket = 'month' then date_trunc('month', t.created_at at time zone 'UTC')
						else date_trunc('day', t.created_at at time zone 'UTC')
					end
				) = bs.bucket_start
				and (t.sender_id = :accountId or t.receiver_id = :accountId)
				and t.created_at between :startDateTime and :endDateTime
			group by bs.bucket_start
			order by bs.bucket_start
			""", nativeQuery = true)
	List<GetTransactionReportProjection> findTransactionReportByAccountIdAndCreatedAtBetween(
			@Param("accountId") long accountId,
			@Param("startDateTime") Instant startDateTime,
			@Param("endDateTime") Instant endDateTime,
			@Param("bucket") String bucket
	);
}
