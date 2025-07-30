DELETE
FROM users;
INSERT INTO users (nickname,
                   phone,
                   user_role,
                   user_sex,
                   user_level,
                   profile_image_url,
                   name,
                   email,
                   created_at,
                   updated_at)
VALUES ('bouldertiger', -- nickname
        '010-1234-5678', -- phone
        'ROLE_USER', -- user_role (enum 문자열)
        'MAN', -- user_sex (enum 문자열)
        'V0', -- user_level (enum 문자열)
        'https://example.com/profile.jpg', -- profileImageUrl
        '김볼더', -- name
        'bouldertiger11@example.com', -- email
        NOW(), -- created_at
        NOW() -- updated_at
       );