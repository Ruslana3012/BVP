INSERT INTO roles (id, name)
VALUES (1, 'ADMIN');

INSERT INTO roles (id, name)
VALUES (2, 'USER');

INSERT INTO users (email,
                   password,
                   role_id,
                   is_email_verified,
                   url_for_verified_email,
                   created)
VALUES ('buchynska.ruslana@gmail.com',
        '$2a$12$/gn1jbwCe/nbV1um.HVAxuxlu9VCv300s9Ww.kMzqTEhJCXFCCgkK',
        1,
        true,
        'buchynska.ruslana/jfhjhejoihdhuhnndhkl239993u212-00((&$i',
        '2024-04-8');