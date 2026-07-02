-- =============================================================================
-- Email Partial Unique Index
-- =============================================================================
-- Bu dosya, Hibernate tarafından desteklenmeyen partial (kısmi) unique index'i
-- tanımlar. Hibernate'in ddl-auto özelliği bu index'i oluşturamaz; bu nedenle
-- ilk kurulumda ve üretim ortamında bu SQL'in elle çalıştırılması gerekir.
--
-- Amaç: Aynı e-posta adresine sahip birden fazla DOĞRULANMAMIŞ kullanıcının
--       sistemde bulunabilmesine izin vermek. Bir kullanıcı e-postasını
--       doğruladığında (email_verified_at IS NOT NULL), o e-posta adresi
--       unique hale gelir ve başka hiçbir kullanıcı (doğrulanmış ya da değil)
--       aynı e-postayı kullanamaz.
-- =============================================================================

-- 1. Hibernate'in otomatik oluşturduğu eski genel unique constraint'i kaldır
--    (varsa; ddl-auto=create ile oluşturulmuş olabilir)
ALTER TABLE users DROP CONSTRAINT IF EXISTS uk_users_email;
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_email_key;

-- 2. email sütununu nullable yap (zaten Hibernate entity'den yapılmış olmalı)
ALTER TABLE users ALTER COLUMN email DROP NOT NULL;

-- 3. Partial unique index: yalnızca doğrulanmış email'ler arasında benzersizlik
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_verified_email
    ON users (email)
    WHERE email_verified_at IS NOT NULL;
