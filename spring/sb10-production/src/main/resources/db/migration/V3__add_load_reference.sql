-- SB08 step 3: every load gets a human-friendly, UNIQUE reference such as FB-000042. The table already has rows in
--         production, so this is a three-step migration:
--   1. add the column "reference" VARCHAR(20), allowing NULL for now (existing rows have no value yet)
--   2. BACKFILL the existing rows: 'FB-' followed by the id padded to 6 digits with zeros (id 42 -> FB-000042)
--      Useful, and portable between H2 and PostgreSQL:  'a' || 'b'   LPAD(text, 6, '0')   CAST(id AS VARCHAR)
--   3. make it NOT NULL, and add a UNIQUE constraint named uk_loads_reference
ALTER TABLE loads ADD COLUMN reference VARCHAR(20);

UPDATE loads SET reference = 'FB-' || LPAD(CAST(id AS VARCHAR), 6, '0');

ALTER TABLE loads ALTER COLUMN reference SET NOT NULL;

ALTER TABLE loads ADD CONSTRAINT uk_loads_reference UNIQUE (reference);
