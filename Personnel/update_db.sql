USE personnel;
ALTER TABLE employe ADD COLUMN IF NOT EXISTS numeroSecuriteSociale VARCHAR(15) UNIQUE; 