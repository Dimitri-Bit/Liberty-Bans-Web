SELECT victim_uuid, operator, reason FROM libertybans_simple_history WHERE type = ? ORDER BY start DESC LIMIT 6 OFFSET ?;
