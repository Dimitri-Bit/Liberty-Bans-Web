SELECT COALESCE(ln_victim.name, 'Unknown') AS victim_name,
       COALESCE(ln_operator.name, 'Console') AS operator_name,
       lh.victim_uuid,
       lh.operator,
       lh.reason,
       lh.type,
       CASE
           WHEN lh.type = 0 AND lb.id IS NOT NULL THEN 1
           WHEN lh.type = 1 AND lm.id IS NOT NULL THEN 1
           WHEN lh.type = 2 AND lw.id IS NOT NULL THEN 1
           ELSE 0
       END AS active
FROM libertybans_simple_history lh
LEFT JOIN libertybans_latest_names ln_victim ON ln_victim.uuid = lh.victim_uuid
LEFT JOIN libertybans_latest_names ln_operator ON ln_operator.uuid = lh.operator
LEFT JOIN libertybans_simple_bans lb ON lb.id = lh.id AND lh.type = 0
LEFT JOIN libertybans_simple_mutes lm ON lm.id = lh.id AND lh.type = 1
LEFT JOIN libertybans_simple_warns lw ON lw.id = lh.id AND lh.type = 2
WHERE lh.type = ?
ORDER BY lh.start DESC
LIMIT 6
OFFSET ?;
