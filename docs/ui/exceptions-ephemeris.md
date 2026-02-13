# Ephemeris Exceptions (Wave 1)

This file tracks scoped visual exceptions allowed for the Transits Ephemeris screen
while we prioritize pixel-close concept fidelity.

## Approved Exceptions

1. Header copy uses fixed visual wording:
- `EPHEMERIS`
- `LIVE CELESTIAL MOVEMENTS`

Reason: concept-driven branding treatment for this screen.

2. Timeline pager dots use custom width morphing (selected vs non-selected).

Reason: matched interaction cue from concept reference.

3. Timeline item highlight treatment uses slightly stronger local tint and border
than generic card tokens for alert rows.

Reason: preserve concept-level emphasis for high-impact events.

## Non-Exceptions

1. General colors still resolve from `AppTheme`.
2. Shapes still resolve from `NeoVedicTokens`.
3. Global app bottom navigation is unchanged.
