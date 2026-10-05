-- SB09 step 3: who owns what.
--   loads.shipper      VARCHAR(50): the username of the shipper who posted the load. REQUIRED.
--                      Existing loads were posted before logins existed: backfill them with 'system'.
--   carriers.username  VARCHAR(50): the login that acts for this carrier. OPTIONAL (NULL = nobody can bid
--                      for it), but two carriers can't share a login: add a UNIQUE constraint named uk_carriers_username.
--                      (A UNIQUE column may still hold many NULLs.)
ALTER TABLE loads ADD COLUMN shipper VARCHAR(50);
UPDATE loads SET shipper = 'system';
ALTER TABLE loads ALTER COLUMN shipper SET NOT NULL;

ALTER TABLE carriers ADD COLUMN username VARCHAR(50);
ALTER TABLE carriers ADD CONSTRAINT uk_carriers_username UNIQUE (username);
