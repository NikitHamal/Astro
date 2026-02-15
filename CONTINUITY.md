## Goal (incl. success criteria):
Fix the edit-profile flow bug where pressing `Update & Save` without changing any field throws an input error (`length=13; index=13`), and push a stable fix.

## Constraints/Assumptions:
- User requested direct fix + push.
- Skip tests/Gradle commands unless explicitly asked.
- Keep behavior safe for existing saved charts/timezone values.

## Key Decisions:
- Add a no-op update short-circuit: if edit form values are unchanged, skip recalculation/update and return success.
- Normalize timezone before validation/storage in edit/create submit path to reduce parsing edge-case failures.
- Keep fix scoped to chart input flow to avoid broad behavioral regressions.

## State:
- Done:
  - Added unchanged-input detection for edit mode in `ChartInputScreen`.
  - Added timezone normalization helper used during submit.
  - Updated continuity ledger to canonical concise format.
- Now:
  - Final source sanity check, commit, and push.
- Next:
  - Confirm with user to retry edit-without-change flow.

## Open Questions (UNCONFIRMED if needed):
- UNCONFIRMED: exact original exception origin in runtime stack (not available from user report alone).

## Working Set (files/ids/commands):
- `app/src/main/java/com/astro/storm/ui/screen/ChartInputScreen.kt`
- `CONTINUITY.md`
- Commands:
  - `git status --short`
  - `git diff -- app/src/main/java/com/astro/storm/ui/screen/ChartInputScreen.kt`
  - `git commit`
  - `git push`
