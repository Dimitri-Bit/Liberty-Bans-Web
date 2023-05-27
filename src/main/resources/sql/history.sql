SELECT COALESCE(ln_victim.name, 'Unknown') AS victim_name, COALESCE(ln_operator.name, 'Console') AS operator_name, lh.victim_uuid, lh.operator, lh.reason, lh.type,
    CASE
        WHEN lh.type = 0 THEN EXISTS(SELECT 1 FROM libertybans_simple_bans lb WHERE lb.id = lh.id)
        WHEN lh.type = 1 THEN EXISTS(SELECT 1 FROM libertybans_simple_mutes lm WHERE lm.id = lh.id)
        WHEN lh.type = 2 THEN EXISTS(SELECT 1 FROM libertybans_simple_warns lw WHERE lw.id = lh.id)
        ELSE NULL
    END AS active
FROM libertybans_simple_history lh
LEFT JOIN libertybans_latest_names ln_victim ON ln_victim.uuid = lh.victim_uuid
LEFT JOIN libertybans_latest_names ln_operator ON ln_operator.uuid = lh.operator
WHERE lh.type = ?
ORDER BY lh.start DESC
LIMIT 6
OFFSET ?;
