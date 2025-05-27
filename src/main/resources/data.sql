-- The string values here are now mapped directly to the enum constants.
-- The column type in H2 will be a VARCHAR.
INSERT INTO
    jackpots (
        jackpot_id,
        current_pool_amount,
        initial_pool_amount,
        contribution_type,
        contribution_value,
        reward_type,
        reward_value,
        reward_pool_limit
    )
VALUES
    (
        'JP001',
        1000.00,
        1000.00,
        'FIXED_PERCENTAGE',
        0.01,
        'FIXED_CHANCE',
        0.05,
        50000.00
    );

INSERT INTO
    jackpots (
        jackpot_id,
        current_pool_amount,
        initial_pool_amount,
        contribution_type,
        contribution_value,
        reward_type,
        reward_value,
        reward_pool_limit
    )
VALUES
    (
        'JP002',
        5000.00,
        5000.00,
        'VARIABLE_PERCENTAGE',
        0.05,
        'VARIABLE_CHANCE',
        0.01,
        100000.00
    );
