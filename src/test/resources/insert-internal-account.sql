insert into account (
    id,
    username,
    hashed_password,
    balance,
    number,
    email,
    phone_number,
    address,
    role,
    type,
    created_at,
    status,
    credit_rating
)
values
    (

     10000,
        'internal_deposit',
        '$2a$10$7sQ6jx6Gf1Wk9mK9u8ZrIeVfD4a3V.gqJ0a7L8k0l.9Q0I9M2uN6m',
        0.00,
        '123456789012',
        'internal.deposit@bank.local',
        '0900000001',
        'internal system account',
        'ADMIN',
        'INTERNAL',
        now(),
        'ACTIVE',
        600
    ),
    (
     10001,
        'internal_withdrawal',
        '$2a$10$7sQ6jx6Gf1Wk9mK9u8ZrIeVfD4a3V.gqJ0a7L8k0l.9Q0I9M2uN6m',
        0.00,
        '123456789013',
        'internal.withdrawal@bank.local',
        '0900000002',
        'internal system account',
        'ADMIN',
        'INTERNAL',
        now(),
        'ACTIVE',
        600
    );
